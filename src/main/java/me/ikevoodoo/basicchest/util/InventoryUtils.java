package me.ikevoodoo.basicchest.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    private InventoryUtils() {

    }

    public static Inventory createInventory(String name, int size, ItemStack[] items) {
        Inventory inventory = Bukkit.createInventory(null, size, LegacyComponentSerializer.legacy('&').deserialize(name));
        for (int i = 0; i < items.length; i++) {
            inventory.setItem(i, items[i]);
        }
        return inventory;
    }

}
