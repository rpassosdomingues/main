package src;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Enemy {
    public enum Difficulty {
        EASY(30, 1.0, 5.0),   // Saúde, massa, velocidade
        MEDIUM(50, 1.5, 10.0),
        HARD(70, 2.0, 15.0);

        private final int health;
        private final double mass; // Massa do inimigo
        private final double speed; // Velocidade do inimigo

        Difficulty(int health, double mass, double speed) {
            this.health = health;
            this.mass = mass; // Inicializa a massa
            this.speed = speed; // Inicializa a velocidade
        }

        public int getHealth() {
            return health; // Retorna a saúde
        }

        public double getMass() {
            return mass; // Retorna a massa
        }

        public double getSpeed() {
            return speed; // Retorna a velocidade
        }
    }

    private String name;
    private int health;
    private double mass; // Massa do inimigo
    private double speed; // Velocidade do inimigo
    private Circle shape; // Forma do inimigo
    private double x; // Posição X do inimigo
    private double y; // Posição Y do inimigo

    public Enemy(String name, Difficulty difficulty, Color color, double x, double y) {
        this.name = name;
        this.health = difficulty.getHealth(); // Define a saúde de acordo com a dificuldade
        this.mass = difficulty.getMass(); // Define a massa de acordo com a dificuldade
        this.speed = difficulty.getSpeed(); // Define a velocidade de acordo com a dificuldade
        this.x = x; // Posição inicial X
        this.y = y; // Posição inicial Y

        // Criar um círculo representando o inimigo
        shape = new Circle(15); // Raio do inimigo
        shape.setFill(color); // Define a cor do círculo
        setPosition(x, y); // Define a posição inicial do inimigo
    }

    public Circle getShape() {
        return shape; // Retorna a forma do inimigo
    }

    public int getHealth() {
        return health; // Retorna a saúde do inimigo
    }

    public String getName() {
        return name; // Retorna o nome do inimigo
    }

    public void takeDamage(int damage) {
        health -= damage; // Reduz a saúde do inimigo
        if (health < 0) {
            health = 0; // Garante que a saúde não fique negativa
        }
    }

    public double getMomentum() {
        return mass * speed; // Retorna o momento do inimigo
    }

    public void moveTowards(double targetX, double targetY) {
        // Move o inimigo em direção à posição do alvo (herói)
        double deltaX = targetX - x;
        double deltaY = targetY - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Normaliza a direção
        if (distance > 0) {
            x += (deltaX / distance) * speed; // Atualiza a posição X
            y += (deltaY / distance) * speed; // Atualiza a posição Y
        }
        
        // Atualiza a posição do círculo
        shape.setTranslateX(x);
        shape.setTranslateY(y);
    }

    public double getX() {
        return x; // Retorna a posição X do inimigo
    }

    public double getY() {
        return y; // Retorna a posição Y do inimigo
    }

    public void setPosition(double x, double y) {
        this.x = x; // Atualiza a posição X do inimigo
        this.y = y; // Atualiza a posição Y do inimigo
        shape.setTranslateX(x); // Atualiza a posição do círculo
        shape.setTranslateY(y); // Atualiza a posição do círculo
    }

    public boolean isAlive() {
        return health > 0; // Retorna se o inimigo ainda está vivo
    }
}
