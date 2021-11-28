package me.dasuonline.vivace.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHead {
    public static ItemStack getPlayerHead(String playerName) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1 , (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(playerName);
        item.setItemMeta(meta);

        return item;
    }
}
