package me.pm7.particleLibExamples;

import me.pm7.particleLibExamples.commands.*;
import me.pm7.particleLibExamples.listeners.Anvil;
import me.pm7.particleLibExamples.listeners.BlockBreak;
import me.pm7.particleLibExamples.listeners.Explosion;
import me.pm7.particleLibExamples.listeners.Minecart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParticleLibExamples extends JavaPlugin {

    private static ParticleLibExamples plugin;

    @Override
    public void onEnable() {

        plugin = this;

        // Plugin startup logic
        getCommand("test").setExecutor(new test());
        getCommand("flame").setExecutor(new flame());
        getCommand("smoke").setExecutor(new smoke());
        getCommand("particleeditor").setExecutor(new particleeditor());
        getCommand("trail").setExecutor(new trail());
        getCommand("firework").setExecutor(new firework());
        Bukkit.getPluginManager().registerEvents(new firework(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
        Bukkit.getPluginManager().registerEvents(new Explosion(), this);
        Bukkit.getPluginManager().registerEvents(new Minecart(), this);
        Bukkit.getPluginManager().registerEvents(new Anvil(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ParticleLibExamples getPlugin() {return plugin;}

}
