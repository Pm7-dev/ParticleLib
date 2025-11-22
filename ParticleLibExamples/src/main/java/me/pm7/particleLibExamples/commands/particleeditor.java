package me.pm7.particleLibExamples.commands;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.data.Direction;
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


    /*
        plz read
        I honestly wouldn't recommend using this for anything at all. This was mainly used when creating particle systems
        so I could edit the config version of a particle effect and then use a simple /particleeditor reload command
        without having to restart the entire server every time I tweaked a particle. It ended up getting really messy
        and likely has a whole bunch of issues to the point where I understand it well enough to use it only because
        I created it.

        If you want to register the command and try to use this, go ahead, but I'd rather treat this as a demonstration
        of how systems can be saved/loaded from config
     */



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
                    5,
                    new ParticleBuilderCube()
                            .shaded(true)
                            .particleLifeTicks(45)
                            .gravity(new GravityNone(new GradientDouble(0.0)))
                            .initialRotation(new ValueRange<>(
                                    new Vector(0, 0, 0),
                                    new Vector(360, 360, 360)
                            ))
                            .scaleOverLifetime(new Vector(0.25, 0.25, 0.25))
                            .spawnOffset(new ValueRange<>(
                                    new Vector(-1.0, -1.1, -1.0),
                                    new Vector(1.0, 1.0, 1.0)
                            ))
                            .initialMovementDirection(new Direction(0, -90))
                    ,
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
