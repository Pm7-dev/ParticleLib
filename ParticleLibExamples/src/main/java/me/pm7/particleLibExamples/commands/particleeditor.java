package me.pm7.particleLibExamples.commands;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitterConstant;
import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import me.pm7.particlelib.interpolation.keyframe.RangedKeyframe;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.particlebuilder.ParticleBuilderCube;
import me.pm7.particlelib.particlebuilder.ParticleBuilderSquare;
import me.pm7.particlelib.physics.GravityAxis;
import me.pm7.particlelib.physics.GravityDirection;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
                    60,
                    80,
                    loc,
                    new ParticleBuilderCube()
                            .shaded(false)
                            .initialDirection(new ValueRange<>(new Vector(-1, -1, -1), new Vector(1, 1, 1)))
                            .particleLifeTicks(new ValueRange<>(17, 20))
                            .colorOverLifetime(new RangedGradientColor(Color.fromRGB(252, 118, 40), Color.fromRGB(252, 234, 40)))
                            .rotationOverVelocity(new ValueRange<>(120.0, 140.0))
                            .scaleOverLifetime(new RangedGradientVector(
                                    EasingMode.CUBIC_OUT,
                                    new RangedKeyframe<>(new Vector(0.55,0.55,0.55), new Vector(0.45,0.45,0.45), 0.1),
                                    new RangedKeyframe<>(new Vector(0 ,0 ,0), 1.0)
                            ))
                            .gravity(new GravityDirection()
                                    .initialSpeed(new ValueRange<>(12.0, 15.0))
                                    .strengthOverLifetime(18)
                                    .bouncinessOverLifetime(0.8)
                                    .dragMultiplier(18.0)
                            )
                    );
            emitter.start();

            config.set("emitterEditor", emitter);
            manager.getPlugin().saveConfig();

        } else if(args[0].toLowerCase().equals("reload")) {
            emitter = (ParticleEmitterConstant) config.get("emitterEditor");
            emitter.setParticleManager(manager);
            emitter.start();
        }

        return true;
    }
}
