package me.ikevoodoo.basicchest.commands;

import me.ikevoodoo.basicchest.BasicChest;
import me.ikevoodoo.basicchest.data.PlayerCache;
import me.ikevoodoo.basicchest.inventory.InventoryCallback;
import me.ikevoodoo.basicchest.inventory.InventoryManager;
import me.ikevoodoo.basicchest.util.InventoryUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChestCommand implements CommandExecutor {

    private final BasicChest plugin;

    public ChestCommand(BasicChest plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("§cYou must be a player to use this command!");
            return true;
        }

        UUID id = player.getUniqueId();
        PlayerCache cache = plugin.getPlayerDataManager().get(id);
        Inventory inv = InventoryUtils.createInventory("Personal Chest", 54, cache.getChestItems());
        InventoryManager.openInventory(player, inv, new InventoryCallback() {

            /**
             * Update the cache
             * */
            @Override
            public void onInventoryUpdated(Player player, int slot, ItemStack stack) {
                cache.setChestItem(slot, stack);
            }

            /**
             * Handle saving on inventory close for extra safety
             * */
            @Override
            public void onInventoryClosed(Player player, Inventory inventory) {
                FileConfiguration config = plugin.getConfig();
                if(config.getBoolean("saveItemsOnClose", false)) {
                    for (int i = 0; i < inventory.getSize(); i++) {
                        ItemStack stack = inventory.getItem(i);
                        if (stack != null && !stack.getType().isAir()) {
                            cache.setChestItem(i, stack);
                        }
                    }
                    plugin.getPlayerDataManager().save(id);
                }
            }
        });
        player.sendMessage("§aOpened your personal chest!");


        return true;
    }
}
