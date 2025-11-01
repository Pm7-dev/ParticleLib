package me.pm7.particlelib.interpolation.gradient;

import me.pm7.particlelib.interpolation.keyframe.EasingMode;

import java.util.List;

/**
 * Represents a list of keyframes to be interpolated between
 */
public abstract class Gradient {

    protected EasingMode easingMode;

    protected Gradient(EasingMode easingMode) {
        this.easingMode = easingMode;
    }

    public abstract List<?> getKeyframes();
}
