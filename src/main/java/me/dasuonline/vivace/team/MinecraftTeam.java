package me.dasuonline.vivace.team;

import lombok.Getter;
import me.dasuonline.vivace.team.manager.TeamUtils;
import me.dasuonline.vivace.util.ItemStackBuilder;
import me.dasuonline.vivace.util.PlayerHead;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

    Player admin;
    List<Player> viceAdmins;
    List<Player> teamMembers;

    List<String> inviteCode;

    int teamLevel = 0;

    public MinecraftTeam(String name, Timestamp createdTime, Player admin) {
        this.teamName = name;
        this.createdTime = createdTime;
        this.admin = admin;

        this.viceAdmins = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
        this.inviteCode = new ArrayList<>();

        this.uniqueId = TeamUtils.getUniqueId();
    }

    public MinecraftTeam(String name, Timestamp createdTime, Player admin, List<Player> viceAdmin, List<Player> teamMember, String uniqueId, int teamLevel) {
        this.teamName = name;
        this.createdTime = createdTime;
        this.admin = admin;
        this.viceAdmins = viceAdmin;
        this.teamMembers = teamMember;
        this.uniqueId = uniqueId;
        this.teamLevel = teamLevel;

        this.inviteCode = new ArrayList<>();
    }

    public boolean addMember(Player player) {
        teamMembers.add(player);

        return true;
    }

    public void addViceAdmin(Player player) {
        teamMembers.remove(player);
        viceAdmins.add(player);
    }

    public void removeViceAdmin(Player player) {
        viceAdmins.remove(player);
        teamMembers.add(player);
    }

    public void sendMessage(Player p, String message) {
        String pmessage = "[§a TEAM §f] > [ " + p.getName() + " ] " + message;

        admin.sendMessage(pmessage);

        for (Player va : viceAdmins) va.sendMessage(pmessage);
        for (Player mb : teamMembers) mb.sendMessage(pmessage);
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
}
