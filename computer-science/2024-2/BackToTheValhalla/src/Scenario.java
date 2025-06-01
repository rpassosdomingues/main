package src;

import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public abstract class Scenario {
    protected List<Enemy> enemies;
    protected Hero hero;
    protected int heroHealth;
    private Color backgroundColor;
    private Color pathColor;
    protected int waveCount;
    protected double waveLength;

    StackPane root = new StackPane();

    public Scenario(Color backgroundColor, Color pathColor, int waveCount, double waveLength) {
        this.enemies = new ArrayList<>();
        this.heroHealth = 100;
        this.backgroundColor = backgroundColor;
        this.pathColor = pathColor;
        this.waveCount = waveCount;
        this.waveLength = waveLength;

        root.setPrefSize(600, 600); // Defina a largura e a altura desejadas.
        root.setStyle("-fx-background-color: " + toHexString(backgroundColor) + ";");
    }

    public StackPane getRoot() {
        return root;
    }

    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies);
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
        initializeHero();
    }

    public void createStage(Enemy.Difficulty difficulty) {
        createWavePath(waveCount, waveLength);
        initializeHero();
        initializeEnemies(difficulty);
        startGameLoop(); // Inicia o loop do jogo
    }

    private void createPlace(Color placeColor) {
        Rectangle place = new Rectangle(0, 0, root.getWidth(), root.getHeight());
        place.setFill(placeColor);
        root.getChildren().add(place);
    }

    public void createWavePath(int waveCount, double wavelength) {
        Path wavePath = new Path();
        wavePath.setStroke(pathColor);
        wavePath.setStrokeWidth(30);
        wavePath.getStyleClass().add("wave-path");

        double yPosition = root.getHeight() * 0.8;

        // Define o ponto inicial
        wavePath.getElements().add(new MoveTo(0, yPosition));

        for (int i = 1; i <= waveCount; i++) {
            double midX = i * (wavelength / 2);
            double peakY = yPosition - 20;
            double valleyY = yPosition + 20;

            wavePath.getElements().add(new CubicCurveTo(midX, peakY, midX, valleyY, i * wavelength, yPosition));
        }

        root.getChildren().add(wavePath);
    }

    protected void initializeHero() {
        if (hero == null) {
            hero = HeroFactory.createHero("Thor");
        }
        root.getChildren().add(hero.getNode());
        hero.setPosition(0, 200);
    }

    protected void initializeEnemies(Enemy.Difficulty difficulty) {
        for (int i = 0; i < 5; i++) {
            Enemy enemy = new Enemy("Inimigo" + (i + 1), difficulty, Color.RED, 750, 100 + (i * 50));
            enemies.add(enemy);
            root.getChildren().add(enemy.getShape());
        }
    }

    public void addPortals() {
        double xPositionStart = 100.0; // X position do início do caminho
        double xPositionEnd = 400.0; // X position do fim do caminho
        double yPosition = 300.0; // Y position do caminho

        // Portal no início do caminho
        Portal startPortal = new Portal(xPositionStart, yPosition, 20, Color.RED, Color.BLUE);

        // Portal no final do caminho (ajuste a posição X conforme necessário)
        Portal endPortal = new Portal(xPositionEnd, yPosition, 20, Color.BLUE, Color.RED);

        // Adiciona os portais ao StackPane, garantindo que fiquem no topo
        root.getChildren().add(startPortal);
        root.getChildren().add(endPortal);
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (heroHealth > 0) {
                moveEnemies();
                checkCollisions();
                try {
                    Thread.sleep(100); // Ajuste a taxa de atualização do jogo
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            gameOver();
        }).start();
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            enemy.moveTowards(hero.getX(), hero.getY());
        }
    }

    private void checkCollisions() {
        for (Enemy enemy : enemies) {
            if (isColliding(hero, enemy)) {
                resolveCollision(hero, enemy);
            }
        }
    }

    private boolean isColliding(Hero hero, Enemy enemy) {
        double deltaX = Math.abs(hero.getX() - enemy.getX());
        double deltaY = Math.abs(hero.getY() - enemy.getY());
        return deltaX < 20 && deltaY < 20;
    }

    private void resolveCollision(Hero hero, Enemy enemy) {
        if (enemy.getMomentum() > hero.getMomentum()) {
            heroHealth -= 5;
            if (heroHealth <= 0) {
                gameOver();
            }
        } else {
            enemy.takeDamage(25);
        }
    }

    private void gameOver() {
        root.getChildren().clear();
        System.out.println("Game Over");
    }

    public abstract void setup(); // Cada cenário terá sua configuração específica.
}
