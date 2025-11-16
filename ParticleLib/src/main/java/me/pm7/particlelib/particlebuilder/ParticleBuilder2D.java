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

    public ParticleBuilder2D() {
        super();
        this.initialRoll = new ValueRange<>(0.0);
        this.rollSpeedOverLifetime = new GradientDouble(0.0);
        this.colorOverLifetime = new GradientColor(Color.WHITE);
        this.shaded = true;
    }

    protected ParticleBuilder2D(ValueRange<Integer> particleLifeTicks, int ticksPerCalculation, ValueRange<Vector> spawnOffset, ValueRange<Direction> initialMovementDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Double> initialRoll, Gradient rollSpeedOverLifetime, Gradient colorOverLifetime, boolean shaded) {
        super(particleLifeTicks, ticksPerCalculation, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity);
        this.initialRoll = initialRoll;
        this.rollSpeedOverLifetime = rollSpeedOverLifetime;
        this.colorOverLifetime = colorOverLifetime;
    }

    public abstract ParticleBuilder2D initialRoll(ValueRange<Double> initialRoll);
    public abstract ParticleBuilder2D initialRoll(double initialRoll);

    public abstract ParticleBuilder2D rollSpeedOverLifetime(RangedGradientDouble rollSpeedOverLifetime);
    public abstract ParticleBuilder2D rollSpeedOverLifetime(GradientDouble rollSpeedOverLifetime);
    public abstract ParticleBuilder2D rollSpeedOverLifetime(double rollSpeed);

    public abstract ParticleBuilder2D colorOverLifetime(RangedGradientColor colorGradient);
    public abstract ParticleBuilder2D colorOverLifetime(GradientColor colorGradient);
    public abstract ParticleBuilder2D colorOverLifetime(Color color);

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
    public ParticleBuilder2D(Map<String, Object> map) {
        super(map);
        this.initialRoll = (ValueRange<Double>) map.get("initialRoll");
        this.rollSpeedOverLifetime = (Gradient) map.get("rollSpeedOverLifetime");
        this.colorOverLifetime = (Gradient) map.get("colorOverLifetime");
        this.shaded = (boolean) map.get("shaded");
    }
}
