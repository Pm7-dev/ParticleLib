package me.pm7.particlelib.particledata;

import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;


public abstract class ParticleData3D extends ParticleData {

    protected ValueRange<Vector> initialRotation;
    protected Gradient rotationSpeedOverLifetime;

    public ParticleData3D() {
        super();
        this.initialRotation = new ValueRange<>(new Vector(0, 0, 0));
        this.rotationSpeedOverLifetime = new GradientVector(new Vector());
    }

    protected ParticleData3D(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Vector> initialDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Vector> initialRotation, Gradient rotationSpeedOverLifetime) {
        super(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity);
        this.initialRotation = initialRotation;
        this.rotationSpeedOverLifetime = rotationSpeedOverLifetime;
    }

    public abstract ParticleData3D initialRotation(ValueRange<Vector> initialRotation);
    public abstract ParticleData3D initialRotation(Vector initialRotation);

    public abstract ParticleData3D rotationSpeedOverLifetime(RangedGradientVector rotationSpeedOverLifetime);
    public abstract ParticleData3D rotationSpeedOverLifetime(GradientVector rotationSpeedOverLifetime);
    public abstract ParticleData3D rotationSpeedOverLifetime(Vector rotationSpeed);

    // Config stuff
    @Override
    public Map<String, Object> serialize() {
Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "3d");
        map.put("initialRotation", initialRotation);
        map.put("rotationSpeedOverLifetime", rotationSpeedOverLifetime);
        return map;
    }
    public ParticleData3D(Map<String, Object> map) {
        super(map);
        this.initialRotation = (ValueRange<Vector>) map.get("initialRotation");
        this.rotationSpeedOverLifetime = (Gradient) map.get("rotationSpeedOverLifetime");
    }
}
