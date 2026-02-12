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
from functools import lru_cache
from typing import Optional, Dict, List, Tuple

# ============ UTIL ============

def _safe_strip(s):
    return s.strip() if isinstance(s, str) else s

@lru_cache(maxsize=1)
def _get_system():
    """Cache system type to avoid repeated calls"""
    return platform.system().lower()

def _first_nonloop_iface():
    """
    Retorna (iface_name, mac, ip) da primeira interface com IPv4 não-loopback.
    Garante que o MAC seja da MESMA interface do IP escolhido.
    """
    for iface, addrs in psutil.net_if_addrs().items():
        ipv4 = mac = None
        for a in addrs:
            if a.family == socket.AF_INET and not a.address.startswith("127."):
                ipv4 = a.address
                break
        if ipv4:
            for a in addrs:
                if hasattr(psutil, "AF_LINK") and a.family == psutil.AF_LINK:
                    mac = a.address
                    break
            return iface, mac, ipv4
    return None, None, None

# ============ REDE BÁSICO ============

def get_ip_mac():
    iface, mac, ip = _first_nonloop_iface()
    return mac, ip, iface

def get_wifi_info() -> Dict:
    system = _get_system()
    ssid = bssid = channel = auth = None
    
    try:
        if "windows" in system:
            output = subprocess.check_output(["netsh", "wlan", "show", "interfaces"], 
                                            text=True, encoding="utf-8", errors="ignore")
            
            patterns = {
                'ssid': r"SSID\s*:\s*(.+)",
                'bssid': r"BSSID\s*:\s*([0-9A-Fa-f:]+)",
                'channel': r"(?:Channel|Canal)\s*:\s*(\d+)",
                'auth': r"(?:Authentication|Autenticação|Tipo de autenticação)\s*:\s*(.+)"
            }
            
            for key, pattern in patterns.items():
                match = re.search(pattern, output)
                if match:
                    value = _safe_strip(match.group(1))
                    if key == 'ssid':
                        ssid = value
                    elif key == 'bssid':
                        bssid = value
                    elif key == 'channel':
                        channel = int(value)
                    elif key == 'auth':
                        auth = value
        else:
            # Linux (NetworkManager)
            output = subprocess.check_output(["nmcli", "-t", "-f", "in-use,ssid,bssid,chan,security", "dev", "wifi"], text=True)
            connected = next((line for line in output.splitlines() if line.startswith("*:")), None)
            if connected:
                parts = connected.split(":")
                ssid = parts[1] if len(parts) > 1 else None
                bssid = parts[2] if len(parts) > 2 else None
                channel = int(parts[3]) if len(parts) > 3 and parts[3].isdigit() else None
                auth = parts[4] if len(parts) > 4 else None
    except Exception:
        pass

    oui = ":".join(bssid.split(":")[:3]) if bssid else None
    return {"SSID": ssid, "BSSID": bssid, "Canal": channel, "OUI": oui, "Auth": auth}

def get_rssi() -> Optional[float]:
    system = _get_system()
    try:
        if "windows" in system:
            output = subprocess.check_output(["netsh", "wlan", "show", "interfaces"], 
                                            text=True, encoding="utf-8", errors="ignore")
            match = re.search(r"(?:Signal|Sinal)\s*:\s*(\d+)%", output)
            if match:
                percent = int(match.group(1))
                return (percent / 2) - 100  # Aproximação: dBm ≈ (percent/2) - 100
        else:
            # Linux (iwconfig)
            output = subprocess.check_output(["iwconfig"], text=True, encoding="utf-8", errors="ignore")
            match = re.search(r"Signal level=(-?\d+)\s*dBm", output)
            if match:
                return int(match.group(1))
    except Exception:
        pass
    return None

def get_ttl(host: str = "8.8.8.8") -> Optional[int]:
    try:
        is_windows = _get_system() == "windows"
        cmd = ["ping", "-n" if is_windows else "-c", "1", host]
        output = subprocess.check_output(cmd, text=True, encoding="utf-8", errors="ignore")
        match = re.search(r"ttl=(\d+)", output.lower())
        if match:
            return int(match.group(1))
    except Exception:
        pass
    return None

# ============ DHCP / DNS CONFIG / DNS CHECK ============

def get_dhcp_dns_info(iface_name: Optional[str] = None) -> Dict:
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
    system = _get_system()

    try:
        if "windows" in system:
            output = subprocess.check_output(["ipconfig", "/all"], text=True, encoding="utf-8", errors="ignore")
            
            # Localizar o bloco da interface
            text = output
            if iface_name:
                blocks = re.split(r"\r?\n\r?\n", output)
                for b in blocks:
                    if iface_name.lower() in b.lower():
                        text = b
                        break
            
            # Padrões de busca otimizados
            patterns = {
                'dhcp_en': r"(?i)DHCP (?:Habilitado|Enabled)\s*[:]\s*(\S+)",
                'dhcp_server': r"(?i)Servidor DHCP|DHCP Server\s*[:]\s*([^\r\n]+)",
                'lease_obt': r"(?i)Concessão Obtida|Lease Obtained\s*[:]\s*([^\r\n]+)",
                'lease_exp': r"(?i)Concessão Expira|Lease Expires\s*[:]\s*([^\r\n]+)",
                'gateway': r"(?i)Gateway Padrão|Default Gateway\s*[:]\s*([^\r\n]+)"
            }
            
            matches = {k: re.search(p, text) for k, p in patterns.items()}
            
            info.update({
                "DHCP_Enabled": matches['dhcp_en'].group(1).lower() if matches['dhcp_en'] else None,
                "DHCP_Server": _safe_strip(matches['dhcp_server'].group(1)) if matches['dhcp_server'] else None,
                "Lease_Obtained": _safe_strip(matches['lease_obt'].group(1)) if matches['lease_obt'] else None,
                "Lease_Expires": _safe_strip(matches['lease_exp'].group(1)) if matches['lease_exp'] else None,
                "Default_Gateway": _safe_strip(matches['gateway'].group(1)) if matches['gateway'] else None
            })
            
            # DNS pode estar em múltiplas linhas
            dns_values = []
            for match in re.finditer(r"(?i)(?:Servidores DNS|DNS Servers)\s*[:]\s*([^\r\n]+)", text):
                dns_values.append(match.group(1).strip())
            
            if dns_values:
                after = text.split("DNS Servers")[1] if "DNS Servers" in text else text
                dns_values.extend(re.findall(r"\r?\n\s+(\d{1,3}(?:\.\d{1,3}){3})", after))
            
            info["DNS_Servers"] = list(dict.fromkeys(dns_values))  # Remove duplicatas mantendo ordem

        else:
            # Linux
            if iface_name:
                try:
                    output = subprocess.check_output(["nmcli", "dev", "show", iface_name], text=True)
                    info["DNS_Servers"] = re.findall(r"IP4\.DNS\[\d+\]:\s*([^\n]+)", output)
                    
                    gateway = re.search(r"IP4\.GATEWAY:\s*([^\n]+)", output)
                    info["Default_Gateway"] = gateway.group(1).strip() if gateway else None
                    
                    dhcp_map = dict(re.findall(r"IP4\.DHCP4\.(\S+):\s*([^\n]+)", output))
                    info["DHCP_Server"] = dhcp_map.get("dhcp_server_identifier") or dhcp_map.get("dhcp_server_identifier_address")
                except Exception:
                    pass

            # Fallback de DNS pelo resolv.conf
            if not info["DNS_Servers"]:
                try:
                    with open("/etc/resolv.conf", "r", encoding="utf-8", errors="ignore") as f:
                        info["DNS_Servers"] = [m.group(1) for line in f if (m := re.match(r"nameserver\s+(\S+)", line))]
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

def packet_loss(host: str = "8.8.8.8", count: int = 5, timeout: int = 2) -> Optional[float]:
    """
    Executa ping em lote e retorna % de perda.
    """
    try:
        is_windows = _get_system() == "windows"
        cmd = ["ping", "-n" if is_windows else "-c", str(count)]
        
        if is_windows:
            cmd.extend(["-w", str(timeout * 1000)])
        else:
            cmd.extend(["-W", str(timeout)])
        cmd.append(host)
        
        output = subprocess.check_output(cmd, text=True, encoding="utf-8", errors="ignore")
        
        if is_windows:
            m = re.search(r"(?:Perdidos|Lost) = (\d+)", output)
            if m:
                return round((int(m.group(1)) / count) * 100, 1)
        else:
            m = re.search(r"(\d+(?:\.\d+)?)%\s*packet loss", output)
            if m:
                return float(m.group(1))
    except Exception:
        pass
    return None

# ============ CAPTIVE PORTAL: HTTP 204 & TLS ============

def captive_http204_check() -> Dict:
    """
    Testa URLs comuns de detecção de portal cativo.
    - Esperado: HTTP 204 (sem conteúdo) ou 204/No Content.
    - Se redirecionar (3xx) ou 200 com corpo: provável portal cativo.
    """
    probes = [
        "http://connectivitycheck.gstatic.com/generate_204",
        "http://www.gstatic.com/generate_204",
        "http://clients3.google.com/generate_204",
        "http://www.msftconnecttest.com/connecttest.txt",
    ]
    
    for url in probes:
        try:
            r = requests.get(url, allow_redirects=False, timeout=5)
            
            if r.status_code == 204:
                return {"captive": False, "url": url, "status": r.status_code, "location": None}
            
            if 300 <= r.status_code < 400:
                return {"captive": True, "url": url, "status": r.status_code, "location": r.headers.get("Location")}
            
            # Muitos portais respondem 200 com HTML de login
            if r.status_code == 200 and (r.text or r.headers.get("Content-Length", "0") != "0"):
                return {"captive": True, "url": url, "status": r.status_code, "location": None}
        except Exception:
            continue
    
    return {"captive": None, "url": None, "status": None, "location": None}

def tls_info_for_host(hostname: str, port: int = 443) -> Optional[Dict]:
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
        
        def _name_to_str(name):
            try:
                return ", ".join("=".join(x) for x in name[0])
            except Exception:
                return str(name)

        return {
            "TLS_Version": tls_version,
            "Cert_Subject": _name_to_str(cert.get("subject", [("commonName", "Unknown")])),
            "Cert_Issuer": _name_to_str(cert.get("issuer", [("commonName", "Unknown")])),
            "NotBefore": cert.get("notBefore"),
            "NotAfter": cert.get("notAfter")
        }
    except Exception:
        return None

def portal_tls_if_any(captive_result: Dict) -> Optional[Dict]:
    """
    Se detectado portal cativo e houver Location HTTPS, obtém o certificado do host.
    """
    try:
        loc = captive_result.get("location")
        if loc and loc.lower().startswith("https"):
            host = urlparse(loc).hostname
            if host:
                return tls_info_for_host(host)
    except Exception:
        pass
    return None

# ============ BEACON (LIMITAÇÃO) ============

@lru_cache(maxsize=1)
def get_beacon_info() -> Dict:
    """
    Beacon seq/timestamp não é exposto por netsh/nmcli.
    Requer modo monitor e captura 802.11 (ex.: Scapy/tshark) com placa suportando monitor mode.
    """
    return {"Beacon_Seq": None, "Beacon_Timestamp": None, "Obs": "Requer monitor mode/pcap para capturar beacons."}

# ============ LOOP PRINCIPAL ============

def collect_wifi_metrics(host: str = "8.8.8.8", interval: int = 3):
    """
    Coleta métricas de Wi-Fi continuamente.
    
    Args:
        host: Host para testes de ping
        interval: Intervalo entre coletas em segundos
    """
    mac, ip, iface = get_ip_mac()
    wifi_info = get_wifi_info()
    beacon = get_beacon_info()  # Cached, não muda
    
    # Cache para platform info (não muda)
    platform_info = platform.platform()
    
    rssi_prev = None
    update_counter = 0

    while True:
        # Métricas voláteis (a cada iteração)
        rssi = get_rssi()
        var_rssi = 0 if rssi_prev is None else int(rssi != rssi_prev)
        rssi_prev = rssi
        
        rtt = ping(host, unit='ms')
        ttl = get_ttl(host)
        dns_ok = dns_check()
        
        # Atualiza DHCP/DNS a cada 10 iterações (30s se interval=3)
        if update_counter % 10 == 0:
            dhcp_dns = get_dhcp_dns_info(iface_name=iface)
            wifi_info = get_wifi_info()  # Atualiza também wifi_info
        
        # Packet loss e captive portal: mais custosos, a cada 5 iterações
        if update_counter % 5 == 0:
            loss = packet_loss(host=host, count=5, timeout=2)
            captive = captive_http204_check()
            tls_portal = portal_tls_if_any(captive) if captive.get("captive") else None
        
        metrics = {
            "ts": datetime.now().isoformat(timespec="seconds"),
            "SO": platform_info,
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
            "DNS_OK": dns_ok,
        }
        
        # Adiciona métricas menos frequentes quando disponíveis
        if update_counter % 10 == 0:
            metrics.update({
                "DHCP_Enabled": dhcp_dns.get("DHCP_Enabled"),
                "DHCP_Server": dhcp_dns.get("DHCP_Server"),
                "Lease_Obtained": dhcp_dns.get("Lease_Obtained"),
                "Lease_Expires": dhcp_dns.get("Lease_Expires"),
                "Default_Gateway": dhcp_dns.get("Default_Gateway"),
                "DNS_Config": dhcp_dns.get("DNS_Servers"),
            })
        
        if update_counter % 5 == 0:
            metrics.update({
                "Packet_Loss_pct": loss,
                "Captive": captive.get("captive"),
                "Captive_Probe_URL": captive.get("url"),
                "Captive_HTTP_Status": captive.get("status"),
                "Captive_Location": captive.get("location"),
                "Portal_TLS": tls_portal
            })
        
        print(metrics)
        
        update_counter += 1
        time.sleep(interval)

if __name__ == "__main__":
    collect_wifi_metrics()