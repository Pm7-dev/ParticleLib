package me.pm7.particlelib.particledata;

import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.Color;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public abstract class ParticleData2D extends ParticleData {

    protected ValueRange<Double> initialRoll;
    protected Gradient rollSpeedOverLifetime;

    protected Gradient colorOverLifetime;

    protected boolean shaded;

    public ParticleData2D() {
        super();
        this.initialRoll = new ValueRange<>(0.0);
        this.rollSpeedOverLifetime = new GradientDouble(0.0);
        this.colorOverLifetime = new GradientColor(Color.WHITE);
        this.shaded = true;
    }

    protected ParticleData2D(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Vector> initialDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Double> initialRoll, Gradient rollSpeedOverLifetime, Gradient colorOverLifetime, boolean shaded) {
        super(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity);
        this.initialRoll = initialRoll;
        this.rollSpeedOverLifetime = rollSpeedOverLifetime;
        this.colorOverLifetime = colorOverLifetime;
    }

    public abstract ParticleData2D initialRoll(ValueRange<Double> initialRoll);
    public abstract ParticleData2D initialRoll(double initialRoll);

    public abstract ParticleData2D rollSpeedOverLifetime(RangedGradientDouble rollSpeedOverLifetime);
    public abstract ParticleData2D rollSpeedOverLifetime(GradientDouble rollSpeedOverLifetime);
    public abstract ParticleData2D rollSpeedOverLifetime(double rollSpeed);

    public abstract ParticleData2D colorOverLifetime(RangedGradientColor colorGradient);
    public abstract ParticleData2D colorOverLifetime(GradientColor colorGradient);
    public abstract ParticleData2D colorOverLifetime(Color color);

    public abstract ParticleData2D shaded(boolean shaded);

    // Config stuff
    @Override
    public Map<String, Object> serialize() {
Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "2d");
        map.put("initialRoll", initialRoll);
        map.put("rollSpeedOverLifetime", rollSpeedOverLifetime);
        map.put("colorOverLifetime", colorOverLifetime);
        map.put("shaded", shaded);
        return map;
    }
    public ParticleData2D(Map<String, Object> map) {
        super(map);
        this.initialRoll = (ValueRange<Double>) map.get("initialRoll");
        this.rollSpeedOverLifetime = (Gradient) map.get("rollSpeedOverLifetime");
        this.colorOverLifetime = (Gradient) map.get("colorOverLifetime");
        this.shaded = (boolean) map.get("shaded");
    }
}
