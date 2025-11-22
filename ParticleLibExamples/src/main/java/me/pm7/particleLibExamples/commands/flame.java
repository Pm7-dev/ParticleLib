package me.pm7.particleLibExamples.commands;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.emitter.ParticleEmitterConstant;
import me.pm7.particlelib.data.gradient.GradientColor;
import me.pm7.particlelib.data.gradient.GradientVector;
import me.pm7.particlelib.data.gradient.RangedGradientDouble;
import me.pm7.particlelib.data.keyframe.EasingMode;
import me.pm7.particlelib.data.keyframe.Keyframe;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.particlebuilder.ParticleBuilderSquare;
import me.pm7.particlelib.physics.GravityAxis;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class flame implements CommandExecutor {
    List<ParticleEmitter> flameEmitters = new ArrayList<>();

    // Main flame data gets set here, the rest are just cloned and have their colours changed
    ParticleBuilderSquare flameDataOrange = new ParticleBuilderSquare()
            .initialMovementDirection(new ValueRange<>(new Direction(0, 0), new Direction(360, 360)))
            .particleLifeTicks(15)
            .colorOverLifetime(new GradientColor(
                    EasingMode.LINEAR,
                    new Keyframe<>(Color.WHITE, 0.05),
                    new Keyframe<>(Color.fromRGB(252, 211, 3), 0.20),
                    new Keyframe<>(Color.fromRGB(255, 136, 0), 0.40),
                    new Keyframe<>(Color.fromRGB(222, 64, 11), 0.70)
            ))
            .initialRoll(new ValueRange<>(0.0, 360.0))
            .rollSpeedOverLifetime(new RangedGradientDouble(-100, 100))
            .scaleOverLifetime(new GradientVector(
                    EasingMode.SINE_OUT,
                    new Keyframe<>(new Vector(0.165 ,0.165 ,0.165), 0.75),
                    new Keyframe<>(new Vector(0 ,0 ,0), 0.9)
            ))
            .gravity(new GravityAxis()
                    .initialSpeed(2.0)
                    .axisOverLifetime(new Vector(0, 1, 0))
                    .towardsAxisStrengthOverLifetime(8.0)
                    .alongAxisStrengthOverLifetime(12.0)
            )
            .shaded(false);


    ParticleBuilderSquare flameDataBlue = flameDataOrange.clone()
            .colorOverLifetime(new GradientColor(
                    EasingMode.LINEAR,
                    new Keyframe<>(Color.WHITE, 0.05),
                    new Keyframe<>(Color.fromRGB(64, 188, 255), 0.10),
                    new Keyframe<>(Color.fromRGB(0, 17, 201), 0.30),
                    new Keyframe<>(Color.fromRGB(252, 211, 3), 0.50),
                    new Keyframe<>(Color.fromRGB(222, 64, 11), 0.70)
            )
    );


    ParticleBuilderSquare flameDataWhite = flameDataOrange.clone()
            .colorOverLifetime(new GradientColor(
                    EasingMode.LINEAR,
                    new Keyframe<>(Color.WHITE, 0.10),
                    new Keyframe<>(Color.BLACK, 0.60)
            )
    );

    ParticleBuilderSquare flameDataLime = flameDataOrange.clone()
            .colorOverLifetime(new GradientColor(
                    EasingMode.LINEAR,
                    new Keyframe<>(Color.WHITE, 0.05),
                    new Keyframe<>(Color.fromRGB(63, 232, 74), 0.20),
                    new Keyframe<>(Color.fromRGB(0, 255, 51), 0.40),
                    new Keyframe<>(Color.fromRGB(59, 201, 40), 0.70)
            )
    );

    ParticleBuilderSquare flameDataPink = flameDataOrange.clone()
            .colorOverLifetime(new GradientColor(
                            EasingMode.LINEAR,
                            new Keyframe<>(Color.WHITE, 0.05),
                            new Keyframe<>(Color.fromRGB(221, 63, 232), 0.20),
                            new Keyframe<>(Color.fromRGB(234, 0, 255), 0.40),
                            new Keyframe<>(Color.fromRGB(193, 67, 204), 0.70)
                    )
            );

    ParticleBuilderSquare flameDataLightBlue = flameDataOrange.clone()
            .colorOverLifetime(new GradientColor(
                            EasingMode.LINEAR,
                            new Keyframe<>(Color.WHITE, 0.05),
                            new Keyframe<>(Color.fromRGB(63, 86, 232), 0.20),
                            new Keyframe<>(Color.fromRGB(0, 94, 255), 0.40),
                            new Keyframe<>(Color.fromRGB(66, 111, 189), 0.70)
                    )
            );

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        Location location;

        // I want this one to be useable by players or command blocks
        if (sender instanceof Player) {
            location = ((Player) sender).getLocation();
        }
        else if (sender instanceof BlockCommandSender bcs) {
            location = bcs.getBlock().getLocation().add(0.5, 1.5, 0.5);
        } else return false;


        // numbers as an argument to control which flame to spawn or kill all flames
        switch (args[0].toLowerCase()) {
            case "0": {
                flameEmitters.add(new ParticleEmitterConstant(10, 1, flameDataOrange, location));
                flameEmitters.getLast().start();
                break;
            }
            case "1": {
                flameEmitters.add(new ParticleEmitterConstant(10, 1, flameDataWhite, location));
                flameEmitters.getLast().start();
                break;
            }
            case "2": {
                flameEmitters.add(new ParticleEmitterConstant(10, 1, flameDataBlue, location));
                flameEmitters.getLast().start();
                break;
            }
            case "3": {
                flameEmitters.add(new ParticleEmitterConstant(10, 1, flameDataLime, location));
                flameEmitters.getLast().start();
                break;
            }
            case "4": {
                flameEmitters.add(new ParticleEmitterConstant(10, 1, flameDataPink, location));
                flameEmitters.getLast().start();
                break;
            }
            case "5": {
                flameEmitters.add(new ParticleEmitterConstant(10, 1, flameDataLightBlue, location));
                flameEmitters.getLast().start();
                break;
            }
            case "6": {
                while (!flameEmitters.isEmpty()) {
                    flameEmitters.getFirst().remove();
                    flameEmitters.removeFirst();
                }
                break;
            }
        }

        return true;
    }
}
