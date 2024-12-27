package src;

import javafx.scene.paint.Color;

public class Loki extends Hero {
    public Loki(String name, int health, int attackPower, Color color, double mass, double speed) {
        super(name, health, attackPower, color, mass, speed); // Pass the color to the superclass constructor
    }

    @Override
    public void useAbility() {
        // Implementação da habilidade especial de Loki
        System.out.println(name + " usa sua habilidade: Ilusão!");
    }
}
