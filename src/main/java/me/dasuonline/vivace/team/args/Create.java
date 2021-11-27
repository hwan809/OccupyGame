package me.dasuonline.vivace.team.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class Create implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) { // /팀 생성 x
            teamManager.logMessage(player, ChatColor.RED + "[팀 이름] 을 설정해주세요.");
            return true;
        }

        String teamName = args[1];

        if (containsTeam(teamName)) {
            teamManager.logMessage(player, ChatColor.RED + "이미 존재하는 [팀 이름] 입니다.");
            return true;
        }

        if (teamName.contains("§")) {
            teamManager.logMessage(player, ChatColor.RED + "[팀 이름] 에 컬러코드를 사용할 수 없습니다!");
            return true;
        }

        if (teamName.length() > 7) {
            teamManager.logMessage(player, ChatColor.RED + "[팀 이름] 은 7글자 이하여야 합니다.");
            return true;
        }

        //검사 완료, 팀 생성
        MinecraftTeam newTeam = new MinecraftTeam(teamName,
                new Timestamp(System.currentTimeMillis()), player);

        TeamManager.teamList.add(newTeam);
        teamManager.logMessage(player,"[ " + teamName + " ]" + ChatColor.GREEN + " 팀을 생성했습니다!");

        return true;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }

    public boolean containsTeam(String teamName) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getName().equals(teamName)) {
                return true;
            }
        }

        return false;
    }
}