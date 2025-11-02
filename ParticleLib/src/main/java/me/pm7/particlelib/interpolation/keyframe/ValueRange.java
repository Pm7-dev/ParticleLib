package me.pm7.particlelib.interpolation.keyframe;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A range of two values, usually to be randomly interpolated between
 * @param <T>
 */
public class ValueRange<T> implements ConfigurationSerializable {

    protected final T v1;
    protected final T v2;

    /**
     * Creates a new ValueRange
     * @param v1 the first value
     * @param v2 the second value
     */
    public ValueRange(T v1, T v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    /**
     * Creates a new ValueRange but using only one value, for when you want there to be no variance
     * @param value the only value
     */
    public ValueRange(T value) {
        this.v1 = value;
        this.v2 = value;
    }

    /**
     * Returns the first value
     * @return the first value
     */
    public T getV1() {return v1;}

    /**
     * Returns the second value
     * @return the second value
     */
    public T getV2() {return v2;}

    private static final Random random = new Random();

    /**
     * Gets a random interpolation of vectors when using a ValueRange<Vector>, helper method just because it's used a lot.
     * @param range the Vector value range to interpolate between
     * @return A random Vector between the two values in the ValueRange
     */
    public static Vector getRandom(ValueRange<Vector> range) {
        Vector min = range.getV1();
        Vector max = range.getV2();
        double iX = random.nextDouble() * (max.getX() - min.getX()) + min.getX();
        double iY = random.nextDouble() * (max.getY() - min.getY()) + min.getY();
        double iZ = random.nextDouble() * (max.getZ() - min.getZ()) + min.getZ();
        return new Vector(iX, iY, iZ);
    }


    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("v1", v1);
        map.put("v2", v2);
        return map;
    }
    public ValueRange(Map<String, Object> map) {
        this.v1 = (T) map.get("v1");
        this.v2 = (T) map.get("v2");
    }
}
