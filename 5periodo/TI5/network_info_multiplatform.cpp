/*
network_info_multiplatform.cpp
Single-file C++ utility that gathers network information on Linux, macOS and Windows.
Features:
 - List interfaces with IPv4/IPv6 and MAC
 - Default gateway
 - DNS servers
 - Active TCP connections (via platform netstat/ss)
 - ARP table

Build:
 - Linux/macOS:  g++ -std=c++17 network_info_multiplatform.cpp -o netinfo
 - Windows (MSVC): g++ -std=c++17 network_info_multiplatform.cpp -o netinfo.exe -lws2_32 -liphlpapi

Notes:
 - On Windows, compile with Visual Studio Developer Command Prompt (or adapt build to MinGW and link appropriate libs).
 - Some parts use popen() to call system utilities (netstat/ss/arp/route) for portability.
 - Run with appropriate privileges for complete results (e.g., administrator/root for some entries).
*/

#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <iomanip>
#include <fstream>
#include <memory>
#include <cstdio>
#include <cstring>

#if defined(_WIN32) || defined(_WIN64)
#define OS_WINDOWS
#include <winsock2.h>
#include <iphlpapi.h>
#include <ws2tcpip.h>
#pragma comment(lib, "iphlpapi.lib")
#pragma comment(lib, "ws2_32.lib")
#ifndef GAA_FLAG_INCLUDE_PREFIX
#define GAA_FLAG_INCLUDE_PREFIX 0x00000010
#endif
#else
#include <ifaddrs.h>
#include <net/if.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <netinet/in.h>
#include <sys/ioctl.h>
#if defined(__APPLE__)
#define OS_MAC
#include <net/if_dl.h>
#include <net/route.h>
#endif
#if defined(__linux__)
#define OS_LINUX
#include <netpacket/packet.h>
#include <net/ethernet.h>
#endif
#endif

using std::string;
using std::vector;

struct InterfaceInfo {
    string name;
    string ipv4;
    string ipv6;
    string mac;
};

// Helper: run command and capture stdout
static string run_command_capture(const char *cmd) {
    std::string result;
    FILE *fp = popen(cmd, "r");
    if (!fp) return result;
    char buffer[512];
    while (fgets(buffer, sizeof(buffer), fp)) {
        result += buffer;
    }
    pclose(fp);
    return result;
}

#ifdef OS_WINDOWS
static std::string format_mac(const BYTE *mac, ULONG len) {
    std::ostringstream oss;
    for (ULONG i = 0; i < len; ++i) {
        if (i) oss << ":";
        oss << std::hex << std::setw(2) << std::setfill('0') << (int)mac[i];
    }
    return oss.str();
}

vector<InterfaceInfo> list_interfaces() {
    vector<InterfaceInfo> res;
    ULONG flags = GAA_FLAG_INCLUDE_PREFIX;
    ULONG family = AF_UNSPEC;
    ULONG outBufLen = 15000;
    std::unique_ptr<BYTE[]> buffer;
    buffer.reset(new BYTE[outBufLen]);

    IP_ADAPTER_ADDRESSES *addresses = reinterpret_cast<IP_ADAPTER_ADDRESSES*>(buffer.get());
    DWORD ret = GetAdaptersAddresses(family, flags, nullptr, addresses, &outBufLen);
    if (ret == ERROR_BUFFER_OVERFLOW) {
        buffer.reset(new BYTE[outBufLen]);
        addresses = reinterpret_cast<IP_ADAPTER_ADDRESSES*>(buffer.get());
        ret = GetAdaptersAddresses(family, flags, nullptr, addresses, &outBufLen);
    }
    if (ret != NO_ERROR) return res;

    for (IP_ADAPTER_ADDRESSES *ad = addresses; ad; ad = ad->Next) {
        InterfaceInfo info;
        info.name = ad->AdapterName ? ad->AdapterName : "";
        if (ad->PhysicalAddressLength) info.mac = format_mac(ad->PhysicalAddress, ad->PhysicalAddressLength);

        for (IP_ADAPTER_UNICAST_ADDRESS *ua = ad->FirstUnicastAddress; ua; ua = ua->Next) {
            char addrbuf[INET6_ADDRSTRLEN] = {0};
            void *addrptr = nullptr;
            if (ua->Address.lpSockaddr->sa_family == AF_INET) {
                sockaddr_in *sa = reinterpret_cast<sockaddr_in*>(ua->Address.lpSockaddr);
                addrptr = &sa->sin_addr;
            } else if (ua->Address.lpSockaddr->sa_family == AF_INET6) {
                sockaddr_in6 *sa6 = reinterpret_cast<sockaddr_in6*>(ua->Address.lpSockaddr);
                addrptr = &sa6->sin6_addr;
            }
            if (addrptr) {
                inet_ntop(ua->Address.lpSockaddr->sa_family, addrptr, addrbuf, sizeof(addrbuf));
                if (ua->Address.lpSockaddr->sa_family == AF_INET) info.ipv4 = addrbuf;
                else info.ipv6 = addrbuf;
            }
        }
        res.push_back(info);
    }
    return res;
}

string get_default_gateway_for_interface(const string &ifname) {
    // Use GetIpForwardTable
    DWORD size = 0;
    GetIpForwardTable(nullptr, &size, 0);
    std::unique_ptr<BYTE[]> buffer(new BYTE[size]);
    PMIB_IPFORWARDTABLE table = reinterpret_cast<PMIB_IPFORWARDTABLE>(buffer.get());
    if (GetIpForwardTable(table, &size, 0) == NO_ERROR) {
        for (DWORD i = 0; i < table->dwNumEntries; ++i) {
            MIB_IPFORWARDROW &row = table->table[i];
            // destination 0.0.0.0
            if (row.dwForwardDest == 0) {
                in_addr a; a.S_un.S_addr = row.dwForwardNextHop;
                return string(inet_ntoa(a));
            }
        }
    }
    return "";
}

vector<string> read_dns_servers() {
    vector<string> dns;
    FIXED_INFO *fix = nullptr;
    ULONG len = sizeof(FIXED_INFO);
    fix = reinterpret_cast<FIXED_INFO*>(malloc(len));
    if (GetNetworkParams(fix, &len) == ERROR_BUFFER_OVERFLOW) {
        free(fix);
        fix = reinterpret_cast<FIXED_INFO*>(malloc(len));
    }
    if (GetNetworkParams(fix, &len) == NO_ERROR) {
        IP_ADDR_STRING *p = &fix->DnsServerList;
        while (p) {
            dns.push_back(p->IpAddress.String);
            p = p->Next;
        }
    }
    if (fix) free(fix);
    return dns;
}

#else // Unix (Linux/macOS)

vector<InterfaceInfo> list_interfaces() {
    vector<InterfaceInfo> res;
    struct ifaddrs *ifaddr = nullptr;
    if (getifaddrs(&ifaddr) == -1) return res;

    for (struct ifaddrs *ifa = ifaddr; ifa; ifa = ifa->ifa_next) {
        if (!ifa->ifa_addr) continue;
        string name = ifa->ifa_name;
        // find or create
        InterfaceInfo *info = nullptr;
        for (auto &i : res) if (i.name == name) { info = &i; break; }
        if (!info) { res.push_back(InterfaceInfo{}); res.back().name = name; info = &res.back(); }

        int family = ifa->ifa_addr->sa_family;
        char host[INET6_ADDRSTRLEN];
        if (family == AF_INET) {
            struct sockaddr_in *sa = reinterpret_cast<struct sockaddr_in*>(ifa->ifa_addr);
            if (inet_ntop(AF_INET, &sa->sin_addr, host, sizeof(host))) info->ipv4 = host;
        } else if (family == AF_INET6) {
            struct sockaddr_in6 *sa6 = reinterpret_cast<struct sockaddr_in6*>(ifa->ifa_addr);
            if (inet_ntop(AF_INET6, &sa6->sin6_addr, host, sizeof(host))) info->ipv6 = host;
        }

#if defined(OS_LINUX)
        // For linux, we'll later fetch MAC via ioctl
#endif
#if defined(OS_MAC)
        // On macOS, MAC may be available through AF_LINK address
        if (family == AF_LINK) {
            struct sockaddr_dl *sdl = reinterpret_cast<struct sockaddr_dl*>(ifa->ifa_addr);
            unsigned char *mac = reinterpret_cast<unsigned char*>(LLADDR(sdl));
            std::ostringstream oss;
            for (int i = 0; i < sdl->sdl_alen; ++i) {
                if (i) oss << ":";
                oss << std::hex << std::setw(2) << std::setfill('0') << (int)mac[i];
            }
            info->mac = oss.str();
        }
#endif
    }

#if defined(OS_LINUX)
    // get MAC using ioctl SIOCGIFHWADDR
    int fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (fd >= 0) {
        for (auto &it : res) {
            struct ifreq ifr; memset(&ifr, 0, sizeof(ifr));
            strncpy(ifr.ifr_name, it.name.c_str(), IFNAMSIZ-1);
            if (ioctl(fd, SIOCGIFHWADDR, &ifr) == 0) {
                unsigned char *mac = reinterpret_cast<unsigned char*>(ifr.ifr_hwaddr.sa_data);
                std::ostringstream oss;
                for (int i = 0; i < 6; ++i) {
                    if (i) oss << ":";
                    oss << std::hex << std::setw(2) << std::setfill('0') << (int)mac[i];
                }
                it.mac = oss.str();
            }
        }
        close(fd);
    }
#endif

    freeifaddrs(ifaddr);
    return res;
}

string get_default_gateway_for_interface(const string &ifname) {
#ifdef OS_LINUX
    std::ifstream f("/proc/net/route");
    if (!f.is_open()) return "";
    string line;
    std::getline(f, line);
    while (std::getline(f, line)) {
        std::istringstream iss(line);
        string iface, dest, gateway, flags;
        iss >> iface >> dest >> gateway >> flags;
        if (iface != ifname) continue;
        if (dest == "00000000") {
            unsigned long gw = 0; std::stringstream ss; ss << std::hex << gateway; ss >> gw;
            unsigned int b0 = gw & 0xFF; unsigned int b1 = (gw >> 8) & 0xFF;
            unsigned int b2 = (gw >> 16) & 0xFF; unsigned int b3 = (gw >> 24) & 0xFF;
            char buf[INET_ADDRSTRLEN]; snprintf(buf, sizeof(buf), "%u.%u.%u.%u", b0, b1, b2, b3);
            return string(buf);
        }
    }
    return "";
#elif defined(OS_MAC)
    // Use `route -n get default` and parse "gateway: X.X.X.X"
    string out = run_command_capture("route -n get default 2>/dev/null | awk '/gateway:/ {print $2}'");
    if (!out.empty()) {
        // trim
        while (!out.empty() && (out.back()=='\n' || out.back()=='\r' || out.back()==' ')) out.pop_back();
    }
    return out;
#else
    return "";
#endif
}

vector<string> read_dns_servers() {
    vector<string> dns;
#ifdef OS_LINUX
    std::ifstream f("/etc/resolv.conf");
    if (!f.is_open()) return dns;
    string line;
    while (std::getline(f, line)) {
        std::istringstream iss(line);
        string token;
        if (!(iss >> token)) continue;
        if (token == "nameserver") { string addr; iss >> addr; dns.push_back(addr); }
    }
#elif defined(OS_MAC)
    // macOS also often uses /etc/resolv.conf
    std::ifstream f("/etc/resolv.conf");
    if (f.is_open()) {
        string line;
        while (std::getline(f, line)) {
            std::istringstream iss(line);
            string token;
            if (!(iss >> token)) continue;
            if (token == "nameserver") { string addr; iss >> addr; dns.push_back(addr); }
        }
    }
#else
    // Should not reach here
#endif
    return dns;
}

#endif // end unix block

// Generic functions that use system commands for connections & ARP (portable approach)
vector<string> get_tcp_connections() {
    vector<string> lines;
#ifdef OS_WINDOWS
    string out = run_command_capture("netstat -ano");
#elif defined(OS_LINUX)
    // prefer ss if available, fallback to netstat
    string out = run_command_capture("ss -tna 2>/dev/null || netstat -tna");
#elif defined(OS_MAC)
    string out = run_command_capture("netstat -anp tcp");
#else
    string out;
#endif
    std::istringstream iss(out);
    string line;
    while (std::getline(iss, line)) if (!line.empty()) lines.push_back(line);
    return lines;
}

vector<string> get_arp_table() {
    vector<string> lines;
#ifdef OS_WINDOWS
    string out = run_command_capture("arp -a");
#else
    string out = run_command_capture("arp -a");
#endif
    std::istringstream iss(out);
    string line;
    while (std::getline(iss, line)) if (!line.empty()) lines.push_back(line);
    return lines;
}

int main() {
    std::cout << "=== Network Info (multiplatform) ===\n\n";

    auto ifs = list_interfaces();
    std::cout << "Interfaces:\n";
    for (auto &it : ifs) {
        std::cout << " - " << it.name << "\n";
        std::cout << "    MAC : " << (it.mac.empty() ? "(desconhecido)" : it.mac) << "\n";
        std::cout << "    IPv4: " << (it.ipv4.empty() ? "(nenhum)" : it.ipv4) << "\n";
        std::cout << "    IPv6: " << (it.ipv6.empty() ? "(nenhum)" : it.ipv6) << "\n";
#ifdef OS_WINDOWS
        string gw = get_default_gateway_for_interface(it.name);
        if (!gw.empty()) std::cout << "    Default gateway: " << gw << "\n";
#else
        string gw = get_default_gateway_for_interface(it.name);
        if (!gw.empty()) std::cout << "    Default gateway: " << gw << "\n";
#endif
    }

    std::cout << "\nDNS Servers:\n";
#ifdef OS_WINDOWS
    // reuse windows read_dns_servers
    auto dns = read_dns_servers();
#else
    auto dns = read_dns_servers();
#endif
    if (dns.empty()) std::cout << " (nenhum encontrado)\n";
    for (auto &d : dns) std::cout << " - " << d << "\n";

    std::cout << "\nTCP connections (sample):\n";
    auto conns = get_tcp_connections();
    int shown = 0;
    for (auto &l : conns) {
        // skip header lines loosely (show up to 50)
        if (l.find("ESTABLISHED")!=string::npos || l.find("ESTAB")!=string::npos || l.find("LISTEN")!=string::npos || l.find("Local Address")!=string::npos || l.find("Proto")!=string::npos) {
            // print important-looking lines
        }
        if (shown < 50) { std::cout << " - " << l << "\n"; ++shown; } else break;
    }

    std::cout << "\nARP table:\n";
    auto arp = get_arp_table();
    for (auto &l : arp) std::cout << " - " << l << "\n";

    std::cout << "\nFim.\n";
    return 0;
}
