package me.dasuonline.vivace.team.commands.args;

import fr.minuskube.inv.InventoryListener;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamUtils;
import me.dasuonline.vivace.team.provider.ListInvProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamList extends TeamUtils implements CustomExecutor, Listener {

    private Manager teamManager;

    public static SmartInventory teamListInventory;
    private InventoryManager invManager;

    @Override
    public boolean execute(Player player, String[] args) {
        teamListInventory.open(player);

        return true;
    }

    @Override
    public void init() {
        this.invManager = new InventoryManager(Main.getInstance());
        this.invManager.init();
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());

        teamManager = Main.getManager("Team");

        teamListInventory = SmartInventory.builder()
                .provider(new ListInvProvider())
                .size(3, 9)
                .title(ChatColor.GOLD + "[ 팀 목록 ]")
                .manager(invManager)

                .build();
    }

    @EventHandler
    public void playerClickTeamInv(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("[ 팀 정보 ]")) {
            if (event.getSlot() == 18) {
                Player clickPlayer = (Player) event.getWhoClicked();

                clickPlayer.closeInventory();
                teamListInventory.open(clickPlayer);
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerClickTeamItemStack(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("[ 팀 목록 ]")) {
            Player p = (Player) event.getWhoClicked();

            if (event.getCurrentItem() == null) return;

            if (isTeamItemStack(event.getCurrentItem())) {
                MinecraftTeam itemTeam = getTeamByItemStack(event.getCurrentItem());

                try {
                    p.closeInventory();
                    p.openInventory(itemTeam.getTeamInventory());
                } catch (Exception e) {
                    teamManager.logMessage(p, ChatColor.RED + "잘못된 접근입니다. 팀이 삭제 / 수정된 것 같습니다.");
                    teamListInventory.open(p);
                }

                event.setCancelled(true);
            }
        }
    }
}
