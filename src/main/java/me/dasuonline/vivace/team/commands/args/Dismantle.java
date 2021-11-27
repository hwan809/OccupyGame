package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamManager;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Dismantle extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) { // /팀 생성 x
            teamManager.logMessage(player, ChatColor.RED + "생성한 [팀 이름] 을 입력하세요.");
            return true;
        }

        String teamName = args[1];

        if (!containsTeamName(teamName)) {
            teamManager.logMessage(player, ChatColor.RED + "그런 팀이 없습니다.");
            return true;
        }

        MinecraftTeam nowTeam = getTeamByName(teamName);

        if (!nowTeam.getAdmin().equals(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀의 대표만 팀을 해체할 수 있습니다.");
            return true;
        }

        TeamManager.teamList.remove(nowTeam);
        teamManager.logMessage(player, "[ " + teamName + " ]" + ChatColor.GREEN + " 팀을 해체했습니다!");

        return true;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }

    public boolean containsTeamName(String teamName) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getName().equals(teamName)) {
                return true;
            }
        }

        return false;
    }
}
