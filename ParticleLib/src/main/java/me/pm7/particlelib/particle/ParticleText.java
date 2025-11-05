package me.pm7.particlelib.particle;

import me.pm7.particlelib.ParticleLib;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.interpolation.gradient.GradientColor;
import me.pm7.particlelib.interpolation.gradient.GradientDouble;
import me.pm7.particlelib.interpolation.gradient.GradientVector;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.physics.Gravity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticleText extends Particle {
    private static final Vector3f SQUARE_SCALE_MULTIPLIER = new Vector3f(8f, 4f, 8f);

    private final GradientDouble rotationSpeedOverLifetime;
    private final boolean velocityRotationDirectionPositive;

    private final GradientColor colorOverLifetime;
    private final Component text;

    public ParticleText(ParticleManager manager, Location location, int lifeTicks, Vector spawnOffset, Gravity gravity, Vector initialDirection,
                        GradientVector scaleOverLifetime, double initialRoll, GradientDouble rotationSpeedOverLifetime,
                        ValueRange<Double> rotationOverVelocity, GradientColor colorOverLifetime, boolean shaded,
                        Component text) {
        super(manager, location, lifeTicks, gravity, initialDirection, scaleOverLifetime, rotationOverVelocity);

        this.rotationSpeedOverLifetime = rotationSpeedOverLifetime;
        this.velocityRotationDirectionPositive = random.nextDouble() > 0.5;

        this.colorOverLifetime = colorOverLifetime;

        // Rotate the particle downward so the blue line doesn't interfere with the sweats who have hitboxes on
        Location loc = location.clone().add(spawnOffset);
        loc.setPitch(90.0f);
        loc.setYaw(0.0f);

        this.text = text;

        // Set up display entity with all its initial data
        display = loc.getWorld().spawn(loc, TextDisplay.class, entity -> {

            // Add a little tracker that makes the plugin recognize that this is a particle
            entity.getPersistentDataContainer().set(ParticleLib.PARTICLE_KEY, PersistentDataType.BOOLEAN, true);

            if(!shaded) entity.setBrightness(new Display.Brightness(15, 15));

            if(text == null) {
                entity.text(Component.text(" "));
                entity.setTextOpacity((byte) 10);
            } else {
                entity.setBackgroundColor(Color.fromARGB(10, 0, 0, 0));
            }
            entity.setBillboard(Display.Billboard.CENTER);
            entity.setDefaultBackground(false);

            // Get initial scale
            Vector scale = scaleOverLifetime.interpolate(0.0);
            Vector3f scalef = new Vector3f((float) scale.getX(), (float) scale.getY(), (float) scale.getZ());

            // Get initial rotation
            Quaternionf rotation = new Quaternionf().rotateZ((float) Math.toRadians(initialRoll));

            // Set initial transformation
            if(text == null) entity.setTransformation(new Transformation(new Vector3f(), rotation, scalef.mul(SQUARE_SCALE_MULTIPLIER), new Quaternionf()));
            else entity.setTransformation(new Transformation(new Vector3f(), rotation, scalef, new Quaternionf()));

            // Set initial color
            Color color = colorOverLifetime.interpolate(0);
            if(text == null) entity.setBackgroundColor(color);
            else entity.text(text.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue())));


            entity.setInterpolationDelay(0);
            entity.setInterpolationDuration(manager.TICKS_PER_PARTICLE_CALCULATION);
            entity.setTeleportDuration(manager.TICKS_PER_PARTICLE_CALCULATION);
        });
    }

    protected void transform(double lifePosition, int steps) {

        // Get interpolated scale
        Vector scale = scaleOverLifetime.interpolate(lifePosition);
        Vector3f scalef = new Vector3f((float) scale.getX(), (float) scale.getY(), (float) scale.getZ());

        Quaternionf rotation = display.getTransformation().getLeftRotation();

        // Either set up initial rotation or modify rotation

        // Get the changes in rotation
        float rollChange  = (float) Math.toRadians(rotationSpeedOverLifetime.interpolate(lifePosition) * 0.05 * steps);

        // Rotation over velocity
        double magnitude = displacement.length();
        double v1 = rotationOverVelocity.getV1();
        double v2 = rotationOverVelocity.getV2();
        rollChange += (float) (magnitude*(velocityRotationDirectionPositive ?1:-1)*Math.toRadians(v2 + random.nextDouble()*(v2-v1) - v1/2));

        // Apply change in rotation
        rotation.rotateZ(rollChange);

        if(text == null) display.setTransformation(new Transformation(new Vector3f(), rotation, scalef.mul(SQUARE_SCALE_MULTIPLIER), new Quaternionf()));
        else display.setTransformation(new Transformation(new Vector3f(), rotation, scalef, new Quaternionf()));
    }

    @Override
    public void color(double lifePosition) {
        TextDisplay textDisplay = getDisplay();
        Color color = colorOverLifetime.interpolate(lifePosition);

        if(text == null) {
            textDisplay.setBackgroundColor(color);
        } else {
            textDisplay.text(text.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue())));

            int alpha = color.getAlpha();
            if(alpha < 10) alpha = 10;
            if(alpha > 255) alpha = 255;
            textDisplay.setTextOpacity((byte) alpha);
        }
    }

    @Override
    public TextDisplay getDisplay() {
        return (TextDisplay) display;
    }
}
