package me.dasuonline.vivace;

import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.manager.TeamManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static List<Manager> systemManagers = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

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
