package me.pm7.particleLibExamples.listeners;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.data.gradient.RangedGradientVector;
import me.pm7.particlelib.data.keyframe.EasingMode;
import me.pm7.particlelib.data.keyframe.RangedKeyframe;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.emitter.ParticleEmitterMovement;
import me.pm7.particlelib.particlebuilder.ParticleBuilderCube;
import me.pm7.particlelib.physics.GravityDirection;
import org.bukkit.Color;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.util.Vector;

public class Minecart implements Listener {

    private static final ParticleLibExamples plugin = ParticleLibExamples.getPlugin();

    private final ParticleBuilderCube spark = new ParticleBuilderCube()
            .particleLifeTicks(5)
            .shaded(false)
            .colorOverLifetime(Color.fromRGB(250, 237, 52))
            .initialMovementDirection(new ValueRange<>(
                    new Direction(0, 5),
                    new Direction(360, 10)
            ))
            .velocityOverridesRotation(true)
            .gravity(new GravityDirection()
                    .initialSpeed(new ValueRange<>(12.0, 15.0))
                    .strengthOverLifetime(25)
                    .bouncinessOverLifetime(0.0)
                    .dragMultiplier(18.0)
            )
            .scaleOverLifetime(new RangedGradientVector(
                    EasingMode.LINEAR,
                    new RangedKeyframe<>(new Vector(0.02, 0.02, 0.5), new Vector(0.02, 0.02, 0.3), 0.5),
                    new RangedKeyframe<>(new Vector(0.1, 0.0, 0.1), 1.0)
            ))
            ;

    @EventHandler
    public void onMinecart(EntityPlaceEvent e) {
        if(e.getEntity().getType() == EntityType.MINECART) {
            org.bukkit.entity.Minecart m = (org.bukkit.entity.Minecart) e.getEntity();
            ParticleEmitterMovement trailEmitter = new ParticleEmitterMovement(
                    0.015,
                    1000,
                    spark,
                    m.getLocation()
            );

            m.addPassenger(trailEmitter.getGameObject());

            trailEmitter.start();
        }
    }

}
