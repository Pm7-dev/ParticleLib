package me.pm7.particlelib.emitter;

import me.pm7.particlelib.ParticleManager;
import me.pm7.particlelib.particlebuilder.ParticleBuilder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A particle emitter that, while activated, spawns particles depending on its movement
 */
public class ParticleEmitterMovement extends ParticleEmitter {

    private Location previousLocation;

    private double metersPerParticle;
    private double metersWithoutParticle;

    private int particleCutoff;


    /**
     * Creates a new constant particle emitter
     * @param metersPerParticle The distance in meters required to spawn one particle.
     * @param particleCutoff If the distance moved in a single tick is more than enough to spawn this number of particles, this movement
     *                       will be ignored. This is to prevent large distance teleportations (going through portals, teleporting an emitter far away, etc. ) from lagging/crashing
     *                            a server.
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param particleBuilder The particle data to use when this emitter spawns a particle
     * @param maxParticles {@inheritDoc}
     * @param viewDistance {@inheritDoc}
     */
    public ParticleEmitterMovement(double metersPerParticle, int particleCutoff, ParticleBuilder particleBuilder, Location location, long maxParticles, int viewDistance) {
        super(particleBuilder, location, maxParticles, viewDistance);

        this.metersPerParticle = metersPerParticle;

        this.previousLocation = location.clone();
        this.metersWithoutParticle = 0.0d;
        this.particleCutoff = particleCutoff;
    }

    /**
     * Creates a new constant particle emitter
     * @param metersPerParticle The distance in meters required to spawn one particle.
     * @param particleCutoff If the distance moved in a single tick is more than enough to spawn this number of particles, this movement
     *                       will be ignored. This is to prevent large distance teleportations (going through portals, teleporting an emitter far away, etc. ) from lagging/crashing
     *                            a server.
     * @param location The location to spawn the ParticleEmitter's display entity
     * @param particleBuilder The particle data to use when this emitter spawns a particle
     */
    public ParticleEmitterMovement(double metersPerParticle, int particleCutoff, ParticleBuilder particleBuilder, Location location) {
        super(particleBuilder, location, 0, 0);

        this.metersPerParticle = metersPerParticle;

        this.previousLocation = location.clone();
        this.metersWithoutParticle = 0.0d;
        this.particleCutoff = particleCutoff;
    }

    /**
     * Advances the emitter one tick forward
     */
    @Override
    public void tick() {

        // Get distance
        Location currentLocation = getLocation();
        if(!currentLocation.getWorld().equals(previousLocation.getWorld())) {
            previousLocation = currentLocation.clone();
            return;
        }
        double distance = currentLocation.distance(previousLocation);

        // Ignore spawn if the distance is huge
        if(distance > (1/metersPerParticle) * particleCutoff) {
            previousLocation = currentLocation.clone();
            return;
        }

        int particlesToSpawn = 0;

        // Start counting particles until we run out of distance to spawn them with
        while (distance >= metersPerParticle) {
            particlesToSpawn++;
            distance -= metersPerParticle;
        }

        // Add the distance equivalent of pocket change to this variable
        metersWithoutParticle += distance;
        if(metersWithoutParticle >= metersPerParticle) { // if the scraps have accumulated enough, spawn one more particle
            particlesToSpawn++;
            metersWithoutParticle -= metersPerParticle;
        }

        // Spawn particles in a line between the current location and the previous location for a smooth effect
        for(int i=0; i<particlesToSpawn; i++) {
            Location particleLoc = currentLocation.clone().add(previousLocation.clone().subtract(currentLocation).toVector().normalize().multiply(metersPerParticle * i));
            spawnParticleAtLocation(particleLoc);
        }

        previousLocation = currentLocation.clone();
    }

    private void spawnParticleAtLocation(Location loc) {
        particles.add(particleBuilder.build(this, loc));
        if(maxParticles > 0) {
            while (particles.size() > maxParticles) {
                particles.getFirst().remove();
                particles.removeFirst();
            }
        }
    }

    /**
     * Sets the amount of particles that spawn for each meter traveled by the emitter
     * @param metersPerParticle the new amount.
     */
    public void setMetersPerParticle(double metersPerParticle) {
        this.metersPerParticle = metersPerParticle;
    }
    public double getMetersPerParticle() {return metersPerParticle;}

    // Config stuff
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(super.serialize());
        map.put("type", "constant");
        map.put("particlesPerMeter", metersPerParticle);
        return map;
    }
    public ParticleEmitterMovement(Map<String, Object> map) {
        super(map);
        this.metersPerParticle = (double) map.get("particlesPerMeter");
    }
}
