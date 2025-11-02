package me.pm7.particlelib.interpolation.keyframe;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a keyframe with two values,
 * @param <T>
 */
public class RangedKeyframe<T> extends ValueRange<T> implements ConfigurationSerializable {

    private final double position;

    public RangedKeyframe(T v1, T v2, double position) {
        super(v1, v2);
        if(position > 1.0) this.position = 1.0;
        else this.position = Math.max(position, 0.0);
    }

    public RangedKeyframe(T v, double position) {
        super(v, v);
        if(position > 1.0) this.position = 1.0;
        else this.position = Math.max(position, 0.0);
    }

    public double getPosition() {
        return position;
    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("v1", v1);
        map.put("v2", v2);
        map.put("position", position);
        return map;
    }
    public RangedKeyframe(Map<String, Object> map) {
        super((T) map.get("v1"), (T) map.get("v2"));
        this.position = (double) map.get("position");
    }
}
