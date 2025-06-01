# Makefile para o projeto

JAVAC=javac
JAVA=java
JAVAFX=/opt/javafx-21/lib
SRC=src/*.java
OUT=out

all: clean compile run

clean:
	rm -rf $(OUT)/*.class

compile:
	$(JAVAC) --module-path $(JAVAFX) --add-modules javafx.controls,javafx.fxml -d $(OUT) $(SRC)

run:
	$(JAVA) --module-path $(JAVAFX) --add-modules javafx.controls,javafx.fxml -cp $(OUT) src.Main

