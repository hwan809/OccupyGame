package me.dasuonline.vivace.team;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.args.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamManager implements Manager {
    public static Map<String, CustomExecutor> argsMap = new HashMap<>();
    public static List<CustomExecutor> argsExecutor = new ArrayList<>();

    private void setArgsMap() {
        argsMap.put("도움말", new Help());
        argsMap.put("생성", new Create());
        argsMap.put("해체", new Dismantle());
        argsMap.put("가입", new Join());
        argsMap.put("탈퇴", new Leave());
        argsMap.put("초대", new Invite());
        argsMap.put("수락", new Accept());
        argsMap.put("거절", new Reject());
        argsMap.put("추방", new Kick());
        argsMap.put("관리자", new Administrator());
        argsMap.put("양도", new Transfer());
        argsMap.put("메세지", new Message());
        argsMap.put("목록", new TeamList());
    }

    private void setExecutor() {
        argsExecutor.addAll(argsMap.values());

        for (CustomExecutor ce : argsExecutor) {
            ce.init();
        }
    }

    @Override
    public void init() {
        setArgsMap();
        setExecutor();

        Main.getPlugin(Main.class).getCommand(TeamCommands.cmd1).setExecutor(new TeamCommands());
    }

    @Override
    public void logMessage(Player p, String s) {
        p.sendMessage("[§a TEAM §f] > " + s);
    }

    @Override
    public String getName() {
        return "Team";
    }
}





