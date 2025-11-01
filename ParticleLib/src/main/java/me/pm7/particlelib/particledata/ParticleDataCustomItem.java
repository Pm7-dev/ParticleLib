package me.pm7.particlelib.particledata;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import me.pm7.particlelib.ParticleManager;
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

import java.util.HashMap;
import java.util.Map;

public class ParticleDataCustomItem extends ParticleData3D {
    private Gradient colorOverLifetime;
    private ItemStack item;

    public ParticleDataCustomItem() {
        super();
        this.colorOverLifetime = new GradientColor(Color.WHITE);
        this.item = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA);
    }

    private ParticleDataCustomItem(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Vector> initialDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Vector> initialRotation, Gradient rotationSpeedOverLifetime, Gradient colorOverLifetime, ItemStack item) {
        super(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRotation, rotationSpeedOverLifetime);
        this.colorOverLifetime = colorOverLifetime;
        this.item = item;
    }

    @Override
    public Particle spawnParticle(ParticleManager manager, Location location) {
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

        return new ParticleItem(manager, location, pLifeticks, offset, gravity, direction, scale, rotation, rotationSpeed, rotationOverVelocity, color, item);
    }


    // Data specific to ParticleDataCustomItem
    public ParticleDataCustomItem colorOverLifetime(RangedGradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleDataCustomItem colorOverLifetime(GradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleDataCustomItem colorOverLifetime(Color color) {this.colorOverLifetime = new GradientColor(color); return this;}

    public ParticleDataCustomItem item(ItemStack item) {this.item = item; return this;}


    // Data specific to 3D particle spawners
    public ParticleDataCustomItem initialRotation(ValueRange<Vector> initialRotation) {this.initialRotation = initialRotation; return this;}
    public ParticleDataCustomItem initialRotation(Vector initialRotation) {this.initialRotation = new ValueRange<>(initialRotation); return this;}

    public ParticleDataCustomItem rotationSpeedOverLifetime(RangedGradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}
    public ParticleDataCustomItem rotationSpeedOverLifetime(GradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}
    public ParticleDataCustomItem rotationSpeedOverLifetime(Vector rotationSpeed) {this.rotationSpeedOverLifetime = new GradientVector(rotationSpeed); return this;}

    public ParticleDataCustomItem rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}


    // Data general to all ParticleSpawners
    public ParticleDataCustomItem particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}
    public ParticleDataCustomItem particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    public ParticleDataCustomItem spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}
    public ParticleDataCustomItem spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    public ParticleDataCustomItem initialDirection(ValueRange<Vector> initialDirection) {this.initialDirection = initialDirection; return this;}
    public ParticleDataCustomItem initialDirection(Vector initialDirection) {this.initialDirection = new ValueRange<>(initialDirection); return this;}

    public ParticleDataCustomItem scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleDataCustomItem scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleDataCustomItem scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    public ParticleDataCustomItem gravity(Gravity gravity) {this.gravity = gravity; return this;}


    public ParticleDataCustomItem clone() {
        return new ParticleDataCustomItem(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRotation, rotationSpeedOverLifetime, colorOverLifetime, item);
    }

    // Config stuff
    @Override
    public Map<String, Object> serialize() {
Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "customItem");
        map.put("colorOverLifetime", colorOverLifetime);
        map.put("item", item);
        return map;
    }
    public ParticleDataCustomItem(Map<String, Object> map) {
        super(map);
        this.colorOverLifetime = (Gradient) map.get("colorOverLifetime");
        this.item = (ItemStack) map.get("item");
    }
}
