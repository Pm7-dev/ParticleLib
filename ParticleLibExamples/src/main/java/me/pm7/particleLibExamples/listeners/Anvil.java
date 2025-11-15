package me.pm7.particleLibExamples.listeners;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitterBurst;
import me.pm7.particlelib.data.gradient.GradientColor;
import me.pm7.particlelib.data.gradient.GradientDouble;
import me.pm7.particlelib.data.gradient.GradientVector;
import me.pm7.particlelib.data.gradient.RangedGradientDouble;
import me.pm7.particlelib.data.keyframe.EasingMode;
import me.pm7.particlelib.data.keyframe.Keyframe;
import me.pm7.particlelib.data.keyframe.RangedKeyframe;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.particlebuilder.ParticleBuilderCustomText;
import me.pm7.particlelib.physics.GravityNone;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;



public class Anvil implements Listener {
    private static final ParticleManager manager = ParticleLibExamples.getParticleManager();

    ParticleBuilderCustomText sparks = new ParticleBuilderCustomText()
            .particleLifeTicks(15)
            .text(Component.text("\uE002"))
            .gravity(new GravityNone(new GradientDouble(
                            EasingMode.SINE_OUT,
                            new Keyframe<>(2.0, 0.0),
                            new Keyframe<>(0.0, 0.85)
                    ))
            )
            .rollSpeedOverLifetime(new RangedGradientDouble(
                    EasingMode.SINE_OUT,
                    new RangedKeyframe<>(540.0, -540.0, 0.0),
                    new RangedKeyframe<>(0.0, 0.9)
            ))
            .initialMovementDirection(new ValueRange<>(
                    new Vector(-1, -0.2, -1),
                    new Vector(1, 0.2, 1))
            )
            .scaleOverLifetime(new GradientVector(
                    EasingMode.SINE_IN_OUT,
                    new Keyframe<>(new Vector(0.4, 0.4, 0.4), 0.0),
                    new Keyframe<>(new Vector(1.1, 1.1, 1.1), 0.9)
            ))
            .colorOverLifetime(new GradientColor(
                    EasingMode.LINEAR,
                    new Keyframe<>(Color.fromARGB(255 ,255 ,255, 255), 0.8),
                    new Keyframe<>(Color.fromARGB(0 ,255 ,255, 255), 1.0)
            ))
            .initialRoll(new ValueRange<>(0.0, 360.0));

    ParticleBuilderCustomText bonk = new ParticleBuilderCustomText()
            .particleLifeTicks(15)
            .text(Component.text("\uE000\uF801\uE001"))
            .gravity(new GravityNone(new GradientDouble(
                            EasingMode.SINE_OUT,
                            new Keyframe<>(1.6, 0.0),
                            new Keyframe<>(0.0, 0.85)
                    ))
            )
            .rollSpeedOverLifetime(new RangedGradientDouble(
                    EasingMode.CUBIC_IN,
                    new RangedKeyframe<>(-50.0, 0.0),
                    new RangedKeyframe<>(0.0, 0.9)
            ))
            .initialMovementDirection(new ValueRange<>(
                    new Vector(-0.2, 1, -0.2),
                    new Vector(0.2, 1, 0.2))
            )
            .scaleOverLifetime(new GradientVector(
                    EasingMode.SINE_IN_OUT,
                    new Keyframe<>(new Vector(1, 1, 1), 0.0),
                    new Keyframe<>(new Vector(3, 3, 3), 0.9)
            ))
            .colorOverLifetime(new GradientColor(
                    EasingMode.LINEAR,
                    new Keyframe<>(Color.fromARGB(255 ,255 ,255, 255), 0.8),
                    new Keyframe<>(Color.fromARGB(0 ,255 ,255, 255), 1.0)
            ));

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();
        Entity damager = event.getDamager();

        // Check if the damager is a FallingBlock
        if (damager.getType() == EntityType.FALLING_BLOCK) {
            FallingBlock fallingBlock = (FallingBlock) damager;

            // Check if the material of the FallingBlock is an ANVIL
            if (fallingBlock.getMaterial() == Material.ANVIL) {

                Location loc = victim.getLocation().add(0, 1, 0);

                new ParticleEmitterBurst(
                        manager,
                        20,
                        loc,
                        sparks
                ).start();
                new ParticleEmitterBurst(
                        manager,
                        1,
                        loc,
                        bonk
                ).start();

            }
        }
    }
}
