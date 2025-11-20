package me.pm7.particlelib.physics;

import me.pm7.particlelib.data.gradient.GradientDouble;
import me.pm7.particlelib.data.gradient.GradientVector;
import me.pm7.particlelib.data.keyframe.ValueRange;
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
 * Gravity that pushes particles both towards AND along a defined axis. Gonna be honest I only made this system to
 * recreate the fire particles in the Cymaera video, but I guess this could also be cool for comets, fireworks, lasers,
 * and all sorts of stuff :3
 */
public class GravityAxis extends Gravity implements ConfigurationSerializable {

    private static final Random random = new Random();

    private ValueRange<Double> initialSpeed;
    private GradientVector axisOverLifetime;
    private GradientDouble strengthTowardsAxisOverLifetime;
    private GradientDouble strengthAlongAxisOverLifetime;
    private GradientDouble bouncinessOverLifetime;
    private double dragMultiplier;


    public GravityAxis(
            ValueRange<Double> initialSpeed,
            GradientVector axisOverLifetime,
            GradientDouble strengthTowardsAxisOverLifetime,
            GradientDouble strengthAlongAxisOverLifetime,
            GradientDouble bouncinessOverLifetime,
            double dragMultiplier
    ) {
        this.initialSpeed = initialSpeed;
        this.axisOverLifetime = axisOverLifetime;
        this.strengthTowardsAxisOverLifetime = strengthTowardsAxisOverLifetime;
        this.strengthAlongAxisOverLifetime = strengthAlongAxisOverLifetime;
        this.bouncinessOverLifetime = bouncinessOverLifetime;
        this.dragMultiplier = dragMultiplier;
    }

    // Simplified constructor
    public GravityAxis() {
        this.initialSpeed = new ValueRange<>(1.0);
        this.axisOverLifetime = new GradientVector(new Vector(0, 1, 0));
        this.strengthTowardsAxisOverLifetime = new GradientDouble(0.5);
        this.strengthAlongAxisOverLifetime = new GradientDouble(0.3);
        this.bouncinessOverLifetime = new GradientDouble(0.5);
        this.dragMultiplier = 1.0;
    }

    @Override
    public void applyGravity(Particle particle, int step) {
        Vector velocity = particle.getVelocity();

        Display display = particle.getDisplay();
        Location loc = display.getLocation().clone();

        for(int i=0; i<step; i++) {
            int ticksLived = particle.getTicksLived()-step+i;
            double lifePosition = (double) ticksLived/particle.getLifeTicks();

            Vector velocityPerTick = velocity.clone().multiply(0.05);

            double alongAxisStrength = strengthAlongAxisOverLifetime.interpolate(lifePosition);
            double towardsAxisStrength = strengthTowardsAxisOverLifetime.interpolate(lifePosition);

            // Check for collisions
            if (velocityPerTick.lengthSquared() > 1e-8 ) {
                var rayTraceResult = loc.getWorld().rayTraceBlocks(loc, velocityPerTick.clone().normalize(), velocityPerTick.length() + 0.05);
                if (rayTraceResult != null && rayTraceResult.getHitBlock() != null && rayTraceResult.getHitBlockFace() != null) {
                    Vector v = rayTraceResult.getHitPosition();
                    loc.set(v.getX(), v.getY(), v.getZ());

                    // Bounce
                    bounce(velocity, rayTraceResult.getHitBlockFace(), bouncinessOverLifetime.interpolate(lifePosition),(alongAxisStrength+towardsAxisStrength));

                    // Exit early to make sure it always bounces on the client side if step is 1
                    continue;
                }
            }

            loc.add(velocityPerTick); // Advance by the current velocity

            // Apply gravity
            Vector axis = axisOverLifetime.interpolate(lifePosition).normalize();
            Vector location = loc.toVector().subtract(particle.getSpawnLocation());

            Vector gravity = axis.clone().multiply(alongAxisStrength * 0.05);

            Vector distanceFromAxis = location.subtract(axis.clone().multiply(location.dot(axis))).multiply(-1);
            if(distanceFromAxis.lengthSquared() > 0) gravity.add(distanceFromAxis.normalize().multiply(towardsAxisStrength * 0.05));


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
    public GravityAxis clone() {
        return new GravityAxis(initialSpeed, axisOverLifetime, strengthTowardsAxisOverLifetime, strengthAlongAxisOverLifetime, bouncinessOverLifetime, dragMultiplier);
    }

    public GravityAxis initialSpeed(ValueRange<Double> initialSpeed) {this.initialSpeed = initialSpeed; return this;}
    public GravityAxis initialSpeed(double initialSpeed) {this.initialSpeed = new ValueRange<>(initialSpeed); return this;}
    public GravityAxis axisOverLifetime(GradientVector axisOverLifetime) {this.axisOverLifetime = axisOverLifetime; return this;}
    public GravityAxis axisOverLifetime(Vector axis) {this.axisOverLifetime = new GradientVector(axis); return this;}
    public GravityAxis towardsAxisStrengthOverLifetime(GradientDouble towardsAxisStrengthOverLifetime) {this.strengthTowardsAxisOverLifetime = towardsAxisStrengthOverLifetime; return this;}
    public GravityAxis towardsAxisStrengthOverLifetime(double strength) {this.strengthTowardsAxisOverLifetime = new GradientDouble(strength); return this;}
    public GravityAxis alongAxisStrengthOverLifetime(GradientDouble alongAxisStrengthOverLifetime) {this.strengthAlongAxisOverLifetime = alongAxisStrengthOverLifetime; return this;}
    public GravityAxis alongAxisStrengthOverLifetime(double strength) {this.strengthAlongAxisOverLifetime = new GradientDouble(strength); return this;}
    public GravityAxis bouncinessOverLifetime(GradientDouble bouncinessOverLifetime) {this.bouncinessOverLifetime = bouncinessOverLifetime; return this;}
    public GravityAxis bouncinessOverLifetime(double strength) {this.bouncinessOverLifetime = new GradientDouble(strength); return this;}
    public GravityAxis dragMultiplier(double dragMultiplier) {this.dragMultiplier = dragMultiplier; return this;}

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "axis");
        map.put("initialSpeed", initialSpeed);
        map.put("axisOverLifetime", axisOverLifetime);
        map.put("strengthTowardsAxisOverLifetime", strengthTowardsAxisOverLifetime);
        map.put("strengthAlongAxisOverLifetime", strengthAlongAxisOverLifetime);
        map.put("bouncinessOverLifetime", bouncinessOverLifetime);
        map.put("dragMultiplier", dragMultiplier);
        return map;
    }
    public GravityAxis(Map<String, Object> map) {
        this.initialSpeed = (ValueRange<Double>) map.get("initialSpeed");
        this.axisOverLifetime = (GradientVector) map.get("axisOverLifetime");
        this.strengthTowardsAxisOverLifetime = (GradientDouble) map.get("strengthTowardsAxisOverLifetime");
        this.strengthAlongAxisOverLifetime = (GradientDouble) map.get("strengthAlongAxisOverLifetime");
        this.bouncinessOverLifetime = (GradientDouble) map.get("bouncinessOverLifetime");
        this.dragMultiplier = (double) map.get("dragMultiplier");
    }
}
