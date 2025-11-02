package me.pm7.particlelib.physics;

import me.pm7.particlelib.interpolation.gradient.GradientDouble;
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
 * Gravity that pushes particles towards a relative (offset from the particle's spawn location) center. Simulates gravitational pull around a point.
 * Might be good for planetary motion but idk why you'd be using a particle library for that. play around with this and have fun I guess :D
 */
public class GravityLocation extends Gravity implements ConfigurationSerializable {

    private static final Random random = new Random();

    private ValueRange<Double> initialSpeed;
    private Vector relativeCenterOfGravity;
    private GradientDouble strengthOverLifetime;
    private GradientDouble bouncinessOverLifetime;
    private double dragMultiplier;

    public GravityLocation(
            ValueRange<Double> initialSpeed,
            Vector relativeCenterOfGravity,
            GradientDouble strengthOverLifetime,
            GradientDouble bouncinessOverLifetime,
            double dragMultiplier
    ) {
        this.initialSpeed = initialSpeed;
        this.relativeCenterOfGravity = relativeCenterOfGravity;
        this.strengthOverLifetime = strengthOverLifetime;
        this.bouncinessOverLifetime = bouncinessOverLifetime;
        this.dragMultiplier = dragMultiplier;
    }

    // Simplified constructor
    public GravityLocation() {
        this.initialSpeed = new ValueRange<>(1.0);
        this.relativeCenterOfGravity = new Vector(0, 1, 0);
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
            if (velocityPerTick.lengthSquared() > 1e-8 ) {
                var rayTraceResult = loc.getWorld().rayTraceBlocks(loc, velocityPerTick.clone().normalize(), velocityPerTick.length() + 0.05);
                if (rayTraceResult != null && rayTraceResult.getHitBlock() != null && rayTraceResult.getHitBlockFace() != null) {
                    Vector v = rayTraceResult.getHitPosition();
                    loc.set(v.getX(), v.getY(), v.getZ());

                    // Bounce
                    bounce(velocity, rayTraceResult.getHitBlockFace(), bouncinessOverLifetime.interpolate(lifePosition), currentStrength);

                    // Exit early to make sure it always bounces on the client side if step is 1
                    continue;
                }
            }


            loc.add(velocityPerTick); // Advance by the current velocity
            Vector centerOfGravity = particle.getSpawnLocation().add(relativeCenterOfGravity);

            double distance = centerOfGravity.distance(loc.toVector());
            double power = (currentStrength)/(distance * distance);

            // Apply gravity
            Vector direction = centerOfGravity.subtract(loc.toVector()).normalize();
            direction.multiply(power * 0.05d);
            velocity.add(direction);

            // Apply drag
            double densityMultiplier = switch (loc.getBlock().getType()) {
                case WATER -> WATER_DRAG_MULTIPLIER;
                case LAVA -> LAVA_DRAG_MULTIPLIER;
                default -> AIR_DRAG_MULTIPLIER;
            };
            velocity.multiply(densityMultiplier * dragMultiplier);
        }
        particle.teleport(loc);
    }

    @Override
    public double getInitialSpeed() {
        return random.nextDouble() * (initialSpeed.getV2() - initialSpeed.getV1()) + initialSpeed.getV1();
    }

    @Override
    public GravityLocation clone() {
        return new GravityLocation(initialSpeed, relativeCenterOfGravity, strengthOverLifetime, bouncinessOverLifetime, dragMultiplier);
    }

    public GravityLocation initialSpeed(ValueRange<Double> initialSpeed) {this.initialSpeed = initialSpeed;return this;}
    public GravityLocation initialSpeed(double initialSpeed) {this.initialSpeed = new ValueRange<>(initialSpeed);return this;}
    public GravityLocation relativeCenterOfGravity(Vector relativeCenterOfGravity) {this.relativeCenterOfGravity = relativeCenterOfGravity;return this;}
    public GravityLocation strengthOverLifetime(GradientDouble strengthOverLifetime) {this.strengthOverLifetime = strengthOverLifetime;return this;}
    public GravityLocation strengthOverLifetime(double strength) {this.strengthOverLifetime = new GradientDouble(strength);return this;}
    public GravityLocation bouncinessOverLifetime(GradientDouble bouncinessOverLifetime) {this.bouncinessOverLifetime = bouncinessOverLifetime;return this;}
    public GravityLocation bouncinessOverLifetime(double bounciness) {this.bouncinessOverLifetime = new GradientDouble(bounciness);return this;}
    public GravityLocation dragMultiplier(double dragMultiplier) {this.dragMultiplier = dragMultiplier;return this;}

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "location");
        map.put("initialSpeed", initialSpeed);
        map.put("relativeCenterOfGravity", relativeCenterOfGravity);
        map.put("strengthOverLifetime", strengthOverLifetime);
        map.put("bouncinessOverLifetime", bouncinessOverLifetime);
        map.put("dragMultiplier", dragMultiplier);
        return map;
    }
    public GravityLocation(Map<String, Object> map) {
        this.initialSpeed = (ValueRange<Double>) map.get("initialSpeed");
        this.relativeCenterOfGravity = (Vector) map.get("relativeCenterOfGravity");
        this.strengthOverLifetime = (GradientDouble) map.get("strengthOverLifetime");
        this.bouncinessOverLifetime = (GradientDouble) map.get("bouncinessOverLifetime");
        this.dragMultiplier = (double) map.get("dragMultiplier");
    }
}
