package me.pm7.particleLibExamples.listeners;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitterBurst;
import me.pm7.particlelib.interpolation.gradient.RangedGradientColor;
import me.pm7.particlelib.interpolation.gradient.RangedGradientVector;
import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.RangedKeyframe;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.particlebuilder.ParticleBuilderCube;
import me.pm7.particlelib.physics.GravityDirection;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Light;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

public class Explosion implements Listener {
    private static final ParticleManager manager = ParticleLibExamples.getParticleManager();

    ParticleBuilderCube explosion = new ParticleBuilderCube()
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
            );


    @EventHandler
    public void OnExplode(EntityExplodeEvent e) {

        Location loc = e.getLocation().clone().add(0, 0.8, 0);

        // explosion
        new ParticleEmitterBurst(manager, 60, loc, explosion).start();

        // set to a light
        Block b = loc.getBlock();
        b.setType(org.bukkit.Material.LIGHT);
        Light light = (Light) b.getBlockData();
        light.setLevel(15);
        b.setBlockData(light);

        Bukkit.getScheduler().scheduleSyncDelayedTask(manager.getPlugin(), () -> b.setType(org.bukkit.Material.AIR), 3L);

    }

}
