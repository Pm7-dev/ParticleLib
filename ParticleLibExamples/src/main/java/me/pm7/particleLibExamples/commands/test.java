package me.pm7.particleLibExamples.commands;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.emitter.ParticleEmitterConstant;
import me.pm7.particlelib.interpolation.gradient.GradientColor;
import me.pm7.particlelib.interpolation.gradient.GradientVector;
import me.pm7.particlelib.interpolation.gradient.RangedGradientDouble;
//import me.pm7.particlelib.interpolation.keyframe.EasingMode;
//import me.pm7.particlelib.interpolation.keyframe.Keyframe;
//import me.pm7.particlelib.interpolation.keyframe.RangedKeyframe;
//import me.pm7.particlelib.interpolation.keyframe.ValueRange;
//import me.pm7.particlelib.spawner.ParticleDataSquare;
//import me.pm7.particlelib.physics.GravityAxis;
import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.physics.GravityAxis;
import me.pm7.particlelib.particlebuilder.ParticleBuilderSquare;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;


public class test implements CommandExecutor {
    private static final ParticleManager manager = ParticleLibExamples.getParticleManager();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        Location loc = ((Player) commandSender).getLocation();
        ParticleEmitter emitter = new ParticleEmitterConstant(
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

        return true;
    }
}
