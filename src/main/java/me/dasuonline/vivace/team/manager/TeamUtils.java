package me.dasuonline.vivace.team.manager;

import me.dasuonline.vivace.team.MinecraftTeam;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TeamUtils {
    public MinecraftTeam getTeamByName(String teamName) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }

        return null;
    }

    public MinecraftTeam getTeamById(String id) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getUniqueId().equals(id)) {
                return team;
            }
        }

        return null;
    }

    public MinecraftTeam getPlayerTeam(Player player) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getAdmin().equals(player) ||
                    team.getTeamMember().contains(player) ||
                    team.getViceAdmin().contains(player)) {
                return team;
            }
        }

        return null;
    }

    public boolean containsTeamName(String teamName) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getName().equals(teamName)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsTeamId(String id) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getUniqueId().equals(id)) {
                return true;
            }
        }

        return false;
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

    public static String getUniqueId() {
        String uniqueId = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Calendar dateTime = Calendar.getInstance();
        uniqueId = sdf.format(dateTime.getTime());

        uniqueId = uniqueId + "_" + RandomStringUtils.randomAlphanumeric(6);

        return uniqueId;
    }
}
