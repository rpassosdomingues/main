package src;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game {
    private final double width = 600;
    private final double height = 400;
    private double ballSpeedX = 3;
    private double ballSpeedY = 3;
    private double paddleSpeed = 6;

    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, width, height, Color.BLACK);

        // Bola
        Circle ball = new Circle(10, Color.WHITE);
        ball.setCenterX(width / 2);
        ball.setCenterY(height / 2);

        // Raquete do jogador
        Rectangle paddle = new Rectangle(100, 10, Color.WHITE);
        paddle.setX(width / 2 - 50);
        paddle.setY(height - 30);

        root.getChildren().addAll(ball, paddle);

        // Controlar a raquete com o teclado
        UserInput userInput = new UserInput(paddle, width, paddleSpeed);
        scene.setOnKeyPressed(userInput::handleKeyPress);

        // Animação da bola
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            ball.setCenterX(ball.getCenterX() + ballSpeedX);
            ball.setCenterY(ball.getCenterY() + ballSpeedY);

            // Verificar colisão com as bordas
            if (ball.getCenterX() <= ball.getRadius() || ball.getCenterX() >= width - ball.getRadius()) {
                ballSpeedX *= -1;
            }

            if (ball.getCenterY() <= ball.getRadius()) {
                ballSpeedY *= -1;
            }

            // Verificar colisão com a raquete
            if (ball.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
                ballSpeedY *= -1;
            }

            // Reiniciar se a bola cair
            if (ball.getCenterY() >= height) {
                ball.setCenterX(width / 2);
                ball.setCenterY(height / 2);
                ballSpeedY *= -1;
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        stage.setTitle("Pong Game");
        stage.setScene(scene);
        stage.show();
    }
}

