package me.ikevoodoo.basicchest.util;

import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class ItemUtils {

    private ItemUtils() {

    }

    public static String toB64(ItemStack itemStack) {
        return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
    }
    
    public static ItemStack fromB64(String b64) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(b64));
    }

}
