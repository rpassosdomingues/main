package src;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public abstract class Hero {
    protected String name;
    protected int health;
    protected int attackPower;
    protected double mass; // Massa do herói
    protected double speed; // Velocidade do herói
    protected Polygon triangle; // Triângulo representando o herói
    protected double x; // Posição X do herói
    protected double y; // Posição Y do herói

    public Hero(String name, int health, int attackPower, Color color, double mass, double speed) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.mass = mass; // Inicializando a massa
        this.speed = speed; // Inicializando a velocidade
        this.x = 0; // Inicializa a posição X
        this.y = 0; // Inicializa a posição Y

        // Criar um triângulo
        triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
            0.0, -15.0,
            -10.0, 10.0,
            10.0, 10.0
        });
        triangle.setFill(color); // Define a cor do triângulo
    }

    public Polygon getShape() {
        return triangle; // Retorna a forma do herói
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage; // Reduz a saúde do herói
        if (health < 0) {
            health = 0; // Garante que a saúde não fique negativa
        }
    }

    public double getMomentum() {
        return mass * speed; // Retorna o momento
    }

    public void setPosition(double x, double y) {
        this.x = x; // Atualiza a posição X
        this.y = y; // Atualiza a posição Y
        triangle.setTranslateX(x); // Atualiza a posição do triângulo
        triangle.setTranslateY(y); // Atualiza a posição do triângulo
    }

    public double getX() {
        return x; // Retorna a posição X do herói
    }

    public double getY() {
        return y; // Retorna a posição Y do herói
    }

    public void pushBack() {
        x -= 10; // Exemplo de empurrar o herói para trás
        triangle.setTranslateX(x); // Atualiza a posição do triângulo
    }

    // Novo método para obter o nome do herói
    public String getName() {
        return name;
    }

    // Novo método para obter o poder de ataque do herói
    public int getAttackPower() {
        return attackPower;
    }

    // Novo método para obter o nó que representa o herói
    public Polygon getNode() {
        return triangle; // Retorna a forma do herói como nó
    }

    public abstract void useAbility(); // Cada herói tem uma habilidade única.
}
