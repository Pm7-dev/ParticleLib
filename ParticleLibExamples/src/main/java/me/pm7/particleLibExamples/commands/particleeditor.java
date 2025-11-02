package me.pm7.particleLibExamples.commands;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitterConstant;
import me.pm7.particlelib.interpolation.gradient.GradientColor;
import me.pm7.particlelib.interpolation.gradient.GradientVector;
import me.pm7.particlelib.interpolation.gradient.RangedGradientDouble;
import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.particlebuilder.ParticleBuilderSquare;
import me.pm7.particlelib.physics.GravityAxis;
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
    private static final ParticleManager manager = ParticleLibExamples.getParticleManager();

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

        manager.getPlugin().reloadConfig();
        FileConfiguration config = manager.getPlugin().getConfig();
        if(args[0].toLowerCase().equals("spawn")) {
            emitter = new ParticleEmitterConstant(
                    manager,
                    6,
                    1,
                    loc,
                    new ParticleBuilderSquare()
                            .initialDirection(new ValueRange<>(new Vector(-1, 0.2, -1), new Vector(1, 0.4, 1)))
                            .particleLifeTicks(15)
                            .colorOverLifetime(new GradientColor(
                                    EasingMode.LINEAR,
                                    new Keyframe<>(Color.WHITE, 0.15),
                                    new Keyframe<>(Color.BLACK, 0.70),
                                    new Keyframe<>(Color.BLACK, 0.80),
                                    new Keyframe<>(Color.BLACK.setAlpha(0), 1.0)
                            ))
                            .initialRoll(new ValueRange<>(0.0, 360.0))
                            .rollSpeedOverLifetime(new RangedGradientDouble(-100, 100))
                            .scaleOverLifetime(new GradientVector(
                                    EasingMode.SINE_OUT,
                                    new Keyframe<>(new Vector(1.3 ,1.3 ,1.3), 0.75),
                                    new Keyframe<>(new Vector(0 ,0 ,0), 1.0)
                            ))
                            .gravity(new GravityAxis()
                                    .initialSpeed(new ValueRange<>(1.8, 2.4))
                                    .axisOverLifetime(new Vector(0, 1, 0))
                                    .towardsAxisStrengthOverLifetime(10.0)
                                    .alongAxisStrengthOverLifetime(14.0))
                    );
            emitter.start();

            config.set("particleData", emitter.particleData());
            config.set("particlesPerSpawn", emitter.getParticlesPerSpawn());
            config.set("ticksPerSpawn", emitter.getTicksPerSpawn());
            manager.getPlugin().saveConfig();
        } else if(args[0].toLowerCase().equals("reload")) {
            emitter = new ParticleEmitterConstant(
                    manager,
                    config
            );
            emitter.start();
        }


        return true;
    }
}
