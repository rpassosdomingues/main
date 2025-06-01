# Makefile

JAVAC = javac
JAVA = java
MODULE_PATH = /opt/javafx-21/lib
MODULES = javafx.controls,javafx.fxml

SRC = src/Game.java src/Main.java src/UserInput.java
OUT_DIR = out
CLASS_FILES = $(SRC:src/%.java=$(OUT_DIR)/%.class)

all: clean compile run

clean:
	rm -rf $(OUT_DIR)/*.class

compile: $(CLASS_FILES)

$(OUT_DIR)/%.class: src/%.java
	$(JAVAC) --module-path $(MODULE_PATH) --add-modules $(MODULES) -d $(OUT_DIR) $<

run:
	$(JAVA) --module-path $(MODULE_PATH) --add-modules $(MODULES) -cp $(OUT_DIR) src.Main

.PHONY: all clean compile run

