package src;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

public class UserInput {
    private final Rectangle paddle;
    private final double gameWidth;
    private final double paddleSpeed;

    public UserInput(Rectangle paddle, double gameWidth, double paddleSpeed) {
        this.paddle = paddle;
        this.gameWidth = gameWidth;
        this.paddleSpeed = paddleSpeed;
    }

    public void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT && paddle.getX() > 0) {
            paddle.setX(paddle.getX() - paddleSpeed);
        } else if (event.getCode() == KeyCode.RIGHT && paddle.getX() + paddle.getWidth() < gameWidth) {
            paddle.setX(paddle.getX() + paddleSpeed);
        }
    }
}

