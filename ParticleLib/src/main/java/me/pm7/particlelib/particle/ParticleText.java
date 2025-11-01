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
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticleText extends Particle {
    private static final Vector3f SQUARE_SCALE_MULTIPLIER = new Vector3f(8f, 4f, 8f);

    private final double initialRoll;
    private final GradientDouble rotationSpeedOverLifetime;
    private final boolean velocityRotationDirectionPositive;

    private final GradientColor colorOverLifetime;
    private final Component text;

    public ParticleText(ParticleManager manager, Location location, int lifeTicks, Vector spawnOffset, Gravity gravity, Vector initialDirection,
                        GradientVector scaleOverLifetime, double initialRoll, GradientDouble rotationSpeedOverLifetime,
                        ValueRange<Double> rotationOverVelocity, GradientColor colorOverLifetime, boolean shaded,
                        Component text) {
        super(manager, location, lifeTicks, gravity, initialDirection, scaleOverLifetime, rotationOverVelocity);

        this.initialRoll = initialRoll;
        this.rotationSpeedOverLifetime = rotationSpeedOverLifetime;
        this.velocityRotationDirectionPositive = random.nextDouble() > 0.5;

        this.colorOverLifetime = colorOverLifetime;

        Location loc = location.clone().add(spawnOffset);
        loc.setPitch(90.0f);
        loc.setYaw(0.0f);

        display = (TextDisplay) loc.getWorld().spawnEntity(loc, EntityType.TEXT_DISPLAY);
        TextDisplay textDisplay = getDisplay();
        if(!shaded) textDisplay.setBrightness(new Display.Brightness(15, 15));

        this.text = text;
        if(text == null) {
            textDisplay.text(Component.text(" "));
            textDisplay.setTextOpacity((byte) 10);
        } else {
            textDisplay.setBackgroundColor(Color.fromARGB(10, 0, 0, 0));
        }
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setDefaultBackground(false);

        display.getPersistentDataContainer().set(ParticleLib.PARTICLE_KEY, PersistentDataType.BOOLEAN, true);
        super.initialTransform();
    }

    protected void transform(double lifePosition, int steps) {

        // Get interpolated scale
        Vector scale = scaleOverLifetime.interpolate(lifePosition);
        Vector3f scalef = new Vector3f((float) scale.getX(), (float) scale.getY(), (float) scale.getZ());

        Quaternionf rotation = display.getTransformation().getLeftRotation();

        // Either set up initial rotation or modify rotation
        if(lifePosition == 0.0) {
            rotation.rotateZ((float) Math.toRadians(initialRoll));
        } else {
            // Get the changes in rotation
            float rollChange  = (float) Math.toRadians(rotationSpeedOverLifetime.interpolate(lifePosition) * 0.05 * steps);

            // Rotation over velocity
            double magnitude = displacement.length();
            double v1 = rotationOverVelocity.getV1();
            double v2 = rotationOverVelocity.getV2();
            rollChange += (float) (magnitude*(velocityRotationDirectionPositive ?1:-1)*Math.toRadians(v2 + random.nextDouble()*(v2-v1) - v1/2));

            // Apply change in rotation
            rotation.rotateZ(rollChange);
        }

        if(text == null) {
            display.setTransformation(new Transformation(
                    new Vector3f(),
                    rotation,
                    scalef.mul(SQUARE_SCALE_MULTIPLIER),
                    new Quaternionf()
            ));
        } else {
            display.setTransformation(new Transformation(
                    new Vector3f(),
                    rotation,
                    scalef,
                    new Quaternionf()
            ));
        }
    }

    @Override
    public void color(double lifePosition) {
        TextDisplay textDisplay = getDisplay();
        Color color = colorOverLifetime.interpolate(lifePosition);

        if(text == null) {
            textDisplay.setBackgroundColor(color);
        } else {
            textDisplay.text(text.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue())));
        }
    }

    @Override
    public TextDisplay getDisplay() {
        return (TextDisplay) display;
    }
}
