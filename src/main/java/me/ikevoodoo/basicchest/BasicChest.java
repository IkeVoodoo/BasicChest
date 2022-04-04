package me.ikevoodoo.basicchest;

import com.mongodb.MongoSocketOpenException;
import me.ikevoodoo.basicchest.commands.ChestCommand;
import me.ikevoodoo.basicchest.data.PlayerCache;
import me.ikevoodoo.basicchest.data.PlayerDataManager;
import me.ikevoodoo.basicchest.listeners.PlayerConnectionListener;
import me.ikevoodoo.basicchest.listeners.PlayerInventoryListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;


/**
 * <pre>
 * Basic chest plugin by Ike#2932 for a DevRoom application.
 *
 * The api can be used like so:
 *
 *  {@link BasicChest} basicChest = {@link JavaPlugin}.getPlugin({@link BasicChest}.class);
 *  {@link PlayerDataManager} playerDataManager = basicChest.getPlayerDataManager();
 *
 *  {@link UUID} uuid = ...;
 *  {@link PlayerCache} playerCache = playerDataManager.getPlayerCache(uuid);
 *
 *  // First item in the inventory
 *  int slot = 0;
 *  {@link ItemStack} myItem = ...;
 *
 *  // Set the item in the player's personal chest
 *  playerCache.setChestItem(slot, myItem);
 *
 *  // Get the item at a given slot from the player's personal chest
 *  {@link ItemStack} itemInChest = playerCache.getChestItem(slot);
 *
 *  // Get all the items in the player's personal chest
 *  {@link ItemStack}[] items = playerCache.getChestItems();
 *
 *  // Empty the player's personal chest
 *  playerCache.clearChestItems();
 *
 *  // Save the player to the DB
 *  playerDataManager.save(uuid);
 * </pre>
 * */
public final class BasicChest extends JavaPlugin {

    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.yml");

        if(!configFile.exists()) {
            getLogger().warning("Please configure the plugin and restart the server.");
            saveDefaultConfig();
            return;
        }


        playerDataManager = new PlayerDataManager(this);


        getCommand("chest").setExecutor(new ChestCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInventoryListener(), this);
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
