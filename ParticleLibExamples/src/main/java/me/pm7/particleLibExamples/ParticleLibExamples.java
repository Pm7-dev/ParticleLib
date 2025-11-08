package me.pm7.particleLibExamples;

import me.pm7.particleLibExamples.commands.flame;
import me.pm7.particleLibExamples.commands.particleeditor;
import me.pm7.particleLibExamples.commands.test;
import me.pm7.particleLibExamples.commands.trail;
import me.pm7.particleLibExamples.listeners.Anvil;
import me.pm7.particleLibExamples.listeners.BlockBreak;
import me.pm7.particleLibExamples.listeners.Explosion;
import me.pm7.particlelib.ParticleManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParticleLibExamples extends JavaPlugin {

    private static ParticleManager particleManager;

    @Override
    public void onEnable() {
        particleManager = new ParticleManager(this, 1, 1000, 0);

        // Plugin startup logic
        getCommand("test").setExecutor(new test());
        getCommand("flame").setExecutor(new flame());
        getCommand("particleeditor").setExecutor(new particleeditor());
        getCommand("trail").setExecutor(new trail());
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
        Bukkit.getPluginManager().registerEvents(new Explosion(), this);
        Bukkit.getPluginManager().registerEvents(new Anvil(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ParticleManager getParticleManager() {return particleManager;}
}
