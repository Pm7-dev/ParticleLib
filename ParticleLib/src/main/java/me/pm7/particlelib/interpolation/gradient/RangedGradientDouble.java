package me.pm7.particlelib.interpolation.gradient;

import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import me.pm7.particlelib.interpolation.keyframe.RangedKeyframe;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A gradient of double ranges to be randomly interpolated between. Unlike GradientDouble which interpolates
 * between specific values, this class allows defining ranges of possible values at each keyframe position,
 * from which random values will be generated when baked into a GradientDouble.
 */
public class RangedGradientDouble extends RangedGradient implements ConfigurationSerializable {

    List<RangedKeyframe<Double>> keyframes;

    /**
     * Creates a new RangedGradientDouble using multiple double ranges at different positions
     * @param easingMode The easing mode used when interpolating between keyframes
     * @param keyframes A list of RangedKeyframe containing double ranges to interpolate between
     */
    @SafeVarargs
    public RangedGradientDouble(EasingMode easingMode, RangedKeyframe<Double>... keyframes) {
        super(easingMode);
        this.keyframes = new ArrayList<>(Arrays.asList(keyframes));
        this.keyframes.sort(Comparator.comparingDouble(RangedKeyframe::getPosition));
    }

    /**
     * Creates a RangedGradientDouble using a single double range at position 0
     * @param v1 The minimum value of the range
     * @param v2 The maximum value of the range
     */
    public RangedGradientDouble(double v1, double v2) {
        super(EasingMode.LINEAR);
        this.keyframes = new ArrayList<>(Collections.singletonList(new RangedKeyframe<>(v1, v2, 0.0)));
    }

    /**
     * Generates a GradientDouble by randomly selecting values within the defined ranges
     * @return A new GradientDouble with randomly generated values from the defined ranges
     */
    @Override
    public GradientDouble bake() {
        List<Keyframe<Double>> generatedValues = new ArrayList<>();

        for(RangedKeyframe<Double> range : keyframes) {
            Double randomValue = range.getV1() + ((range.getV2() - range.getV1()) * random.nextDouble());
            generatedValues.add(new Keyframe<>(randomValue, range.getPosition()));
        }

        return new GradientDouble(easingMode, generatedValues);
    }

    /**
     * Returns the list of RangedKeyframe containing the double ranges that make up this gradient
     * @return the list of double range keyframes
     */
    @Override
    public List<RangedKeyframe<Double>> getKeyframes() {
        return keyframes;
    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("easing", easingMode.toString());
        map.put("keyframes", keyframes);
        return map;
    }
    public RangedGradientDouble(Map<String, Object> map) {
        super(EasingMode.valueOf((String) map.get("easing")));
        this.keyframes = ((List<?>) map.get("keyframes")).stream()
                .map(o -> (RangedKeyframe<Double>) o)
                .collect(Collectors.toList());
    }

}