package me.pm7.gradient;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VectorGradient {

    List<GradientValue<Vector>> entries;
    EasingMode easingMode;

    VectorGradient(EasingMode easingMode, GradientValue<Vector>... gradientEntries) {
        this.easingMode = easingMode;
        this.entries = new ArrayList<>(Arrays.asList(gradientEntries));
    }

    public Vector interpolate(double position) {
        position = easingMode.ease(position);

        //TODO: get interpolated vector
    }

}
