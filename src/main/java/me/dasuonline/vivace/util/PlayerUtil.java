package me.dasuonline.vivace.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Objects;

public class PlayerUtil {

    public static OfflinePlayer find(String name) throws IllegalArgumentException {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (Objects.equals(offlinePlayer.getName(), name)) return offlinePlayer;
        }
        throw new IllegalArgumentException("OfflinePlayer named '" + name + "' was not found");
    }
}
