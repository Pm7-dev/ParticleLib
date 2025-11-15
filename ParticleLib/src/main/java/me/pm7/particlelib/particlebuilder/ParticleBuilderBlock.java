package me.pm7.particlelib.particlebuilder;

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
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores data about 3D (Item-Display) particles created by inputting block data.
 */
public class ParticleBuilderBlock extends ParticleBuilder3D {

    private BlockData blockData;

    public ParticleBuilderBlock() {
        super();
        this.blockData = Material.MAGENTA_GLAZED_TERRACOTTA.createBlockData();
    }

    private ParticleBuilderBlock(ValueRange<Integer> particleLifeTicks, ValueRange<Vector> spawnOffset, ValueRange<Direction> initialMovementDirection, Gradient scaleOverLifetime, ValueRange<Double> rotationOverVelocity, Gravity gravity, ValueRange<Vector> initialRotation, boolean velocityOverridesRotation, Gradient rotationSpeedOverLifetime, BlockData blockData) {
        super(particleLifeTicks, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity, initialRotation, velocityOverridesRotation, rotationSpeedOverLifetime);
        this.blockData = blockData;
    }

    @Override
    public Particle build(ParticleEmitter emitter, Location location) {
        int pLifeticks = random.nextInt(particleLifeTicks.getV1(), particleLifeTicks.getV2() + 1);

        // Bake ranged gradients into gradients if necessary
        GradientVector scale = scaleOverLifetime instanceof RangedGradientVector ranged ? ranged.bake() : (GradientVector) scaleOverLifetime;
        GradientVector rotationSpeed = rotationSpeedOverLifetime instanceof RangedGradientVector ranged ? ranged.bake() : (GradientVector) rotationSpeedOverLifetime;
        Vector offset = ValueRange.getRandom(spawnOffset);
        Vector rotation = ValueRange.getRandom(initialRotation);
        Vector direction = Direction.getRandomVector(initialMovementDirection.getV1(), initialMovementDirection.getV2());

        ItemStack item = new ItemStack(blockData.getMaterial());


        return new ParticleItem(emitter, location, pLifeticks, ticksPerCalculation, offset, gravity, direction, scale, rotation, velocityOverridesRotation, rotationSpeed, rotationOverVelocity, new GradientColor(Color.WHITE), item);
    }


    // data specific to ParticleDataBlock
    public ParticleBuilderBlock blockData(BlockData blockData) {this.blockData = blockData; return this;}


    // Data specific to 3D particle spawners
    public ParticleBuilderBlock initialRotation(ValueRange<Vector> initialRotation) {this.initialRotation = initialRotation; return this;}
    public ParticleBuilderBlock initialRotation(Vector initialRotation) {this.initialRotation = new ValueRange<>(initialRotation); return this;}

    public ParticleBuilderBlock velocityOverridesRotation(boolean velocityOverridesRotation) {this.velocityOverridesRotation = velocityOverridesRotation; return this;}

    public ParticleBuilderBlock rotationSpeedOverLifetime(RangedGradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}
    public ParticleBuilderBlock rotationSpeedOverLifetime(GradientVector rotationSpeedOverLifetime) {this.rotationSpeedOverLifetime = rotationSpeedOverLifetime; return this;}
    public ParticleBuilderBlock rotationSpeedOverLifetime(Vector rotationSpeed) {this.rotationSpeedOverLifetime = new GradientVector(rotationSpeed); return this;}

    public ParticleBuilderBlock rotationOverVelocity(ValueRange<Double> rotationOverVelocity) {this.rotationOverVelocity = rotationOverVelocity; return this;}


    // Data general to all ParticleSpawners
    public ParticleBuilderBlock particleLifeTicks(ValueRange<Integer> particleLifeTicks) {this.particleLifeTicks = particleLifeTicks; return this;}
    public ParticleBuilderBlock particleLifeTicks(int particleLifeTicks) {this.particleLifeTicks = new ValueRange<>(particleLifeTicks); return this;}

    public ParticleBuilderBlock ticksPerCalculation(int ticksPerCalculation) {this.ticksPerCalculation = ticksPerCalculation; return this;}

    public ParticleBuilderBlock spawnOffset(ValueRange<Vector> spawnOffset) {this.spawnOffset = spawnOffset; return this;}
    public ParticleBuilderBlock spawnOffset(Vector spawnOffset) {this.spawnOffset = new ValueRange<>(spawnOffset); return this;}

    public ParticleBuilderBlock initialMovementDirection(ValueRange<Direction> initialMovementDirection) {this.initialMovementDirection = initialMovementDirection; return this;}
    public ParticleBuilderBlock initialMovementDirection(Direction initialMovementDirection) {this.initialMovementDirection = new ValueRange<>(initialMovementDirection); return this;}

    public ParticleBuilderBlock scaleOverLifetime(RangedGradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleBuilderBlock scaleOverLifetime(GradientVector scaleGradient) {this.scaleOverLifetime = scaleGradient; return this;}
    public ParticleBuilderBlock scaleOverLifetime(Vector scale) {this.scaleOverLifetime = new GradientVector(scale); return this;}

    public ParticleBuilderBlock gravity(Gravity gravity) {this.gravity = gravity; return this;}


    public ParticleBuilderBlock clone() {
        return new ParticleBuilderBlock(particleLifeTicks, spawnOffset, initialMovementDirection, scaleOverLifetime, rotationOverVelocity, gravity.clone(), initialRotation, velocityOverridesRotation, rotationSpeedOverLifetime, blockData.clone());
    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "block");
        map.put("blockData", blockData);
        return map;
    }
    public ParticleBuilderBlock(Map<String, Object> map) {
        super(map);
        this.blockData = (BlockData) map.get("blockData");
    }
}
