package src;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon; // Importando a classe Polygon para o triângulo
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game {
    private final double width = 600;
    private final double height = 400;
    private double amplitude = 50; // A
    private double k = 0.1; // k
    private double w = 1; // ω
    private double time = 0; // Tempo
    private double triangleX = width / 2; // Posição X do personagem
    private boolean isPlaying = false; // Controle de execução
    private Polygon character; // Usando um triângulo como personagem

    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, width, height, Color.BLACK);

        // Personagem (triângulo) com cor preta
        character = new Polygon();
        character.getPoints().addAll(
                -15.0, 15.0,   // Ponto inferior esquerdo
                15.0, 15.0,    // Ponto inferior direito
                0.0, -15.0     // Ponto superior
        );
        character.setFill(Color.BLACK); // Cor do triângulo
        character.setTranslateX(triangleX);
        character.setTranslateY(height / 2);
        root.getChildren().add(character);

        // Sliders
        Slider amplitudeSlider = new Slider(0, 100, amplitude);
        amplitudeSlider.setLayoutX(10);
        amplitudeSlider.setLayoutY(10);
        amplitudeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            amplitude = newValue.doubleValue();
        });

        Slider kSlider = new Slider(0.01, 1, k);
        kSlider.setLayoutX(10);
        kSlider.setLayoutY(40);
        kSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            k = newValue.doubleValue();
        });

        Slider wSlider = new Slider(0.1, 5, w);
        wSlider.setLayoutX(10);
        wSlider.setLayoutY(70);
        wSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            w = newValue.doubleValue();
        });

        // Botão para iniciar o jogo
        Button startButton = new Button("Start");
        startButton.setLayoutX(10);
        startButton.setLayoutY(100);
        startButton.setOnAction(e -> {
            isPlaying = true; // Ativa o movimento
            startButton.setDisable(true); // Desativa o botão
        });

        root.getChildren().addAll(amplitudeSlider, kSlider, wSlider, startButton);

        // Animação do personagem
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            if (isPlaying) {
                time += 0.02; // Incrementa o tempo

                // Calcular a nova posição Y do personagem
                double y = amplitude * Math.sin(k * triangleX - w * time) + height / 2;

                // Atualiza a posição do personagem
                character.setTranslateX(triangleX); // Atualiza a posição X do triângulo
                character.setTranslateY(y); // Atualiza a posição Y do triângulo
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Controlar a posição horizontal do personagem
        scene.setOnKeyPressed(event -> {
            if (isPlaying) { // Verifica se o jogo está em execução
                if (event.getCode() == KeyCode.LEFT && triangleX > character.getBoundsInLocal().getWidth() / 2) {
                    triangleX -= 5; // Mover para a esquerda
                } else if (event.getCode() == KeyCode.RIGHT && triangleX < width - character.getBoundsInLocal().getWidth() / 2) {
                    triangleX += 5; // Mover para a direita
                }
            }
        });

        stage.setTitle("Wave Character Game");
        stage.setScene(scene);
        stage.show();
    }
}

