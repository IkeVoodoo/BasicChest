package me.ikevoodoo.basicchest.data;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * This class can only be created by the {@link PlayerDataManager} class.
 *
 * Part of the API.
 *
 * */
public class PlayerCache {

    private final ItemStack[] chestItems;

    protected PlayerCache() {
        chestItems = new ItemStack[54];
    }

    public void setChestItem(int slot, ItemStack item) {
        if(slot < 0 || slot > 53)
            return;
        chestItems[slot] = item;
    }

    public ItemStack getChestItem(int slot) {
        return chestItems[slot];
    }

    public void clearChestItems() {
        Arrays.fill(chestItems, null);
    }

    public ItemStack[] getChestItems() {
        return chestItems;
    }

}
