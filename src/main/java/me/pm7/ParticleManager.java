package me.pm7;

import me.pm7.particle.Particle;
import me.pm7.emitter.ParticleEmitter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {
    private static JavaPlugin plugin;

    public static long MAX_PARTICLES;
    public static long TICKS_PER_PARTICLE_TICK;

    public static List<ParticleEmitter> allEmitters;
    public static List<Particle> allParticles;

    public static void init(JavaPlugin plugin, int period, long maxParticles) {
        ParticleManager.plugin = plugin;

        TICKS_PER_PARTICLE_TICK = period;
        MAX_PARTICLES = maxParticles;

        allEmitters = new ArrayList<>();
        allParticles = new ArrayList<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ParticleManager::tick, 0L, period);
    }

    private static void tick() {
        for(ParticleEmitter emitter : allEmitters) {
            if(!emitter.isActive()) continue;
            emitter.tick();
        }
    }

    public static void addParticle(Particle particle) {
        while(allParticles.size() > MAX_PARTICLES) {
            allParticles.getFirst().remove();
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

}
