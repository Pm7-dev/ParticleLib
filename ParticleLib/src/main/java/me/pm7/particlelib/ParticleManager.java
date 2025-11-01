package me.pm7.particlelib;

import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.particle.Particle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    public static List<ParticleManager> managers = new ArrayList<>();

    private final JavaPlugin plugin;

    public final long MAX_PARTICLES;
    public final int TICKS_PER_PARTICLE_CALCULATION;
    public final long MAX_DISTANCE;

    public final List<ParticleEmitter> allEmitters;
    public final List<Particle> allParticles;

    public ParticleManager(JavaPlugin plugin, int period, long maxParticles, long maxDistance) {
        this.plugin = plugin;

        this.MAX_PARTICLES = maxParticles;
        this.TICKS_PER_PARTICLE_CALCULATION = period;
        this.MAX_DISTANCE = maxDistance;

        allEmitters = new ArrayList<>();
        allParticles = new ArrayList<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::tick, 0L, period);
    }

    private int spawnTick = 0;
    private void tick() {

        for(int i=0; i<allEmitters.size(); i++) {
            ParticleEmitter emitter = allEmitters.get(i);
            if(!emitter.isActive()) continue;

            // Only tick emitter if there is a player close enough to it.
            if(MAX_DISTANCE > 0) {
                Location emitterLoc = emitter.getLocation();
                long closestDistance = Long.MAX_VALUE;
                for(Player p : emitterLoc.getWorld().getPlayers()) {
                    double distanceSquared = p.getLocation().toVector().distanceSquared(emitterLoc.toVector());
                    if(distanceSquared < closestDistance) {
                        closestDistance = (long) distanceSquared;
                    }
                }
                if(closestDistance > MAX_DISTANCE * MAX_DISTANCE) {
                    continue;
                }
            }

            if(!emitter.getLocation().isChunkLoaded()) continue;
            emitter.tick();
            if(!allEmitters.contains(emitter)) i--;// avoid concurrentmodificationexception if the emitter dies this tick
        }

        for(int i=0; i<allParticles.size(); i++) {
            Particle particle = allParticles.get(i);
            if(spawnTick == particle.getSpawnTick()) {
                particle.tick(TICKS_PER_PARTICLE_CALCULATION);
                if(!allParticles.contains(particle)) i--; // avoid concurrentmodificationexception if the particle dies this tick
            }
        }

        spawnTick++;
        if(spawnTick == TICKS_PER_PARTICLE_CALCULATION) spawnTick = 0;
    }

    public void addParticle(Particle particle) {
        allParticles.add(particle);
        while(allParticles.size() > MAX_PARTICLES) {
            allParticles.getFirst().remove();
        }
    }

    public int getCurrentSpawnTick() {return spawnTick;}

    public JavaPlugin getPlugin() {return plugin;}

}
