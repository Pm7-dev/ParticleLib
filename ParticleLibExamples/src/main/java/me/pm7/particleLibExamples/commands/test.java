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
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class test implements CommandExecutor {
    private static final ParticleManager manager = ParticleLibExamples.getParticleManager();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        Player p = (Player) commandSender;
        Location spawnLocation = p.getLocation().clone();

//        TextDisplay textDisplay = (TextDisplay) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.TEXT_DISPLAY);
//        textDisplay.setText("Moving Display!");
//        textDisplay.setBillboard(Display.Billboard.CENTER);
//
//        // Set the teleport duration for smooth movement
//        textDisplay.setTeleportDuration(10);
//
//        // Teleport the display entity to the target location
//        // This will initiate the interpolated movement over the specified duration
//        textDisplay.teleport(spawnLocation.clone().add(new Vector(0, 1, 0)));

        Display display = spawnLocation.getWorld().spawn(spawnLocation, BlockDisplay.class, entity -> {
            entity.setTransformation(new Transformation(
                    new Vector3f(1, 1, 1),
                    new Quaternionf(),
                    new Vector3f(1, 1, 1),
                    new Quaternionf()
            ));
            entity.setBlock(Material.GRASS_BLOCK.createBlockData());
            entity.setTeleportDuration(10);
        });
        display.teleport(spawnLocation.clone().add(new Vector(0, 1, 0)));

        return true;
    }
}
