package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Invite extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    private Map<String, Long> inviteCooldowns = new HashMap<>();

    @Override
    public boolean execute(Player player, String[] args) {
        if (!playerHasTeam(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀에 소속되어 있지 않습니다.");
            return true;
        }

        MinecraftTeam inviteTeam = getPlayerTeam(player);

        if (!inviteTeam.getAdmin().equals(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀의 대표만 팀원을 초대할 수 있습니다.");
            return true;
        }

        if (args.length < 2) {
            teamManager.logMessage(player, ChatColor.RED + "초대할 [플레이어] 닉네임을 입력해주세요.");
            return true;
        }

        Player invitedPlayer = Bukkit.getPlayer(args[1]);

        if (invitedPlayer == null) {
            teamManager.logMessage(player, ChatColor.RED + "그런 이름의 [플레이어] 가 없습니다.");
            return true;
        }

        String playersKey = player.getUniqueId().toString() + invitedPlayer.getUniqueId().toString();

        if (inviteCooldowns.containsKey(playersKey)) {
            long timeStamp = inviteCooldowns.get(playersKey);

            if (timeStamp > System.currentTimeMillis()) {
                long timeLeft = (timeStamp - System.currentTimeMillis()) / 1000;

                teamManager.logMessage(player, ChatColor.RED + invitedPlayer.getName() + "님에게, "
                        + timeLeft + "초 이후 초대를 보낼 수 있습니다.");
                return true;
            }
        }

        if (isPlayerTeamMember(invitedPlayer, inviteTeam)) {
            teamManager.logMessage(player, ChatColor.RED + "이미 이 팀에 소속되어 있는 [플레이어] 입니다.");
            return true;
        }

        if (playerHasTeam(invitedPlayer)) {
            teamManager.logMessage(player, ChatColor.RED + "이미 다른 팀에 소속되어 있는 [플레이어] 입니다.");
            return true;
        }

        TextComponent tempMessage = new TextComponent("[§a TEAM §f] > " + "[ " + inviteTeam.getTeamName() + " ]" +
                ChatColor.GREEN + " 팀의 마스터 " + ChatColor.WHITE + "[ " + player.getName() + " ] " +
                ChatColor.GREEN + "의 초대가 왔습니다! " + ChatColor.WHITE + "<");

        TextComponent acceptMessage, rejectMessage;
        String inviteCode = RandomStringUtils.randomAlphanumeric(6);

        acceptMessage = new TextComponent(ChatColor.GREEN + "수락" + ChatColor.WHITE);
        acceptMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/팀 수락 " + inviteTeam.getUniqueId() + ":" + inviteCode));
        acceptMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.YELLOW + "클릭하면 수락합니다.").create()));


        rejectMessage = new TextComponent(ChatColor.RED + "거절" + ChatColor.WHITE);
        rejectMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/팀 거절 " + inviteTeam.getUniqueId() + ":" + inviteCode));
        rejectMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.YELLOW + "클릭하면 거절합니다.").create()));

        teamManager.logMessage(player, "[ " + invitedPlayer.getName() + " ] " + ChatColor.GREEN + "님께 초대를 보냈습니다!");
        invitedPlayer.spigot().sendMessage(tempMessage, acceptMessage, new TextComponent(" | "), rejectMessage, new TextComponent(">"));

        inviteTeam.addInviteCode(inviteCode);
        inviteCooldowns.put(playersKey, System.currentTimeMillis() + (1000 * 1800));

        return true;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
