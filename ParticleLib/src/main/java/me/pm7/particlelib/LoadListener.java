package me.pm7.particlelib;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class LoadListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntitiesLoad(EntitiesLoadEvent e) {
        List<Entity> entities = new ArrayList<>(e.getEntities());
        for(int i=0; i<entities.size(); i++) {
            Entity entity = entities.get(i);
            PersistentDataContainer container = entity.getPersistentDataContainer();

            if(container.has(ParticleLib.PARTICLE_KEY)) {
                entities.remove(entity);
                entity.remove();
                i--;
            } else if(container.has(ParticleLib.EMITTER_KEY, PersistentDataType.LONG)) {
                if(container.get(ParticleLib.EMITTER_KEY, PersistentDataType.LONG) != ParticleLib.SESSION_IDENTIFIER) {
                    entities.remove(entity);
                    entity.remove();
                    i--;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntitiesUnload(EntitiesUnloadEvent e) {
        List<Entity> entities = new ArrayList<>(e.getEntities());
        for(int i=0; i<entities.size(); i++) {
            Entity entity = entities.get(i);
            PersistentDataContainer container = entity.getPersistentDataContainer();
            if(container.has(ParticleLib.PARTICLE_KEY)) {
                entities.remove(entity);
                entity.remove();
                i--;
            }
        }
    }

}
