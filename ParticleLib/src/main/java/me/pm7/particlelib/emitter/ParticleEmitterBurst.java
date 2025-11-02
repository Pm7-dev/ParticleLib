package me.pm7.particlelib.emitter;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.particlebuilder.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
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
     * @param manager The particle manager to tick this emitter
     * @param count The amount of particles to spawn as part of this burst
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param particleBuilder The particle data to use when this emitter spawns a particle
     */
    public ParticleEmitterBurst(ParticleManager manager, int count, Location location, ParticleBuilder particleBuilder) {
        super(manager, location, particleBuilder);
        this.count = count;
    }

    /**
     * Spawns all particles part of the burst before removing the emitter
     */
    @Override
    public void tick() {
        for(int i=0; i<count; i++) {
            spawner.build(manager, getLocation());
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
    public ParticleEmitterBurst(ParticleManager manager, ConfigurationSection section) {
        super(manager, section);
        Map<String, Object> map = section.getValues(false);
        this.count = (int) map.get("count");
    }
}
