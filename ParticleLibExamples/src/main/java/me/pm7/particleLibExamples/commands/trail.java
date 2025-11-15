package me.pm7.particleLibExamples.commands;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitterMovement;
import me.pm7.particlelib.data.gradient.GradientDouble;
import me.pm7.particlelib.data.gradient.GradientVector;
import me.pm7.particlelib.data.gradient.RangedGradientColor;
import me.pm7.particlelib.data.keyframe.EasingMode;
import me.pm7.particlelib.data.keyframe.Keyframe;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.particlebuilder.ParticleBuilderCube;
import me.pm7.particlelib.physics.GravityNone;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class trail implements CommandExecutor {

    private ParticleBuilderCube trail = new ParticleBuilderCube()
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
            ));

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        Player p = (Player) sender;

        if(strings[0].equalsIgnoreCase("start")) {
            if(!p.getPassengers().isEmpty()) return true;

            ParticleEmitterMovement trailEmitter = new ParticleEmitterMovement(
                    0.03,
                    1000,
                    trail,
                    p.getLocation()
            );

            p.addPassenger(trailEmitter.getGameObject());

            trailEmitter.start();

        } else if(strings[0].equalsIgnoreCase("stop")) {
            if(p.getPassengers().isEmpty()) return true;
            p.getPassengers().getFirst().remove();
        }

        return true;
    }
}
