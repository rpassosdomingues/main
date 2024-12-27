package src;

import javafx.scene.paint.Color;

public class Odin extends Hero {
    public Odin(String name, int health, int attackPower, Color color, double mass, double speed) {
        super(name, health, attackPower, color, mass, speed); // Pass the color to the superclass constructor
    }

    @Override
    public void useAbility() {
        // Implementação da habilidade especial de Odin
        System.out.println(name + " usa sua habilidade: Sabedoria Eterna!");
    }
}
