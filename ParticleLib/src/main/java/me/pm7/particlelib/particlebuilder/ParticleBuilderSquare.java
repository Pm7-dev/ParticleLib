package me.pm7.particlelib.particlebuilder;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.particle.ParticleText;
import me.pm7.particlelib.particle.Particle;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Stores data about 2D (Text-Display) particles with no text. They are just solid-colour squares.
 */
public class ParticleBuilderSquare extends ParticleBuilder2D {

    public ParticleBuilderSquare() {
        super();
    }

    private ParticleBuilderSquare(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Vector> initialDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Double> initialRoll, Gradient rollSpeedOverLifetime, Gradient colorOverLifetime, boolean shaded) {
        super(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRoll, rollSpeedOverLifetime, colorOverLifetime, shaded);
    }

    @Override
    public Particle build(ParticleEmitter emitter, Location location) {
        int pLifeticks = random.nextInt(particleLifeTicks.getV1(), particleLifeTicks.getV2() + 1);

        // Bake ranged gradients into gradients if necessary
        GradientVector scale = scaleOverLifetime instanceof RangedGradientVector ranged ? ranged.bake() : (GradientVector) scaleOverLifetime;
        GradientDouble rollSpeed = rollSpeedOverLifetime instanceof RangedGradientDouble ranged ? ranged.bake() : (GradientDouble) rollSpeedOverLifetime;
        GradientColor color = colorOverLifetime instanceof RangedGradientColor ranged ? ranged.bake() : (GradientColor) colorOverLifetime;
        Vector offset = ValueRange.getRandom(spawnOffset);
        double roll = random.nextDouble() * (initialRoll.getV2() - initialRoll.getV1()) + initialRoll.getV1();
        Vector direction = ValueRange.getRandom(initialDirection).normalize();

        return new ParticleText(emitter, location, pLifeticks, ticksPerCalculation, offset, gravity, direction, scale, roll, rollSpeed, rotationOverVelocity, color, shaded, null);
    }


    // Data specific to 2d particle spawners
    public ParticleBuilderSquare initialRoll(ValueRange<Double> initialRoll) {this.initialRoll = initialRoll; return this;}
    public ParticleBuilderSquare initialRoll(double initialRoll) {this.initialRoll = new ValueRange<>(initialRoll); return this;}

    public ParticleBuilderSquare rollSpeedOverLifetime(RangedGradientDouble rollSpeedOverLifetime) {this.rollSpeedOverLifetime = rollSpeedOverLifetime; return this;}
    public ParticleBuilderSquare rollSpeedOverLifetime(GradientDouble rollSpeedOverLifetime) {this.rollSpeedOverLifetime = rollSpeedOverLifetime; return this;}
    public ParticleBuilderSquare rollSpeedOverLifetime(double rollSpeed) {this.rollSpeedOverLifetime = new GradientDouble(rollSpeed); return this;}

    public ParticleBuilderSquare rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}

    public ParticleBuilderSquare colorOverLifetime(RangedGradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleBuilderSquare colorOverLifetime(GradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleBuilderSquare colorOverLifetime(Color color) {this.colorOverLifetime = new GradientColor(color); return this;}

    public ParticleBuilderSquare shaded(boolean shaded) {this.shaded = shaded; return this;}


    // Data general to all ParticleSpawners
    public ParticleBuilderSquare particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}
    public ParticleBuilderSquare particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    public ParticleBuilderSquare ticksPerCalculation(int ticksPerCalculation) {this.ticksPerCalculation = ticksPerCalculation; return this;}

    public ParticleBuilderSquare spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}
    public ParticleBuilderSquare spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    public ParticleBuilderSquare initialDirection(ValueRange<Vector> initialDirection) {this.initialDirection = initialDirection; return this;}
    public ParticleBuilderSquare initialDirection(Vector initialDirection) {this.initialDirection = new ValueRange<>(initialDirection); return this;}

    public ParticleBuilderSquare scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleBuilderSquare scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleBuilderSquare scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    public ParticleBuilderSquare gravity(Gravity gravity) {this.gravity = gravity; return this;}



    public ParticleBuilderSquare clone() {
        return new ParticleBuilderSquare(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRoll, rollSpeedOverLifetime, colorOverLifetime, shaded);
    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        return super.serialize();
    }
    public ParticleBuilderSquare(Map<String, Object> map) {
        super(map);
    }
}
