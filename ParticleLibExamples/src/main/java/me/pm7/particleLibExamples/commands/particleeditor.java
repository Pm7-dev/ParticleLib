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
import me.pm7.particlelib.particlebuilder.ParticleBuilderCustomText;
import me.pm7.particlelib.particlebuilder.ParticleBuilderSquare;
import me.pm7.particlelib.physics.GravityAxis;
import me.pm7.particlelib.physics.GravityDirection;
import me.pm7.particlelib.physics.GravityNone;
import net.kyori.adventure.text.Component;
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
                    16,
                    40,
                    loc,
                    new ParticleBuilderCustomText()
                            .particleLifeTicks(25)
                            .text(Component.text("\uE002"))
                            .gravity(new GravityNone(new GradientDouble(
                                        EasingMode.CUBIC_OUT,
                                        new Keyframe<>(5.0, 0.0),
                                        new Keyframe<>(0.0, 1.0)
                                    ))
                            )
                            .rollSpeedOverLifetime(new RangedGradientDouble(
                                    EasingMode.LINEAR,
                                    new RangedKeyframe<>(360.0, -360.0, 0.0),
                                    new RangedKeyframe<>(0.0, 0.65)
                            ))
                            .initialDirection(new ValueRange<>(
                                    new Vector(-1, -0.3, -1),
                                    new Vector(1, 0.3, 1))
                            )
                            .scaleOverLifetime(new GradientVector(
                                    EasingMode.LINEAR,
                                    new Keyframe<>(new Vector(0.1, 0.1, 0.1), 0.0),
                                    new Keyframe<>()
                            ))
                            .scaleOverLifetime(new Vector(2, 2, 2))
                            .colorOverLifetime(new GradientColor(
                                    EasingMode.LINEAR,
                                    new Keyframe<>(Color.fromARGB(255 ,255 ,255, 255), 0.85),
                                    new Keyframe<>(Color.fromARGB(0 ,255 ,255, 255), 1.0)
                            ))
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
