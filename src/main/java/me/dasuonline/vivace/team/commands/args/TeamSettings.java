package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.entity.Player;

public class TeamSettings extends TeamUtils implements CustomExecutor {

    @Override
    public boolean execute(Player player, String[] args) {
        return true;
    }

    @Override
    public void init() {

    }
}
