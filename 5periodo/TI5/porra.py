import random
import json

def gerar_mac():
    return ":".join(f"{random.randint(0, 255):02x}" for _ in range(6))

def gerar_ip():
    return f"192.168.{random.randint(0, 255)}.{random.randint(1, 254)}"

def gerar_oui():
    return ":".join(f"{random.randint(0, 255):02x}" for _ in range(3))

def gerar_rede_legitima():
    ssid = random.choice([
        "VIVO-Fibra", "NET-WiFi", "ClaroBox", "TIM_5G", "Oi_Fibra",
        "StarlinkHome", "AsusRouter", "TP-Link", "D-Link_5G"
    ])
    return {
        "SSID": ssid,
        "BSSID": gerar_mac(),
        "Canal": random.choice([1, 6, 11, 36, 44, 149]),
        "OUI": gerar_oui(),
        "Auth": random.choice(["WPA2", "WPA3", "WPA2/WPA3"]),
        "MAC": gerar_mac(),
        "IP": gerar_ip(),
        "RTT (ms)": round(random.uniform(2, 40), 3),
        "TTL": random.randint(45, 128),
        "RSSI (dBm)": random.randint(-85, -40),
        "VAR_RSSI": round(random.uniform(0.5, 4.0), 2),
        "Maliciosa": 0
    }

def gerar_rede_maliciosa():
    ssid = random.choice([
        "Free_Public_WiFi", "Airport_Free", "Facebook_Login", "StarbucksWiFi",
        "Hacked_AP", "VIVO-Fibra_", "WiFi_Gratis", "UnknownSSID", "Network_Test"
    ])
    return {
        "SSID": ssid,
        "BSSID": gerar_mac(),
        "Canal": random.choice([3, 5, 7, 9, 13, 40]),
        "OUI": gerar_oui(),
        "Auth": random.choice(["OPEN", "WEP", "Fake_WPA2"]),
        "MAC": gerar_mac(),
        "IP": gerar_ip(),
        "RTT (ms)": round(random.uniform(30, 200), 3),
        "TTL": random.randint(20, 50),
        "RSSI (dBm)": random.randint(-95, -60),
        "VAR_RSSI": round(random.uniform(3.0, 15.0), 2),
        "Maliciosa": 1
    }

# Gera dataset
def gerar_dataset(qtd=1000, proporcao_maliciosas=0.3):
    redes = []
    qtd_mal = int(qtd * proporcao_maliciosas)
    for _ in range(qtd - qtd_mal):
        redes.append(gerar_rede_legitima())
    for _ in range(qtd_mal):
        redes.append(gerar_rede_maliciosa())
    random.shuffle(redes)
    return redes

# Exemplo de uso
dataset = gerar_dataset(1000)
with open("redes_wifi_dataset.json", "w") as f:
    json.dump(dataset, f, indent=4)

print(f"{len(dataset)} registros gerados e salvos em redes_wifi_dataset.json")
