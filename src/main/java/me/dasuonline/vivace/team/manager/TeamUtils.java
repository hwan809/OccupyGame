package me.dasuonline.vivace.team.manager;

import me.dasuonline.vivace.team.MinecraftTeam;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TeamUtils {
    public MinecraftTeam getTeamByName(String teamName) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getTeamName().equals(teamName)) {
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
            if (team.getAdmin().getUniqueId().equals(player.getUniqueId())) {
                return team;
            }

            for (OfflinePlayer viceAdmins : team.getViceAdmins()) {
                if (viceAdmins.getUniqueId().equals(player.getUniqueId())) {
                    return team;
                }
            }

            for (OfflinePlayer members : team.getTeamMembers()) {
                if (members.getUniqueId().equals(player.getUniqueId())) {
                    return team;
                }
            }
        }

        return null;
    }

    public boolean containsTeamName(String teamName) {
        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getTeamName().equals(teamName)) {
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
        if (team.getAdmin().getUniqueId().equals(player.getUniqueId())) return true;

        for (OfflinePlayer viceAdmins : team.getViceAdmins()) {
            if (viceAdmins.getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        }

        for (OfflinePlayer members : team.getTeamMembers()) {
            if (members.getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

    public boolean isPlayerMember(Player player) {

        for (MinecraftTeam team : TeamManager.teamList) {
            if (team.getAdmin().getUniqueId().equals(player.getUniqueId())) {
                return true;
            }

            for (OfflinePlayer viceAdmins : team.getViceAdmins()) {
                if (viceAdmins.getUniqueId().equals(player.getUniqueId())) {
                    return true;
                }
            }

            for (OfflinePlayer members : team.getTeamMembers()) {
                if (members.getUniqueId().equals(player.getUniqueId())) {
                    return true;
                }
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

    public String getTeamTitle(int teamLevel) {
        if (teamLevel == 0) return "§a파티§f";
        if (teamLevel == 1) return "§e팀§f";
        if (teamLevel == 2) return "§f크§0루§f";

        return "NULL";
    }

    public boolean isTeamItemStack(ItemStack itemStack) {
        if (itemStack != null) {
            Material teamMaterial = itemStack.getType();

            return  teamMaterial == Material.GRASS ||
                    teamMaterial == Material.ENDER_STONE ||
                    teamMaterial == Material.DRAGON_EGG;
        }

        return false;
    }

    public MinecraftTeam getTeamByItemStack(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        String uniqueId = ChatColor.stripColor(im.getLore().get(3));

        for (MinecraftTeam tempTeam : TeamManager.teamList) {
            if (uniqueId.contains(tempTeam.getUniqueId())) {
                return tempTeam;
            }
        }

        return null;
    }
}
