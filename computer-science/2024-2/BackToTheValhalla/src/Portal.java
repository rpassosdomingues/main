package src;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

public class Portal extends Circle {
    public Portal(double x, double y, double radius, Color startColor, Color endColor) {
        super(x, y, radius);

        // Gradiente radial com cores personalizadas
        RadialGradient gradient = new RadialGradient(
                0, 0, // proportional radius and center
                0.5, 0.5, // center of the gradient
                radius * 2, // radius
                true, // proportional
                CycleMethod.REPEAT, // cycle method
                new Stop(0, startColor), // start color
                new Stop(1, endColor) // end color
        );
        setFill(gradient);
    }
}
