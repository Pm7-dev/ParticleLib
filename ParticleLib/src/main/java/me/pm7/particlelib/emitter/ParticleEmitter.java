package me.pm7.particlelib.emitter;

import me.pm7.particlelib.ParticleLib;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.particlebuilder.ParticleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * An emitter of particles bound to the location of an an empty display entity
 * <p>
 * NOTE: While this class implements ConfigurationSerializable, when loading any ParticleEmitterConstant from a FileConfiguration
 * you MUST  use the setParticleManager() method, as the ParticleManager is not saved to config.
 */
public abstract class ParticleEmitter implements ConfigurationSerializable {
    protected ParticleManager manager;

    protected BlockDisplay gameObject;
    private boolean active;

    protected ParticleBuilder particleBuilder;

    /**
     * Creates a new ParticleEmitter
     * @param manager The particle manager to tick this emitter
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param particleBuilder The particle data to use when this emitter spawns a particle
     */
    public ParticleEmitter(ParticleManager manager, Location location, ParticleBuilder particleBuilder) {
        this.manager = manager;

        this.gameObject = (BlockDisplay) location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
        this.gameObject.getPersistentDataContainer().set(ParticleLib.EMITTER_KEY, PersistentDataType.LONG, ParticleLib.SESSION_IDENTIFIER);

        this.active = false;
        this.particleBuilder = particleBuilder;

        manager.allEmitters.add(this);
    }

    /**
     * Advances the emitter's spawn cycle by one tick
     */
    public abstract void tick();

    /**
     * Enables the emitter
     */
    public void start() {
        if(getLocation().isChunkLoaded()) tick();
        active = true;
    }

    /**
     * Stops the emitter until it is enabled again with start()
     */
    public void pause() {active = false;}

    /**
     * Checks if the emitter is currently active
     * @return The current status of the emitter
     */
    public boolean isActive() {return active;}

    /**
     * Teleports the emitter to a specified location
     * @param location The location to teleport the emitter to
     */
    public void teleport(Location location) {this.gameObject.teleport(location);}

    /**
     * Gets the current location of the emitter
     * @return The location of the emitter
     */
    public Location getLocation() {return this.gameObject.getLocation();}

    /**
     * Gets the entity this emitter is represented by
     * @return The Display entity of the emitter
     */
    public Display getGameObject() {return gameObject;}

    /**
     * Removes this emitter (Does not remove currently active particles spawned by it)
     */
    public void remove() {
        gameObject.remove();
        manager.allEmitters.remove(this);
    }

    /**
     * Sets the particle spawn data of this emitter and returns the emitter
     * @param particleBuilder The new spawn data for the emitter
     * @return The emitter after the change
     */
    public ParticleEmitter particleData(ParticleBuilder particleBuilder) {this.particleBuilder = particleBuilder; return this;}

    /**
     * Returns the particle spawn data of this emitter
     * @return The particle spawn data
     */
    public ParticleBuilder particleData() {return particleBuilder;}

    public void setParticleManager(ParticleManager manager) {
        if(this.manager != null) this.manager.allEmitters.remove(this);
        this.manager = manager;
        manager.allEmitters.add(this);
    }

    public ParticleManager getParticleManager() {return manager;}

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "none");
        map.put("uuid", gameObject.getUniqueId().toString());
        map.put("location", getLocation());
        map.put("particleBuilder", particleBuilder);
        return map;
    }
    public ParticleEmitter(Map<String, Object> map) {
        this.manager = null;

        Location loc = (Location) map.get("location");
        if(!loc.isChunkLoaded()) loc.getWorld().loadChunk(loc.getChunk());
        this.gameObject = (BlockDisplay) Bukkit.getEntity(UUID.fromString( (String) map.get("uuid")));
        if(gameObject == null) {
            this.gameObject = (BlockDisplay) loc.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
            this.gameObject.getPersistentDataContainer().set(ParticleLib.EMITTER_KEY, PersistentDataType.LONG, ParticleLib.SESSION_IDENTIFIER);
        } else {
            this.gameObject.teleport(loc);
        }

        this.particleBuilder = (ParticleBuilder) map.get("particleBuilder");
        this.active = false;
    }
}
