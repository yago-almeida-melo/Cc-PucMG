#!/usr/bin/env python3
import subprocess
import json
import time
from ping3 import ping
import os

# Caminho garantido do Termux para acessar a pasta Download
SAVE_PATH = os.path.expanduser("~/storage/downloads/wifi_log.jsonl")

def run_cmd(cmd):
    try:
        out = subprocess.check_output(cmd, shell=True, text=True)
        return out.strip()
    except subprocess.CalledProcessError:
        return None


def freq_to_channel(freq):
    """Converte frequência para canal (2.4GHz / 5GHz)."""
    if freq is None:
        return None
    try:
        f = int(freq)
    except:
        return None

    if 2412 <= f <= 2472:
        return (f - 2407) // 5
    if f == 2484:
        return 14
    if 5180 <= f <= 5825:
        return (f - 5000) // 5
    return None


def get_wifi_termux():
    """Obtém informações oficiais via Termux API (método permitido no Android)."""
    out = run_cmd("termux-wifi-connectioninfo")
    if not out:
        return {}

    try:
        return json.loads(out)
    except:
        return {}


def get_ttl_and_rtt(host="8.8.8.8"):
    """Captura TTL e RTT usando 'ping', que funciona no Android."""
    out = run_cmd(f"ping -c 1 {host}")
    ttl = None
    rtt = None

    if out:
        import re
        m = re.search(r"ttl=(\d+)", out, re.I)
        if m:
            ttl = int(m.group(1))

    try:
        rtt = ping(host, unit="ms")
    except:
        rtt = None

    return ttl, (round(rtt, 3) if rtt else None)


def collect_one():
    """Executa apenas UMA coleta e salva no JSONL."""
    wifi = get_wifi_termux()

    ssid = wifi.get("ssid")
    bssid = wifi.get("bssid")
    freq = wifi.get("frequency_mhz")
    rssi = wifi.get("rssi")
    ip = wifi.get("ip")
    link_speed = wifi.get("link_speed_mbps")
    channel = freq_to_channel(freq)
    oui = ":".join(bssid.split(":")[:3]) if bssid else None

    ttl, rtt = get_ttl_and_rtt()

    payload = {
        "SSID": ssid,
        "BSSID": bssid,
        "Canal": channel,
        "Frequencia (MHz)": freq,
        "OUI": oui,
        "IP": ip,
        "RTT (ms)": rtt,
        "TTL": ttl,
        "RSSI (dBm)": rssi,
        "LINK_SPEED": link_speed,
        "Timestamp": time.time()
    }

    print(json.dumps(payload, ensure_ascii=False, indent=2))

    # Salva no JSONL
    with open(SAVE_PATH, "a", encoding="utf-8") as f:
        f.write(json.dumps(payload, ensure_ascii=False) + "\n")


if _name_ == "_main_":
    collect_one()