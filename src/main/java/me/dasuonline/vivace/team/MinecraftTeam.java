package me.dasuonline.vivace.team;

import lombok.Getter;
import me.dasuonline.vivace.team.manager.TeamUtils;
import me.dasuonline.vivace.util.ItemStackBuilder;
import me.dasuonline.vivace.util.PlayerHead;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MinecraftTeam {
    String teamName;
    Timestamp createdTime;
    String uniqueId;

    OfflinePlayer admin;
    List<OfflinePlayer> viceAdmins;
    List<OfflinePlayer> teamMembers;

    List<String> inviteCode;

    int teamLevel = 0;

    public MinecraftTeam(String name, Timestamp createdTime, OfflinePlayer admin) {
        this.teamName = name;
        this.createdTime = createdTime;
        this.admin = admin;

        this.viceAdmins = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
        this.inviteCode = new ArrayList<>();

        this.uniqueId = TeamUtils.getUniqueId();
    }

    public MinecraftTeam(String name, Timestamp createdTime, OfflinePlayer admin, List<OfflinePlayer> viceAdmin, List<OfflinePlayer> teamMember, String uniqueId, int teamLevel) {
        this.teamName = name;
        this.createdTime = createdTime;
        this.admin = admin;
        this.viceAdmins = viceAdmin;
        this.teamMembers = teamMember;
        this.uniqueId = uniqueId;
        this.teamLevel = teamLevel;

        this.inviteCode = new ArrayList<>();
    }

    public void saveAsConfig(FileConfiguration configFile, String path) {
        String customTeamPath = path + "." + this.teamName;

        configFile.set(customTeamPath + ".teamName", teamName);
        configFile.set(customTeamPath + ".createdTime", createdTime.toString());
        configFile.set(customTeamPath + ".admin", admin);
        configFile.set(customTeamPath + ".uniqueId", uniqueId);
        configFile.set(customTeamPath + ".teamLevel", teamLevel);

        List<String> viceAdminIds = new ArrayList<>();
        List<String> teamMemberIds = new ArrayList<>();

        for (OfflinePlayer viceadmin : viceAdmins) {
            viceAdminIds.add(viceadmin.getUniqueId().toString());
        }

        for (OfflinePlayer teamMember : teamMembers) {
            teamMemberIds.add(teamMember.getUniqueId().toString());
        }

        configFile.set(customTeamPath + ".viceAdmins", viceAdminIds);
        configFile.set(customTeamPath + ".teamMembers", teamMemberIds);
    }

    public void addMember(Player player) {
        teamMembers.add(player);
    }

    public void addViceAdmin(Player player) {
        teamMembers.remove(player);
        viceAdmins.add(player);
    }

    public void removeViceAdmin(Player player) {
        viceAdmins.remove(player);
        teamMembers.add(player);
    }

    public void changeAdmin(Player player) {
        teamMembers.add(admin);
        admin = player;
    }

    public void upgrade() {
        if (teamLevel != 2) {
            teamLevel++;
        }
    }

    public void upgradeTo(int teamLevel) {
        this.teamLevel = teamLevel;
    }

    public void sendMessage(Player p, String message) {
        String pmessage = "[§a TEAM §f] > [ " + p.getName() + " ] " + message;

        admin.getPlayer().sendMessage(pmessage);

        for (OfflinePlayer va : viceAdmins) {
            if (va.isOnline()) {
                va.getPlayer().sendMessage(pmessage);
            }
        }
        for (OfflinePlayer mb : teamMembers) {
            mb.getPlayer().sendMessage(pmessage);
        }
    }

    public void addInviteCode(String code) {
        inviteCode.add(code);
    }

    public void removeInviteCode(String code) {
        inviteCode.remove(code);
    }

    public boolean removePlayer(Player player) {
        if (viceAdmins.contains(player)) {
            viceAdmins.remove(player);
            return true;
        }

        if (teamMembers.contains(player)) {
            teamMembers.remove(player);
            return true;
        }

        return false;
    }

    public ItemStack getTeamItemStack() {
        ItemStackBuilder itemStack = new ItemStackBuilder(getTeamMaterial());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd hh:mm:ss");
        String timeString = sdf.format(createdTime);

        itemStack.setName(ChatColor.GREEN + "[ TEAM ] > " + ChatColor.RED + teamName);
        itemStack.addLore("")
                 .addLore(ChatColor.GOLD + "< 마스터 > " + ChatColor.WHITE + admin.getName())
                 .addLore(ChatColor.GOLD + "< 설립 날짜 > " + ChatColor.WHITE + timeString)
                 .addLore(ChatColor.GOLD + "< 고유 번호 > " + ChatColor.WHITE + uniqueId)
                 .addLore("")
                 .addLore(ChatColor.YELLOW + "자세한 정보를 보려면 클릭하세요.");

        return itemStack.build();
    }

    public Inventory getTeamInventory() {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, ChatColor.GOLD + "[ 팀 정보 ] > " +
                ChatColor.RED + teamName);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd hh:mm:ss");
        String timeString = sdf.format(createdTime);

        ItemStackBuilder teamAdmin = new ItemStackBuilder(PlayerHead.getPlayerHead(admin.getName()))
                .setName(ChatColor.GOLD + "< 마스터 > " + ChatColor.WHITE + admin.getName());
        ItemStackBuilder teamLogo = new ItemStackBuilder(getTeamMaterial()).setName(ChatColor.GOLD + "팀 정보");

        teamLogo.addLore("")
                .addLore(ChatColor.GOLD + "< 아이콘 > " + ChatColor.WHITE + getTeamMaterial().name())
                .addLore(ChatColor.GOLD + "< 설립 날짜 > " + ChatColor.WHITE + timeString);

        teamAdmin.addLore("")
                .addLore(ChatColor.GOLD + "< 부마스터 > " + ChatColor.WHITE + viceAdmins.size() + " 명")
                .addLore(ChatColor.GOLD + "< 팀원 > " + ChatColor.WHITE + teamMembers.size() + " 명");

        inventory.setItem(12, teamAdmin.build()); inventory.setItem(14, teamLogo.build());
        inventory.setItem(18, new ItemStackBuilder(Material.BARRIER).setName(ChatColor.RED + "돌아가기").build());

        return inventory;
    }

    public Material getTeamMaterial() {
        if (teamLevel == 0) {
            return Material.GRASS;
        } else if (teamLevel == 1) {
            return Material.ENDER_STONE;
        } else if (teamLevel == 2) {
            return Material.DRAGON_EGG;
        } else {
            return Material.AIR;
        }
    }

    public int getTeamPlayersNum() {
        return 1 + viceAdmins.size() + teamMembers.size();
    }

    public boolean isTeamFull() {
        int nowPlayerAmount = getTeamPlayersNum();

        if (teamLevel == 0 && nowPlayerAmount == 15) {
            return true;
        } else if (teamLevel == 1 && nowPlayerAmount == 30) {
            return true;
        } else return teamLevel == 2 && nowPlayerAmount == 30;
    }

    public boolean isTeamUpgradeAble() {
        if (teamLevel != 2) return isTeamFull();

        return false;
    }
}
