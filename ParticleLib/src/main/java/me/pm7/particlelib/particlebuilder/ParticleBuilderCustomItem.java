package me.pm7.particlelib.particlebuilder;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
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

    private ParticleBuilderCustomItem(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Vector> initialDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Vector> initialRotation, Gradient rotationSpeedOverLifetime, Gradient colorOverLifetime, ItemStack item) {
        super(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRotation, rotationSpeedOverLifetime);
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
        Vector direction = ValueRange.getRandom(initialDirection).normalize();

        PotionContents.Builder builder = PotionContents.potionContents();
        builder.potion(null);
        builder.customColor(color.interpolate(0));
        item.setData(DataComponentTypes.POTION_CONTENTS, builder);

        return new ParticleItem(emitter, location, pLifeticks, ticksPerCalculation, offset, gravity, direction, scale, rotation, rotationSpeed, rotationOverVelocity, color, item);
    }


    // Data specific to ParticleDataCustomItem
    public ParticleBuilderCustomItem colorOverLifetime(RangedGradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleBuilderCustomItem colorOverLifetime(GradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleBuilderCustomItem colorOverLifetime(Color color) {this.colorOverLifetime = new GradientColor(color); return this;}

    public ParticleBuilderCustomItem item(ItemStack item) {this.item = item; return this;}


    // Data specific to 3D particle spawners
    public ParticleBuilderCustomItem initialRotation(ValueRange<Vector> initialRotation) {this.initialRotation = initialRotation; return this;}
    public ParticleBuilderCustomItem initialRotation(Vector initialRotation) {this.initialRotation = new ValueRange<>(initialRotation); return this;}

    public ParticleBuilderCustomItem rotationSpeedOverLifetime(RangedGradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}
    public ParticleBuilderCustomItem rotationSpeedOverLifetime(GradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}
    public ParticleBuilderCustomItem rotationSpeedOverLifetime(Vector rotationSpeed) {this.rotationSpeedOverLifetime = new GradientVector(rotationSpeed); return this;}

    public ParticleBuilderCustomItem rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}


    // Data general to all ParticleSpawners
    public ParticleBuilderCustomItem particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}
    public ParticleBuilderCustomItem particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    public ParticleBuilderCustomItem ticksPerCalculation(int ticksPerCalculation) {this.ticksPerCalculation = ticksPerCalculation; return this;}

    public ParticleBuilderCustomItem spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}
    public ParticleBuilderCustomItem spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    public ParticleBuilderCustomItem initialDirection(ValueRange<Vector> initialDirection) {this.initialDirection = initialDirection; return this;}
    public ParticleBuilderCustomItem initialDirection(Vector initialDirection) {this.initialDirection = new ValueRange<>(initialDirection); return this;}

    public ParticleBuilderCustomItem scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleBuilderCustomItem scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleBuilderCustomItem scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    public ParticleBuilderCustomItem gravity(Gravity gravity) {this.gravity = gravity; return this;}


    public ParticleBuilderCustomItem clone() {
        return new ParticleBuilderCustomItem(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRotation, rotationSpeedOverLifetime, colorOverLifetime, item);
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
