package me.pm7.particlelib.interpolation.gradient;

import me.pm7.particlelib.interpolation.keyframe.EasingMode;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A gradient of colors to be interpolated between
 */
public class GradientColor extends Gradient implements ConfigurationSerializable {

    private final List<Keyframe<Color>> keyframes;

    /**
     * Creates a new GradientColor using a list of Color Keyframes and sorts the keyframes
     * @param easingMode The easing mode used when interpolating between keyframes
     * @param keyframes The list of Color Keyframes
     */
    public GradientColor(EasingMode easingMode, List<Keyframe<Color>> keyframes) {
        super(easingMode);
        this.keyframes = new ArrayList<>(keyframes);
        this.keyframes.sort(Comparator.comparingDouble(Keyframe::position));
    }

    /**
     * Creates a new GradientColor
     * @param easingMode The easing mode used when interpolating between keyframes
     * @param keyframes A list of Color Keyframes to interpolate between
     */
    @SafeVarargs
    public GradientColor(EasingMode easingMode, Keyframe<Color>... keyframes) {
        super (easingMode);
        this.keyframes = new ArrayList<>(Arrays.asList(keyframes));
        this.keyframes.sort(Comparator.comparingDouble(Keyframe::position));
    }

    /**
     * Creates a GradientColor using only one color (when you have to input a gradient but only want one color)
     * @param c The color to use
     */
    public GradientColor(Color c) {
        super(EasingMode.LINEAR);
        this.keyframes = new ArrayList<>();
        keyframes.add(new Keyframe<>(c, 0.0));
    }

    /**
     * Interpolates between the keyframes based on the input position and the easing mode, returning an interpolated color
     * @param position The input position
     * @return The interpolated color
     */
    public Color interpolate(double position) {
        position = easingMode.ease(position);

        Keyframe<Color> lower = null;
        Keyframe<Color> upper = null;

        // Find neighbors
        for (int i = 0; i < keyframes.size(); i++) {
            Keyframe<Color> keyframe = keyframes.get(i);

            if (keyframe.position() == position) return keyframe.value();

            if (keyframe.position() > position) {
                upper = keyframe;
                if(i!=0) lower = keyframes.get(i - 1);
                break;
            }
        }

        if (upper == null) return keyframes.getLast().value();
        if (lower == null) return keyframes.getFirst().value();

        // Normalize progress
        double localProgress = (position - lower.position()) / (upper.position() - lower.position());

        Color c1 = lower.value();
        Color c2 = upper.value();

        // Convert sRGB components to linear RGB
        double r1 = srgbToLinear(c1.getRed() / 255.0);
        double g1 = srgbToLinear(c1.getGreen() / 255.0);
        double b1 = srgbToLinear(c1.getBlue() / 255.0);

        double r2 = srgbToLinear(c2.getRed() / 255.0);
        double g2 = srgbToLinear(c2.getGreen() / 255.0);
        double b2 = srgbToLinear(c2.getBlue() / 255.0);

        // Linear interpolation in linear RGB space
        double rLin = (1 - localProgress) * r1 + localProgress * r2;
        double gLin = (1 - localProgress) * g1 + localProgress * g2;
        double bLin = (1 - localProgress) * b1 + localProgress * b2;

        // Convert back to sRGB for Bukkit Color
        int r = (int) Math.round(linearToSrgb(rLin) * 255);
        int g = (int) Math.round(linearToSrgb(gLin) * 255);
        int b = (int) Math.round(linearToSrgb(bLin) * 255);
        int a = (int) (c1.getAlpha()  + (c2.getAlpha()  - c1.getAlpha())  * localProgress);

        return Color.fromARGB(a, r, g, b);
    }

    /**
     * Returns the list of Color Keyframes that make up this gradient
     * @return the list of Color Keyframes
     */
    @Override
    public List<Keyframe<Color>> getKeyframes() {
        return keyframes;
    }

    // Convert from sRGB to linear RGB
    private double srgbToLinear(double c) {
        return (c <= 0.04045)
                ? c / 12.92
                : Math.pow((c + 0.055) / 1.055, 2.4);
    }

    // Convert from linear RGB to sRGB
    private double linearToSrgb(double c) {
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
    public GradientColor(Map<String, Object> map) {
        super(EasingMode.valueOf((String) map.get("easing")));
        this.keyframes = ((List<?>) map.get("keyframes")).stream()
                .map(o -> (Keyframe<Color>) o)
                .collect(Collectors.toList());
    }
}
