package me.pm7.particlelib.interpolation.gradient;

import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import me.pm7.particlelib.interpolation.keyframe.RangedKeyframe;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A gradient of vector ranges to be randomly interpolated between. Unlike GradientVector which interpolates
 * between specific vectors, this class allows defining ranges of possible vectors at each keyframe position,
 * from which random values will be generated when baked into a GradientVector.
 */
public class RangedGradientVector extends RangedGradient implements ConfigurationSerializable {

    List<RangedKeyframe<Vector>> keyframes;

    /**
     * Creates a new RangedGradientVector using multiple vector ranges at different positions
     * @param easingMode The easing mode used when interpolating between keyframes
     * @param keyframes A list of RangedKeyframe containing vector ranges to interpolate between
     */
    @SafeVarargs
    public RangedGradientVector(EasingMode easingMode, RangedKeyframe<Vector>... keyframes) {
        super(easingMode);
        this.keyframes = new ArrayList<>(Arrays.asList(keyframes));
        this.keyframes.sort(Comparator.comparingDouble(RangedKeyframe::getPosition));
    }

    /**
     * Creates a RangedGradientVector using a single vector range at position 0
     * @param v1 The first vector defining the range boundary
     * @param v2 The second vector defining the range boundary
     */
    public RangedGradientVector(Vector v1, Vector v2) {
        super(EasingMode.LINEAR);
        this.keyframes = new ArrayList<>(Collections.singletonList(new RangedKeyframe<>(v1, v2, 0.0)));
    }

    /**
     * Generates a GradientVector by randomly selecting vectors within the defined ranges.
     * Each component (x, y, z) is interpolated independently within its range.
     * @return A new GradientVector with randomly generated vectors from the defined ranges
     */
    @Override
    public GradientVector bake() {
        List<Keyframe<Vector>> generatedValues = new ArrayList<>();

        for(RangedKeyframe<Vector> range : keyframes) {

            double localProgress = random.nextDouble();
            Vector v1 = range.getV1();
            Vector v2 = range.getV2();
            double x = (localProgress * (v2.getX() - v1.getX()) + v1.getX());
            double y = (localProgress * (v2.getY() - v1.getY()) + v1.getY());
            double z = (localProgress * (v2.getZ() - v1.getZ()) + v1.getZ());

            generatedValues.add(new Keyframe<>(new Vector(x, y, z), range.getPosition()));

        }

        return new GradientVector(easingMode, generatedValues);
    }

    /**
     * Returns the list of RangedKeyframe containing the vector ranges that make up this gradient
     * @return the list of vector range keyframes
     */
    @Override
    public List<RangedKeyframe<Vector>> getKeyframes() {
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
    public RangedGradientVector(Map<String, Object> map) {
        super(EasingMode.valueOf((String) map.get("easing")));
        this.keyframes = ((List<?>) map.get("keyframes")).stream()
                .map(o -> (RangedKeyframe<Vector>) o)
                .collect(Collectors.toList());
    }
}