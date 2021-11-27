package me.dasuonline.vivace.team;

import lombok.Getter;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MinecraftTeam {
    String name;
    Timestamp createdTime;
    String uniqueId;

    Player admin;
    List<Player> viceAdmins;
    List<Player> teamMembers;

    List<String> inviteCode;

    public MinecraftTeam(String name, Timestamp createdTime, Player admin) {
        this.name = name;
        this.createdTime = createdTime;
        this.admin = admin;

        this.viceAdmins = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
        this.inviteCode = new ArrayList<>();

        this.uniqueId = TeamUtils.getUniqueId();
    }

    public MinecraftTeam(String name, Timestamp createdTime, Player admin, List<Player> viceAdmin, List<Player> teamMember) {
        this.name = name;
        this.createdTime = createdTime;
        this.admin = admin;
        this.viceAdmins = viceAdmin;
        this.teamMembers = teamMember;

        this.inviteCode = new ArrayList<>();
        this.uniqueId = TeamUtils.getUniqueId();
    }

    public boolean addMember(Player player) {
        teamMembers.add(player);

        return true;
    }

    public boolean addViceAdmin(Player player) {
        teamMembers.remove(player);
        viceAdmins.add(player);

        return true;
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
}
