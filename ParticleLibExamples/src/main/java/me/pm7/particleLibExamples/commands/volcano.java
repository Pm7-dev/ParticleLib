package me.pm7.particleLibExamples.commands;

import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.data.gradient.RangedGradientColor;
import me.pm7.particlelib.data.gradient.RangedGradientVector;
import me.pm7.particlelib.data.keyframe.EasingMode;
import me.pm7.particlelib.data.keyframe.RangedKeyframe;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.emitter.ParticleEmitterConstant;
import me.pm7.particlelib.particlebuilder.ParticleBuilderCube;
import me.pm7.particlelib.physics.GravityDirection;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class volcano implements CommandExecutor {


    ParticleBuilderCube volcanoData = new ParticleBuilderCube()
            .particleLifeTicks(150)
            .scaleOverLifetime(new RangedGradientVector(
                    EasingMode.LINEAR,
                    new RangedKeyframe<>(new Vector(0.75, 0.75, 0.75), new Vector(1.25, 1.25, 1.25), 0.9),
                    new RangedKeyframe<>(new Vector(0.0, 0.0, 0.0), 1.0)
            ))
            .initialMovementDirection(new ValueRange<>(new Direction(0, 10), new Direction(360, 30)))
            .gravity(new GravityDirection()
                    .initialSpeed(new ValueRange<>(5.0, 7.5))
                    .directionOverLifetime(new Vector(0, -1, 0))
                    .strengthOverLifetime(20)
                    .dragMultiplier(0.0)
                    .bouncinessOverLifetime(0.4)
            )
            .shaded(false)
            .colorOverLifetime(new RangedGradientColor(
                    Color.fromRGB(227, 62, 20), Color.fromRGB(227, 106, 20)
            ))
            .initialRotation(new ValueRange<>(new Vector(0, 0, 0), new Vector(360, 360, 360)))
            .rotationOverVelocity(new ValueRange<>(10.0, 15.0))
    ;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        Player p = (Player) sender;
        Location loc = p.getLocation().clone();

        new ParticleEmitterConstant(7, 2, volcanoData, loc).start();

        return true;
    }
}
