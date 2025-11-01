package me.pm7.particlelib.particledata;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParticleDataCube extends ParticleData3D {

    private Gradient colorOverLifetime;
    private boolean shaded;

    public ParticleDataCube() {
        super();
        this.colorOverLifetime = new GradientColor(Color.WHITE);
        this.shaded = false;
    }

    private ParticleDataCube(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Vector> initialDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Vector> initialRotation, Gradient rotationSpeedOverLifetime, Gradient colorOverLifetime, boolean shaded) {
        super(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRotation, rotationSpeedOverLifetime);
        this.colorOverLifetime = colorOverLifetime;
        this.shaded = shaded;
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

        return new ParticleItem(manager, location, pLifeticks, offset, gravity, direction, scale, rotation, rotationSpeed, rotationOverVelocity, color, item);
    }


    // Data specific to ParticleDataCube
    public ParticleDataCube colorOverLifetime(RangedGradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleDataCube colorOverLifetime(GradientColor colorGradient) {this.colorOverLifetime = colorGradient; return this;}
    public ParticleDataCube colorOverLifetime(Color color) {this.colorOverLifetime = new GradientColor(color); return this;}

    public ParticleDataCube shaded(boolean shaded) {this.shaded = shaded; return this;}


    // Data specific to 3D particle spawners
    public ParticleDataCube initialRotation(ValueRange<Vector> initialRotation) {this.initialRotation = initialRotation; return this;}
    public ParticleDataCube initialRotation(Vector initialRotation) {this.initialRotation = new ValueRange<>(initialRotation); return this;}

    public ParticleDataCube rotationSpeedOverLifetime(RangedGradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}
    public ParticleDataCube rotationSpeedOverLifetime(GradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}
    public ParticleDataCube rotationSpeedOverLifetime(Vector rotationSpeed) {this.rotationSpeedOverLifetime = new GradientVector(rotationSpeed); return this;}

    public ParticleDataCube rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}


    // Data general to all ParticleSpawners
    public ParticleDataCube particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}
    public ParticleDataCube particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    public ParticleDataCube spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}
    public ParticleDataCube spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    public ParticleDataCube initialDirection(ValueRange<Vector> initialDirection) {this.initialDirection = initialDirection; return this;}
    public ParticleDataCube initialDirection(Vector initialDirection) {this.initialDirection = new ValueRange<>(initialDirection); return this;}

    public ParticleDataCube scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleDataCube scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleDataCube scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    public ParticleDataCube gravity(Gravity gravity) {this.gravity = gravity; return this;}


    public ParticleDataCube clone() {
        return new ParticleDataCube(particleLifeTicks, spawnOffset, initialDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRotation, rotationSpeedOverLifetime, colorOverLifetime, shaded);
    }

    // Config stuff
    @Override
    public Map<String, Object> serialize() {
Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "cube");
        map.put("colorOverLifetime", colorOverLifetime);
        map.put("shaded", shaded);
        return map;
    }
    public ParticleDataCube(Map<String, Object> map) {
        super(map);
        this.colorOverLifetime = (Gradient) map.get("colorOverLifetime");
        this.shaded = (boolean) map.get("shaded");
    }
}
