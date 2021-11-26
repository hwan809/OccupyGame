package me.dasuonline.vivace.commands.interfaces;

import org.bukkit.entity.Player;

public interface Manager {
    public void init();
    public void logMessage(Player p, String s);
    public String getName();
}
