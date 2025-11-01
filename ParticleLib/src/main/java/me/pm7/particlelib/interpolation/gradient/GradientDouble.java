package me.pm7.particlelib.interpolation.gradient;

import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A gradient of doubles to be interpolated between
 */
public class GradientDouble extends Gradient implements ConfigurationSerializable {

    private final List<Keyframe<Double>> keyframes;

    /**
     * Creates a new GradientDouble using a list of Double Keyframes and sorts the keyframes
     * @param easingMode The easing mode used when interpolating between keyframes
     * @param keyframes The list of Double Keyframes
     */
    public GradientDouble(EasingMode easingMode, List<Keyframe<Double>> keyframes) {
        super(easingMode);
        this.keyframes = keyframes;
    }

    /**
     * Creates a new GradientDouble
     * @param easingMode The easing mode used when interpolating between keyframes
     * @param keyframes A list of Double Keyframes to interpolate between
     */
    @SafeVarargs
    public GradientDouble(EasingMode easingMode, Keyframe<Double>... keyframes) {
        super (easingMode);
        this.keyframes = new ArrayList<>(Arrays.asList(keyframes));
        this.keyframes.sort(Comparator.comparingDouble(Keyframe::position));
    }

    /**
     * Creates a GradientDouble using only one double value (when you have to input a gradient but only want one value)
     * @param d The double value to use
     */
    public GradientDouble(double d) {
        super(EasingMode.LINEAR);
        this.keyframes = new ArrayList<>();
        keyframes.add(new Keyframe<>(d, 0.0));
    }

    /**
     * Interpolates between the keyframes based on the input position and the easing mode, returning an interpolated double
     * @param position The input position
     * @return The interpolated double value
     */
    public double interpolate(double position) {
        position = easingMode.ease(position);

        Keyframe<Double> lower = null;
        Keyframe<Double> upper = null;

        // Find neighbors
        for (int i = 0; i < keyframes.size(); i++) {
            Keyframe<Double> keyframe = keyframes.get(i);

            if (keyframe.position() == position) return keyframe.value();

            if (keyframe.position() > position) {
                upper = keyframe;
                if(i!=0) lower = keyframes.get(i - 1);
                break;
            }
        }

        if (upper == null) return keyframes.getLast().value();
        if (lower == null) return keyframes.getFirst().value();

        // Normalize progress between lower and upper and interpolate
        double localProgress = (position - lower.position()) / (upper.position() - lower.position());
        return lower.value() + (upper.value() - lower.value()) * localProgress;
    }

    /**
     * Returns the list of Double Keyframes that make up this gradient
     * @return the list of Double Keyframes
     */
    @Override
    public List<Keyframe<Double>> getKeyframes() {
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
    public GradientDouble(Map<String, Object> map) {
        super(EasingMode.valueOf((String) map.get("easing")));
        this.keyframes = ((List<?>) map.get("keyframes")).stream()
                .map(o -> (Keyframe<Double>) o)
                .collect(Collectors.toList());
    }

}