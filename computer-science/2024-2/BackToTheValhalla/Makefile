# Definindo o caminho para o JavaFX e os módulos necessários
JAVAFX_PATH = /opt/javafx-21/lib
JAVAFX_MODULES = javafx.controls,javafx.fxml

# Diretórios de código-fonte e saída
SRC_DIR = src
OUT_DIR = out

# Arquivos fonte .java
SRC_FILES = $(wildcard $(SRC_DIR)/*.java)

# Classe principal
MAIN_CLASS = Main

# Compilador e executável
JAVAC = javac
JAVA = java

# Alvo padrão que limpa, compila e executa a aplicação
all: clean compile run

# Limpar arquivos compilados antigos
clean:
	@echo "Limpando arquivos compilados antigos..."
	rm -rf $(OUT_DIR)/*.class

# Compilar arquivos Java, criando o diretório de saída se não existir
compile: | $(OUT_DIR)
	@echo "Compilando arquivos .java..."
	$(JAVAC) --module-path $(JAVAFX_PATH) --add-modules $(JAVAFX_MODULES) -d $(OUT_DIR) $(SRC_FILES)

# Executar a aplicação
run:
	@echo "Executando a aplicação..."
	$(JAVA) --module-path $(JAVAFX_PATH) --add-modules $(JAVAFX_MODULES) -cp $(OUT_DIR) $(SRC_DIR).$(MAIN_CLASS)

# Criar o diretório de saída se ele não existir
$(OUT_DIR):
	@echo "Criando diretório de saída..."
	mkdir -p $(OUT_DIR)

# Declara que os alvos não são arquivos
.PHONY: all clean compile run
