package src;

import javafx.scene.paint.Color;

public class Stage extends Scenario {
    private Enemy.Difficulty difficulty;

    public Stage(Color backgroundColor, Color pathColor, int waveCount, double waveLength, Enemy.Difficulty difficulty) {
        super(backgroundColor, pathColor, waveCount, waveLength);
        this.difficulty = difficulty; // Inicializa a dificuldade
    }

    @Override
    public void setup() {
        createWavePath(waveCount, waveLength); // Cria o caminho da onda
        addPortals();
        //initializeHero(); // Inicializa o herói
        //initializeEnemies(difficulty); // Inicializa os inimigos com a dificuldade especificada
    }
}
