package me.pm7.particleLibExamples.commands;

import me.pm7.particlelib.emitter.ParticleEmitterConstant;
import me.pm7.particlelib.interpolation.gradient.GradientColor;
import me.pm7.particlelib.interpolation.gradient.GradientDouble;
import me.pm7.particlelib.interpolation.gradient.GradientVector;
import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import me.pm7.particlelib.particlebuilder.ParticleBuilderSquare;
import me.pm7.particlelib.physics.GravityNone;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class smoke implements CommandExecutor {

    ParticleBuilderSquare smokeData = new ParticleBuilderSquare()
            .particleLifeTicks(100)
            .scaleOverLifetime(new GradientVector(
                    EasingMode.SINE_IN,
                    new Keyframe<>(new Vector(0.5, 0.5, 0.5), 0.1),
                    new Keyframe<>(new Vector(12, 12, 12), 1.0)
            ))
            .gravity(new GravityNone(new GradientDouble(
                    EasingMode.LINEAR,
                    new Keyframe<>(2.0, 0.0),
                    new Keyframe<>(0.3, 1.0)
            )))
            .shaded(false)
            .colorOverLifetime(new GradientColor(
                    EasingMode.LINEAR,
                    new Keyframe<>(Color.fromRGB(195, 195, 195), 0.2),
                    new Keyframe<>(Color.fromARGB(0, 195, 195, 195), 1.0)
            ))
            ;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        Player p = (Player) sender;

        new ParticleEmitterConstant()
                .



        return true;
    }
}
