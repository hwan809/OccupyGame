package me.dasuonline.vivace.occupy.listener;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.occupy.manager.OccupyManager;
import me.dasuonline.vivace.occupy.system.OccupyLand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;

public class GameStarter implements CommandExecutor {

    private final Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("점령전")) return false;
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (!player.isOp()) return true;

        if (OccupyManager.landList.isEmpty()) {
            player.sendMessage(ChatColor.RED + "[ 점령전 ] 아직 설정된 점령전 구역이 없습니다.");
            return true;
        }

        OccupyLand gameLand = OccupyManager.landList.get(random.nextInt(OccupyManager.landList.size()));

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.GOLD + "[ 점령전 ] " + ChatColor.GREEN + "이 시작되었습니다! \n"
                    + ChatColor.GOLD + "[ 팀 ] " + ChatColor.WHITE + "을 조직해 점령전에 참여하세요.");
        }

        OccupyManager.occupyGame.startGame(gameLand);

        return true;
    }
}
