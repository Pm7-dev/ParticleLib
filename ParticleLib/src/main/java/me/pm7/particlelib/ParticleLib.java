package me.pm7.particlelib;

import me.pm7.particlelib.emitter.ParticleEmitter;
import me.pm7.particlelib.emitter.ParticleEmitterBurst;
import me.pm7.particlelib.emitter.ParticleEmitterConstant;
import me.pm7.particlelib.interpolation.gradient.*;
import me.pm7.particlelib.interpolation.keyframe.Keyframe;
import me.pm7.particlelib.interpolation.keyframe.RangedKeyframe;
import me.pm7.particlelib.interpolation.keyframe.ValueRange;
import me.pm7.particlelib.particle.Particle;
import me.pm7.particlelib.physics.GravityAxis;
import me.pm7.particlelib.physics.GravityDirection;
import me.pm7.particlelib.physics.GravityLocation;
import me.pm7.particlelib.physics.GravityNone;
import me.pm7.particlelib.particledata.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ParticleLib extends JavaPlugin {
    public static long SESSION_IDENTIFIER = System.currentTimeMillis();

    static ParticleLib plugin;

    public static NamespacedKey PARTICLE_KEY = new NamespacedKey("particlelib", "particle");
    public static NamespacedKey EMITTER_KEY = new NamespacedKey("particlelib", "emitter");

    @Override
    public void onEnable() {

        plugin = this;

        Bukkit.getPluginManager().registerEvents(new LoadListener(), this);

        // the wall
        ConfigurationSerialization.registerClass(ValueRange.class);
        ConfigurationSerialization.registerClass(Keyframe.class);
        ConfigurationSerialization.registerClass(RangedKeyframe.class);
        ConfigurationSerialization.registerClass(GradientColor.class);
        ConfigurationSerialization.registerClass(GradientDouble.class);
        ConfigurationSerialization.registerClass(GradientVector.class);
        ConfigurationSerialization.registerClass(RangedGradientColor.class);
        ConfigurationSerialization.registerClass(RangedGradientDouble.class);
        ConfigurationSerialization.registerClass(RangedGradientVector.class);
        ConfigurationSerialization.registerClass(GravityAxis.class);
        ConfigurationSerialization.registerClass(GravityDirection.class);
        ConfigurationSerialization.registerClass(GravityLocation.class);
        ConfigurationSerialization.registerClass(GravityNone.class);
        ConfigurationSerialization.registerClass(ParticleData.class);
        ConfigurationSerialization.registerClass(ParticleData2D.class);
        ConfigurationSerialization.registerClass(ParticleData3D.class);
        ConfigurationSerialization.registerClass(ParticleDataBlock.class);
        ConfigurationSerialization.registerClass(ParticleDataCube.class);
        ConfigurationSerialization.registerClass(ParticleDataCustomItem.class);
        ConfigurationSerialization.registerClass(ParticleDataCustomText.class);
        ConfigurationSerialization.registerClass(ParticleDataSquare.class);
        ConfigurationSerialization.registerClass(ParticleEmitter.class);
        ConfigurationSerialization.registerClass(ParticleEmitterBurst.class);
        ConfigurationSerialization.registerClass(ParticleEmitterConstant.class);
    }

    @Override
    public void onDisable() {

        for(ParticleManager manager : ParticleManager.managers) {
            List<Particle> allParticles = manager.allParticles;
            while(!allParticles.isEmpty()) {
                allParticles.getFirst().remove();
            }

            List<ParticleEmitter> allEmitters = manager.allEmitters;
            while (!allEmitters.isEmpty()) {
                allEmitters.getFirst().remove();
            }
        }
    }
}
