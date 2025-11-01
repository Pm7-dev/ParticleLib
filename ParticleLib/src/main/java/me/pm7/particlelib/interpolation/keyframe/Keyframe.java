package me.pm7.particlelib.interpolation.keyframe;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a value T with a position between 0.0 and 0.1, for interpolating between in gradients
 * @param <T> The type of value to use in the keyframe
 */
public class Keyframe<T> implements ConfigurationSerializable {

    private T value;
    private double position;

    /**
     * Creates a new Keyframe
     * @param value The value of this keyframe
     * @param position How far along the gradient this keyframe is (from 0.0 to 1.0)
     */
    public Keyframe(T value, double position) {
        this.value = value;

        if (position > 1.0) this.position = 1.0;
        else this.position = Math.max(position, 0.0);
    }

    /**
     * Returns the value of this keyframe
     * @return the value of this keyframe
     */
    public T value() {return value;}

    /**
     * Returns the position of this keyframe (from 0.0 to 0.1)
     * @return the keyframe's position
     */
    public double position() {return position;}

    // Config stuff
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("value", value);
        map.put("position", position);
        return map;
    }
    public Keyframe(Map<String, Object> map) {
        this.value = (T) map.get("value");
        this.position = (double) map.get("position");
    }
}
