package me.pm7.particlelib.emitter;

import me.pm7.particlelib.particlebuilder.ParticleBuilder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

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
     * @param particlesPerSpawn The number of particles to spawn each time the emitter spawns particles
     * @param ticksPerSpawn The number of ticks to pass between each spawn
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param particleBuilder The particle data to use when this emitter spawns a particle
     */
    public ParticleEmitterConstant(int particlesPerSpawn, int ticksPerSpawn, ParticleBuilder particleBuilder, Location location, long maxParticles, int viewDistance) {
        super(particleBuilder, location, maxParticles, viewDistance);
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
                particleBuilder.build(this, getLocation());
            }
            while (particles.size() > maxParticles) {
                particles.getFirst().remove();
                particles.removeFirst();
            }
        }
    }

    /**
     * Sets the amount of particles to spawn each time the emitter spawns particles
     * @param particlesPerSpawn the new amount.
     */
    public void setParticlesPerSpawn(int particlesPerSpawn) {this.particlesPerSpawn = particlesPerSpawn;}
    public int getParticlesPerSpawn() {return particlesPerSpawn;}

    /**
     * Sets the amount of ticks that must pass before each emitter spawn
     * @param ticksPerSpawn the new amount.
     */
    public void setTicksPerSpawn(int ticksPerSpawn) {this.ticksPerSpawn = ticksPerSpawn;}
    public int getTicksPerSpawn() {return ticksPerSpawn;}

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "constant");
        map.put("particlesPerSpawn", particlesPerSpawn);
        map.put("ticksPerSpawn", ticksPerSpawn);
        return map;
    }
    public ParticleEmitterConstant(Map<String, Object> map) {
        super(map);
        this.particlesPerSpawn = (int) map.get("particlesPerSpawn");
        this.ticksPerSpawn = (int) map.get("ticksPerSpawn");
    }
}
