package me.pm7.particlelib.emitter;

import me.pm7.particlelib.particlebuilder.ParticleBuilder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * An emitter of particles bound to the location of an empty display entity. When started, this emitter creates a number
 * of particles before removing itself.
 */
public class ParticleEmitterBurst extends ParticleEmitter {

    private final int count;

    /**
     * Creates a new burst particle emitter
     * @param count The number of particles to spawn as part of this burst
     * @param particleBuilder The particle builder to use when this emitter spawns a particle
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param viewDistance At least one player must be within this range for the emitter to tick. 0 to disable
     */
    public ParticleEmitterBurst(int count, ParticleBuilder particleBuilder, Location location, int viewDistance) {
        super(particleBuilder, location, 0, viewDistance);
        this.count = count;
    }

    /**
     * Creates a new burst particle emitter with a bit less data
     * @param count The number of particles to spawn as part of this burst
     * @param particleBuilder The particle builder to use when this emitter spawns a particle
     * @param location The location to spawn the ParticleEmitter's display entity
     */
    public ParticleEmitterBurst(int count, ParticleBuilder particleBuilder, Location location) {
        super(particleBuilder, location, 0, 0);
        this.count = count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tick() {
        for(int i=0; i<count; i++) {
            spawnParticle();
        }
        remove();
    }

    /**
     * Serializes this emitter into a config-friendly map of its keys and values. Only meant to be used when saving an
     * emitter to config.
     * @return A map of this emitter's data
     */
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "burst");
        map.put("count", count);
        return map;
    }

    /**
     * Creates a ParticleEmitter from a map of string keys and value. Only meant to be used when loading an emitter from
     * config.
     * @param map the data to load this emitter with
     */
    public ParticleEmitterBurst(Map<String, Object> map) {
        super(map);
        this.count = (int) map.get("count");
    }
}
