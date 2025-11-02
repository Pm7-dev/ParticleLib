package me.pm7.particleLibExamples.listeners;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitterBurst;
import me.pm7.particlelib.interpolation.gradient.RangedGradientVector;
import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.RangedKeyframe;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.physics.GravityDirection;
import me.pm7.particlelib.particlebuilder.ParticleBuilderBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

public class BlockBreak implements Listener {
    private static final ParticleManager manager = ParticleLibExamples.getParticleManager();

    private final ParticleBuilderBlock particleData = new ParticleBuilderBlock()
            .particleLifeTicks(new ValueRange<>(26, 34))
            .gravity(new GravityDirection()
                    .initialSpeed(3.0)
                    .strengthOverLifetime(16)
                    .bouncinessOverLifetime(0.5)
            )
            .spawnOffset(new ValueRange<>(
                    new Vector(-0.4, -0.4, -0.4),
                    new Vector(0.4, 0.4, 0.4)
            ))
            .initialDirection(new ValueRange<>(
                    new Vector(-0.6, 0.25, -0.6),
                    new Vector(0.6, 1, 0.6)
            ))
            .initialRotation(new ValueRange<>(
                    new Vector(0, 0, 0),
                    new Vector(360, 360, 360)
            ))
            .scaleOverLifetime(new RangedGradientVector(
                    EasingMode.LINEAR,
                    new RangedKeyframe<>(new Vector(0.125, 0.125, 0.125), new Vector(0.106, 0.106, 0.106), 0.8),
                    new RangedKeyframe<>(new Vector(0, 0, 0), 1.0)
            ))
            .rotationOverVelocity(new ValueRange<>(120.0, 140.0))
    ;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        particleData.blockData(e.getBlock().getBlockData());
        e.getBlock().setType(Material.AIR);
        Location loc = e.getBlock().getLocation().add(0.5, 0.5, 0.5);

        new ParticleEmitterBurst(manager, loc, particleData, 25).start();
    }

}
