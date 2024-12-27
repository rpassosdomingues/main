package src;

import javafx.scene.paint.Color;

public class HeroFactory {
    public static Hero createHero(String heroName) {
        switch (heroName) {
            case "Thor":
                return new Thor("Thor", 100, 50, Color.YELLOW, 10.0, 5.0); // Provide necessary parameters
            case "Loki":
                return new Loki("Loki", 90, 40, Color.GREEN, 8.0, 4.0); // Example values for Loki
            case "Heimdall":
                return new Heimdall("Heimdall", 110, 60, Color.GOLD, 12.0, 6.0); // Example values for Heimdall
            case "Feiticeira Negra":
                return new FeiticeiraNegra("Feiticeira Negra", 80, 30, Color.PURPLE, 7.0, 3.0); // Example values
            case "Odin":
                return new Odin("Odin", 120, 70, Color.GRAY, 15.0, 7.0); // Example values for Odin
            default:
                return null;
        }
    }
}
