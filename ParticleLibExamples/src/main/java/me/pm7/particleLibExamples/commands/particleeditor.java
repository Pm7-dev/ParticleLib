package me.pm7.particleLibExamples.commands;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitterConstant;
import me.pm7.particlelib.data.gradient.*;
import me.pm7.particlelib.data.keyframe.EasingMode;
import me.pm7.particlelib.data.keyframe.Keyframe;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.particlebuilder.ParticleBuilderCube;
import me.pm7.particlelib.physics.GravityNone;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class particleeditor implements CommandExecutor {
    private static final ParticleLibExamples plugin = ParticleLibExamples.getPlugin();

    ParticleEmitterConstant emitter = null;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        Player p = (Player) commandSender;

        Location loc;
        if(emitter != null) {
            loc = emitter.getLocation().clone();
            emitter.remove();
            emitter = null;
        } else {
            loc = p.getLocation().clone();
        }

        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        if(args[0].toLowerCase().equals("spawn")) {
            emitter = new ParticleEmitterConstant(
                    3,
                    1,
                    new ParticleBuilderCube()
                            .shaded(false)
                            .particleLifeTicks(45)
                            .gravity(new GravityNone(new GradientDouble(0.0)))
                            .initialRotation(new ValueRange<>(
                                    new Vector(0, 0, 0),
                                    new Vector(360, 360, 360)
                            ))
                            .scaleOverLifetime(new GradientVector(
                                    EasingMode.SINE_IN,
                                    new Keyframe<>(new Vector(0.2, 0.2, 0.2), 0.9),
                                    new Keyframe<>(new Vector(0, 0, 0), 1.0)
                            ))
                            .colorOverLifetime(new RangedGradientColor(
                                    Color.fromARGB(255,0, 94, 194),
                                    Color.fromARGB(255,0, 10, 194)
                            ))
                            .spawnOffset(new ValueRange<>(
                                    new Vector(-0.03, -1.1, -0.03),
                                    new Vector(0.03, -1.0, 0.03)
                            )),
                    loc
                    );
            emitter.start();

            config.set("emitterEditor", emitter);
            plugin.saveConfig();

        } else if(args[0].toLowerCase().equals("reload")) {
            emitter = (ParticleEmitterConstant) config.get("emitterEditor");
            emitter.start();
        }

        return true;
    }
}
