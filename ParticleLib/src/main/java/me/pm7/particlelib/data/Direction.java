package me.pm7.particlelib.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record Direction(double yaw, double pitch) implements ConfigurationSerializable {

    public Vector toVector() {

        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        return new Vector(
                Math.cos(yawRad) * Math.cos(pitchRad),
                Math.sin(pitchRad),
                Math.sin(yawRad) * Math.cos(pitchRad)
        );
    }

    public static Vector getRandomVector(Direction d1, Direction d2) {

        double yawRad = Math.toRadians(d1.yaw + (Math.random() * (d2.yaw - d1.yaw)));
        double pitchRad = Math.toRadians(d1.pitch + (Math.random() * (d2.pitch - d1.pitch)));

        return new Vector(
                Math.cos(yawRad) * Math.cos(pitchRad),
                Math.sin(pitchRad),
                Math.sin(yawRad) * Math.cos(pitchRad)
        );

    }

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("yaw", yaw);
        map.put("pitch", pitch);
        return map;
    }
    public Direction(Map<String, Object> map) {
        this((double) map.get("yaw"), (double) map.get("pitch"));
    }
}
