package me.dasuonline.vivace.occupy.listener;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.occupy.manager.OccupyManager;
import me.dasuonline.vivace.occupy.system.OccupyLand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;

public class GameCommandExecutor implements CommandExecutor {

    private final Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("점령전")) return false;
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        if (!player.isOp()) return true;
        if (args.length == 0) return true;

        String subCommand = args[0];

        if (subCommand.equals("시작")) {
            if (OccupyManager.landList.isEmpty()) {
                player.sendMessage(ChatColor.RED + "[ 점령전 ] 아직 설정된 점령전 구역이 없습니다.");
                return true;
            }

            if (OccupyManager.occupyGame.isStarted()) return true;

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(ChatColor.GOLD + "[ 점령전 ] " + ChatColor.GREEN + "이 시작되었습니다! \n"
                        + ChatColor.GOLD + "[ 팀 ] " + ChatColor.WHITE + "을 조직해 점령전에 참여하세요.");
            }

            OccupyManager.occupyGame.startGame();
        } else if (subCommand.equals("보상설정")) {
            if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "[ 점령전 ] 구역의 숫자를 입력해주세요.");
                return true;
            }

            int code = -1;

            try {
                code = Integer.parseInt(args[1]);
            } catch (Exception ignored) {}

            ItemStack newItem = player.getInventory().getItemInMainHand();

            if (newItem == null) return true;
            if (newItem.getType() == Material.AIR) return true;

            for (OccupyLand land : OccupyManager.landList) {
                if (land.landCode == code) {
                    land.addReward(newItem);
                    player.sendMessage(ChatColor.RED + "[ 점령전 ] 보상이 추가되었습니다!");
                    return true;
                }
            }

            player.sendMessage("[ 점령전 ] 그런 이름의 구역이 없습니다.");
            return true;
        } else if (subCommand.equals("보상목록")) {
            if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "[ 점령전 ] 구역의 숫자를 입력해주세요.");
                return true;
            }

            int code = -1;

            try {
                code = Integer.parseInt(args[1]);
            } catch (Exception ignored) {}

            for (OccupyLand land : OccupyManager.landList) {
                if (land.landCode == code) {
                    player.openInventory(land.getRewardsGUI());
                    player.sendMessage(ChatColor.RED + "[ 점령전 ] 보상이 추가되었습니다!");
                    return true;
                }
            }

            player.sendMessage("[ 점령전 ] 그런 이름의 구역이 없습니다.");
        } else if (subCommand.equals("보상제거")) {
            if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "[ 점령전 ] 구역의 숫자를 입력해주세요.");
                return true;
            }

            int code = -1;

            try {
                code = Integer.parseInt(args[1]);
            } catch (Exception ignored) {}

            ItemStack newItem = player.getInventory().getItemInMainHand();

            if (newItem == null) return true;
            if (newItem.getType() == Material.AIR) return true;

            for (OccupyLand land : OccupyManager.landList) {
                if (land.landCode == code) {
                    for (ItemStack identicalItem : land.occupyRewards) {
                        if (identicalItem.equals(newItem)) {
                            land.removeReward(identicalItem);
                            player.sendMessage(ChatColor.RED + "[ 점령전 ] 보상이 삭제되었습니다!");
                            return true;
                        }
                    }

                    player.sendMessage(ChatColor.RED + "[ 점령전 ] 그런 보상이 없습니다.");
                    return true;
                }
            }

            player.sendMessage("[ 점령전 ] 그런 이름의 구역이 없습니다.");
            return true;
        }

        return false;
    }
}
