package me.pm7.particleLibExamples.commands;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.particle.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class test implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        int parts = 0;
        int emitters = 0;
        int orphans = 0;

        System.out.println("kinda Total Orphaned Particles: " + ParticleManager.getOrphanedParticles().size());

        List<ParticleEmitter> allEmitters = ParticleManager.getAllEmitters();
        while (!allEmitters.isEmpty()) {
            ParticleEmitter emitter = allEmitters.getFirst();
            while (!emitter.getParticles().isEmpty()) {
                Particle p = emitter.getParticles().getFirst();
                p.remove();
                parts++;
            }

            emitters++;
            emitter.remove();
        }

        while (!ParticleManager.getOrphanedParticles().isEmpty()) {
            Particle p = ParticleManager.getOrphanedParticles().getFirst();
            p.remove();
            orphans++;
        }

        System.out.println("Total Particles: " + parts);
        System.out.println("Total Emitters: " + emitters);
        System.out.println("Total Orphaned Particles: " + orphans);

        return true;
    }
}
