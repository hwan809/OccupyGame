package me.dasuonline.vivace.team;

import jdk.internal.foreign.PlatformLayouts;
import lombok.Getter;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.entity.Player;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MinecraftTeam {
    String name;
    Timestamp createdTime;
    String uniqueId;

    Player admin;
    List<Player> viceAdmin;
    List<Player> teamMember;

    List<String> inviteCode;

    public MinecraftTeam(String name, Timestamp createdTime, Player admin) {
        this.name = name;
        this.createdTime = createdTime;
        this.admin = admin;

        this.viceAdmin = new ArrayList<>();
        this.teamMember = new ArrayList<>();
        this.inviteCode = new ArrayList<>();

        this.uniqueId = TeamUtils.getUniqueId();
    }

    public MinecraftTeam(String name, Timestamp createdTime, Player admin, List<Player> viceAdmin, List<Player> teamMember) {
        this.name = name;
        this.createdTime = createdTime;
        this.admin = admin;
        this.viceAdmin = viceAdmin;
        this.teamMember = teamMember;

        this.inviteCode = new ArrayList<>();
        this.uniqueId = TeamUtils.getUniqueId();
    }

    public boolean addMember(Player player) {
        teamMember.add(player);

        return true;
    }

    public void addInviteCode(String code) {
        inviteCode.add(code);
    }

    public void removeInviteCode(String code) {
        inviteCode.remove(code);
    }

    public boolean removePlayer(Player player) {
        if (viceAdmin.contains(player)) {
            viceAdmin.remove(player);
            return true;
        }

        if (teamMember.contains(player)) {
            teamMember.remove(player);
            return true;
        }

        return false;
    }
}
