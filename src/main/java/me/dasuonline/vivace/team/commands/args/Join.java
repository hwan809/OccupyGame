package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Join extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) { // /팀 생성 x
            teamManager.logMessage(player, ChatColor.RED + "가입할 [팀 이름] 을 입력해주세요.");
            return true;
        }

        String teamName = args[1];

        if (!containsTeam(teamName)) {
            teamManager.logMessage(player, ChatColor.RED + "그런 팀이 없습니다.");
            return true;
        }

        MinecraftTeam nowTeam = getTeam(teamName);

        if (isPlayerTeamMember(player, nowTeam)) {
            teamManager.logMessage(player, ChatColor.RED + "이미 들어가려는 팀에 소속되어 있습니다.");
            return true;
        }

        if (isPlayerMember(player)) {
            teamManager.logMessage(player, ChatColor.RED + "이미 다른 팀에 소속되어 있습니다.");
            return true;
        }

        nowTeam.getTeamMember().add(player);
        teamManager.logMessage(player, "[ " + teamName + " ]" + ChatColor.GREEN + " 팀에 가입했습니다!");

        return true;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
