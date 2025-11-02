package me.pm7.particlelib.physics;

import me.pm7.particlelib.interpolation.gradient.GradientDouble;
import me.pm7.particlelib.particle.Particle;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GravityNone extends Gravity implements ConfigurationSerializable {

    private GradientDouble speedOverLifetime;

    public GravityNone(GradientDouble speedOverLifetime) {
        this.speedOverLifetime = speedOverLifetime;
    }

    public GravityNone() {
        this.speedOverLifetime = new GradientDouble(1.0);
    }

    @Override
    public void applyGravity(Particle particle, int step) {
        Vector velocity = particle.getVelocity();

        Display display = particle.getDisplay();
        Location loc = display.getLocation().clone();

        for(int i=0; i<step; i++) {
            int ticksLived = particle.getTicksLived()-step+i;
            double lifePosition = (double) ticksLived/particle.getLifeTicks();

            loc.add(velocity.clone().multiply(0.20)); // Advance by the current velocity
            velocity.normalize().multiply(speedOverLifetime.interpolate(lifePosition)); // adjust velocity
        }

        particle.teleport(loc);
    }

    @Override
    public double getInitialSpeed() {
        return speedOverLifetime.getKeyframes().getFirst().value();
    }

    @Override
    public GravityNone clone() {
        return new GravityNone(speedOverLifetime);
    }

    public GravityNone speedOverLifetime(GradientDouble speedOverLifetime) {this.speedOverLifetime = speedOverLifetime;return this;}
    public GravityNone speedOverLifetime(double speed) {this.speedOverLifetime = new GradientDouble(speed);return this;}

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "none");
        map.put("speedOverLifetime", speedOverLifetime);
        return map;
    }
    public GravityNone(Map<String, Object> map) {
        this.speedOverLifetime = (GradientDouble) map.get("speedOverLifetime");
    }
}
