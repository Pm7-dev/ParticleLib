package me.pm7.particlelib.emitter;

import me.pm7.particlelib.ParticleLib;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.particle.Particle;
import me.pm7.particlelib.particlebuilder.ParticleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * An emitter of particles bound to the location of an empty display entity
 */
public abstract class ParticleEmitter implements ConfigurationSerializable {

    protected BlockDisplay gameObject;
    protected boolean active;
    protected long maxParticles;
    protected int viewDistance;
    protected final List<Particle> particles;

    protected ParticleBuilder particleBuilder;

    /**
     * Creates a new ParticleEmitter
     * @param particleBuilder The particle builder to use when this emitter spawns a particle
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param maxParticles The maximum amount of particles this emitter can have before it trims the oldest ones
     * @param viewDistance At least one player must be within this range for the emitter to tick. 0 to disable
     */
    public ParticleEmitter(ParticleBuilder particleBuilder, Location location, long maxParticles, int viewDistance) {
        this.particles = new ArrayList<>();

        this.particleBuilder = particleBuilder;

        this.gameObject = (BlockDisplay) location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
        this.gameObject.getPersistentDataContainer().set(ParticleLib.EMITTER_KEY, PersistentDataType.LONG, ParticleLib.SESSION_IDENTIFIER);

        this.maxParticles = maxParticles;
        this.viewDistance = viewDistance;

        this.active = false;

        ParticleManager.getAllEmitters().add(this);
    }

    /**
     * Creates a new ParticleEmitter with a bit less data
     * @param particleBuilder The particle builder to use when this emitter spawns a particle
     * @param location The location to spawn the ParticleEmitter's display entity
     */
    public ParticleEmitter(ParticleBuilder particleBuilder, Location location) {
        this.particles = new ArrayList<>();

        this.particleBuilder = particleBuilder;

        this.gameObject = (BlockDisplay) location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
        this.gameObject.getPersistentDataContainer().set(ParticleLib.EMITTER_KEY, PersistentDataType.LONG, ParticleLib.SESSION_IDENTIFIER);

        this.maxParticles = 0;
        this.viewDistance = 0;

        this.active = false;

        ParticleManager.getAllEmitters().add(this);
    }

    /**
     * Advances the emitter's spawn cycle by one tick. Only meant for internal use but you do you.
     */
    public abstract void tick();

    /**
     * Enables the emitter to be ticked in the ParticleManager's loop
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
     * Spawns a particle at the emitter's location, removing the oldest particle if the maxParticles limit is passed
     */
    public void spawnParticle() {
        particles.add(particleBuilder.build(this, getLocation()));
        if(maxParticles > 0) {
            while (particles.size() > maxParticles) {
                particles.getFirst().remove();
                particles.removeFirst();
            }
        }
    }

    /**
     * Teleports the emitter's entity to a specified location
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
     * @return the maximum number of particles this emitter can have before it starts removing old ones.
     */
    public long getMaxParticles() {return maxParticles;}

    /**
     * Sets the maximum number of particles this emitter can have active before it starts removing old ones.
     * @param maxParticles The new amount of max particles, 0 to disable.
     */
    public void setMaxParticles(long maxParticles) {this.maxParticles = maxParticles;}

    /**
     * Returns the view distance of the emitter. View distance means that the emitter will stop being ticked once there
     * is no longer any player within the distance. This optimization is meant for server lag rather than client lag.
     * @return The view distance of the emitter
     */
    public int getViewDistance() {return viewDistance;}

    /**
     * Sets the view distance of this emitter. View distance means that the emitter will stop being ticked once there
     * is no longer any player within the distance. This optimization is meant for server lag rather than client lag.
     * @param viewDistance The view distance, 0 to disable.
     */
    public void setViewDistance(int viewDistance) {this.viewDistance = viewDistance;}

    /**
     * Removes this emitter and orphans all its particles, sending them to the "orphaned particles" list in ParticleManager
     */
    public void remove() {
        gameObject.remove();

        while (!particles.isEmpty()){
            particles.getFirst().orphan();
        }

        ParticleManager.getAllEmitters().remove(this);
    }

    /**
     * Sets the particle builder of this emitter and returns the emitter. The particle builder stores the data used when
     * spawning particles.
     * @param particleBuilder The new spawn data for the emitter
     * @return The emitter after the change
     */
    public ParticleEmitter getParticleBuilder(ParticleBuilder particleBuilder) {this.particleBuilder = particleBuilder; return this;}

    /**
     * Returns the particle builder of this emitter and returns the emitter. The particle builder stores the data used when
     * spawning particles.
     * @return The particle builder
     */
    public ParticleBuilder getParticleBuilder() {return particleBuilder;}

    /**
     * Returns a list of every alive particle this emitter has spawned
     * @return the list of particles
     */
    public List<Particle> getParticles() {return particles;}

    /**
     * Serializes this emitter into a config-friendly map of its keys and values. Only meant to be used when saving an
     * emitter to config.
     * @return A map of this emitter's data
     */
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "none");
        map.put("uuid", gameObject.getUniqueId().toString());
        map.put("location", getLocation());
        map.put("particleBuilder", particleBuilder);
        map.put("maxParticles", maxParticles);
        map.put("viewDistance", viewDistance);
        return map;
    }

    /**
     * Creates a ParticleEmitter from a map of string keys and value. Only meant to be used when loading an emitter from
     * config.
     * @param map the data to load this emitter with
     */
    public ParticleEmitter(Map<String, Object> map) {
        this.particles = new ArrayList<>();

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

        this.maxParticles = (long) map.get("maxParticles");
        this.viewDistance = (int) map.get("viewDistance");
    }
}
