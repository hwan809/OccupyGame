package me.dasuonline.vivace;

import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.TeamCommands;
import me.dasuonline.vivace.team.TeamManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.management.ManagementPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin {

    public static List<Manager> systemManagers = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        systemManagers.add(new TeamManager());

        for (Manager classes : systemManagers) {
            classes.init();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Manager getManager(String s) {
        for (Manager sm : systemManagers) {
            if (s.equals(sm.getName())) {
                return sm;
            }
        }

        return null;
    }
}
