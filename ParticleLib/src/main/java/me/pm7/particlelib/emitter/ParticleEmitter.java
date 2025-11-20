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
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param particleBuilder The particle data to use when this emitter spawns a particle
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
     * Creates a new ParticleEmitter
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param particleBuilder The particle data to use when this emitter spawns a particle
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
     * Spawns a particle at the emitter's location and removes the oldest particle if the maxParticles limit is passed
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

    //TODO: documentation maybe
    public long getMaxParticles() {return maxParticles;}
    public void setMaxParticles(long maxParticles) {this.maxParticles = maxParticles;}

    public int getViewDistance() {return viewDistance;}
    public void setViewDistance(int viewDistance) {this.viewDistance = viewDistance;}

    /**
     * Removes this emitter and sends all currently active particles to the "orphaned particles" list in ParticleManager
     */
    public void remove() {
        gameObject.remove();

        while (!particles.isEmpty()){
            particles.getFirst().orphan();
        }

        ParticleManager.getAllEmitters().remove(this);
    }

    /**
     * Sets the particle spawn data of this emitter and returns the emitter
     * @param particleBuilder The new spawn data for the emitter
     * @return The emitter after the change
     */
    public ParticleEmitter particleBuilder(ParticleBuilder particleBuilder) {this.particleBuilder = particleBuilder; return this;}

    /**
     * Returns the particle spawn data of this emitter
     * @return The particle spawn data
     */
    public ParticleBuilder particleBuilder() {return particleBuilder;}

    public List<Particle> getParticles() {return particles;}

    // Config stuff
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
