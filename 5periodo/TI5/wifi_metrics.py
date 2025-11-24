import psutil
import socket
import subprocess
import platform
import re
import time
from ping3 import ping

def get_ip_mac():
    interfaces = psutil.net_if_addrs()
    for iface, addrs in interfaces.items():
        for addr in addrs:
            if addr.family == socket.AF_INET and not addr.address.startswith("127."):
                ip = addr.address
            elif addr.family == psutil.AF_LINK:
                mac = addr.address
        return mac, ip
    return None, None

def get_wifi_info():
    system = platform.system().lower()
    try:
        if "windows" in system:
            output = subprocess.check_output(["netsh", "wlan", "show", "interfaces"], text=True)

            ssid = re.search(r"SSID\s*:\s*(.+)", output)
            bssid = re.search(r"BSSID\s*:\s*([0-9A-Fa-f:]+)", output)
            # procura em inglês e português
            channel = re.search(r"(?:Channel|Canal)\s*:\s*(\d+)", output)
            auth = re.search(r"(?:Authentication|Autenticação|Tipo de autenticação)\s*:\s*(.+)", output)

            ssid = ssid.group(1).strip() if ssid else None
            bssid = bssid.group(1).strip() if bssid else None
            channel = int(channel.group(1)) if channel else None
            auth = auth.group(1).strip() if auth else None
        else:
            # Linux
            output = subprocess.check_output(["nmcli", "-t", "-f", "ssid,bssid,chan,security", "dev", "wifi"], text=True)
            connected = next((line for line in output.splitlines() if "*" in line), None)
            if connected:
                parts = connected.replace("*", "").split(":")
                ssid, bssid, channel, auth = parts[0], parts[1], int(parts[2]), parts[3]
            else:
                ssid = bssid = channel = auth = None
    except Exception:
        ssid = bssid = channel = auth = None

    oui = ":".join(bssid.split(":")[:3]) if bssid else None
    return {"SSID": ssid, "BSSID": bssid, "Canal": channel, "OUI": oui, "Auth": auth}

def get_rssi():
    system = platform.system().lower()
    try:
        if "windows" in system:
            output = subprocess.check_output(["netsh", "wlan", "show", "interfaces"], text=True)
            match = re.search(r"(?:Signal|Sinal)\s*:\s*(\d+)%", output)
            if match:
                percent = int(match.group(1))
                # Converte % para dBm aproximado
                rssi = (percent / 2) - 100
                return rssi
        elif "linux" in system:
            output = subprocess.check_output(["iwconfig"], text=True)
            match = re.search(r"Signal level=(-?\d+) dBm", output)
            if match:
                return int(match.group(1))
    except Exception:
        return None

def get_ttl(host="8.8.8.8"):
    try:
        output = subprocess.check_output(["ping", "-c", "1", host] if platform.system() != "Windows" else ["ping", "-n", "1", host], text=True)
        match = re.search(r"ttl=(\d+)", output.lower())
        if match:
            return int(match.group(1))
    except Exception:
        return None

def collect_wifi_metrics(host="8.8.8.8"):
    mac, ip = get_ip_mac()
    wifi_info = get_wifi_info()
    rssi_prev = None

    while True:
        rssi = get_rssi()
        ttl = get_ttl(host)
        rtt = ping(host, unit='ms')
        var_rssi = 0 if rssi_prev is None else int(rssi != rssi_prev)
        rssi_prev = rssi

        print({
            "SSID": wifi_info["SSID"],
            "BSSID": wifi_info["BSSID"],
            "Canal": wifi_info["Canal"],
            "OUI": wifi_info["OUI"],
            "Auth": wifi_info["Auth"],
            "MAC": mac,
            "IP": ip,
            "RTT (ms)": round(rtt, 3) if rtt else None,
            "TTL": ttl,
            "RSSI (dBm)": rssi,
            "VAR_RSSI": var_rssi
        }
    )

        time.sleep(3)  # mede a cada 3 segundos

if __name__ == "__main__":
    collect_wifi_metrics()