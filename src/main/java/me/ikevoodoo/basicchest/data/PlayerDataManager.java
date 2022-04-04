package me.ikevoodoo.basicchest.data;

import com.mongodb.MongoSocketOpenException;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.ikevoodoo.basicchest.BasicChest;
import me.ikevoodoo.basicchest.database.MongoHandler;
import me.ikevoodoo.basicchest.util.ItemUtils;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Provides a wrapper to {@link MongoHandler} for player data.
 *
 * Should only be constructed from {@link BasicChest}
 *
 * Every method is internal except methods whose javadoc explicitly mentions being part of the API
 * */
public class PlayerDataManager {

    private final Object2ObjectOpenHashMap<UUID, PlayerCache> playerCache;

    private final MongoHandler handler;

    public PlayerDataManager(BasicChest plugin) {
        playerCache = new Object2ObjectOpenHashMap<>();

        FileConfiguration config = plugin.getConfig();

        handler = new MongoHandler(
                config.getString("db.host"),
                config.getInt("db.port"),
                config.getString("db.name"),
                config.getString("db.credentials.user"),
                config.getString("db.credentials.password"));

    }

    /**
     * Terminates the cache for a user and saves the data to the database.
     *
     * Internal use only.
     *
     * @param id The UUID of the user.
     * */
    public void terminate(UUID id) {
        PlayerCache cache = playerCache.remove(id);
        if (cache == null)
            return;

        save(id, cache.getChestItems());

        // We don't need the items to be cached anymore
        cache.clearChestItems();
    }

    /**
     * Save a player to the DB
     *
     * Part of the API
     *
     * */
    public void save(UUID id) {
        save(id, playerCache.get(id).getChestItems());
    }

    /**
     * Load a player's data from the database
     *
     * Internal use only
     *
     * @param id The uuid of the player to load
     * */
    public void load(UUID id) {
        // If the user is already loaded, don't load them again.
        if (playerCache.containsKey(id))
            return;

        // If the user is not loaded, load them.
        PlayerCache cache = new PlayerCache();
        playerCache.put(id, cache);

        handler.get(new Document().append("_id", id.toString()), doc -> {
            if (doc == null)
                return;

            int count = doc.getInteger("count");

            for (int i = 0; i < count; i++) {
                String item = doc.getString(i + "");
                if (item != null)
                    cache.setChestItem(i, ItemUtils.fromB64(item));
            }
        });
    }

    /**
     * Get the cache for a specific player
     *
     * Part of the API
     *
     * @param id The player to get the cache for
     * */
    public PlayerCache get(UUID id) {
        return playerCache.get(id);
    }

    private void save(UUID id, ItemStack[] items) {
        Document doc = new Document();
        doc.append("_id", id.toString());
        doc.append("count", items.length);

        // Encode each item and save it to the database.
        for (int index = 0; index < items.length; index++) {
            ItemStack item = items[index];
            if(item != null && !item.getType().isAir())
                doc.append(index + "", ItemUtils.toB64(item));
            else
                doc.append(index + "", null);
        }
        handler.set(doc);
    }
}
