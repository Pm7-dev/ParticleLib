package me.pm7.particlelib.particlebuilder;

import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.data.gradient.*;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.particle.Particle;
import me.pm7.particlelib.particle.ParticleText;
import me.pm7.particlelib.physics.Gravity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
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

    private ParticleBuilderCustomText(ValueRange<Integer> particleLifeTicks, int ticksPerCalculation, ValueRange<Vector> spawnOffset, ValueRange<Direction> initialMovementDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Double> initialRoll, Gradient rollSpeedOverLifetime, Gradient colorOverLifetime, boolean shaded, Component text) {
        super(particleLifeTicks, ticksPerCalculation, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRoll, rollSpeedOverLifetime, colorOverLifetime, shaded);
        this.text = text;
    }

    @Override
    public Particle build(ParticleEmitter emitter, Location location) {
        int pLifeticks = random.nextInt(particleLifeTicks.getV1(), particleLifeTicks.getV2() + 1);

        // Bake ranged gradients into gradients if necessary
        GradientVector scale = scaleOverLifetime instanceof RangedGradientVector ranged ? ranged.bake() : (GradientVector) scaleOverLifetime;
        GradientDouble rollSpeed = rollSpeedOverLifetime instanceof RangedGradientDouble ranged ? ranged.bake() : (GradientDouble) rollSpeedOverLifetime;
        GradientColor color = colorOverLifetime instanceof RangedGradientColor ranged ? ranged.bake() : (GradientColor) colorOverLifetime;
        Vector offset = ValueRange.getRandom(spawnOffset);
        double roll = random.nextDouble() * (initialRoll.getV2() - initialRoll.getV1()) + initialRoll.getV1();
        Vector direction = Direction.getRandomVector(initialMovementDirection.getV1(), initialMovementDirection.getV2());

        return new ParticleText(emitter, location, pLifeticks, ticksPerCalculation, offset, gravity, direction, scale, roll, rollSpeed, rotationOverVelocity, color, shaded, text);
    }


    // Data specific to ParticleDataCustomText

    /**
     * Sets the text component used for particles created by this builder.
     *
     * @param text the text component
     * @return this builder
     */
    public ParticleBuilderCustomText text(Component text) {this.text = text; return this;}


    // Data specific to 2d particle spawners

    /**
     * {@inheritDoc}
     * @param initialRoll {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText initialRoll(ValueRange<Double> initialRoll) {this.initialRoll = initialRoll; return this;}

    /**
     * {@inheritDoc}
     * @param initialRoll {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText initialRoll(double initialRoll) {this.initialRoll = new ValueRange<>(initialRoll); return this;}

    /**
     * {@inheritDoc}
     * @param rollSpeedOverLifetime {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText rollSpeedOverLifetime(RangedGradientDouble rollSpeedOverLifetime) {this.rollSpeedOverLifetime = rollSpeedOverLifetime; return this;}

    /**
     * {@inheritDoc}
     * @param rollSpeedOverLifetime {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText rollSpeedOverLifetime(GradientDouble rollSpeedOverLifetime) {this.rollSpeedOverLifetime = rollSpeedOverLifetime; return this;}

    /**
     * {@inheritDoc}
     * @param rollSpeed {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText rollSpeedOverLifetime(double rollSpeed) {this.rollSpeedOverLifetime = new GradientDouble(rollSpeed); return this;}

    /**
     * {@inheritDoc}
     * @param rotationOverVelocity {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}

    /**
     * {@inheritDoc}
     * @param colorGradient {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText colorOverLifetime(RangedGradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}

    /**
     * {@inheritDoc}
     * @param colorGradient {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText colorOverLifetime(GradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}

    /**
     * {@inheritDoc}
     * @param color {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText colorOverLifetime(Color color) {this.colorOverLifetime = new GradientColor(color); return this;}

    /**
     * {@inheritDoc}
     * @param shaded {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText shaded(boolean shaded) {this.shaded = shaded; return this;}


    // Data general to all ParticleSpawners

    /**
     * {@inheritDoc}
     * @param particleLifeTicks {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}

    /**
     * {@inheritDoc}
     * @param particleLifeTicks {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    /**
     * {@inheritDoc}
     * @param ticksPerCalculation {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText ticksPerCalculation(int ticksPerCalculation) {this.ticksPerCalculation = ticksPerCalculation; return this;}

    /**
     * {@inheritDoc}
     * @param spawnOffset {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}

    /**
     * {@inheritDoc}
     * @param spawnOffset {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    /**
     * {@inheritDoc}
     * @param initialMovementDirection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText initialMovementDirection(ValueRange<Direction> initialMovementDirection) {this.initialMovementDirection = initialMovementDirection; return this;}

    /**
     * {@inheritDoc}
     * @param initialMovementDirection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText initialMovementDirection(Direction initialMovementDirection) {this.initialMovementDirection = new ValueRange<>(initialMovementDirection); return this;}

    /**
     * {@inheritDoc}
     * @param scaleGradient {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}

    /**
     * {@inheritDoc}
     * @param scaleGradient {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}

    /**
     * {@inheritDoc}
     * @param scale {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    /**
     * {@inheritDoc}
     * @param gravity {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText gravity(Gravity gravity) {this.gravity = gravity; return this;}

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomText clone() {
        return new ParticleBuilderCustomText(particleLifeTicks, ticksPerCalculation, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRoll, rollSpeedOverLifetime, colorOverLifetime, shaded, text);
    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "customText");
        map.put("text", JSONComponentSerializer.json().serialize(text));
        return map;
    }
    public ParticleBuilderCustomText(Map<String, Object> map) {
        super(map);
        this.text = JSONComponentSerializer.json().deserialize((String) map.get("text"));
    }
}