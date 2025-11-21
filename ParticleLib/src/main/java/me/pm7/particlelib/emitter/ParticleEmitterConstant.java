package me.pm7.particlelib.emitter;

import me.pm7.particlelib.particlebuilder.ParticleBuilder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * An emitter of particles bound to the location of an empty display entity. When active, this emitter spawns particles
 * at a specified rate until stoped or removed.
 */
public class ParticleEmitterConstant extends ParticleEmitter {

    private int particlesPerSpawn;
    private int ticksPerSpawn;

    private int loopTick;

    /**
     * Creates a new constant particle emitter
     * @param particlesPerSpawn The number of particles to spawn each time the emitter spawns particles
     * @param ticksPerSpawn The number of ticks to pass between each spawn
     * @param particleBuilder The particle builder to use when this emitter spawns a particle
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param maxParticles The maximum amount of particles this emitter can have before it trims the oldest ones
     * @param viewDistance At least one player must be within this range for the emitter to tick. 0 to disable
     */
    public ParticleEmitterConstant(int particlesPerSpawn, int ticksPerSpawn, ParticleBuilder particleBuilder, Location location, long maxParticles, int viewDistance) {
        super(particleBuilder, location, maxParticles, viewDistance);
        this.particlesPerSpawn = particlesPerSpawn;
        this.ticksPerSpawn = ticksPerSpawn;
        loopTick = ticksPerSpawn;
    }

    /**
     * Creates a new constant particle emitter with a bit less data
     * @param particlesPerSpawn The number of particles to spawn each time the emitter spawns particles
     * @param ticksPerSpawn The number of ticks to pass between each spawn
     * @param particleBuilder The particle builder to use when this emitter spawns a particle
     * @param location The location to spawn the ParticleEmitter's display entity
     */
    public ParticleEmitterConstant(int particlesPerSpawn, int ticksPerSpawn, ParticleBuilder particleBuilder, Location location) {
        super(particleBuilder, location, 0, 0);
        this.particlesPerSpawn = particlesPerSpawn;
        this.ticksPerSpawn = ticksPerSpawn;
        loopTick = ticksPerSpawn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tick() {
        loopTick++;
        if(loopTick >= ticksPerSpawn) {
            loopTick = 0;
            for(int i=0; i<particlesPerSpawn; i++) {
                spawnParticle();
            }
        }
    }

    /**
     * Sets the amount of particles to spawn each time the emitter spawns a group of particles
     * @param particlesPerSpawn amount of particles to spawn
     */
    public void setParticlesPerSpawn(int particlesPerSpawn) {this.particlesPerSpawn = particlesPerSpawn;}
    public int getParticlesPerSpawn() {return particlesPerSpawn;}

    /**
     * Sets the amount of ticks that pass between each group of particles spawned by the emitter
     * @param ticksPerSpawn the tick period to wait between spawns
     */
    public void setTicksPerSpawn(int ticksPerSpawn) {this.ticksPerSpawn = ticksPerSpawn;}
    public int getTicksPerSpawn() {return ticksPerSpawn;}

    /**
     * Serializes this emitter into a config-friendly map of its keys and values. Only meant to be used when saving an
     * emitter to config.
     * @return A map of this emitter's data
     */
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "constant");
        map.put("particlesPerSpawn", particlesPerSpawn);
        map.put("ticksPerSpawn", ticksPerSpawn);
        return map;
    }

    /**
     * Creates a ParticleEmitter from a map of string keys and value. Only meant to be used when loading an emitter from
     * config.
     * @param map the data to load this emitter with
     */
    public ParticleEmitterConstant(Map<String, Object> map) {
        super(map);
        this.particlesPerSpawn = (int) map.get("particlesPerSpawn");
        this.ticksPerSpawn = (int) map.get("ticksPerSpawn");
    }
}
