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

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ParticleManager::tick, 0L, 1L);
    }

    private static void tick() {

        for(int i=0; i<allEmitters.size(); i++) {
            ParticleEmitter emitter = allEmitters.get(i);

            // Tick the emitter's particles
            List<Particle> particles = emitter.getParticles();
            for(int p=0; p<particles.size(); p++) {
                Particle particle = particles.get(p);
                particle.tick();
                if(!particles.contains(particle)) p--; // avoid concurrentmodificationexception if the particle dies this tick
            }

            if(!emitter.isActive()) continue;

            // Only tick emitter if there is a player close enough to it.
            int viewDistance = emitter.getViewDistance();
            if(viewDistance > 0) {
                Location emitterLoc = emitter.getLocation();
                long closestDistance = Long.MAX_VALUE;
                for(Player p : emitterLoc.getWorld().getPlayers()) {
                    double distanceSquared = p.getLocation().toVector().distanceSquared(emitterLoc.toVector());
                    if(distanceSquared < closestDistance) {
                        closestDistance = (long) distanceSquared;
                    }
                }
                if(closestDistance > (long) viewDistance * viewDistance) {
                    continue;
                }
            }

            if(!emitter.getLocation().isChunkLoaded()) continue;
            emitter.tick();

            if(!allEmitters.contains(emitter)) i--;// avoid concurrentmodificationexception if the emitter dies this tick
        }

        for(int i = 0; i< orphanedParticles.size(); i++) {
            Particle particle = orphanedParticles.get(i);
            particle.tick();
            if(!orphanedParticles.contains(particle)) i--; // avoid concurrentmodificationexception if the particle dies this tick
        }
    }

    public void addParticle(Particle particle) {
        orphanedParticles.add(particle);
    }

    public static List<ParticleEmitter> getAllEmitters() {return allEmitters;}
    public static List<Particle> getOrphanedParticles() {return orphanedParticles;}
    public static void killAllOrphans() {
        while (!orphanedParticles.isEmpty()) {
            orphanedParticles.getFirst().remove();
            orphanedParticles.removeFirst();
        }
    }
}
