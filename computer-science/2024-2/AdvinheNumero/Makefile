# Makefile

# Caminhos das pastas
SRC_DIR = src
OUT_DIR = out

# Compilador
JAVAC = javac

# Opções de compilação
JAVACFLAGS = -d $(OUT_DIR)

# Arquivo de entrada principal
MAIN_CLASS = Main

# Encontra todos os arquivos .java na pasta src
SOURCES := $(wildcard $(SRC_DIR)/*.java)

# Regra padrão: compilar e rodar o jogo
all: compile run

# Regra para compilar
compile:
	@mkdir -p $(OUT_DIR)
	$(JAVAC) $(JAVACFLAGS) $(SOURCES)

# Regra para rodar o jogo
run:
	java -cp $(OUT_DIR) $(MAIN_CLASS)

# Limpar a pasta out
clean:
	rm -rf $(OUT_DIR)/*.class

