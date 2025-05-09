# Diretórios
SRC_DIR := src
INC_DIR := include
BUILD_DIR := build

# Arquivos fonte
SRCS := $(wildcard $(SRC_DIR)/*.cpp)
TARGET := $(BUILD_DIR)/grafos

# Configuração do compilador
CXX := g++
CXXFLAGS := -Wall -Wextra -std=c++17 -I$(INC_DIR)

# Regra padrão
all: $(TARGET)

# Como gerar o binário
$(TARGET): $(SRCS)
	@if not exist $(BUILD_DIR) mkdir $(BUILD_DIR)
	$(CXX) $(CXXFLAGS) $^ -o $@

# Limpar a pasta de build
clean:
	rm -rf $(BUILD_DIR)

# Limpar e recompilar tudo
rebuild: clean all

# Compilar e executar
run: all
	./$(TARGET)