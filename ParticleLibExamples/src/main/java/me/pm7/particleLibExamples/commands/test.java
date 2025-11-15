package me.pm7.particleLibExamples.commands;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
//import me.pm7.particlelib.interpolation.keyframe.EasingMode;
//import me.pm7.particlelib.interpolation.keyframe.Keyframe;
//import me.pm7.particlelib.interpolation.keyframe.RangedKeyframe;
//import me.pm7.particlelib.interpolation.keyframe.ValueRange;
//import me.pm7.particlelib.spawner.ParticleDataSquare;
//import me.pm7.particlelib.physics.GravityAxis;
import me.pm7.particlelib.data.Direction;
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

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        Direction direction = new Direction(0, 0);
        Direction direction2 = new Direction(360, 90);

        System.out.println(Direction.getRandomVector(direction, direction2));

        return true;
    }
}
