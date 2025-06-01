package src;

import javafx.scene.paint.Color;

public class StageFactory {

    public static Scenario createStage(int stageNumber, Enemy.Difficulty difficulty) {
        switch (stageNumber) {
            case 1:
                return new Stage(Color.GREEN, Color.BROWN, 2, 150, difficulty); // Passa a dificuldade
            case 2:
                return new Stage(Color.RED, Color.YELLOW, 1, 250, difficulty);
            case 3:
                return new Stage(Color.SKYBLUE, Color.WHITE, 1, 300, difficulty);
            default:
                throw new IllegalArgumentException("Stage number not recognized.");
        }
    }
}
