package me.pm7.particlelib.interpolation.gradient;

import me.pm7.particlelib.interpolation.keyframe.EasingMode;

import java.util.Random;

/**
 * Represents a list of Ranged Keyframes to be baked into particles and then interpolated between
 */
public abstract class RangedGradient extends Gradient{
    protected static Random random = new Random();

    protected RangedGradient(EasingMode easingMode) {
        super(easingMode);
    }

    abstract Gradient bake();
}
