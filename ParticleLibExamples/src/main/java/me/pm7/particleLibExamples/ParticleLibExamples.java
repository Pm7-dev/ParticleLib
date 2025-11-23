package me.pm7.particleLibExamples;

import me.pm7.particleLibExamples.commands.*;
import me.pm7.particleLibExamples.listeners.Anvil;
import me.pm7.particleLibExamples.listeners.BlockBreak;
import me.pm7.particleLibExamples.listeners.Explosion;
import me.pm7.particleLibExamples.listeners.Minecart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParticleLibExamples extends JavaPlugin {

    private static ParticleLibExamples plugin;

    @Override
    public void onEnable() {

        plugin = this;

        // Plugin startup logic
        getCommand("flame").setExecutor(new flame());
        getCommand("smoke").setExecutor(new smoke());
        getCommand("trail").setExecutor(new trail());
        getCommand("firework").setExecutor(new firework());
        getCommand("volcano").setExecutor(new volcano());

        PluginManager pm = Bukkit.getPluginManager(); // woah that's me!
        pm.registerEvents(new firework(), this);
        pm.registerEvents(new BlockBreak(), this);
        pm.registerEvents(new Explosion(), this);
        pm.registerEvents(new Minecart(), this);
        pm.registerEvents(new Anvil(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ParticleLibExamples getPlugin() {return plugin;}

}
