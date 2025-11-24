import pytest
import wifi_metrics
from unittest.mock import patch

def test_get_ip_mac_returns_tuple():
    with patch("psutil.net_if_addrs", return_value={
        "Wi-Fi": [
            type("addr", (), {"family": wifi_metrics.psutil.AF_LINK, "address": "AA:BB:CC:DD:EE:FF"}),
            type("addr", (), {"family": wifi_metrics.socket.AF_INET, "address": "192.168.0.10"})
        ]
    }):
        mac, ip = wifi_metrics.get_ip_mac()
        assert mac == "AA:BB:CC:DD:EE:FF"
        assert ip == "192.168.0.10"

@patch("subprocess.check_output")
def test_get_wifi_info_windows(mock_subprocess):
    mock_output = """
        SSID : MinhaRede
        BSSID : AA:BB:CC:11:22:33
        Channel : 6
        Authentication : WPA2-Personal
    """
    mock_subprocess.return_value = mock_output
    with patch("platform.system", return_value="Windows"):
        result = wifi_metrics.get_wifi_info()
        assert result["SSID"] == "MinhaRede"
        assert result["BSSID"] == "AA:BB:CC:11:22:33"
        assert result["Canal"] == 6
        assert result["OUI"] == "AA:BB:CC"

@patch("subprocess.check_output")
def test_get_rssi_windows(mock_subprocess):
    mock_subprocess.return_value = "Signal : 80%"
    with patch("platform.system", return_value="Windows"):
        rssi = wifi_metrics.get_rssi()
        assert rssi == -60  # (80/2) - 100

@patch("subprocess.check_output")
def test_get_ttl_linux(mock_subprocess):
    mock_subprocess.return_value = "64 bytes from 8.8.8.8: ttl=117 time=15.2 ms"
    with patch("platform.system", return_value="Linux"):
        ttl = wifi_metrics.get_ttl()
        assert ttl == 117
