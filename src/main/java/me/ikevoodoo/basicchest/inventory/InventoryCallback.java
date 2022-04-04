package me.ikevoodoo.basicchest.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryCallback {

    void onInventoryUpdated(Player player, int slot, ItemStack stack);

    void onInventoryClosed(Player player, Inventory inventory);

}
