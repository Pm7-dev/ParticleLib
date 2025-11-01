package me.pm7.particlelib.particledata;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.particle.ParticleText;
import me.pm7.particlelib.particle.Particle;
import me.pm7.particlelib.physics.Gravity;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class ParticleDataSquare extends ParticleData2D {

    public ParticleDataSquare() {
        super();
    }

    private ParticleDataSquare(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Vector> initialDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Double> initialRoll, Gradient rollSpeedOverLifetime, Gradient colorOverLifetime, boolean shaded) {
        super(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRoll, rollSpeedOverLifetime, colorOverLifetime, shaded);
    }

    @Override
    public Particle spawnParticle(ParticleManager manager, Location location) {
        int pLifeticks = random.nextInt(particleLifeTicks.getV1(), particleLifeTicks.getV2() + 1);

        // Bake ranged gradients into gradients if necessary
        GradientVector scale = scaleOverLifetime instanceof RangedGradientVector ranged ? ranged.bake() : (GradientVector) scaleOverLifetime;
        GradientDouble rollSpeed = rollSpeedOverLifetime instanceof RangedGradientDouble ranged ? ranged.bake() : (GradientDouble) rollSpeedOverLifetime;
        GradientColor color = colorOverLifetime instanceof RangedGradientColor ranged ? ranged.bake() : (GradientColor) colorOverLifetime;
        Vector offset = ValueRange.getRandom(spawnOffset);
        double roll = random.nextDouble() * (initialRoll.getV2() - initialRoll.getV1()) + initialRoll.getV1();
        Vector direction = ValueRange.getRandom(initialDirection).normalize();

        return new ParticleText(manager, location, pLifeticks, offset, gravity, direction, scale, roll, rollSpeed, rotationOverVelocity, color, shaded, null);
    }


    // Data specific to 2d particle spawners
    public ParticleDataSquare initialRoll(ValueRange<Double> initialRoll) {this.initialRoll = initialRoll; return this;}
    public ParticleDataSquare initialRoll(double initialRoll) {this.initialRoll = new ValueRange<>(initialRoll); return this;}

    public ParticleDataSquare rollSpeedOverLifetime(RangedGradientDouble rollSpeedOverLifetime) {this.rollSpeedOverLifetime = rollSpeedOverLifetime; return this;}
    public ParticleDataSquare rollSpeedOverLifetime(GradientDouble rollSpeedOverLifetime) {this.rollSpeedOverLifetime = rollSpeedOverLifetime; return this;}
    public ParticleDataSquare rollSpeedOverLifetime(double rollSpeed) {this.rollSpeedOverLifetime = new GradientDouble(rollSpeed); return this;}

    public ParticleDataSquare rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}

    public ParticleDataSquare colorOverLifetime(RangedGradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleDataSquare colorOverLifetime(GradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleDataSquare colorOverLifetime(Color color) {this.colorOverLifetime = new GradientColor(color); return this;}

    public ParticleDataSquare shaded(boolean shaded) {this.shaded = shaded; return this;}


    // Data general to all ParticleSpawners
    public ParticleDataSquare particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}
    public ParticleDataSquare particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    public ParticleDataSquare spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}
    public ParticleDataSquare spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    public ParticleDataSquare initialDirection(ValueRange<Vector> initialDirection) {this.initialDirection = initialDirection; return this;}
    public ParticleDataSquare initialDirection(Vector initialDirection) {this.initialDirection = new ValueRange<>(initialDirection); return this;}

    public ParticleDataSquare scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleDataSquare scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleDataSquare scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    public ParticleDataSquare gravity(Gravity gravity) {this.gravity = gravity; return this;}



    public ParticleDataSquare clone() {
        return new ParticleDataSquare(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRoll, rollSpeedOverLifetime, colorOverLifetime, shaded);
    }

    // Config stuff
    @Override
    public Map<String, Object> serialize() {
        return super.serialize();
    }
    public ParticleDataSquare(Map<String, Object> map) {
        super(map);
    }
}
