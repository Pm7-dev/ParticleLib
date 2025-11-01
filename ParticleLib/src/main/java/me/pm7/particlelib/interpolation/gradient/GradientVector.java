package me.pm7.particlelib.interpolation.gradient;

import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A gradient of vectors to be interpolated between
 */
public class GradientVector extends Gradient implements ConfigurationSerializable {

    private final List<Keyframe<Vector>> keyframes;

    /**
     * Creates a new GradientVector using a list of Vector Keyframes and sorts the keyframes
     * @param easingMode The easing mode used when interpolating between keyframes
     * @param keyframes The list of Vector Keyframes
     */
    GradientVector(EasingMode easingMode, List<Keyframe<Vector>> keyframes) {
        super(easingMode);
        this.keyframes = keyframes;
    }

    /**
     * Creates a new GradientVector
     * @param easingMode The easing mode used when interpolating between keyframes
     * @param keyframes A list of Vector Keyframes to interpolate between
     */
    @SafeVarargs
    public GradientVector(EasingMode easingMode, Keyframe<Vector>... keyframes) {
        super (easingMode);
        this.keyframes = new ArrayList<>(Arrays.asList(keyframes));
        this.keyframes.sort(Comparator.comparingDouble(Keyframe::position));
    }

    /**
     * Creates a GradientVector using only one vector (when you have to input a gradient but only want one vector)
     * @param v The vector to use
     */
    public GradientVector(Vector v) {
        super(EasingMode.LINEAR);
        this.keyframes = new ArrayList<>();
        keyframes.add(new Keyframe<>(v, 0.0));
    }

    /**
     * Interpolates between the keyframes based on the input position and the easing mode, returning an interpolated vector
     * @param position The input position
     * @return The interpolated vector
     */
    public Vector interpolate(double position) {
        position = easingMode.ease(position);

        Keyframe<Vector> lower = null;
        Keyframe<Vector> upper = null;

        // Find neighbors
        for (int i = 0; i < keyframes.size(); i++) {
            Keyframe<Vector> keyframe = keyframes.get(i);

            if (keyframe.position() == position) return keyframe.value();

            if (keyframe.position() > position) {
                upper = keyframe;
                if(i!=0) lower = keyframes.get(i - 1);
                break;
            }
        }

        if (upper == null) return keyframes.getLast().value().clone();
        if (lower == null) return keyframes.getFirst().value().clone();

        double localProgress = (position - lower.position()) / (upper.position() - lower.position());
        Vector v1 = lower.value();
        Vector v2 = upper.value();
        double x = (v1.getX() + (v2.getX() - v1.getX()) * localProgress);
        double y = (v1.getY() + (v2.getY() - v1.getY()) * localProgress);
        double z = (v1.getZ() + (v2.getZ() - v1.getZ()) * localProgress);

        return new Vector(x, y, z);
    }

    /**
     * Returns the list of Vector Keyframes that make up this gradient
     * @return the list of Vector Keyframes
     */
    @Override
    public List<Keyframe<Vector>> getKeyframes() {
        return keyframes;
    }
    
    // Config stuff
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("easing", easingMode.toString());
        map.put("keyframes", keyframes);
        return map;
    }
    public GradientVector(Map<String, Object> map) {
        super(EasingMode.valueOf((String) map.get("easing")));
        this.keyframes = ((List<?>) map.get("keyframes")).stream()
                .map(o -> (Keyframe<Vector>) o)
                .collect(Collectors.toList());
    }

}