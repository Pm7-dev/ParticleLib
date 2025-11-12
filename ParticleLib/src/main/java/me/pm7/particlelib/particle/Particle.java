package me.pm7.particlelib.particle;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.interpolation.gradient.GradientVector;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.Random;

public abstract class Particle {

    protected final ParticleEmitter emitter;

    private final Vector spawnLocation;
    private final int lifeTicks;
    protected final Random random;

    private final Gravity gravity;
    protected final GradientVector scaleOverLifetime;
    protected final ValueRange<Double> rotationOverVelocity;
    protected final boolean velocityRotationDirectionPositive;
    protected final Vector velocity; // This gets modified by the gravity

    private int ticksLived;
    protected Display display;
    protected Vector displacement;

    protected Particle(ParticleEmitter emitter, Location location, int lifeTicks, Gravity gravity, Vector initialDirection, GradientVector scaleOverLifetime, ValueRange<Double> rotationOverVelocity) {
        this.emitter = emitter;
        this.random = new Random();

        this.spawnLocation = location.toVector();
        this.lifeTicks = lifeTicks;
        this.ticksLived = 0;
        this.gravity = gravity;

        this.scaleOverLifetime = scaleOverLifetime;
        this.rotationOverVelocity = rotationOverVelocity;

        this.velocity = initialDirection.normalize();

        this.velocityRotationDirectionPositive = random.nextDouble() > 0.5;
    }

    // Ticks the particle a number of steps forward
    public void tick() {

        // If the display is dead, kill this particle
        if(display == null || display.isDead()) {
            remove();
            return;
        }

        // Advance life ticks by steps
        ticksLived+=1;
        if(ticksLived >= lifeTicks) {
            remove();
            return;
        }

        // Tick velocity & apply gravity
        gravity.applyGravity(this, 1);

        // Animate transformation and color
        double lifePosition = getLifePosition();
        display.setInterpolationDelay(0); // I think this has to go before transformations?
        transform(lifePosition, 1);
        color(lifePosition);
    }

    protected abstract void transform(double lifePosition, int steps);

    public abstract void color(double lifePosition);

    public void teleport(Location loc) {
        if(!loc.isChunkLoaded()) {
            remove();
            return;
        }
        displacement = loc.toVector().subtract(display.getLocation().toVector());

        if(displacement.equals(new Vector(0, 0, 0))) return; // no movement
        display.teleport(loc);
    }

    public void remove() {
        display.remove();
        if(emitter == null) {
            ParticleManager.getOrphanedParticles().remove(this);
        } else {
            emitter.getParticles().remove(this);
        }
    }

    public Vector getVelocity() {return velocity;}
    public Double getLifePosition() {return (double) ticksLived / lifeTicks;}
    public double getSize() {
        Vector3f scale = display.getTransformation().getScale();
        return (scale.x + scale.y + scale.z)/3.0d;
    }

    public abstract Display getDisplay();

    public Vector getSpawnLocation() {return spawnLocation.clone();}
    public int getLifeTicks() {return lifeTicks;}
    public int getTicksLived() {return ticksLived;}
}
