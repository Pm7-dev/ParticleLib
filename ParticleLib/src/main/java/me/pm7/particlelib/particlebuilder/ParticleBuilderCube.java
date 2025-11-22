package me.pm7.particlelib.particlebuilder;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.data.gradient.*;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.particle.Particle;
import me.pm7.particlelib.particle.ParticleItem;
import me.pm7.particlelib.physics.Gravity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores data about particles using my custom cube models
 */
public class ParticleBuilderCube extends ParticleBuilder3D {

    private Gradient colorOverLifetime;
    private boolean shaded;

    public ParticleBuilderCube() {
        super();
        this.colorOverLifetime = new GradientColor(Color.WHITE);
        this.shaded = false;
    }

    private ParticleBuilderCube(ValueRange<Integer> particleLifeTicks, int ticksPerCalculation, ValueRange<Vector> spawnOffset, ValueRange<Direction> initialMovementDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Vector> initialRotation, boolean velocityOverridesRotation, Gradient rotationSpeedOverLifetime, Gradient colorOverLifetime, boolean shaded) {
        super(particleLifeTicks, ticksPerCalculation, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRotation, velocityOverridesRotation, rotationSpeedOverLifetime);
        this.colorOverLifetime = colorOverLifetime;
        this.shaded = shaded;
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

        String model = this.shaded ? "particle_cube" : "particle_cube_shadeless";
        ItemStack item = new ItemStack(Material.POPPED_CHORUS_FRUIT);
        ItemMeta meta = item.getItemMeta();
        CustomModelDataComponent cmdp = meta.getCustomModelDataComponent();
        cmdp.setStrings(Collections.singletonList(model));
        meta.setCustomModelDataComponent(cmdp);
        item.setItemMeta(meta);

        PotionContents.Builder builder = PotionContents.potionContents();
        builder.potion(null);
        builder.customColor(color.interpolate(0));
        item.setData(DataComponentTypes.POTION_CONTENTS, builder);

        return new ParticleItem(emitter, location, pLifeticks, ticksPerCalculation, offset, gravity, direction, scale, rotation, velocityOverridesRotation, rotationSpeed, rotationOverVelocity, color, item);
    }


    // Data specific to ParticleDataCube

    /**
     * Sets the color gradient over the particle's lifetime.
     *
     * @param colorGradient the ranged color gradient
     * @return this builder
     */
    public ParticleBuilderCube colorOverLifetime(RangedGradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}

    /**
     * Sets the color gradient over the particle's lifetime.
     *
     * @param colorGradient the color gradient
     * @return this builder
     */
    public ParticleBuilderCube colorOverLifetime(GradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}

    /**
     * Sets a constant color over the particle's lifetime.
     *
     * @param color the color
     * @return this builder
     */
    public ParticleBuilderCube colorOverLifetime(Color color) {this.colorOverLifetime = new GradientColor(color); return this;}

    /**
     * Sets whether this cube particle should be shaded.
     *
     * @param shaded shaded flag
     * @return this builder
     */
    public ParticleBuilderCube shaded(boolean shaded) {this.shaded = shaded; return this;}


    // Data specific to 3D particle spawners

    /**
     * {@inheritDoc}
     * @param initialRotation {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube initialRotation(ValueRange<Vector> initialRotation) {this.initialRotation = initialRotation; return this;}

    /**
     * {@inheritDoc}
     * @param initialRotation {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube initialRotation(Vector initialRotation) {this.initialRotation = new ValueRange<>(initialRotation); return this;}

    /**
     * {@inheritDoc}
     * @param velocityOverridesRotation {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube velocityOverridesRotation(boolean velocityOverridesRotation) {this.velocityOverridesRotation = velocityOverridesRotation; return this;}

    /**
     * {@inheritDoc}
     * @param rotationSpeedOverLifetime {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube rotationSpeedOverLifetime(RangedGradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}

    /**
     * {@inheritDoc}
     * @param rotationSpeedOverLifetime {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube rotationSpeedOverLifetime(GradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}

    /**
     * {@inheritDoc}
     * @param rotationSpeed {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube rotationSpeedOverLifetime(Vector rotationSpeed) {this.rotationSpeedOverLifetime = new GradientVector(rotationSpeed); return this;}

    /**
     * {@inheritDoc}
     * @param rotationOverVelocity {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}


    // Data general to all ParticleSpawners

    /**
     * {@inheritDoc}
     * @param particleLifeTicks {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}

    /**
     * {@inheritDoc}
     * @param particleLifeTicks {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    /**
     * {@inheritDoc}
     * @param ticksPerCalculation {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube ticksPerCalculation(int ticksPerCalculation) {this.ticksPerCalculation = ticksPerCalculation; return this;}

    /**
     * {@inheritDoc}
     * @param spawnOffset {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}

    /**
     * {@inheritDoc}
     * @param spawnOffset {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    /**
     * {@inheritDoc}
     * @param initialMovementDirection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube initialMovementDirection(ValueRange<Direction> initialMovementDirection) {this.initialMovementDirection = initialMovementDirection; return this;}

    /**
     * {@inheritDoc}
     * @param initialMovementDirection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube initialMovementDirection(Direction initialMovementDirection) {this.initialMovementDirection = new ValueRange<>(initialMovementDirection); return this;}

    /**
     * {@inheritDoc}
     * @param scaleGradient {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}

    /**
     * {@inheritDoc}
     * @param scaleGradient {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}

    /**
     * {@inheritDoc}
     * @param scale {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    /**
     * {@inheritDoc}
     * @param gravity {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube gravity(Gravity gravity) {this.gravity = gravity; return this;}

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ParticleBuilderCube clone() {
        return new ParticleBuilderCube(particleLifeTicks, ticksPerCalculation, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRotation, velocityOverridesRotation, rotationSpeedOverLifetime, colorOverLifetime, shaded);
    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "cube");
        map.put("colorOverLifetime", colorOverLifetime);
        map.put("shaded", shaded);
        return map;
    }
    public ParticleBuilderCube(Map<String, Object> map) {
        super(map);
        this.colorOverLifetime = (Gradient) map.get("colorOverLifetime");
        this.shaded = (boolean) map.get("shaded");
    }
}