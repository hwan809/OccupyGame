package me.dasuonline.vivace.team.commands.args;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.manager.TeamUtils;
import me.dasuonline.vivace.team.provider.ListInvProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamList extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    private SmartInventory teamListInventory;
    private InventoryManager invManager;

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }

    @Override
    public void init() {
        this.invManager = new InventoryManager(Main.getInstance());
        this.invManager.init();

        teamListInventory = SmartInventory.builder()
                .manager(invManager)
                .provider(new ListInvProvider())
                .size(6, 9)
                .title(ChatColor.GOLD + "[ 팀 목록 ]")
                .build();

        teamManager = Main.getManager("Team");
    }
}
