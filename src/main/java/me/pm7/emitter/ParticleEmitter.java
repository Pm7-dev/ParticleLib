package me.pm7.emitter;

import me.pm7.ParticleManager;
import me.pm7.particle.Particle;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public abstract class ParticleEmitter {

    protected boolean active;
    protected BlockDisplay gameObject;

    protected HashMap<Particle, Integer> particleSpawnTick;

    public ParticleEmitter(Location location) {
        this.active = false;
        this.gameObject = (BlockDisplay) location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);

        this.particleSpawnTick = new HashMap<>();

        ParticleManager.allEmitters.add(this);
    }

    public void start() {
        active = true;
    }

    public void pause() {
        active = false;
    }

    public boolean isActive() {return active;}

    public abstract void tick();


    public void remove() {
        ParticleManager.allEmitters.remove(this);
        gameObject.remove();
    }

    public void teleport(Location location) {
        this.gameObject.teleport(location);
    }

    public Location getLocation() {
        return this.gameObject.getLocation();
    }

    public Display getGameObject() {
        return gameObject;
    }

}
