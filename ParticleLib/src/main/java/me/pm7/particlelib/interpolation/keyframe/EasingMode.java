package me.pm7.particlelib.interpolation.keyframe;

/**
 * Represents different ways to ease along a gradient
 */
public enum EasingMode {

    SINE_IN_OUT,
    SINE_IN,
    SINE_OUT,

    CUBIC_IN_OUT,
    CUBIC_IN,
    CUBIC_OUT,

    LINEAR;

    /**
     * Eases a value from 0.0 to 1.0 using this easing method
     * @param x the position from 0.0 to 1.0
     * @return the modified (eased) position from 0.0 to 1.0
     */
    public double ease(double x) { // Algorithms gleefully stolen from easings.net
        return switch (this) {
            case SINE_IN_OUT -> -(Math.cos(Math.PI * x) - 1) / 2;
            case SINE_IN -> 1 - Math.cos((x * Math.PI) / 2);
            case SINE_OUT -> Math.sin((x * Math.PI) / 2);

            case CUBIC_IN_OUT -> x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
            case CUBIC_IN -> x * x * x;
            case CUBIC_OUT -> 1 - Math.pow(1 - x, 3);

            case LINEAR -> x;
        };
    }

}
