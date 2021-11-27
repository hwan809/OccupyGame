package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Leave extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        if (!isPlayerMember(player)) {
            teamManager.logMessage(player, ChatColor.RED + "소속된 팀이 없습니다.");
            return true;
        }

        MinecraftTeam playerTeam = getPlayerTeam(player);

        if (playerTeam.getAdmin().equals(player)) {
            teamManager.logMessage(player, ChatColor.RED + "당신은 " + ChatColor.WHITE + "[ " +
                    playerTeam.getName() + " ]" + ChatColor.RED + " 의 마스터입니다! 마스터는 팀에서 탈퇴할 수 없습니다.");
            return true;
        }

        if (playerTeam.removePlayer(player)) {
            teamManager.logMessage(player, "[ " + playerTeam.getName() + " ]" + ChatColor.GREEN + " 팀에서 탈퇴했습니다!");
        } else {
            teamManager.logMessage(player, ChatColor.RED + "ERROR - Leave Unable [Unexpected Exception] 관리자에게 문의하세요.");
        }

        return true;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
