package me.dasuonline.vivace.team.manager;

import me.dasuonline.vivace.team.MinecraftTeam;
import org.bukkit.entity.Player;

import java.util.List;

public class TeamUtils {
    public boolean containsTeam(String teamName) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getName().equals(teamName)) {
                return true;
            }
        }

        return false;
    }

    public MinecraftTeam getTeam(String teamName) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }

        return null;
    }

    public boolean isPlayerTeamMember(Player player, MinecraftTeam team) {
        List<Player> teamMembers = team.getTeamMember();
        List<Player> teamviceAdmins = team.getViceAdmin();

        return teamMembers.contains(player) ||
                teamviceAdmins.contains(player) ||
                team.getAdmin().equals(player);
    }

    public boolean isPlayerMember(Player player) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getAdmin().equals(player) ||
                    team.getTeamMember().contains(player) ||
                    team.getViceAdmin().contains(player)) {
                return true;
            }
        }

        return false;
    }
}
