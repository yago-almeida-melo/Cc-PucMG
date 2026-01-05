import psutil
import socket
import subprocess
import platform
import re
import time
from ping3 import ping
import requests
import ssl
from urllib.parse import urlparse
from datetime import datetime

# ============ UTIL ============

def _safe_strip(s):
    return s.strip() if isinstance(s, str) else s

def _first_nonloop_iface():
    wifi_keywords = ["wi-fi", "wifi", "wlan", "wireless"]
    for iface, addrs in psutil.net_if_addrs().items():
        if any(k in iface.lower() for k in wifi_keywords):
            ipv4 = None
            mac = None
            for a in addrs:
                if a.family == socket.AF_INET and not a.address.startswith("127."):
                    ipv4 = a.address
                if hasattr(psutil, "AF_LINK") and a.family == psutil.AF_LINK:
                    mac = a.address
            if ipv4:
                return iface, mac, ipv4
    # fallback original
    for iface, addrs in psutil.net_if_addrs().items():
        ipv4 = None
        mac = None
        for a in addrs:
            if a.family == socket.AF_INET and not a.address.startswith("127."):
                ipv4 = a.address
            if hasattr(psutil, "AF_LINK") and a.family == psutil.AF_LINK:
                mac = a.address
        if ipv4:
            return iface, mac, ipv4

    return None, None, None

# ============ REDE BÁSICO ============

def get_ip_mac():
    iface, mac, ip = _first_nonloop_iface()
    return mac, ip, iface

def get_wifi_info():
    system = platform.system().lower()
    try:
        if "windows" in system:
            output = subprocess.check_output(["netsh", "wlan", "show", "interfaces"], text=True, encoding="utf-8", errors="ignore")

            ssid = re.search(r"SSID\s*:\s*(.+)", output)
            bssid = re.search(r"BSSID\s*:\s*([0-9A-Fa-f:]+)", output)
            channel = re.search(r"(?:Channel|Canal)\s*:\s*(\d+)", output)
            auth = re.search(r"(?:Authentication|Autenticação|Tipo de autenticação)\s*:\s*(.+)", output)

            ssid = _safe_strip(ssid.group(1)) if ssid else None
            bssid = _safe_strip(bssid.group(1)) if bssid else None
            channel = int(channel.group(1)) if channel else None
            auth = _safe_strip(auth.group(1)) if auth else None
        else:
            # Linux (NetworkManager)
            try:
                output = subprocess.check_output(["nmcli", "-t", "-f", "in-use,ssid,bssid,chan,security", "dev", "wifi"], text=True)
                connected = next((line for line in output.splitlines() if line.startswith("*:")), None)
                if connected:
                    # "*:SSID:BSSID:CHAN:SEC"
                    parts = connected.split(":")
                    ssid = parts[1] if len(parts) > 1 else None
                    bssid = parts[2] if len(parts) > 2 else None
                    channel = int(parts[3]) if len(parts) > 3 and parts[3].isdigit() else None
                    auth = parts[4] if len(parts) > 4 else None
                else:
                    ssid = bssid = channel = auth = None
            except Exception:
                ssid = bssid = channel = auth = None
    except Exception:
        ssid = bssid = channel = auth = None

    oui = ":".join(bssid.split(":")[:3]) if bssid else None
    return {"SSID": ssid, "BSSID": bssid, "Canal": channel, "OUI": oui, "Auth": auth}

def get_rssi():
    system = platform.system().lower()
    try:
        if "windows" in system:
            output = subprocess.check_output(["netsh", "wlan", "show", "interfaces"], text=True, encoding="utf-8", errors="ignore")
            match = re.search(r"(?:Signal|Sinal)\s*:\s*(\d+)%", output)
            if match:
                percent = int(match.group(1))
                # Aproximação: dBm ≈ (percent/2) - 100
                return (percent / 2) - 100
        else:
            # Linux (iwconfig)
            output = subprocess.check_output(["iwconfig"], text=True, encoding="utf-8", errors="ignore")
            match = re.search(r"Signal level=(-?\d+)\s*dBm", output)
            if match:
                return int(match.group(1))
    except Exception:
        pass
    return None

def get_ttl(host="8.8.8.8"):
    try:
        cmd = ["ping", "-n", "1", host] if platform.system() == "Windows" else ["ping", "-c", "1", host]
        output = subprocess.check_output(cmd, text=True, encoding="utf-8", errors="ignore")
        match = re.search(r"ttl=(\d+)", output.lower())
        if match:
            return int(match.group(1))
    except Exception:
        pass
    return None

# ============ DHCP / DNS CONFIG / DNS CHECK ============

def get_dhcp_dns_info(iface_name=None):
    """
    Retorna informações de DHCP e DNS configurado.
    Windows: ipconfig /all
    Linux: nmcli dev show <iface> (se disponível) ou /etc/resolv.conf (DNS).
    """
    info = {
        "DHCP_Enabled": None,
        "DHCP_Server": None,
        "Lease_Obtained": None,
        "Lease_Expires": None,
        "Default_Gateway": None,
        "DNS_Servers": []
    }
    system = platform.system().lower()

    try:
        if "windows" in system:
            output = subprocess.check_output(["ipconfig", "/all"], text=True, encoding="utf-8", errors="ignore")
            # Tente localizar o bloco da interface
            block_pattern = re.compile(rf"^\s*Adaptador.{re.escape(iface_name)}.?:\s*(.?)\n\s\n", re.DOTALL | re.IGNORECASE | re.MULTILINE)
            block = None
            m = block_pattern.search(output)
            if not m:
                # fallback: pega bloco "Ethernet adapter X" ou "Wireless LAN adapter X" contendo o IP
                blocks = re.split(r"\r?\n\r?\n", output)
                for b in blocks:
                    if iface_name and iface_name.lower() in b.lower():
                        block = b
                        break
            else:
                block = m.group(1)

            text = block if block else output  # fallback para todo output

            dhcp_en = re.search(r"(?i)DHCP Habilitado\s*[:]\s*(\S+)|DHCP Enabled\s*[:]\s*(\S+)", text)
            dhcp_server = re.search(r"(?i)Servidor DHCP\s*[:]\s*([^\r\n]+)|DHCP Server\s*[:]\s*([^\r\n]+)", text)
            lease_obt = re.search(r"(?i)Concessão Obtida\s*[:]\s*([^\r\n]+)|Lease Obtained\s*[:]\s*([^\r\n]+)", text)
            lease_exp = re.search(r"(?i)Concessão Expira\s*[:]\s*([^\r\n]+)|Lease Expires\s*[:]\s*([^\r\n]+)", text)
            gateway = re.search(r"(?i)Gateway Padrão\s*[:]\s*([^\r\n]+)|Default Gateway\s*[:]\s*([^\r\n]+)", text)

            dns_lines = re.findall(r"(?i)Servidores DNS\s*[:]\s*([^\r\n]+)|DNS Servers\s*[:]\s*([^\r\n]+)", text)
            dns_values = []
            for a, b in dns_lines:
                line = a or b
                if line:
                    dns_values.append(line.strip())
            # DNS pode continuar em linhas seguintes no ipconfig; capture também linhas identadas após "DNS Servers"
            if dns_values:
                after = text.split("DNS Servers")[1] if "DNS Servers" in text else text
                cont = re.findall(r"\r?\n\s+(\d{1,3}(?:\.\d{1,3}){3})", after)
                for ip in cont:
                    if ip not in dns_values:
                        dns_values.append(ip)

            info.update({
                "DHCP_Enabled": (dhcp_en.group(1) or dhcp_en.group(2)).lower() if dhcp_en else None,
                "DHCP_Server": _safe_strip((dhcp_server.group(1) or dhcp_server.group(2)) if dhcp_server else None),
                "Lease_Obtained": _safe_strip((lease_obt.group(1) or lease_obt.group(2)) if lease_obt else None),
                "Lease_Expires": _safe_strip((lease_exp.group(1) or lease_exp.group(2)) if lease_exp else None),
                "Default_Gateway": _safe_strip((gateway.group(1) or gateway.group(2)) if gateway else None),
                "DNS_Servers": [d for d in dns_values if d]
            })

        else:
            # Linux
            if iface_name:
                try:
                    output = subprocess.check_output(["nmcli", "dev", "show", iface_name], text=True)
                    # DNS servers (múltiplas linhas)
                    dns = re.findall(r"IP4\.DNS\[\d+\]:\s*([^\n]+)", output)
                    gateway = re.search(r"IP4\.GATEWAY:\s*([^\n]+)", output)
                    dhcp = re.findall(r"IP4\.DHCP4\.(\S+):\s*([^\n]+)", output)
                    dhcp_map = {k: v for k, v in dhcp}

                    info["DNS_Servers"] = dns
                    info["Default_Gateway"] = gateway.group(1).strip() if gateway else None
                    info["DHCP_Server"] = dhcp_map.get("dhcp_server_identifier") or dhcp_map.get("dhcp_server_identifier_address")
                    # nmcli não traz lease obtida/expira de forma padronizada em todas distros; pode estar em dhclient leases.
                except Exception:
                    pass

            # Fallback de DNS pelo resolv.conf
            if not info["DNS_Servers"]:
                try:
                    with open("/etc/resolv.conf", "r", encoding="utf-8", errors="ignore") as f:
                        for line in f:
                            m = re.match(r"nameserver\s+(\S+)", line)
                            if m:
                                info["DNS_Servers"].append(m.group(1))
                except Exception:
                    pass

    except Exception:
        pass

    return info

def dns_check():
    """
    Testa resolução DNS básica.
    """
    try:
        socket.getaddrinfo("example.com", 80)
        return True
    except Exception:
        return False

# ============ PERDA DE PACOTES (PING EM LOTE) ============

def packet_loss(host="8.8.8.8", count=5, timeout=2):
    """
    Executa ping em lote e retorna % de perda.
    """
    try:
        system = platform.system()
        if system == "Windows":
            cmd = ["ping", "-n", str(count), "-w", str(timeout * 1000), host]
        else:
            cmd = ["ping", "-c", str(count), "-W", str(timeout), host]
        output = subprocess.check_output(cmd, text=True, encoding="utf-8", errors="ignore")
        if system == "Windows":
            m = re.search(r"Perdidos = (\d+)", output) or re.search(r"Lost = (\d+)", output)
            if m:
                lost = int(m.group(1))
                return round((lost / count) * 100, 1)
        else:
            m = re.search(r"(\d+(\.\d+)?)%\s*packet loss", output)
            if m:
                return float(m.group(1))
    except Exception:
        pass
    return None

# ============ CAPTIVE PORTAL: HTTP 204 & TLS ============

def captive_http204_check():
    """
    Testa URLs comuns de detecção de portal cativo.
    - Esperado: HTTP 204 (sem conteúdo) ou 204/No Content.
    - Se redirecionar (3xx) ou 200 com corpo: provável portal cativo.
    """
    probes = [
        "http://connectivitycheck.gstatic.com/generate_204",
        "http://www.gstatic.com/generate_204",
        "http://clients3.google.com/generate_204",
        "http://www.msftconnecttest.com/connecttest.txt",  # Windows
    ]
    for url in probes:
        try:
            r = requests.get(url, allow_redirects=False, timeout=5)
            if r.status_code == 204:
                return {"captive": False, "url": url, "status": r.status_code, "location": None}
            if 300 <= r.status_code < 400:
                return {"captive": True, "url": url, "status": r.status_code, "location": r.headers.get("Location")}
            if r.status_code == 200 and (r.text or r.headers.get("Content-Length", "0") != "0"):
                # Muitos portais respondem 200 com HTML de login
                return {"captive": True, "url": url, "status": r.status_code, "location": None}
        except Exception:
            continue
    # Se nenhum teste concluiu, considere inconclusivo
    return {"captive": None, "url": None, "status": None, "location": None}

def tls_info_for_host(hostname, port=443):
    """
    Abre uma conexão TLS e retorna informações básicas do certificado.
    Útil quando o portal cativo redireciona para HTTPS.
    """
    try:
        ctx = ssl.create_default_context()
        with socket.create_connection((hostname, port), timeout=5) as sock:
            with ctx.wrap_socket(sock, server_hostname=hostname) as ssock:
                cert = ssock.getpeercert()
                tls_version = ssock.version()
        # Extrai sujeito/issuer em formato simples
        def _name_to_str(name):
            try:
                return ", ".join("=".join(x) for x in name[0])
            except Exception:
                return str(name)

        subject = _name_to_str(cert.get("subject", [("commonName", "Unknown")]))
        issuer = _name_to_str(cert.get("issuer", [("commonName", "Unknown")]))
        not_before = cert.get("notBefore")
        not_after = cert.get("notAfter")
        return {
            "TLS_Version": tls_version,
            "Cert_Subject": subject,
            "Cert_Issuer": issuer,
            "NotBefore": not_before,
            "NotAfter": not_after
        }
    except Exception:
        return None

def portal_tls_if_any(captive_result):
    """
    Se detectado portal cativo e houver Location HTTPS, obtém o certificado do host.
    """
    try:
        loc = captive_result.get("location")
        if loc and loc.lower().startswith("https"):
            host = urlparse(loc).hostname
            return tls_info_for_host(host)
    except Exception:
        pass
    return None

# ============ BEACON (LIMITAÇÃO) ============

def get_beacon_info():
    """
    Beacon seq/timestamp não é exposto por netsh/nmcli.
    Requer modo monitor e captura 802.11 (ex.: Scapy/tshark) com placa suportando monitor mode.
    Aqui retornamos None para ambos e deixamos o comentário explicativo.
    """
    return {"Beacon_Seq": None, "Beacon_Timestamp": None, "Obs": "Requer monitor mode/pcap para capturar beacons."}

# ============ LOOP PRINCIPAL ============

def collect_wifi_metrics(host="8.8.8.8"):
    mac, ip, iface = get_ip_mac()
    wifi_info = get_wifi_info()
    rssi_prev = None

    # Info menos volátil (atualizamos a cada ciclo também, mas já buscamos uma vez)
    dhcp_dns = get_dhcp_dns_info(iface_name=iface)

    while True:
        rssi = get_rssi()
        ttl = get_ttl(host)
        rtt = ping(host, unit='ms')
        var_rssi = 0 if rssi_prev is None else int(rssi != rssi_prev)
        rssi_prev = rssi

        loss = packet_loss(host=host, count=5, timeout=2)
        dns_ok = dns_check()

        captive = captive_http204_check()
        tls_portal = portal_tls_if_any(captive) if captive.get("captive") else None

        beacon = get_beacon_info()

        # Atualiza de tempos em tempos DHCP/DNS (caso o IP mude, etc.)
        dhcp_dns = get_dhcp_dns_info(iface_name=iface)

        print({
            "ts": datetime.now().isoformat(timespec="seconds"),
            "SO": platform.platform(),
            "Interface": iface,
            "MAC": mac,
            "IP": ip,

            # Wi-Fi
            "SSID": wifi_info.get("SSID"),
            "BSSID": wifi_info.get("BSSID"),
            "OUI": wifi_info.get("OUI"),
            "Canal": wifi_info.get("Canal"),
            "Auth": wifi_info.get("Auth"),
            "RSSI_dBm": rssi,
            "VAR_RSSI": var_rssi,

            # Beacon (limitação)
            "Beacon_Seq": beacon.get("Beacon_Seq"),
            "Beacon_Timestamp": beacon.get("Beacon_Timestamp"),

            # Rede: ping
            "RTT_ms": round(rtt, 3) if rtt is not None else None,
            "TTL": ttl,
            "Packet_Loss_pct": loss,

            # DHCP/DNS configurado
            "DHCP_Enabled": dhcp_dns.get("DHCP_Enabled"),
            "DHCP_Server": dhcp_dns.get("DHCP_Server"),
            "Lease_Obtained": dhcp_dns.get("Lease_Obtained"),
            "Lease_Expires": dhcp_dns.get("Lease_Expires"),
            "Default_Gateway": dhcp_dns.get("Default_Gateway"),
            "DNS_Config": dhcp_dns.get("DNS_Servers"),

            # DNS check
            "DNS_OK": dns_ok,

            # Captive portal
            "Captive": captive.get("captive"),
            "Captive_Probe_URL": captive.get("url"),
            "Captive_HTTP_Status": captive.get("status"),
            "Captive_Location": captive.get("location"),

            # TLS do portal (se houver HTTPS)
            "Portal_TLS": tls_portal
        })

        time.sleep(3)  # mede a cada 3 segundos

if __name__ == "__main__":
    collect_wifi_metrics()