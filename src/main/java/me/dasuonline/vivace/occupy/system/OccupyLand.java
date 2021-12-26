package me.dasuonline.vivace.occupy.system;

import me.dasuonline.vivace.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static me.dasuonline.vivace.occupy.manager.OccupyManager.landList;

public class OccupyLand {

    public int landCode;

    private String landName;
    private final World world;

    private int minX;
    private int maxX;
    private int minZ;
    private int maxZ;
    private int Y;

    public List<ItemStack> occupyRewards = new ArrayList<>();

    public OccupyLand(Location loc1, Location loc2, int landCode) {
        this(loc1.getWorld(), loc1.getBlockX(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockZ(),
                loc1.getBlockY(), loc2.getBlockY(), landCode);
    }

    public OccupyLand(World world, int x1, int z1, int x2, int z2, int y1, int y2, int landCode) {
        this.world = world;

        minX = Math.min(x1, x2);
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxZ = Math.max(z1, z2);
        Y = Math.max(y1, y2);

        this.landCode = landCode;
    }

    public World getWorld() {
        return world;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public boolean contains(OccupyLand region) {
        return region.getWorld().equals(world) &&
                region.getMinX() >= minX && region.getMaxX() <= maxX &&
                region.getMinZ() >= minZ && region.getMaxZ() <= maxZ;
    }

    public boolean contains(Location location) {
        return contains(location.getBlockX(), location.getBlockZ());
    }

    public boolean contains(int x, int z) {
        return x >= minX && x <= maxX &&
                z >= minZ && z <= maxZ;
    }

    public boolean isPlayerInside(Player player) {
        Location playerLoc = player.getLocation();

        double x = playerLoc.getX();
        double y = playerLoc.getY();
        double z = playerLoc.getZ();

        boolean isInside = x >= minX && x <= maxX && z >= minZ && z <= maxZ;
        boolean isAbove = y >= Y;

        return isAbove && isInside;
    }

    public boolean overlaps(OccupyLand region) {
        return region.getWorld().equals(world) &&
                !(region.getMinX() > maxX || region.getMinZ() > maxZ ||
                        minZ > region.getMaxX() || minZ > region.getMaxZ());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof OccupyLand)) {
            return false;
        }
        final OccupyLand other = (OccupyLand) obj;
        return world.equals(other.world)
                && minX == other.minX
                && minZ == other.minZ
                && maxX == other.maxX
                && maxZ == other.maxZ;
    }

    @Override
    public String toString() {
        return "Region[world:" + world.getName() +
                ", minX:" + minX +
                ", minZ:" + minZ +
                ", maxX:" + maxX +
                ", maxZ:" + maxZ + "]";
    }

    public void addReward(ItemStack itemStack) {
        this.occupyRewards.add(itemStack);
    }

    public void removeReward(ItemStack itemStack) {
        this.occupyRewards.remove(itemStack);
    }

    public Inventory getRewardsGUI() {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "점령지 [ " + this.landCode + " ] 의 보상");
        for (ItemStack item : this.occupyRewards) {
            inventory.addItem(item);
        }

        return inventory;
    }
}
