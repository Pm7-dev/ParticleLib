package me.pm7.particlelib.particlebuilder;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.particle.Particle;
import me.pm7.particlelib.particle.ParticleText;
import me.pm7.particlelib.physics.Gravity;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores data about 3D (Item-Display) particles created through an ItemStack.
 */
public class ParticleBuilderCustomText extends ParticleBuilder2D {

    private Component text;

    public ParticleBuilderCustomText() {
        super();
        this.colorOverLifetime = new GradientColor(Color.WHITE);
        this.text = Component.text("Text");
        this.shaded = true;
    }

    private ParticleBuilderCustomText(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Vector> initialDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Double> initialRoll, Gradient rollSpeedOverLifetime, Gradient colorOverLifetime, boolean shaded, Component text) {
        super(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRoll, rollSpeedOverLifetime, colorOverLifetime, shaded);
        this.text = text;
    }

    @Override
    public Particle build(ParticleManager manager, Location location) {
        int pLifeticks = random.nextInt(particleLifeTicks.getV1(), particleLifeTicks.getV2() + 1);

        // Bake ranged gradients into gradients if necessary
        GradientVector scale = scaleOverLifetime instanceof RangedGradientVector ranged ? ranged.bake() : (GradientVector) scaleOverLifetime;
        GradientDouble rollSpeed = rollSpeedOverLifetime instanceof RangedGradientDouble ranged ? ranged.bake() : (GradientDouble) rollSpeedOverLifetime;
        GradientColor color = colorOverLifetime instanceof RangedGradientColor ranged ? ranged.bake() : (GradientColor) colorOverLifetime;
        Vector offset = ValueRange.getRandom(spawnOffset);
        double roll = random.nextDouble() * (initialRoll.getV2() - initialRoll.getV1()) + initialRoll.getV1();
        Vector direction = ValueRange.getRandom(initialDirection).normalize();

        return new ParticleText(manager, location, pLifeticks, offset, gravity, direction, scale, roll, rollSpeed, rotationOverVelocity, color, shaded, text);
    }


    // Data specific to ParticleDataCustomText
    public ParticleBuilderCustomText text(Component text) {this.text = text; return this;}


    // Data specific to 2d particle spawners
    public ParticleBuilderCustomText initialRoll(ValueRange<Double> initialRoll) {this.initialRoll = initialRoll; return this;}
    public ParticleBuilderCustomText initialRoll(double initialRoll) {this.initialRoll = new ValueRange<>(initialRoll); return this;}

    public ParticleBuilderCustomText rollSpeedOverLifetime(RangedGradientDouble rollSpeedOverLifetime) {this.rollSpeedOverLifetime = rollSpeedOverLifetime; return this;}
    public ParticleBuilderCustomText rollSpeedOverLifetime(GradientDouble rollSpeedOverLifetime) {this.rollSpeedOverLifetime = rollSpeedOverLifetime; return this;}
    public ParticleBuilderCustomText rollSpeedOverLifetime(double rollSpeed) {this.rollSpeedOverLifetime = new GradientDouble(rollSpeed); return this;}

    public ParticleBuilderCustomText rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}

    public ParticleBuilderCustomText colorOverLifetime(RangedGradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleBuilderCustomText colorOverLifetime(GradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleBuilderCustomText colorOverLifetime(Color color) {this.colorOverLifetime = new GradientColor(color); return this;}

    public ParticleBuilderCustomText shaded(boolean shaded) {this.shaded = shaded; return this;}


    // Data general to all ParticleSpawners
    public ParticleBuilderCustomText particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}
    public ParticleBuilderCustomText particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    public ParticleBuilderCustomText spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}
    public ParticleBuilderCustomText spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    public ParticleBuilderCustomText initialDirection(ValueRange<Vector> initialDirection) {this.initialDirection = initialDirection; return this;}
    public ParticleBuilderCustomText initialDirection(Vector initialDirection) {this.initialDirection = new ValueRange<>(initialDirection); return this;}

    public ParticleBuilderCustomText scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleBuilderCustomText scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleBuilderCustomText scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    public ParticleBuilderCustomText gravity(Gravity gravity) {this.gravity = gravity; return this;}


    public ParticleBuilderCustomText clone() {
        return new ParticleBuilderCustomText(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRoll, rollSpeedOverLifetime, colorOverLifetime, shaded, text);
    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "customText");
        map.put("text", text);
        return map;
    }
    public ParticleBuilderCustomText(Map<String, Object> map) {
        super(map);
        this.text = (Component) map.get("text");
    }
}
