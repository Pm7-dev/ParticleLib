package me.pm7.particlelib.physics;

import me.pm7.particlelib.interpolation.gradient.GradientDouble;
import me.pm7.particlelib.interpolation.gradient.GradientVector;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.particle.Particle;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Gravity that pushes particles in an axis direction. Good for a default gravity system where you just want to make
 * particles move down.
 */
public class GravityDirection extends Gravity implements ConfigurationSerializable {

    private static final Random random = new Random();

    private ValueRange<Double> initialSpeed;
    private GradientVector directionOverLifetime;
    private GradientDouble strengthOverLifetime;
    private GradientDouble bouncinessOverLifetime;
    private double dragMultiplier;

    public GravityDirection(
            ValueRange<Double> initialSpeed,
            GradientVector directionOverLifetime,
            GradientDouble strengthOverLifetime,
            GradientDouble bouncinessOverLifetime,
            double dragMultiplier
    ) {
        this.initialSpeed = initialSpeed;
        this.directionOverLifetime = directionOverLifetime;
        this.strengthOverLifetime = strengthOverLifetime;
        this.bouncinessOverLifetime = bouncinessOverLifetime;
        this.dragMultiplier = dragMultiplier;
    }

    // Simplified constructor
    public GravityDirection() {
        this.initialSpeed = new ValueRange<>(1.0);
        this.directionOverLifetime = new GradientVector(new Vector(0, -1, 0));
        this.strengthOverLifetime = new GradientDouble(0.5);
        this.bouncinessOverLifetime = new GradientDouble(0.5);
        this.dragMultiplier = 1.0;
    }

    @Override
    public void applyGravity(Particle particle, int step) {
        Vector velocity = particle.getVelocity();

        Display display = particle.getDisplay();
        Location loc = display.getLocation().clone();

        if(particle.getTicksLived()-step == 0) {
            particle.getVelocity().multiply(this.getInitialSpeed());
        }

        for(int i=0; i<step; i++) {
            int ticksLived = particle.getTicksLived()-step+i;
            double lifePosition = (double) ticksLived/particle.getLifeTicks();

            Vector velocityPerTick = velocity.clone().multiply(0.05);

            double currentStrength = strengthOverLifetime.interpolate(lifePosition);

            // Check for collisions
            double velocityLength = velocityPerTick.length();
            if (velocityLength > 1e-8 ) {
                var rayTraceResult = loc.getWorld().rayTraceBlocks(loc, velocityPerTick.clone().normalize(), velocityLength*1.1 + particle.getSize()*0.6);
                if (rayTraceResult != null && rayTraceResult.getHitBlock() != null && rayTraceResult.getHitBlockFace() != null) {

                    Vector v = rayTraceResult.getHitPosition();
                    Vector normal = rayTraceResult.getHitBlockFace().getDirection();

                    // Move particle center back by its radius so it “sits” just touching the surface
                    Vector adjusted = v.clone().add(normal.multiply(particle.getSize()*0.35));
                    loc.set(adjusted.getX(), adjusted.getY(), adjusted.getZ());

                    // Bounce
                    bounce(velocity, rayTraceResult.getHitBlockFace(), bouncinessOverLifetime.interpolate(lifePosition), currentStrength);

                    // Exit early to make sure it always bounces on the client side if step is 1
                    continue;
                }
            } else {
                // If the velocity is zero, just make sure to not apply gravity to velocity if particle is resting on a block
                var rayTraceResult = loc.getWorld().rayTraceBlocks(loc, directionOverLifetime.interpolate(lifePosition).normalize(), particle.getSize()/2);
                if (rayTraceResult != null && rayTraceResult.getHitBlock() != null) {
                    continue;
                }
            }

            loc.add(velocityPerTick); // Advance by the current velocity

            // Apply gravity
            Vector gravity = directionOverLifetime.interpolate(lifePosition)
                    .normalize()
                    .multiply(currentStrength * 0.05); // 0.05 because this runs each tick
            velocity.add(gravity);

            // Apply drag
            double densityMultiplier = switch (loc.getBlock().getType()) {
                case WATER -> WATER_DRAG_MULTIPLIER;
                case LAVA -> LAVA_DRAG_MULTIPLIER;
                default -> AIR_DRAG_MULTIPLIER;
            };
            velocity.multiply(1-((1-densityMultiplier)*dragMultiplier));
        }
        particle.teleport(loc);
    }

    @Override
    public double getInitialSpeed() {
        return random.nextDouble() * (initialSpeed.getV2() - initialSpeed.getV1()) + initialSpeed.getV1();
    }

    @Override
    public GravityDirection clone() {
        return new GravityDirection(initialSpeed, directionOverLifetime, strengthOverLifetime, bouncinessOverLifetime, dragMultiplier);
    }

    public GravityDirection initialSpeed(ValueRange<Double> initialSpeed) {this.initialSpeed = initialSpeed;return this;}
    public GravityDirection initialSpeed(double initialSpeed) {this.initialSpeed = new ValueRange<>(initialSpeed);return this;}
    public GravityDirection directionOverLifetime(GradientVector directionOverLifetime) {this.directionOverLifetime = directionOverLifetime;return this;}
    public GravityDirection directionOverLifetime(Vector direction) {this.directionOverLifetime = new GradientVector(direction);return this;}
    public GravityDirection strengthOverLifetime(GradientDouble strengthOverLifetime) {this.strengthOverLifetime = strengthOverLifetime;return this;}
    public GravityDirection strengthOverLifetime(double strength) {this.strengthOverLifetime = new GradientDouble(strength);return this;}
    public GravityDirection bouncinessOverLifetime(GradientDouble bouncinessOverLifetime) {this.bouncinessOverLifetime = bouncinessOverLifetime;return this;}
    public GravityDirection bouncinessOverLifetime(double bounciness) {this.bouncinessOverLifetime = new GradientDouble(bounciness);return this;}
    public GravityDirection dragMultiplier(double dragMultiplier) {this.dragMultiplier = dragMultiplier;return this;}

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "direction");
        map.put("initialSpeed", initialSpeed);
        map.put("directionOverLifetime", directionOverLifetime);
        map.put("strengthOverLifetime", strengthOverLifetime);
        map.put("bouncinessOverLifetime", bouncinessOverLifetime);
        map.put("dragMultiplier", dragMultiplier);
        return map;
    }
    public GravityDirection(Map<String, Object> map) {
        this.initialSpeed = (ValueRange<Double>) map.get("initialSpeed");
        this.directionOverLifetime = (GradientVector) map.get("directionOverLifetime");
        this.strengthOverLifetime = (GradientDouble) map.get("strengthOverLifetime");
        this.bouncinessOverLifetime = (GradientDouble) map.get("bouncinessOverLifetime");
        this.dragMultiplier = (double) map.get("dragMultiplier");
    }
}
