package me.dasuonline.vivace.occupy.manager;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.occupy.listener.GameStarter;
import me.dasuonline.vivace.occupy.listener.LandManager;
import me.dasuonline.vivace.occupy.system.OccupyGame;
import me.dasuonline.vivace.occupy.system.OccupyLand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OccupyManager implements Manager {

    public static List<OccupyLand> landList = new ArrayList<>();
    public static OccupyGame occupyGame = new OccupyGame();

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(new LandManager(), Main.getInstance());

        Main.getInstance().getCommand("구역").setExecutor(new LandManager());
        Main.getInstance().getCommand("점령전").setExecutor(new GameStarter());
    }

    @Override
    public void save() {

    }

    @Override
    public void logMessage(Player p, String s) {
        p.sendMessage("[§a 점령전 §f] > " + s);
    }

    @Override
    public String getName() {
        return "점령전";
    }
}
