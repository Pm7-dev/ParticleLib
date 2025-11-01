package me.pm7.gradient;

public enum EasingMode {

    SINE_IN_OUT,
    SINE_IN,
    SINE_OUT,

    CUBIC_IN_OUT,
    CUBIC_IN,
    CUBIC_OUT,

    BOUNCE_IN_OUT,
    BOUNCE_IN,
    BOUNCE_OUT,

    LINEAR;

    // Gleefully stolen from easings.net
    public double ease(double x) {
        return switch (this) {
            case SINE_IN_OUT -> -(Math.cos(Math.PI * x) - 1) / 2;
            case SINE_IN -> 1 - Math.cos((x * Math.PI) / 2);
            case SINE_OUT -> Math.sin((x * Math.PI) / 2);

            case CUBIC_IN_OUT -> x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
            case CUBIC_IN -> x * x * x;
            case CUBIC_OUT -> 1 - Math.pow(1 - x, 3);

            case BOUNCE_IN_OUT -> x < 0.5
                    ? (1 - easeOutBounce(1 - 2 * x)) / 2
                    : (1 + easeOutBounce(2 * x - 1)) / 2;
            case BOUNCE_IN -> 1 - easeOutBounce(1 - x);
            case BOUNCE_OUT -> easeOutBounce(x);

            case LINEAR -> x;
        };
    }

    private double easeOutBounce(double x) {
        double n1 = 7.5625;
        double d1 = 2.75;

        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return n1 * (x -= 1.5 / d1) * x + 0.75;
        } else if (x < 2.5 / d1) {
            return n1 * (x -= 2.25 / d1) * x + 0.9375;
        } else {
            return n1 * (x -= 2.625 / d1) * x + 0.984375;
        }
    }

}
