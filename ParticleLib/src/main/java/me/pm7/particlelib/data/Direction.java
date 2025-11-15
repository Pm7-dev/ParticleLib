package me.pm7.particlelib.data;

import org.bukkit.util.Vector;

public record Direction(double yaw, double pitch) {

    public Vector toVector() {

        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        return new Vector(
                Math.cos(Math.toRadians(yawRad)) * Math.cos(Math.toRadians(pitchRad)),
                Math.sin(Math.toRadians(pitchRad)),
                Math.sin(Math.toRadians(yawRad)) * Math.cos(Math.toRadians(pitchRad))
        );
    }

    public static Vector getRandomVector(Direction d1, Direction d2) {

        double yawRad = Math.toRadians(d1.yaw + (Math.random() * (d2.yaw - d1.yaw)));
        double pitchRad = Math.toRadians(d1.pitch + (Math.random() * (d2.pitch - d1.pitch)));

        return new Vector(
                Math.cos(Math.toRadians(yawRad)) * Math.cos(Math.toRadians(pitchRad)),
                Math.sin(Math.toRadians(pitchRad)),
                Math.sin(Math.toRadians(yawRad)) * Math.cos(Math.toRadians(pitchRad))
        );

    }

}
