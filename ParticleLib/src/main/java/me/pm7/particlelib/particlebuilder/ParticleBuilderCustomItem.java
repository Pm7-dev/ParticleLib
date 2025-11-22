package me.pm7.particlelib.particlebuilder;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.data.gradient.*;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.particle.ParticleItem;
import me.pm7.particlelib.particle.Particle;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores data about 2D (Text-Display) particles created through a text component
 */
public class ParticleBuilderCustomItem extends ParticleBuilder3D {
    private Gradient colorOverLifetime;
    private ItemStack item;

    public ParticleBuilderCustomItem() {
        super();
        this.colorOverLifetime = new GradientColor(Color.WHITE);
        this.item = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA);
    }

    private ParticleBuilderCustomItem(ValueRange<Integer> particleLifeTicks, int ticksPerCalculation, ValueRange<Vector> spawnOffset, ValueRange<Direction> initialMovementDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Vector> initialRotation, boolean velocityOverridesRotation, Gradient rotationSpeedOverLifetime, Gradient colorOverLifetime, ItemStack item) {
        super(particleLifeTicks, ticksPerCalculation, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRotation, velocityOverridesRotation, rotationSpeedOverLifetime);
        this.colorOverLifetime = colorOverLifetime;
        this.item = item;
    }

    @Override
    public Particle build(ParticleEmitter emitter, Location location) {
        int pLifeticks = random.nextInt(particleLifeTicks.getV1(), particleLifeTicks.getV2() + 1);

        // Bake ranged gradients into gradients if necessary
        GradientVector scale = scaleOverLifetime instanceof RangedGradientVector ranged ? ranged.bake() : (GradientVector) scaleOverLifetime;
        GradientVector rotationSpeed = rotationSpeedOverLifetime instanceof RangedGradientVector ranged ? ranged.bake() : (GradientVector) rotationSpeedOverLifetime;
        GradientColor color = colorOverLifetime instanceof RangedGradientColor ranged ? ranged.bake() : (GradientColor) colorOverLifetime;
        Vector offset = ValueRange.getRandom(spawnOffset);
        Vector rotation = ValueRange.getRandom(initialRotation);
        Vector direction = Direction.getRandomVector(initialMovementDirection.getV1(), initialMovementDirection.getV2());

        PotionContents.Builder builder = PotionContents.potionContents();
        builder.potion(null);
        builder.customColor(color.interpolate(0));
        item.setData(DataComponentTypes.POTION_CONTENTS, builder);

        return new ParticleItem(emitter, location, pLifeticks, ticksPerCalculation, offset, gravity, direction, scale, rotation, velocityOverridesRotation, rotationSpeed, rotationOverVelocity, color, item);
    }


    // Data specific to ParticleDataCustomItem

    /**
     * Sets the color gradient over the particle's lifetime.
     *
     * @param colorGradient the ranged color gradient
     * @return this builder
     */
    public ParticleBuilderCustomItem colorOverLifetime(RangedGradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}

    /**
     * Sets the color gradient over the particle's lifetime.
     *
     * @param colorGradient the color gradient
     * @return this builder
     */
    public ParticleBuilderCustomItem colorOverLifetime(GradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}

    /**
     * Sets a constant color over the particle's lifetime.
     *
     * @param color the color
     * @return this builder
     */
    public ParticleBuilderCustomItem colorOverLifetime(Color color) {this.colorOverLifetime = new GradientColor(color); return this;}

    /**
     * Sets the item stack used to render this particle.
     *
     * @param item the item stack
     * @return this builder
     */
    public ParticleBuilderCustomItem item(ItemStack item) {this.item = item; return this;}


    // Data specific to 3D particle spawners

    /**
     * {@inheritDoc}
     * @param initialRotation {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem initialRotation(ValueRange<Vector> initialRotation) {this.initialRotation = initialRotation; return this;}

    /**
     * {@inheritDoc}
     * @param initialRotation {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem initialRotation(Vector initialRotation) {this.initialRotation = new ValueRange<>(initialRotation); return this;}

    /**
     * {@inheritDoc}
     * @param velocityOverridesRotation {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem velocityOverridesRotation(boolean velocityOverridesRotation) {this.velocityOverridesRotation = velocityOverridesRotation; return this;}

    /**
     * {@inheritDoc}
     * @param rotationSpeedOverLifetime {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem rotationSpeedOverLifetime(RangedGradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}

    /**
     * {@inheritDoc}
     * @param rotationSpeedOverLifetime {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem rotationSpeedOverLifetime(GradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}

    /**
     * {@inheritDoc}
     * @param rotationSpeed {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem rotationSpeedOverLifetime(Vector rotationSpeed) {this.rotationSpeedOverLifetime = new GradientVector(rotationSpeed); return this;}

    /**
     * {@inheritDoc}
     * @param rotationOverVelocity {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}


    // Data general to all ParticleSpawners

    /**
     * {@inheritDoc}
     * @param particleLifeTicks {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}

    /**
     * {@inheritDoc}
     * @param particleLifeTicks {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    /**
     * {@inheritDoc}
     * @param ticksPerCalculation {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem ticksPerCalculation(int ticksPerCalculation) {this.ticksPerCalculation = ticksPerCalculation; return this;}

    /**
     * {@inheritDoc}
     * @param spawnOffset {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}

    /**
     * {@inheritDoc}
     * @param spawnOffset {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    /**
     * {@inheritDoc}
     * @param initialMovementDirection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem initialMovementDirection(ValueRange<Direction> initialMovementDirection) {this.initialMovementDirection = initialMovementDirection; return this;}

    /**
     * {@inheritDoc}
     * @param initialMovementDirection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem initialMovementDirection(Direction initialMovementDirection) {this.initialMovementDirection = new ValueRange<>(initialMovementDirection); return this;}

    /**
     * {@inheritDoc}
     * @param scaleGradient {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}

    /**
     * {@inheritDoc}
     * @param scaleGradient {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}

    /**
     * {@inheritDoc}
     * @param scale {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    /**
     * {@inheritDoc}
     * @param gravity {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem gravity(Gravity gravity) {this.gravity = gravity; return this;}

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCustomItem clone() {
        return new ParticleBuilderCustomItem(particleLifeTicks, ticksPerCalculation, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRotation, velocityOverridesRotation, rotationSpeedOverLifetime, colorOverLifetime, item);
    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "customItem");
        map.put("colorOverLifetime", colorOverLifetime);
        map.put("item", item);
        return map;
    }
    public ParticleBuilderCustomItem(Map<String, Object> map) {
        super(map);
        this.colorOverLifetime = (Gradient) map.get("colorOverLifetime");
        this.item = (ItemStack) map.get("item");
    }
}