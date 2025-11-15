package me.pm7.particlelib.particlebuilder;

import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.data.gradient.*;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores data about particles rendered using item displays and can build 3D particles with that data
 */
public abstract class ParticleBuilder3D extends ParticleBuilder {

    protected ValueRange<Vector> initialRotation;
    protected boolean velocityOverridesRotation;
    protected Gradient rotationSpeedOverLifetime;

    public ParticleBuilder3D() {
        super();
        this.initialRotation = new ValueRange<>(new Vector(0, 0, 0));
        this.rotationSpeedOverLifetime = new GradientVector(new Vector());
    }

    protected ParticleBuilder3D(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Direction> initialMovementDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Vector> initialRotation, boolean velocityOverridesRotation, Gradient rotationSpeedOverLifetime) {
        super(particleLifeTicks, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity);
        this.initialRotation = initialRotation;
        this.velocityOverridesRotation = velocityOverridesRotation;
        this.rotationSpeedOverLifetime = rotationSpeedOverLifetime;
    }

    /**
     * Sets the initial VISUAL rotation of the particle to a range of rotation. Each component of the vector is one
     * axis of rotation x -> y -> z, in degrees. This is NOT the movement direction of the particle.
     * @param initialRotation the new initial rotation range
     * @return this builder
     */
    public abstract ParticleBuilder3D initialRotation(ValueRange<Vector> initialRotation);
    /**
     * Sets the initial VISUAL rotation of the particle to a single rotation. Each component of the vector is one
     * axis of rotation x -> y -> z, in degrees. This is NOT the movement direction of the particle.
     * @param initialRotation the new initial rotation
     * @return this builder
     */
    public abstract ParticleBuilder3D initialRotation(Vector initialRotation);

    /**
     * Sets whether the velocity of the particle should override the rotation of the particle (particle will face
     * in the direction of the velocity)
     * @param velocityOverridesRotation the value to set
     * @return this builder
     */
    public abstract ParticleBuilder3D velocityOverridesRotation(boolean velocityOverridesRotation);

    public abstract ParticleBuilder3D rotationSpeedOverLifetime(RangedGradientVector rotationSpeedOverLifetime);
    public abstract ParticleBuilder3D rotationSpeedOverLifetime(GradientVector rotationSpeedOverLifetime);
    public abstract ParticleBuilder3D rotationSpeedOverLifetime(Vector rotationSpeed);

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "3d");
        map.put("initialRotation", initialRotation);
        map.put("velocityOverridesRotation", velocityOverridesRotation);
        map.put("rotationSpeedOverLifetime", rotationSpeedOverLifetime);
        return map;
    }
    public ParticleBuilder3D(Map<String, Object> map) {
        super(map);
        this.initialRotation = (ValueRange<Vector>) map.get("initialRotation");
        this.velocityOverridesRotation = (boolean) map.get("velocityOverridesRotation");
        this.rotationSpeedOverLifetime = (Gradient) map.get("rotationSpeedOverLifetime");
    }
}
