package me.pm7.particlelib.particlebuilder;

import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.data.gradient.*;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.Color;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores data about particles rendered using text displays and can build 2D particles with that data
 */
public abstract class ParticleBuilder2D extends ParticleBuilder {

    protected ValueRange<Double> initialRoll;
    protected Gradient rollSpeedOverLifetime;

    protected Gradient colorOverLifetime;

    protected boolean shaded;

    /**
     * Creates a 2D particle builder with some default data, meant to be modified
     */
    public ParticleBuilder2D() {
        super();
        this.initialRoll = new ValueRange<>(0.0);
        this.rollSpeedOverLifetime = new GradientDouble(0.0);
        this.colorOverLifetime = new GradientColor(Color.WHITE);
        this.shaded = true;
    }

    /**
     * Creates a 2D particle builder with specific data, not recommended for use.
     */
    protected ParticleBuilder2D(ValueRange<Integer> particleLifeTicks, int ticksPerCalculation, ValueRange<Vector> spawnOffset, ValueRange<Direction> initialMovementDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Double> initialRoll, Gradient rollSpeedOverLifetime, Gradient colorOverLifetime, boolean shaded) {
        super(particleLifeTicks, ticksPerCalculation, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity);
        this.initialRoll = initialRoll;
        this.rollSpeedOverLifetime = rollSpeedOverLifetime;
        this.colorOverLifetime = colorOverLifetime;
    }

    /**
     * Sets the initial roll rotation of each particle built with this builder to a range of rolls in degrees
     * @param initialRoll the range of rolls for particles to spawn with.
     * @return this builder
     */
    public abstract ParticleBuilder2D initialRoll(ValueRange<Double> initialRoll);

    /**
     * Sets the initial roll rotation of each particle built with this builder to a specific roll in degrees
     * @param initialRoll the roll for particles to spawn with.
     * @return this builder
     */
    public abstract ParticleBuilder2D initialRoll(double initialRoll);

    /**
     * Sets the roll speed over the particle's lifetime in deg/s
     * @param rollSpeedOverLifetime the roll speed over lifetime
     * @return this builder
     */
    public abstract ParticleBuilder2D rollSpeedOverLifetime(RangedGradientDouble rollSpeedOverLifetime);

    /**
     * Sets the roll speed over the particle's lifetime in deg/s
     * @param rollSpeedOverLifetime the roll speed over lifetime
     * @return this builder
     */
    public abstract ParticleBuilder2D rollSpeedOverLifetime(GradientDouble rollSpeedOverLifetime);

    /**
     * Sets the roll speed over the particle's lifetime in deg/s
     * @param rollSpeed the roll speed over lifetime
     * @return this builder
     */
    public abstract ParticleBuilder2D rollSpeedOverLifetime(double rollSpeed);

    /**
     * Sets the color of the particles to change over their lifetime based on the specified color gradient.
     * The provided {@link RangedGradientColor} defines a range of possible colors that
     * will be interpolated over the particle's lifetime.
     * @param colorGradient the ranged gradient of colors to define how the particle's color changes
     *                      over its lifetime
     * @return this builder
     */
    public abstract ParticleBuilder2D colorOverLifetime(RangedGradientColor colorGradient);

    /**
     * Sets the color of the particles to change over their lifetime based on a specified {@code GradientColor}.
     * The {@code GradientColor} defines a gradient of colors that are interpolated and applied throughout the particle’s lifetime.
     *
     * @param colorGradient the gradient of colors to define how the particle's color changes over its lifetime
     * @return this builder
     */
    public abstract ParticleBuilder2D colorOverLifetime(GradientColor colorGradient);

    /**
     * Sets the color of the particles to change over their lifetime to a specific {@code Color}.
     * The color will remain consistent throughout the particle's entire lifetime.
     *
     * @param color the specific color to set for the particles over their lifetime
     * @return this builder
     */
    public abstract ParticleBuilder2D colorOverLifetime(Color color);

    /**
     * Sets whether the particles built with this builder will be shaded or not. For 2D particles, unshaded just means
     * having the display's light set to be 15 block & 15 sky light.
     *
     * @param shaded a boolean indicating whether particles should be shaded.
     * @return this builder.
     */
    public abstract ParticleBuilder2D shaded(boolean shaded);

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "2d");
        map.put("initialRoll", initialRoll);
        map.put("rollSpeedOverLifetime", rollSpeedOverLifetime);
        map.put("colorOverLifetime", colorOverLifetime);
        map.put("shaded", shaded);
        return map;
    }
    protected ParticleBuilder2D(Map<String, Object> map) {
        super(map);
        this.initialRoll = (ValueRange<Double>) map.get("initialRoll");
        this.rollSpeedOverLifetime = (Gradient) map.get("rollSpeedOverLifetime");
        this.colorOverLifetime = (Gradient) map.get("colorOverLifetime");
        this.shaded = (boolean) map.get("shaded");
    }
}
