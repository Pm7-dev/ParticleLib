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
 * Stores data required to build particles
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
     * Creates a particle builder with some default data, meant to be modified
     */
    public ParticleBuilder() {
        this.particleLifeTicks = new ValueRange<>(60, 60);
        this.ticksPerCalculation = 1;
        this.spawnOffset = new ValueRange<>(new Vector(0, 0, 0));
        this.initialMovementDirection = new ValueRange<>(new Direction(0, 60), new Direction(360, 120));
        this.scaleOverLifetime = new GradientVector(new Vector(1.0, 1.0, 1.0));
        this.rotationOverVelocity = new ValueRange<>(0.0);
        this.gravity = new GravityNone(new GradientDouble(1.0));
    }

    /**
     * Creates a particle builder with specific data, not recommended for use.
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
     * Builds a particle from an emitter and location using this builder's data
     * @param emitter the emitter
     * @param location the location
     * @return the built particle
     */
    public abstract Particle build(ParticleEmitter emitter, Location location);

    /**
     * Sets the particle life ticks of this builder
     * @param particleLifeTicks the range particle life ticks
     * @return this builder
     */
    public abstract ParticleBuilder particleLifeTicks(ValueRange<Integer> particleLifeTicks);

    /**
     * Sets the particle life ticks of this builder
     * @param particleLifeTicks the particle life ticks integer
     * @return this builder
     */
    public abstract ParticleBuilder particleLifeTicks(int particleLifeTicks);

    /**
     * Sets the ticks per calculation for particles spawned by this builder. Defaults to 1, but setting a higher number
     * can drastically reduce the amount of server teleportations and calculations on particles that don't need as fine
     * of detail.
     * @param ticksPerCalculation The ticks per calculation
     * @return this builder
     */
    public abstract ParticleBuilder ticksPerCalculation(int ticksPerCalculation);

    /**
     * Sets the range of position offsets to spawn particles at.
     * @param spawnOffset The range of spawn offsets that particles can have from the center of this emitter
     * @return this builder
     */
    public abstract ParticleBuilder spawnOffset(ValueRange<Vector> spawnOffset);

    /**
     * Sets a particular position offset for particles to spawn at
     * @param spawnOffset The spawn position offset from the center of this emitter
     * @return this builder
     */
    public abstract ParticleBuilder spawnOffset(Vector spawnOffset);

    /**
     * Sets the initial MOVEMENT direction of the particle to a range of directions. This is NOT the initial rotation of the particle.
     * @param initialMovementDirection the new initial movement direction
     * @return this builder
     */
    public abstract ParticleBuilder initialMovementDirection(ValueRange<Direction> initialMovementDirection);

    /**
     * Sets the initial MOVEMENT direction of the particle to a singular direction. This is NOT the initial rotation of the particle.
     * @param initialMovementDirection the new initial movement direction
     * @return this builder
     */
    public abstract ParticleBuilder initialMovementDirection(Direction initialMovementDirection);

    /**
     * Sets the scale over lifetime of particles for this builder in meters
     * @param scaleGradient the scale over lifetime
     * @return this builder
     */
    public abstract ParticleBuilder scaleOverLifetime(RangedGradientVector scaleGradient);

    /**
     * Sets the scale over lifetime of particles for this builder in meters
     * @param scaleGradient the scale over lifetime
     * @return this builder
     */
    public abstract ParticleBuilder scaleOverLifetime(GradientVector scaleGradient);

    /**
     * Sets the scale of particles for this builder in meters
     * @param scale the scale over lifetime
     * @return this builder
     */
    public abstract ParticleBuilder scaleOverLifetime(Vector scale);

    /**
     * Sets the amount that this particle rotates due to velocity. In deg/(m/s) on each axis xyz
     * @param rotationOverVelocity The rotation over velocity
     * @return this builder
     */
    public abstract ParticleBuilder rotationOverVelocity(ValueRange<Double> rotationOverVelocity);

    /**
     * Sets the gravity to use for each particle
     * @param gravity the gravity
     * @return this builder
     */
    public abstract ParticleBuilder gravity(Gravity gravity);

    /**
     * Clones this emitter, keeping all the data
     * @return a clone of this emitter
     */
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
    protected ParticleBuilder(Map<String, Object> map) {
        this.particleLifeTicks = (ValueRange<Integer>) map.get("particleLifeTicks");
        this.ticksPerCalculation = (int) map.get("ticksPerCalculation");
        this.spawnOffset = (ValueRange<Vector>) map.get("spawnOffset");
        this.initialMovementDirection = (ValueRange<Direction>) map.get("initialMovementDirection");
        this.scaleOverLifetime = (Gradient) map.get("scaleOverLifetime");
        this.rotationOverVelocity = (ValueRange<Double>) map.get("rotationOverVelocity");
        this.gravity = (Gravity) map.get("gravity");
    }

}
