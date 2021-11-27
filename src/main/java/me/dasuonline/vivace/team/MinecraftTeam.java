package me.dasuonline.vivace.team;

import jdk.internal.foreign.PlatformLayouts;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Getter
public class MinecraftTeam {
    String name;
    Timestamp createdTime;

    Player admin;
    List<Player> viceAdmin;
    List<Player> teamMember;

    public MinecraftTeam(String name, Timestamp createdTime, Player admin) {
        this.name = name;
        this.createdTime = createdTime;
        this.admin = admin;
    }

    public MinecraftTeam(String name, Timestamp createdTime, Player admin, List<Player> viceAdmin, List<Player> teamMember) {
        this.name = name;
        this.createdTime = createdTime;
        this.admin = admin;
        this.viceAdmin = viceAdmin;
        this.teamMember = teamMember;
    }
}
