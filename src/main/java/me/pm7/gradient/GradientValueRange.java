package me.pm7.gradient;

public record GradientValueRange<T>(T min, T max, double position) {

    public GradientValueRange(T value, double position) {
        this(value, value, position);
    }

    public GradientValue<T> interpolate(double position) {
        //TODO: remove this method.
    }

}
