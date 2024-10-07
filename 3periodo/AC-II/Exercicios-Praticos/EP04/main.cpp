// Variáveis globais
int intervalo = 3000; // Intervalo em ms entre as execucoes

// Indicadores de estado
bool dadosCarregados = false; // Indica se os dados foram carregados na memória
bool prontoExecucao = false; // Indica se o sistema está pronto para executar as operações

// Dados
String dadosRecebidos = ""; // Armazena os dados recebidos pelo serial

// Memória e apontadores
const int TAMANHO_MEMORIA = 100; // Tamanho máximo da memória
String memoria[TAMANHO_MEMORIA] = {""}; // Memória simulada
int apontadorEscrita = 4, apontadorLeitura = 4; // Apontadores de leitura e escrita
int opcode, op1, op2; // Operações extraídas da memória
int w; // Armazena o resultado das operações

// Definição dos pinos dos LEDs
const int LEDS[] = {10, 11, 12, 13}; // LEDs S0, S1, S2, S3

void setup() {
  Serial.begin(9600);
  Serial.println("Entre com os dados: \n");

  // Configura os pinos dos LEDs como saída
  for (int i = 0; i < 4; i++) {
    pinMode(LEDS[i], OUTPUT);
  }
}

void loop() {
  // Etapa de carregamento dos dados
  if (!dadosCarregados) {
    if (Serial.available() > 0) {
      dadosRecebidos = Serial.readString(); // Lê os dados inseridos via serial
      Serial.print("Dados Inseridos: ");
      Serial.println(dadosRecebidos);
      dadosCarregados = true; // Marca que os dados foram carregados
    }
  }

  // Quando os dados já estão carregados
  if (dadosCarregados && !prontoExecucao) {
    prontoExecucao = carregarMemoria(dadosRecebidos);
  } else if (prontoExecucao == true) {
    mostraMemoria();
    Serial.println("Insira qualquer caractere para iniciar as operacoes.");
    aguardarInteracaoUsuario();
  }
}

// Função que aguarda a interação do usuário para iniciar a execução
void aguardarInteracaoUsuario() {
  while (true) {
    if (Serial.available() > 0) {
      Serial.read(); // Lê a resposta do usuário (não utilizado diretamente)
      decodificarInstrucao();
      resetaLeds();
      Serial.println("Operacao encerrada.");
      while (true); // Fim da execução
    }
  }
}

// Função que percorre a memória, exibindo o seu conteúdo
void mostraMemoria(){
  Serial.print("> |");
  for(int i = 4; i < sizeof(memoria)/sizeof(memoria[0]) && memoria[i] != ""; i++){
    Serial.print(memoria[i] + "|");
  }
  Serial.println("");
}

// Separa a string de entrada e carrega na memória
bool carregarMemoria(String entrada) {
  bool carregado = true;
  int posicaoInicial = 0;
  int posicaoEspaco = entrada.indexOf(' '); // Localiza o primeiro espaço

  // Processa cada parte da entrada até o final da string
  while (posicaoInicial < entrada.length() && carregado == true) {
    // Se não houver mais espaços, significa que estamos no último valor
    if (posicaoEspaco == -1) {
      posicaoEspaco = entrada.length(); // Define para pegar até o final da string
    }

    // Verifica se a memória está cheia
    if (apontadorEscrita > 99) {
      Serial.println("Erro: Memoria cheia!");
      carregado = false;
    } else {
      // Carrega o pedaço atual da string na memória
      memoria[apontadorEscrita] = entrada.substring(posicaoInicial, posicaoEspaco);
      apontadorEscrita++;
      posicaoInicial = posicaoEspaco + 1;
      posicaoEspaco = entrada.indexOf(' ', posicaoInicial);
    }
  }

  return carregado;
}

// Decodifica e executa a instrução com base no opcode e operandos A e B
void decodificar(int opcode, int A, int B) {
  // Verifica o opcode e realiza a operação correspondente
  switch (opcode) {
    case 0x0: w = 1; break; // 1 lógico
    case 0x1: w = A | ~B; w &= 0xF; break; // A or not B (limita a 4 bits)
    case 0x2: w = A; break; // Copia A
    case 0x3: w = ~(A) ^ ~(B); w &= 0xF; break; // not A xor not B
    case 0x4: w = ~(A & B); w &= 0xF; break; // A nand B
    case 0x5: w = ~A; w &= 0xF; break; // not A
    case 0x6: w = A & ~B; w &= 0xF; break; // A and not B
    case 0x7: w = ~(A) | ~(B); w &= 0xF; break; // not A and not B
    case 0x8: w = A ^ B; break; // A xor B
    case 0x9: w = 0; break; // 0 lógico
    case 0xA: w = B; break; // Copia B
    case 0xB: w = A & B; break; // A and B
    case 0xC: w = ~B; w &= 0xF; break; // not B
    case 0xD: w = ~(~A & B); w &= 0xF; break; // not A nand B
    case 0xE: w = A | B; break; // A or B
    case 0xF: w = ~A & B; w &= 0xF; break; // not A and B
    default: Serial.println("Operacao inesperada!");
  }
}

// Decodifica e executa todas as instruções carregadas na memória
void decodificarInstrucao() {
  apontadorLeitura = 4; // Reinicia o apontador de leitura para o início dos dados

  while (apontadorLeitura < apontadorEscrita) {
    char xChar = memoria[apontadorLeitura].charAt(0);
    char yChar = memoria[apontadorLeitura].charAt(1);
    opcode = hexToDecimal(memoria[apontadorLeitura].charAt(2));
    op1 = hexToDecimal(xChar);
    op2 = hexToDecimal(yChar);

    decodificar(opcode, op1, op2);
    apresentarEstadoMemoria();
    delay(intervalo); // Pausa de 3 segundos entre cada operação

    apontadorLeitura++;
  }
}

// Apresenta o estado da memória no monitor serial
void apresentarEstadoMemoria() {
  Serial.print("|PC: " + String(apontadorLeitura) + " |W: " + String(w) + " |X: " + String(op1) + " |Y: " + String(op2) + "|");
  acendeLeds();
  
  for (int i = 4; i < apontadorEscrita; i++) {
    Serial.print(memoria[i] + "|");
  }
  Serial.println();
}

// Reseta todos os LEDs
void resetaLeds() {
  for (int i = 0; i < 4; i++) {
    digitalWrite(LEDS[i], LOW);
  }
}

// Acende os LEDs conforme o valor de W (4 bits)
void acendeLeds() {
  resetaLeds();
  for (int i = 0; i < 4; i++) {
    if (w & (1 << i)) {
      digitalWrite(LEDS[i], HIGH);
    }
  }
}

// Converte caractere hexadecimal para decimal
int hexToDecimal(char hexChar) {
  if (hexChar >= '0' && hexChar <= '9') {
    return hexChar - '0';
  } else if (hexChar >= 'A' && hexChar <= 'F') {
    return 10 + (hexChar - 'A');
  }
  return 0; // Caso de erro
}