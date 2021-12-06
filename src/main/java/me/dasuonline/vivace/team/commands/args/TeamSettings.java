package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamManager;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeamSettings extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.isOp()) return false;

        if (args.length < 2) {
            teamManager.logMessage(player, ChatColor.RED + "/팀 설정 " +
                    "[생성권|업그레이드권|강제해체|팀장변경|강제업글|업글초기화|생성권지급|업글권지급]");
            return true;
        }

        String command = args[1];

        if (command.equals("생성권")) {
            ItemStack createItem = player.getInventory().getItemInMainHand();

            if (createItem == null || createItem.getType().equals(Material.AIR)) {
                teamManager.logMessage(player, ChatColor.RED + "손에 설정할 팀 생성권 아이템을 들고 명령어를 입력해주세요.");
                return true;
            }

            TeamManager.teamCreateItem = createItem;

            teamManager.logMessage(player, ChatColor.RED + "팀 생성권을 설정했습니다.");
            return true;
        } else if (command.equals("업그레이드권")) {
            ItemStack upgradeItem = player.getInventory().getItemInMainHand();

            if (upgradeItem == null || upgradeItem.getType().equals(Material.AIR)) {
                teamManager.logMessage(player, ChatColor.RED + "손에 설정할 팀 업그레이드권 아이템을 들고 명령어를 입력해주세요.");
                return true;
            }

            TeamManager.teamUpgradeItem = upgradeItem;

            teamManager.logMessage(player, ChatColor.RED + "팀 업그레이드권을 설정했습니다.");
            return true;
        } else if (command.equals("강제해체")) {
            if (args.length < 3) {
                teamManager.logMessage(player, ChatColor.RED + "해체할 팀 이름을 입력하세요.");
                return true;
            }

            String teamName = args[2];

            if (!containsTeamName(teamName)) {
                teamManager.logMessage(player, ChatColor.RED + "그런 팀명이 없습니다.");
                return true;
            }

            MinecraftTeam deletedTeam = getTeamByName(teamName);
            TeamManager.teamList.remove(deletedTeam);

            teamManager.logMessage(player, ChatColor.RED + "팀 [ " + teamName + " ] 을 삭제했습니다.");
            return true;
        } else if (command.equals("팀장변경")) {

            if (args.length < 4) {
                teamManager.logMessage(player, ChatColor.RED + "/팀 설정 팀장변경 [팀명] [플레이어]");
                return true;
            }

            String teamName = args[2];

            if (!containsTeamName(teamName)) {
                teamManager.logMessage(player, ChatColor.RED + "그런 팀명이 없습니다.");
                return true;
            }

            MinecraftTeam nowTeam = getTeamByName(teamName);
            Player newAdmin = Bukkit.getPlayer(args[3]);

            if (newAdmin == null) {
                teamManager.logMessage(player, ChatColor.RED + "그런 이름의 [플레이어] 가 없습니다.");
                return true;
            }

            if (isPlayerMember(newAdmin)) {
                if (!getPlayerTeam(newAdmin).equals(nowTeam)) {
                    teamManager.logMessage(player, ChatColor.RED + "다른 팀에 소속되어 있는 [플레이어] 입니다.");
                    return true;
                }
            } else {
                teamManager.logMessage(player, ChatColor.RED + "팀에 소속되어 있지 않은 [플레이어] 입니다.");
                return true;
            }

            nowTeam.changeAdmin(newAdmin);
            teamManager.logMessage(player, ChatColor.RED + "[ " + nowTeam.getTeamName() + " ] 팀의 팀장을 [ " +
                    newAdmin.getName() + " ] 으로 바꾸었습니다.");
            return true;
        } else if (command.equals("강제업글")) {
            if (args.length < 3) { // /팀 설정 강제업글 팀이름
                teamManager.logMessage(player, ChatColor.RED + "업그레이드할 [팀 이름] 을 입력하세요.");
                return true;
            }

            String teamName = args[2];

            if (!containsTeamName(teamName)) {
                teamManager.logMessage(player, ChatColor.RED + "그런 팀이 없습니다.");
                return true;
            }

            MinecraftTeam nowTeam = getTeamByName(teamName);
            nowTeam.upgrade();
            teamManager.logMessage(player, ChatColor.RED + "[ " + nowTeam.getTeamName() + " ] 팀의 등급을 업그레이드 했습니다.");
            return true;
        } else if (command.equals("업글초기화")) {
            if (args.length < 3) { // /팀 설정 업글초기화 팀이름
                teamManager.logMessage(player, ChatColor.RED + "업그레이드를 초기화할 [팀 이름] 을 입력하세요.");
                return true;
            }

            String teamName = args[2];

            if (!containsTeamName(teamName)) {
                teamManager.logMessage(player, ChatColor.RED + "그런 팀이 없습니다.");
                return true;
            }

            MinecraftTeam nowTeam = getTeamByName(teamName);
            nowTeam.upgradeTo(0);
            teamManager.logMessage(player, ChatColor.RED + "[ " + nowTeam.getTeamName() + " ] 팀의 등급을 초기화 했습니다.");
            return true;
        } else if (command.equals("생성권지급")) {
            player.getInventory().addItem(TeamManager.teamCreateItem);
        } else if (command.equals("업글권지급")) {
            player.getInventory().addItem(TeamManager.teamUpgradeItem);
        }
        return false;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
