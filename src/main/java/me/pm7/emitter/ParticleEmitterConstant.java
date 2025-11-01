package me.pm7.emitter;

import me.pm7.particle.ParticleType;
import org.bukkit.Location;

public class ParticleEmitterConstant extends ParticleEmitter {



    public ParticleEmitterConstant(Location location, ParticleType particleType, int particlesPerSpawn, int ticksPerSpawn) {
        super(location);
    }

    @Override
    public void tick() {

    }
}
