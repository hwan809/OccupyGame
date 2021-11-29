package me.dasuonline.vivace.team.provider;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.commands.args.TeamList;
import me.dasuonline.vivace.team.manager.TeamManager;
import me.dasuonline.vivace.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.BinaryOperator;

public class ListInvProvider implements InventoryProvider {

    private Manager teamManager;
    private final ItemStack borderItem = new ItemStackBuilder(Material.STAINED_GLASS_PANE).setName(ChatColor.GREEN + " ").build();

    @Override
    public void init(Player player, InventoryContents contents) {
        teamManager = Main.getManager("Team");

        ClickableItem[] items = new ClickableItem[TeamManager.teamList.size()];

        int i = 0;

        for (MinecraftTeam team : TeamManager.teamList) {
            items[i] = ClickableItem.empty(team.getTeamItemStack());
            i++;
        }

        Pagination pagination = contents.pagination();
        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        ItemStack rarrow = new ItemStackBuilder(Material.ARROW).setName(ChatColor.GOLD + "다음 페이지").build();
        ItemStack larrow = new ItemStackBuilder(Material.ARROW).setName(ChatColor.GOLD + "이전 페이지").build();
        ItemStack quit = new ItemStackBuilder(Material.BARRIER).setName(ChatColor.RED + "나가기").build();

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
        contents.set(2, 3, ClickableItem.of(rarrow, e ->
                TeamList.teamListInventory.open(player, pagination.previous().getPage())));

        contents.set(2, 5, ClickableItem.of(larrow, e ->
                TeamList.teamListInventory.open(player, pagination.next().getPage())));

        contents.set(2, 0, ClickableItem.of(quit, e ->
                player.closeInventory()));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
