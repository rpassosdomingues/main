package src;

import javafx.scene.paint.Color;

public class FeiticeiraNegra extends Hero {
    public FeiticeiraNegra(String name, int health, int attackPower, Color color, double mass, double speed) {
        super(name, health, attackPower, color, mass, speed); // Pass the color to the superclass constructor
    }

    @Override
    public void useAbility() {
        // Implementação da habilidade especial da Feiticeira Negra
        System.out.println(name + " usa sua habilidade: Magia Negra!");
    }
}
