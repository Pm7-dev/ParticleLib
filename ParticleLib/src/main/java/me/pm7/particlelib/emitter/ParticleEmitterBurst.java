package me.pm7.particlelib.emitter;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.particlebuilder.ParticleBuilder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A Particle Emitter that, once activated, ticks once and deletes itself.
 */
public class ParticleEmitterBurst extends ParticleEmitter {

    private final int count;

    /**
     * Creates a new burst particle emitter
     * @param count The number of particles to spawn as part of this burst
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param particleBuilder The particle data to use when this emitter spawns a particle
     */
    public ParticleEmitterBurst(int count, ParticleBuilder particleBuilder, Location location, int viewDistance) {
        super(particleBuilder, location, 0, viewDistance);
        this.count = count;
    }

    /**
     * Spawns all particles that are part of the burst before removing the emitter
     */
    @Override
    public void tick() {
        for(int i=0; i<count; i++) {
            particleBuilder.build(this, getLocation());
        }
        remove();
    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "burst");
        map.put("count", count);
        return map;
    }
    public ParticleEmitterBurst(Map<String, Object> map) {
        super(map);
        this.count = (int) map.get("count");
    }
}
