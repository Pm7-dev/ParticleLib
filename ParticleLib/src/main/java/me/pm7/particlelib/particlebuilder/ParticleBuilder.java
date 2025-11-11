package me.pm7.particlelib.particlebuilder;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
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

    //TODO: add ticksPerCalculation here. it will not be fun.

    protected ValueRange<Integer> particleLifeTicks;
    protected ValueRange<Vector> spawnOffset;
    protected ValueRange<Vector> initialDirection;
    protected Gradient scaleOverLifetime;
    protected ValueRange<Double> rotationOverVelocity;
    protected Gravity gravity;

    public ParticleBuilder() {
        this.particleLifeTicks = new ValueRange<>(60, 60);
        this.spawnOffset = new ValueRange<>(new Vector(0, 0, 0));
        this.initialDirection = new ValueRange<>(new Vector(-0.5, 1.0, -0.5), new Vector(0.5, 1.0, 0.5));
        this.scaleOverLifetime = new GradientVector(new Vector(0.125, 0.125, 0.125));
        this.rotationOverVelocity = new ValueRange<>(0.0);
        this.gravity = new GravityNone(new GradientDouble(1.0));
    }

    protected ParticleBuilder(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Vector> initialDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity) {
        this.particleLifeTicks = particleLifeTicks;
        this.spawnOffset = spawnOffset;
        this.initialDirection = initialDirection;
        this.scaleOverLifetime = scaleOverLifetime;
        this.rotationOverVelocity = rotationOverVelocity;
        this.gravity = gravity;
    }

    public abstract Particle build(ParticleEmitter emitter, Location location);

    public abstract ParticleBuilder particleLifeTicks(ValueRange<Integer> particleLifeTicks);
    public abstract ParticleBuilder particleLifeTicks(int particleLifeTicks);

    public abstract ParticleBuilder spawnOffset(ValueRange<Vector> spawnOffset);
    public abstract ParticleBuilder spawnOffset(Vector spawnOffset);

    public abstract ParticleBuilder initialDirection(ValueRange<Vector> initialDirection);
    public abstract ParticleBuilder initialDirection(Vector initialDirection);

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
        map.put("spawnOffset", spawnOffset);
        map.put("initialDirection", initialDirection);
        map.put("scaleOverLifetime", scaleOverLifetime);
        map.put("rotationOverVelocity", rotationOverVelocity);
        map.put("gravity", gravity);
        return map;
    }
    public ParticleBuilder(Map<String, Object> map) {
        this.particleLifeTicks = (ValueRange<Integer>) map.get("particleLifeTicks");
        this.spawnOffset = (ValueRange<Vector>) map.get("spawnOffset");
        this.initialDirection = (ValueRange<Vector>) map.get("initialDirection");
        this.scaleOverLifetime = (Gradient) map.get("scaleOverLifetime");
        this.rotationOverVelocity = (ValueRange<Double>) map.get("rotationOverVelocity");
        this.gravity = (Gravity) map.get("gravity");
    }

}
