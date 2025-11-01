package me.pm7.particle;

import me.pm7.ParticleManager;

public abstract class Particle {

    public void remove() {
        ParticleManager.allParticles.remove(this);
    }

}
