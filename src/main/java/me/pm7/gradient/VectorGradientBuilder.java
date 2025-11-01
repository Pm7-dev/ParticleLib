package me.pm7.gradient;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VectorGradientBuilder {
    private static Random random = new Random();

    List<GradientValueRange<Vector>> entries;
    EasingMode easingMode;

    public VectorGradientBuilder(EasingMode easingMode, GradientValueRange<Vector>... gradientEntries) {
        this.easingMode = easingMode;
        this.entries = new ArrayList<>(Arrays.asList(gradientEntries));
    }

    public VectorGradient build() {
        GradientValue<Vector>[] list = new GradientValue[entries.size()];
        for(int i=0; i<entries.size(); i++) {
            //TODO: remove that stupid method in GradientValueRange and interpolate it yourself you idiot.
            list[i] = entries.get(i).interpolate(random.nextDouble());
        }
        return new VectorGradient(easingMode, list);
    }


}
