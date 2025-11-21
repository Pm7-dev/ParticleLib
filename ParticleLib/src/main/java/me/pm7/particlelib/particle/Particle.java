package me.pm7.particlelib.particle;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.data.gradient.GradientVector;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.Random;

/**
 * A particle lol.
 */
public abstract class Particle {

    protected ParticleEmitter parentEmitter;
    protected final Random random;
    private final int lifeTicks;
    private final Vector spawnLocation;

    private final Gravity gravity;
    protected final GradientVector scaleOverLifetime;
    protected final ValueRange<Double> rotationOverVelocity;
    protected final boolean velocityRotationDirectionPositive;
    protected final Vector velocity; // This gets modified by the gravity

    protected final int ticksPerCalculation;
    private int currentTick;
    private int ticksLived;
    protected Vector displacement;
    
    protected Display display;

    /**
     * Makes a new particle. Only meant for internal use but if you have something crazy you should go for it.
     * @param parentEmitter
     * @param location
     * @param lifeTicks
     * @param ticksPerCalculation
     * @param gravity
     * @param initialMovementDirection
     * @param scaleOverLifetime
     * @param rotationOverVelocity
     */
    protected Particle(ParticleEmitter parentEmitter, Location location, int lifeTicks, int ticksPerCalculation, Gravity gravity, Vector initialMovementDirection, GradientVector scaleOverLifetime, ValueRange<Double> rotationOverVelocity) {
        this.parentEmitter = parentEmitter;
        this.random = new Random();
        this.lifeTicks = lifeTicks;
        this.ticksPerCalculation = ticksPerCalculation;
        this.spawnLocation = location.toVector();
        
        this.gravity = gravity;
        this.scaleOverLifetime = scaleOverLifetime;
        this.rotationOverVelocity = rotationOverVelocity;
        this.velocityRotationDirectionPositive = random.nextDouble() > 0.5;
        this.velocity = initialMovementDirection.normalize().multiply(gravity.getInitialSpeed());

        this.currentTick = ticksPerCalculation;
        this.ticksLived = 0;
    }

    /**
     * Ticks the particle one tick forward. Only meant for internal use
     */
    public void tick() {
        currentTick++;
        if(currentTick>=ticksPerCalculation) currentTick=0;
        else return;

        // If the display is dead, kill this particle
        if(display == null || display.isDead()) {
            remove();
            return;
        }

        // Advance life ticks by steps
        ticksLived+=ticksPerCalculation;
        if(ticksLived >= lifeTicks) {
            remove();
            return;
        }

        // Tick velocity & apply gravity
        gravity.applyGravity(this, ticksPerCalculation);

        // Animate transformation and color
        double lifePosition = getLifePosition();
        display.setInterpolationDelay(0); // I think this has to go before transformations?
        transform(lifePosition, ticksPerCalculation);
        color(lifePosition);
    }

    protected abstract void transform(double lifePosition, int steps);

    protected abstract void color(double lifePosition);

    /**
     * Internal use only I guess, do what you want
     * @param loc the location to teleport the particle to
     */
    public void teleport(Location loc) {
        if(!loc.isChunkLoaded()) {
            remove();
            return;
        }
        displacement = loc.toVector().subtract(display.getLocation().toVector());

        if(displacement.equals(new Vector(0, 0, 0))) return; // no movement
        display.teleport(loc);
    }

    /**
     * Move the particle to the orphaned particles list in ParticleManager
     */
    public void orphan() {
        if(parentEmitter != null) {
            parentEmitter.getParticles().remove(this);
            parentEmitter = null;
        }
        ParticleManager.getOrphanedParticles().add(this);
    }

    /**
     * kills the particle and removes it from any lists that keep track of particles
     */
    public void remove() {
        if(display != null) display.remove();
        if(parentEmitter == null) {
            ParticleManager.getOrphanedParticles().remove(this);
        } else {
            parentEmitter.getParticles().remove(this);
        }
    }

    /**
     * Returns the current velocity of the particle
     * @return the vector velocity
     */
    public Vector getVelocity() {return velocity;}

    /**
     * Returns a position from 0.0 to 1.0 of how far the particle is through its life
     * @return the life position
     */
    public double getLifePosition() {return (double) ticksLived / lifeTicks;}

    /**
     * Returns the current scale of the particle
     * @return the scale of the particle
     */
    public double getSize() {
        Vector3f scale = display.getTransformation().getScale();
        return (scale.x + scale.y + scale.z)/3.0d;
    }

    /**
     * Gets the display entity that this particle is visualized with
     * @return the display entity
     */
    public abstract Display getDisplay();

    /**
     * Gets the spawn location of this particle as a vector
     * @return the spawn vector
     */
    public Vector getSpawnLocation() {return spawnLocation.clone();}

    /**
     * Gets the amount of ticks this particle will live for
     * @return the number of ticks this particle lives for
     */
    public int getLifeTicks() {return lifeTicks;}

    /**
     * Gets the amount of ticks this particle has lived
     * @return the number of ticks lived
     */
    public int getTicksLived() {return ticksLived;}
}
