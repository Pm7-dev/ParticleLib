package me.pm7.particleLibExamples.listeners;

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
import org.bukkit.util.Vector;

public class Minecart implements Listener {

    // Particle for sparks
    private final ParticleBuilderCube spark = new ParticleBuilderCube()
            .particleLifeTicks(8)
            .shaded(false)
            .colorOverLifetime(Color.fromRGB(250, 237, 52))
            .initialMovementDirection(new ValueRange<>(
                    new Direction(0, 1),
                    new Direction(360, 10)
            ))
            .velocityOverridesRotation(true) // very important for this one
            .gravity(new GravityDirection()
                    .initialSpeed(new ValueRange<>(9.0, 12.0))
                    .strengthOverLifetime(25)
                    .bouncinessOverLifetime(0.0)
                    .dragMultiplier(18.0)
            )
            .scaleOverLifetime(new RangedGradientVector(
                    EasingMode.LINEAR,
                    new RangedKeyframe<>(new Vector(0.01, 0.01, 0.75), new Vector(0.01, 0.01, 0.5), 0.1),
                    new RangedKeyframe<>(new Vector(0.01, 0.01, 0.0), 1.0)
            ))
            .spawnOffset(new Vector(0, -0.075, 0))
            ;

    @EventHandler
    public void onMinecart(EntityPlaceEvent e) {

        // Adds a movement emitter as a passenger to all minecarts. Yes, this does stop players from entering the minecraft, but demonstration purposes and laziness and all that
        if(e.getEntity().getType() == EntityType.MINECART) {
            org.bukkit.entity.Minecart m = (org.bukkit.entity.Minecart) e.getEntity();
            ParticleEmitterMovement trailEmitter = new ParticleEmitterMovement(
                    0.025,
                    1000,
                    spark,
                    m.getLocation()
            );

            m.addPassenger(trailEmitter.getGameObject());

            trailEmitter.start();

            // I also don't ever actually delete the particle emitter spawned from this, so that's another issue to tack onto this
        }
    }

}
