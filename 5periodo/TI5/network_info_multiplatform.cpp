/*
network_info_multiplatform.cpp
Utilitário C++ de arquivo único que coleta informações de rede no Linux, macOS e Windows.
Recursos:
 - Lista interfaces com IPv4/IPv6 e MAC
 - Gateway padrão
 - Servidores DNS
 - Conexões TCP ativas (via utilitários do sistema netstat/ss)
 - Tabela ARP

Compilação:
 - Linux/macOS:  g++ -std=c++17 network_info_multiplatform.cpp -o netinfo
 - Windows (MSVC): g++ network_info_multiplatform.cpp -o netinfo -std=c++17 -Iinclude -liphlpapi -lws2_32
        


Observações:
 - No Windows, Adapte a compilação para MinGW e vincule as bibliotecas apropriadas).
 - Algumas partes utilizam popen() para chamar utilitários do sistema (netstat/ss/arp/route) visando portabilidade.
 - Execute com privilégios apropriados para resultados completos (ex: administrador/root para algumas entradas).
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
#include "include/json.hpp"  //biblioteca JSON para C++ (https://github.com/nlohmann/json)
#include <codecvt>
#include <locale>

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
using json = nlohmann::json;

struct InterfaceInfo {
    string name;
    string ipv4;
    string ipv6;
    string mac;
    string alias;        // novo: apelido ou descrição da interface
    bool is_up;          // novo: se a interface está ativa
    int mtu;             // novo: MTU da interface
    uint64_t rx_packets; // novo: pacotes recebidos
    uint64_t tx_packets; // novo: pacotes enviados
};

// Função auxiliar: executa um comando e captura a saída padrão (stdout)
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
        InterfaceInfo *info = nullptr;
        for (auto &i : res) if (i.name == name) { info = &i; break; }
        if (!info) { res.push_back(InterfaceInfo{}); res.back().name = name; info = &res.back(); 
            // inicializar novos campos
            info->alias = "";
            info->is_up = false;
            info->mtu = 0;
            info->rx_packets = 0;
            info->tx_packets = 0;
        }

        int family = ifa->ifa_addr->sa_family;
        char host[INET6_ADDRSTRLEN];
        if (family == AF_INET) {
            struct sockaddr_in *sa = reinterpret_cast<struct sockaddr_in*>(ifa->ifa_addr);
            if (inet_ntop(AF_INET, &sa->sin_addr, host, sizeof(host))) info->ipv4 = host;
        } else if (family == AF_INET6) {
            struct sockaddr_in6 *sa6 = reinterpret_cast<struct sockaddr_in6*>(ifa->ifa_addr);
            if (inet_ntop(AF_INET6, &sa6->sin6_addr, host, sizeof(host))) info->ipv6 = host;
        }
#if defined(OS_MAC)
        // macOS: MAC via AF_LINK
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
    // obter MAC usando ioctl SIOCGIFHWADDR, igual ao original …
    // além disso, posso pegar MTU e flags (UP/DOWN) e contagem de pacotes lendo /sys/class/net/<iface>/… ou via ioctl.
    int fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (fd >= 0) {
        for (auto &it : res) {
            struct ifreq ifr; memset(&ifr,0,sizeof(ifr));
            strncpy(ifr.ifr_name, it.name.c_str(), IFNAMSIZ-1);
            if (ioctl(fd, SIOCGIFHWADDR, &ifr)==0) {
                unsigned char *mac = reinterpret_cast<unsigned char*>(ifr.ifr_hwaddr.sa_data);
                std::ostringstream oss;
                for (int i=0;i<6;++i) {
                    if (i) oss<<":";
                    oss<<std::hex<<std::setw(2)<<std::setfill('0')<<(int)mac[i];
                }
                it.mac = oss.str();
            }
            // MTU
            memset(&ifr,0,sizeof(ifr));
            strncpy(ifr.ifr_name, it.name.c_str(), IFNAMSIZ-1);
            if (ioctl(fd, SIOCGIFMTU, &ifr)==0) {
                it.mtu = ifr.ifr_mtu;
            }
            // flags para ver se está up
            memset(&ifr,0,sizeof(ifr));
            strncpy(ifr.ifr_name, it.name.c_str(), IFNAMSIZ-1);
            if (ioctl(fd, SIOCGIFFLAGS, &ifr)==0) {
                it.is_up = (ifr.ifr_flags & IFF_UP) != 0;
            }
            // contagem de pacotes RX/TX (via /sys/class/net/)
            string rx_path = "/sys/class/net/"+it.name+"/statistics/rx_packets";
            string tx_path = "/sys/class/net/"+it.name+"/statistics/tx_packets";
            {
                std::ifstream f(rx_path);
                if (f.is_open()) { f >> it.rx_packets; }
            }
            {
                std::ifstream f(tx_path);
                if (f.is_open()) { f >> it.tx_packets; }
            }
        }
        close(fd);
    }
#endif

    freeifaddrs(ifaddr);
    return res;
}
#endif // fim Unix

// Funções genéricas que usam comandos do sistema para conexões e ARP (abordagem portátil)
vector<string> get_tcp_connections() {
    vector<string> lines;
#ifdef OS_WINDOWS
    string out = run_command_capture("netstat -ano");
#elif defined(OS_LINUX)
    // Prefere ss se disponível, caso contrário usa netstat
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

// Função que converte strings locais (ANSI/Windows) para UTF-8 de forma segura
static std::string to_utf8_safe(const std::string &input) {
    #if defined(_WIN32)
        // Converter de CP_ACP (ANSI) -> UTF-16 -> UTF-8
        if (input.empty()) return "";
        int wide_size = MultiByteToWideChar(CP_ACP, 0, input.c_str(), -1, nullptr, 0);
        std::wstring wide_str(wide_size, 0);
        MultiByteToWideChar(CP_ACP, 0, input.c_str(), -1, &wide_str[0], wide_size);
        int utf8_size = WideCharToMultiByte(CP_UTF8, 0, wide_str.c_str(), -1, nullptr, 0, nullptr, nullptr);
        std::string utf8_str(utf8_size, 0);
        WideCharToMultiByte(CP_UTF8, 0, wide_str.c_str(), -1, &utf8_str[0], utf8_size, nullptr, nullptr);
        return utf8_str;
    #else
    // Em Linux/macOS normalmente já está em UTF-8
    return input;
#endif
}
static std::string clean_utf8(const std::string &input) {
    std::string out;
    out.reserve(input.size());
    for (unsigned char c : input) {
        // Mantém apenas bytes válidos UTF-8 (ASCII seguro)
        if ((c >= 0x20 && c <= 0x7E) || c == '\n' || c == '\r' || c == '\t')
            out += c;
    }
    return out;
}

// Cria o objeto JSON com codificação segura
json interfaceInfoToJson(const InterfaceInfo &info) {
    json j;
    j["name"]        = to_utf8_safe(info.name);
    j["alias"]       = to_utf8_safe(info.alias);
    j["is_up"]       = info.is_up;
    j["mtu"]         = info.mtu;
    j["mac"]         = to_utf8_safe(info.mac);
    j["ipv4"]        = to_utf8_safe(info.ipv4);
    j["ipv6"]        = to_utf8_safe(info.ipv6);
    j["rx_packets"]  = info.rx_packets;
    j["tx_packets"]  = info.tx_packets;
    return j;
}

int main() {
    json out;
    // adicionar metadados
    out["timestamp"] = std::chrono::duration_cast<std::chrono::seconds>(
                          std::chrono::system_clock::now().time_since_epoch()
                      ).count();
#if defined(OS_WINDOWS)
    out["os"] = "Windows";
#elif defined(OS_LINUX)
    out["os"] = "Linux";
#elif defined(OS_MAC)
    out["os"] = "macOS";
#else
    out["os"] = "Unknown";
#endif

    auto ifs = list_interfaces();
    json jifs = json::array();
    for (auto &it : ifs) {
        jifs.push_back(interfaceInfoToJson(it));
    }
    out["interfaces"] = jifs;

    auto dns = read_dns_servers();
    json jdns = json::array();
    for (auto &d : dns) {
        jdns.push_back(clean_utf8(d));
    }
    out["dns_servers"] = jdns;

    // gateway padrão: para simplicidade pegar a 1ª interface
    if (!ifs.empty()) {
        out["default_gateway"] = get_default_gateway_for_interface(ifs[0].name);
    } else {
        out["default_gateway"] = nullptr;
    }

    auto tcp = get_tcp_connections();
    json jtcp = json::array();
    for(auto &line : tcp){
        jtcp.push_back(clean_utf8(line));
    }
    out["tcp_table"] = jtcp;

    auto arp = get_arp_table();
    json jarp = json::array();
    for (auto &line : arp) {
        jarp.push_back(clean_utf8(line));
    }
    out["arp_table"] = jarp;

    try {
        std::ofstream ofs("netinfo.json", std::ios::out | std::ios::binary);
        if (ofs.is_open()) {
            ofs << std::setw(4) << out << std::endl;
            ofs.close();
            std::cout << "Dados exportados para netinfo.json\n";
        } else {
            std::cerr << "Erro ao abrir arquivo para escrita\n";
        }
    } catch (const std::exception &e) {
        std::cerr << "Erro ao exportar JSON: " << e.what() << std::endl;
    }

    return 0;
}
