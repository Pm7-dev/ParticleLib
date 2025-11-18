package me.pm7.particleLibExamples.commands;

import me.pm7.particleLibExamples.ParticleLibExamples;
import me.pm7.particlelib.data.Direction;
import me.pm7.particlelib.data.gradient.*;
import me.pm7.particlelib.data.keyframe.EasingMode;
import me.pm7.particlelib.data.keyframe.Keyframe;
import me.pm7.particlelib.data.keyframe.RangedKeyframe;
import me.pm7.particlelib.data.keyframe.ValueRange;
import me.pm7.particlelib.emitter.ParticleEmitterBurst;
import me.pm7.particlelib.emitter.ParticleEmitterMovement;
import me.pm7.particlelib.particlebuilder.ParticleBuilderSquare;
import me.pm7.particlelib.physics.GravityDirection;
import me.pm7.particlelib.physics.GravityNone;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class firework implements CommandExecutor, Listener {
    private static final ParticleLibExamples plugin = ParticleLibExamples.getPlugin();
    private static final Random random = new Random();

    private Player player;
    private Integer taskID = null;

    private final ParticleBuilderSquare smoke = new ParticleBuilderSquare()
            .particleLifeTicks(90)
            .initialRoll(new ValueRange<>(0.0, 360.0))
            .scaleOverLifetime(new RangedGradientVector(
                    EasingMode.SINE_OUT,
                    new RangedKeyframe<>(new Vector(0.0, 0.0, 0.0), 0.0),
                    new RangedKeyframe<>(new Vector(0.45, 0.45, 0.45), 1.0)
            ))
            .colorOverLifetime(new GradientColor(
                    EasingMode.LINEAR,
                    new Keyframe<>(Color.fromARGB(175, 230, 230, 230), 0.1),
                    new Keyframe<>(Color.fromARGB(25, 230, 230, 230), 1.0)
            ))
            .shaded(false)
            .gravity(new GravityNone(new GradientDouble(0)))
    ;
    private final ParticleBuilderSquare explosion = new ParticleBuilderSquare()
            .particleLifeTicks(30)
            .initialMovementDirection(new ValueRange<>(new Direction(0, 0), new Direction(360, 360)))
            .gravity(new GravityNone(new GradientDouble(
                    EasingMode.LINEAR,
                    new Keyframe<>(2.6, 0.0),
                    new Keyframe<>(0.3, 1.0)
            )))
            .scaleOverLifetime(new GradientVector(
                    EasingMode.LINEAR,
                    new Keyframe<>(new Vector(0.5, 0.5, 0.5), 0.5),
                    new Keyframe<>(new Vector(0, 0, 0), 1.0)
            ))
            .rollSpeedOverLifetime(new RangedGradientDouble(
                    EasingMode.LINEAR,
                    new RangedKeyframe<>(-120.0, 120.0, 0.0))
            )
            .shaded(false)
    ;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        Player p = (Player) sender;

        switch (args[0]) {
            case "start":

                if(taskID == null) {

                    player = p;

                    taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                        Location loc = player.getLocation().clone();

                        int x = loc.getBlockX() + random.nextInt(16) - 8;
                        int z = loc.getBlockZ() + random.nextInt(16) - 8;
                        Location fireworkLoc = loc.getWorld().getHighestBlockAt(x, z).getLocation().add(0.5, 1, 0.5);

                        spawnFirework(fireworkLoc);
                    }, 1L, 25L);
                }

                break;
            case "stop":
                break;
            case "item":

                ItemStack item = new ItemStack(Material.POPPED_CHORUS_FRUIT);
                ItemMeta meta = item.getItemMeta();
                meta.setItemModel(new NamespacedKey("minecraft", "firework_rocket"));
                meta.getPersistentDataContainer().set(new NamespacedKey("particlelib", "firework"), org.bukkit.persistence.PersistentDataType.BYTE, (byte) 1);
                meta.itemName(Component.text("Custom Firework"));
                item.setItemMeta(meta);

                p.getInventory().addItem(item);

                break;
            default:
                sender.sendMessage("Invalid subcommand");
                break;
        }


        return true;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if(e.getItem() == null) return;
        if(!e.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey("particlelib", "firework"), org.bukkit.persistence.PersistentDataType.BYTE)) return;

        spawnFirework(e.getInteractionPoint());
    }

    private void spawnFirework(Location location) {
        double initialY = location.getY();
        ParticleEmitterMovement smokeEmitter = new ParticleEmitterMovement(
                0.085,
                1000,
                smoke,
                location
        );
        smokeEmitter.start();

        new BukkitRunnable() {

            @Override
            public void run() {

                Location loc = smokeEmitter.getLocation().clone().add(0, 0.6, 0);

                if(loc.getY() - initialY > 12) {
                    smokeEmitter.remove();

                    ParticleBuilderSquare colouredExplosion = explosion.clone();
                    switch (random.nextInt(8)) {
                        case 0:
                            colouredExplosion.colorOverLifetime(Color.fromRGB(250, 33, 17));
                            break;
                        case 1:
                            colouredExplosion.colorOverLifetime(Color.fromRGB(237, 151, 21));
                            break;
                        case 2:
                            colouredExplosion.colorOverLifetime(Color.fromRGB(241, 252, 28));
                            break;
                        case 3:
                            colouredExplosion.colorOverLifetime(Color.fromRGB(62, 252, 28));
                            break;
                        case 4:
                            colouredExplosion.colorOverLifetime(Color.fromRGB(28, 252, 230));
                            break;
                        case 5:
                            colouredExplosion.colorOverLifetime(Color.fromRGB(28, 114, 252));
                            break;
                        case 6:
                            colouredExplosion.colorOverLifetime(Color.fromRGB(129, 28, 252));
                            break;
                        case 7:
                            colouredExplosion.colorOverLifetime(Color.fromRGB(248, 28, 252));
                            break;
                    }

                    new ParticleEmitterBurst(75, colouredExplosion, loc).start();
                    cancel();
                    return;
                }

                smokeEmitter.teleport(loc);

            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
