package me.pm7.particleLibExamples.commands;

import me.pm7.particlelib.data.gradient.RangedGradientVector;
import me.pm7.particlelib.data.keyframe.RangedKeyframe;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.emitter.ParticleEmitterConstant;
import me.pm7.particlelib.data.gradient.GradientColor;
import me.pm7.particlelib.data.gradient.GradientDouble;
import me.pm7.particlelib.data.gradient.GradientVector;
import me.pm7.particlelib.data.keyframe.EasingMode;
import me.pm7.particlelib.data.keyframe.Keyframe;
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

    // Very simple smoke particle, likely for early on in the video
    ParticleBuilderSquare smokeData = new ParticleBuilderSquare()
            .particleLifeTicks(240)
            .scaleOverLifetime(new RangedGradientVector(
                    EasingMode.SINE_IN,
                    new RangedKeyframe<>(new Vector(0.5, 0.5, 0.5), new Vector(1.2, 1.2, 1.2), 0.0),
                    new RangedKeyframe<>(new Vector(18, 14, 18), new Vector(13, 11, 13), 1.0)
            ))
            .gravity(new GravityNone(new GradientDouble(
                    EasingMode.LINEAR,
                    new Keyframe<>(0.5, 0.0),
                    new Keyframe<>(0.1, 1.0)
            )))
            .shaded(false)
            .colorOverLifetime(new GradientColor(
                    EasingMode.LINEAR,
                    new Keyframe<>(Color.fromARGB(175, 230, 230, 230), 0.1),
                    new Keyframe<>(Color.fromARGB(0, 230, 230, 230), 1.0)
            ))
            .spawnOffset(new ValueRange<>(
                    new Vector(-0.1, 0, -0.1),
                    new Vector(0.1, 0, 0.1))
            )
            ;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        Player p = (Player) sender;

        new ParticleEmitterConstant(1, 2, smokeData, p.getLocation()).start();

        return true;
    }
}
