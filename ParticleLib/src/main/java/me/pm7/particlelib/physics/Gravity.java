package me.pm7.particlelib.physics;

import me.pm7.particlelib.particle.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.Random;

public abstract class Gravity {
    private static final Random random = new Random();

    // pulled these numbers straight out of nowhere
    protected static final double AIR_DRAG_MULTIPLIER = 0.9947;
    protected static final double WATER_DRAG_MULTIPLIER = 0.9415;
    protected static final double LAVA_DRAG_MULTIPLIER = 0.8608;

    public abstract void applyGravity(Particle particle, int step);

    public abstract double getInitialSpeed();

    protected void bounce(Vector velocity, BlockFace face, double bounciness, double gravityStrength) {
        if (velocity.lengthSquared() < gravityStrength*0.1 && random.nextBoolean()) {
            velocity.zero();
            return;
        }
        Vector normal = face.getDirection();
        double dot = velocity.dot(normal);
        Vector reflection = velocity.clone().subtract(normal.clone().multiply(2 * dot)).multiply(bounciness);
        velocity.copy(reflection);
    }

    public abstract Gravity clone();

}
