package me.pm7.particlelib.particlebuilder;

import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.data.gradient.*;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.particle.Particle;
import me.pm7.particlelib.physics.Gravity;
import me.pm7.particlelib.physics.GravityNone;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Stores data about particles and can build particles with that data
 */
public abstract class ParticleBuilder implements ConfigurationSerializable {
    protected final Random random = new Random();

    protected ValueRange<Integer> particleLifeTicks;
    protected int ticksPerCalculation;
    protected ValueRange<Vector> spawnOffset;
    protected ValueRange<Direction> initialMovementDirection;
    protected Gradient scaleOverLifetime;
    protected ValueRange<Double> rotationOverVelocity;
    protected Gravity gravity;

    /**
     * Creates a particle builder with some default data
     */
    public ParticleBuilder() {
        this.particleLifeTicks = new ValueRange<>(60, 60);
        this.ticksPerCalculation = 1;
        this.spawnOffset = new ValueRange<>(new Vector(0, 0, 0));
        this.initialMovementDirection = new ValueRange<>(new Direction(0, 60), new Direction(360, 120));
        this.scaleOverLifetime = new GradientVector(new Vector(0.125, 0.125, 0.125));
        this.rotationOverVelocity = new ValueRange<>(0.0);
        this.gravity = new GravityNone(new GradientDouble(1.0));
    }

    /**
     * Creates a particle builder with some specific data, not suggested for use.
     */
    protected ParticleBuilder(ValueRange<Integer> particleLifeTicks, int ticksPerCalculation, ValueRange<Vector> spawnOffset, ValueRange<Direction> initialMovementDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity) {
        this.particleLifeTicks = particleLifeTicks;
        this.ticksPerCalculation = ticksPerCalculation;
        this.spawnOffset = spawnOffset;
        this.initialMovementDirection = initialMovementDirection;
        this.scaleOverLifetime = scaleOverLifetime;
        this.rotationOverVelocity = rotationOverVelocity;
        this.gravity = gravity;
    }

    /**
     * Builds a particle with an emitter and location using this builder's data
     * @param emitter the emitter
     * @param location the location
     * @return
     */
    public abstract Particle build(ParticleEmitter emitter, Location location);

    public abstract ParticleBuilder particleLifeTicks(ValueRange<Integer> particleLifeTicks);
    public abstract ParticleBuilder particleLifeTicks(int particleLifeTicks);

    public abstract ParticleBuilder ticksPerCalculation(int ticksPerCalculation);

    public abstract ParticleBuilder spawnOffset(ValueRange<Vector> spawnOffset);
    public abstract ParticleBuilder spawnOffset(Vector spawnOffset);

    /**
     * Sets the initial MOVEMENT direction of the particle to a range of directions. This is NOT the rotation of the particle.
     * @param initialMovementDirection the new initial movement direction
     * @return this builder
     */
    public abstract ParticleBuilder initialMovementDirection(ValueRange<Direction> initialMovementDirection);

    /**
     * Sets the initial MOVEMENT direction of the particle to a singular direction. This is NOT the rotation of the particle.
     * @param initialMovementDirection the new initial movement direction
     * @return this builder
     */
    public abstract ParticleBuilder initialMovementDirection(Direction initialMovementDirection);

    public abstract ParticleBuilder scaleOverLifetime(RangedGradientVector scaleGradient);
    public abstract ParticleBuilder scaleOverLifetime(GradientVector scaleGradient);
    public abstract ParticleBuilder scaleOverLifetime(Vector scale);

    public abstract ParticleBuilder rotationOverVelocity(ValueRange<Double> rotationOverVelocity);

    public abstract ParticleBuilder gravity(Gravity gravity);

    public abstract ParticleBuilder clone();

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "none");
        map.put("particleLifeTicks", particleLifeTicks);
        map.put("ticksPerCalculation", ticksPerCalculation);
        map.put("spawnOffset", spawnOffset);
        map.put("initialMovementDirection", initialMovementDirection);
        map.put("scaleOverLifetime", scaleOverLifetime);
        map.put("rotationOverVelocity", rotationOverVelocity);
        map.put("gravity", gravity);
        return map;
    }
    public ParticleBuilder(Map<String, Object> map) {
        this.particleLifeTicks = (ValueRange<Integer>) map.get("particleLifeTicks");
        this.ticksPerCalculation = (int) map.get("ticksPerCalculation");
        this.spawnOffset = (ValueRange<Vector>) map.get("spawnOffset");
        this.initialMovementDirection = (ValueRange<Direction>) map.get("initialMovementDirection");
        this.scaleOverLifetime = (Gradient) map.get("scaleOverLifetime");
        this.rotationOverVelocity = (ValueRange<Double>) map.get("rotationOverVelocity");
        this.gravity = (Gravity) map.get("gravity");
    }

}
