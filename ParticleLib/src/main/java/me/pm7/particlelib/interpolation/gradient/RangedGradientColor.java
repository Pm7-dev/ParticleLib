package me.pm7.particlelib.interpolation.gradient;

import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import me.pm7.particlelib.interpolation.keyframe.RangedKeyframe;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A gradient of color ranges to be randomly interpolated between. Unlike GradientColor which interpolates 
 * between specific colors, this class allows defining ranges of possible colors at each keyframe position,
 * from which random values will be generated when baked into a GradientColor.
 */
public class RangedGradientColor extends RangedGradient implements ConfigurationSerializable {

    List<RangedKeyframe<Color>> keyframes;

    /**
     * Creates a new RangedGradientColor using multiple color ranges at different positions
     * @param easingMode The easing mode used when interpolating between keyframes
     * @param keyframes A list of RangedKeyframe containing color ranges to interpolate between
     */
    @SafeVarargs
    public RangedGradientColor(EasingMode easingMode, RangedKeyframe<Color>... keyframes) {
        super(easingMode);
        this.keyframes = new ArrayList<>(Arrays.asList(keyframes));
        this.keyframes.sort(Comparator.comparingDouble(RangedKeyframe::getPosition));
    }

    /**
     * Creates a RangedGradientColor using a single color range at position 0
     * @param v1 The first color of the range
     * @param v2 The second color of the range
     */
    public RangedGradientColor(Color v1, Color v2) {
        super(EasingMode.LINEAR);
        this.keyframes = new ArrayList<>(Collections.singletonList(new RangedKeyframe<>(v1, v2, 0.0)));
    }

    /**
     * Generates a GradientColor by randomly selecting colors within the defined ranges.
     * Colors are interpolated in linear RGB space to ensure accurate color blending.
     * @return A new GradientColor with randomly generated colors from the defined ranges
     */
    @Override
    public GradientColor bake() {
        List<Keyframe<Color>> generatedValues = new ArrayList<>();

        for(RangedKeyframe<Color> range : keyframes) {
            double position = random.nextDouble();

            Color c1 = range.getV1();
            Color c2 = range.getV2();

            // Convert sRGB components to linear RGB
            double r1 = srgbToLinear(c1.getRed() / 255.0);
            double g1 = srgbToLinear(c1.getGreen() / 255.0);
            double b1 = srgbToLinear(c1.getBlue() / 255.0);

            double r2 = srgbToLinear(c2.getRed() / 255.0);
            double g2 = srgbToLinear(c2.getGreen() / 255.0);
            double b2 = srgbToLinear(c2.getBlue() / 255.0);

            // Linear interpolation in linear RGB space
            double rLin = (1 - position) * r1 + position * r2;
            double gLin = (1 - position) * g1 + position * g2;
            double bLin = (1 - position) * b1 + position * b2;

            // Convert back to sRGB for Bukkit Color
            int r = (int) Math.round(linearToSrgb(rLin) * 255);
            int g = (int) Math.round(linearToSrgb(gLin) * 255);
            int b = (int) Math.round(linearToSrgb(bLin) * 255);
            int a = (int) (c1.getAlpha()  + (c2.getAlpha()  - c1.getAlpha())  * position);

            generatedValues.add(new Keyframe<>(Color.fromARGB(a, r, g, b), range.getPosition()));
        }

        return new GradientColor(easingMode, generatedValues);
    }

    /**
     * Returns the list of RangedKeyframe containing the color ranges that make up this gradient
     * @return the list of color range keyframes
     */
    @Override
    public List<RangedKeyframe<Color>> getKeyframes() {
        return keyframes;
    }

    /**
     * Converts a color component from sRGB to linear RGB color space
     * @param c The color component value in sRGB space (0.0 to 1.0)
     * @return The color component value in linear RGB space
     */
    private static double srgbToLinear(double c) {
        return (c <= 0.04045)
                ? c / 12.92
                : Math.pow((c + 0.055) / 1.055, 2.4);
    }

    /**
     * Converts a color component from linear RGB to sRGB color space
     * @param c The color component value in linear RGB space (0.0 to 1.0)
     * @return The color component value in sRGB space
     */
    private static double linearToSrgb(double c) {
        return (c <= 0.0031308)
                ? 12.92 * c
                : 1.055 * Math.pow(c, 1 / 2.4) - 0.055;
    }

    // Config stuff
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("easing", easingMode.toString());
        map.put("keyframes", keyframes);
        return map;
    }
    public RangedGradientColor(Map<String, Object> map) {
        super(EasingMode.valueOf((String) map.get("easing")));
        this.keyframes = ((List<?>) map.get("keyframes")).stream()
                .map(o -> (RangedKeyframe<Color>) o)
                .collect(Collectors.toList());
    }
}