package me.ikevoodoo.basicchest.listeners;

import me.ikevoodoo.basicchest.inventory.InventoryCallback;
import me.ikevoodoo.basicchest.inventory.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayerInventoryListener implements Listener {

    @EventHandler
    public void on(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;

        InventoryCallback callback = InventoryManager.getCallback(player);
        if(callback == null) return;
        callback.onInventoryUpdated(player, event.getSlot(), event.getCursor());
    }

    @EventHandler
    public void on(InventoryDragEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;

        InventoryCallback callback = InventoryManager.getCallback(player);
        if(callback == null) return;
        for(Map.Entry<Integer, ItemStack> entry : event.getNewItems().entrySet()) {
            callback.onInventoryUpdated(player, entry.getKey(), entry.getValue());
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        InventoryManager.terminateInventory(player);
    }
}
