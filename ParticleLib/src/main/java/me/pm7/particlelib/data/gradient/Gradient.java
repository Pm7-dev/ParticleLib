package me.pm7.particlelib.data.gradient;

import me.pm7.particlelib.data.keyframe.EasingMode;

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
