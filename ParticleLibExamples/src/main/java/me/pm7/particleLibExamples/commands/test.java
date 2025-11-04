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

        System.out.println(ParticleLibExamples.getParticleManager().getPlugin().getConfig().getString("emitterEditor.uuid"));

        return true;
    }
}
