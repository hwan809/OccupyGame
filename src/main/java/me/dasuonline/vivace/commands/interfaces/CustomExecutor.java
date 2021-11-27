package me.dasuonline.vivace.commands.interfaces;

import org.bukkit.entity.Player;

public interface CustomExecutor {

    boolean execute(Player player, String[] args);
    void init();
}
