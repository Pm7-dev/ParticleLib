package me.pm7.particlelib.particle;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import me.pm7.particlelib.ParticleLib;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.interpolation.gradient.GradientColor;
import me.pm7.particlelib.interpolation.gradient.GradientVector;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticleItem extends Particle {

    private final GradientVector rotationSpeedOverLifetime;

    private final GradientColor colorOverLifetime;

    public ParticleItem(ParticleEmitter parentEmitter, Location location, int lifeTicks, Vector spawnOffset, Gravity gravity, Vector initialDirection,
                        GradientVector scaleOverLifetime, Vector initialRotation, GradientVector rotationSpeedOverLifetime,
                        ValueRange<Double> rotationOverVelocity, GradientColor colorOverLifetime, ItemStack item) {
        super(parentEmitter, location, lifeTicks, gravity, initialDirection, scaleOverLifetime, rotationOverVelocity);

        this.rotationSpeedOverLifetime = rotationSpeedOverLifetime;

        this.colorOverLifetime = colorOverLifetime;

        // Rotate the particle downward so the blue line doesn't interfere with the sweats who have hitboxes on
        Location loc = location.clone().add(spawnOffset);
        loc.setPitch(90.0f);
        loc.setYaw(0.0f);

        // Set up display entity with all its initial data
        display = loc.getWorld().spawn(loc, ItemDisplay.class, entity -> {

            // Add a little tracker that makes the plugin recognize that this is a particle
            entity.getPersistentDataContainer().set(ParticleLib.PARTICLE_KEY, PersistentDataType.BOOLEAN, true);

            // Set initial color
            Color color = colorOverLifetime.interpolate(0);
            PotionContents.Builder builder = PotionContents.potionContents();
            builder.potion(null);
            builder.customColor(color);
            item.setData(DataComponentTypes.POTION_CONTENTS, builder);
            entity.setItemStack(item); // set item

            // Get initial scale
            Vector scale = scaleOverLifetime.interpolate(0.0);
            Vector3f scalef = new Vector3f((float) scale.getX(), (float) scale.getY(), (float) scale.getZ());

            // Get initial rotation
            Quaternionf rotation = new Quaternionf()
                    .rotateX((float)(-Math.PI / 2)) // Undo initial downward rotation
                    .rotateY((float) Math.toRadians(initialRotation.getY())) // get initial for the rest
                    .rotateX((float) Math.toRadians(initialRotation.getX()))
                    .rotateZ((float) Math.toRadians(initialRotation.getZ()));

            // Set initial transformation
            entity.setTransformation(new Transformation(new Vector3f(), rotation, scalef, new Quaternionf()));

            entity.setInterpolationDelay(0);
            entity.setInterpolationDuration(ticksPerCalculation);
            entity.setTeleportDuration(ticksPerCalculation);
        });
    }

    protected void transform(double lifePosition, int steps) {

        // Get interpolated scale
        Vector scale = scaleOverLifetime.interpolate(lifePosition);
        Vector3f scalef = new Vector3f((float) scale.getX(), (float) scale.getY(), (float) scale.getZ());

        Quaternionf rotation = display.getTransformation().getLeftRotation();

        // modify rotation
        Vector rotationSpeed = rotationSpeedOverLifetime.interpolate(lifePosition);
        float yawChange   = (float) Math.toRadians(rotationSpeed.getY() * 0.05 * steps);
        float pitchChange = (float) Math.toRadians(rotationSpeed.getX() * 0.05 * steps);
        float rollChange  = (float) Math.toRadians(rotationSpeed.getZ() * 0.05 * steps);

        // Rotation over velocity
        double magnitude = displacement.length();
        int mul = velocityRotationDirectionPositive ? 1 : -1;
        double v1 = rotationOverVelocity.getV1();
        double v2 = rotationOverVelocity.getV2();
        yawChange   += (float) (magnitude * mul * Math.toRadians(v2 + random.nextDouble()*(v2-v1) - v1/2));
        pitchChange += (float) (magnitude * mul * Math.toRadians(v2 + random.nextDouble()*(v2-v1) - v1/2));
        rollChange  += (float) (magnitude * mul * Math.toRadians(v2 + random.nextDouble()*(v2-v1) - v1/2));

        // Apply change in rotation
        rotation
                .rotateY(yawChange)
                .rotateX(pitchChange)
                .rotateZ(rollChange);

        display.setTransformation(new Transformation(new Vector3f(), rotation, scalef, new Quaternionf()));
    }

    @Override
    public void color(double lifePosition) {
        Color color = colorOverLifetime.interpolate(lifePosition);

        ItemStack item = getDisplay().getItemStack();
        PotionContents.Builder builder = PotionContents.potionContents();
        builder.potion(null);
        builder.customColor(color);
        item.setData(DataComponentTypes.POTION_CONTENTS, builder);
        getDisplay().setItemStack(item);
    }

    @Override
    public ItemDisplay getDisplay() {
        return (ItemDisplay) display;
    }
}
