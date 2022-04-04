package me.ikevoodoo.basicchest.listeners;

import me.ikevoodoo.basicchest.BasicChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final BasicChest plugin;

    public PlayerConnectionListener(BasicChest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(AsyncPlayerPreLoginEvent event) {
        plugin.getPlayerDataManager().load(event.getUniqueId());
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        plugin.getPlayerDataManager().terminate(event.getPlayer().getUniqueId());
    }

}
