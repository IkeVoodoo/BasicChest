package me.ikevoodoo.basicchest.inventory;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class InventoryManager {

    private InventoryManager() {

    }

    private static final Object2ObjectArrayMap<UUID, Inventory> inventories = new Object2ObjectArrayMap<>();
    private static final Object2ObjectArrayMap<UUID, InventoryCallback> callbacks = new Object2ObjectArrayMap<>();

    public static void openInventory(Player player, Inventory inventory, InventoryCallback callback) {
        inventories.put(player.getUniqueId(), inventory);
        callbacks.put(player.getUniqueId(), callback);
        player.openInventory(inventory);
    }

    public static InventoryCallback getCallback(Player player) {
        return callbacks.get(player.getUniqueId());
    }

    public static void terminateInventory(Player player) {
        InventoryCallback callback = callbacks.remove(player.getUniqueId());
        if (callback == null) return;
        Inventory inventory = inventories.remove(player.getUniqueId());
        callback.onInventoryClosed(player, inventory);
    }


}
