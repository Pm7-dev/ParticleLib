package me.pm7.particlelib.emitter;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.particlebuilder.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

/**
 * A particle emitter that, while activated, spawns particles at a specified rate
 */
public class ParticleEmitterConstant extends ParticleEmitter {

    private int particlesPerSpawn;
    private int ticksPerSpawn;

    private int loopTick;

    /**
     * Creates a new constant particle emitter
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param particleBuilder The particle data to use when this emitter spawns a particle
     * @param particlesPerSpawn The amount of particles to spawn each time the emitter spawns particles
     * @param ticksPerSpawn The amount of ticks to pass between each spawn
     */
    public ParticleEmitterConstant(ParticleManager manager, Location location, ParticleBuilder particleBuilder, int particlesPerSpawn, int ticksPerSpawn) {
        super(manager, location, particleBuilder);
        this.particlesPerSpawn = particlesPerSpawn;
        this.ticksPerSpawn = ticksPerSpawn;
        loopTick = ticksPerSpawn;
    }

    /**
     * Advances the emitter one tick forward
     */
    @Override
    public void tick() {
        loopTick++;
        if(loopTick >= ticksPerSpawn) {
            loopTick = 0;
            for(int i=0; i<particlesPerSpawn; i++) {
                spawner.build(manager, getLocation());
            }
        }
    }

    /**
     * Sets the amount of particles to spawn each time the emitter spawns particles
     * @param particlesPerSpawn
     */
    public void setParticlesPerSpawn(int particlesPerSpawn) {this.particlesPerSpawn = particlesPerSpawn;}
    public int getParticlesPerSpawn() {return particlesPerSpawn;}

    /**
     * Sets the amount of ticks that must pass before each emitter spawn
     * @param ticksPerSpawn
     */
    public void setTicksPerSpawn(int ticksPerSpawn) {this.ticksPerSpawn = ticksPerSpawn;}
    public int getTicksPerSpawn() {return ticksPerSpawn;}

    // Config stuff
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "constant");
        map.put("particlesPerSpawn", particlesPerSpawn);
        map.put("ticksPerSpawn", ticksPerSpawn);
        return map;
    }
    public ParticleEmitterConstant(ParticleManager manager, ConfigurationSection section) {
        super(manager, section);
        Map<String, Object> map = section.getValues(false);
        this.particlesPerSpawn = (int) map.get("particlesPerSpawn");
        this.ticksPerSpawn = (int) map.get("ticksPerSpawn");
    }
}
