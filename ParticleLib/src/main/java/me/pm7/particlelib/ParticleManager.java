package me.pm7.particlelib;

import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.particle.Particle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    private static List<ParticleEmitter> allEmitters;
    private static List<Particle> orphanedParticles;


    static void init(JavaPlugin plugin) {

        allEmitters = new ArrayList<>();
        orphanedParticles = new ArrayList<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            tick();
        }, 0L, 1L);
    }

    private int spawnTick = 0;
    private static void tick() {

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

        for(int i = 0; i< orphanedParticles.size(); i++) {
            Particle particle = orphanedParticles.get(i);
            if(spawnTick == particle.getSpawnTick()) {
                particle.tick(TICKS_PER_PARTICLE_CALCULATION);
                if(!orphanedParticles.contains(particle)) i--; // avoid concurrentmodificationexception if the particle dies this tick
            }
        }

        spawnTick++;
        if(spawnTick == TICKS_PER_PARTICLE_CALCULATION) spawnTick = 0;
    }

    public void addParticle(Particle particle) {
        orphanedParticles.add(particle);
        while(orphanedParticles.size() > MAX_PARTICLES) {
            orphanedParticles.getFirst().remove();
        }
    }

    public int getCurrentSpawnTick() {return spawnTick;}

    public static List<ParticleEmitter> getAllEmitters() {return allEmitters;}
    public static List<Particle> getOrphanedParticles() {return orphanedParticles;}
    public static void killAllOrphans() {
        while (!orphanedParticles.isEmpty()) {
            orphanedParticles.getFirst().remove();
            orphanedParticles.removeFirst();
        }
    }
}
