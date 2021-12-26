package me.dasuonline.vivace.occupy.listener;

import me.dasuonline.vivace.occupy.manager.OccupyManager;
import me.dasuonline.vivace.occupy.system.OccupyLand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public class LandManager implements Listener, CommandExecutor {
    public static Map<String, Block> leftPoints = new HashMap<>();
    public static Map<String, Block> rightPoints = new HashMap<>();

    @EventHandler
    public void setMineCuboid(PlayerInteractEvent event) {
        if (!event.getPlayer().isOp()) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.BLAZE_ROD) return;

        Action setType = event.getAction();

        if (setType != Action.RIGHT_CLICK_BLOCK && setType != Action.LEFT_CLICK_BLOCK) return;

        Player op = event.getPlayer();

        if (setType == Action.RIGHT_CLICK_BLOCK) {
            leftPoints.put(op.getName(), event.getClickedBlock());
            op.sendMessage(ChatColor.RED + "점령전 POS2 설정");
        } else {
            rightPoints.put(op.getName(), event.getClickedBlock());
            op.sendMessage(ChatColor.RED + "점령전 POS1 설정");
        }

        event.setCancelled(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("구역")) return false;
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (!player.isOp()) return true;

        if (!leftPoints.containsKey(player.getName()) ||
                !rightPoints.containsKey(player.getName())) {
            player.sendMessage(ChatColor.RED + "구역 설정이 완료되지 않았습니다.");
            System.out.println(leftPoints);
            System.out.println(rightPoints);
            return true;
        }

        Block b1 = leftPoints.get(player.getName());
        Block b2 = rightPoints.get(player.getName());

        OccupyLand newLand = new OccupyLand(b1.getLocation(), b2.getLocation(), OccupyManager.landList.size());

        OccupyManager.landList.add(newLand);

        player.sendMessage(ChatColor.GOLD + "점령전 구역 설정을 완료했습니다 ! \n" +
                ChatColor.WHITE + newLand);

        return true;
    }
}
