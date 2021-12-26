package me.dasuonline.vivace.team.manager;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.commands.args.*;
import me.dasuonline.vivace.team.commands.TeamCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class TeamManager implements Manager {
    public static Map<String, CustomExecutor> argsMap = new HashMap<>();

    public static List<MinecraftTeam> teamList = new ArrayList<>();

    public static ItemStack teamCreateItem;
    public static ItemStack teamUpgradeItem;

    private File teamConfigFile;
    private FileConfiguration teamConfig;

    private void setArgsMap() {
        argsMap.put("도움말", new Help());
        argsMap.put("생성", new Create());
        argsMap.put("업그레이드", new TeamUpgrade());
        argsMap.put("해체", new Dismantle());
        argsMap.put("탈퇴", new Leave());
        argsMap.put("초대", new Invite());
        argsMap.put("수락", new Accept());
        argsMap.put("거절", new Reject());
        argsMap.put("추방", new Kick());
        argsMap.put("관리자", new Administrator());
        argsMap.put("채팅", new Message());
        argsMap.put("목록", new TeamList());

        argsMap.put("설정", new TeamSettings());
        argsMap.put("관리", new TeamSettings());
    }

    private void setExecutor() {

        for (CustomExecutor ce : argsMap.values()) {
            ce.init();
        }
    }

    private void createConfig() {
        teamConfigFile = new File(Main.getInstance().getDataFolder(), "team.yml");

        if (!teamConfigFile.exists()) {
            try {
                teamConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        teamConfig = new YamlConfiguration();

        try {
            teamConfig.load(teamConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    private void getConfigData() {
        ConfigurationSection configureList = teamConfig.getConfigurationSection("teams");

        if (configureList != null) {
            for (String teamName : configureList.getValues(false).keySet()) {
                Main.getInstance().getLogger().info(ChatColor.RED + "[ LOADING TEAM ] > " + ChatColor.GOLD + teamName);
                teamList.add(loadAsConfig(teamConfig, "teams", teamName));
            }
        }

        teamCreateItem = teamConfig.getItemStack("team_create_item", new ItemStack(Material.GRASS));
        teamUpgradeItem = teamConfig.getItemStack("team_upgrade_item", new ItemStack(Material.GRASS));
    }

    private void saveConfigData() {
        teamConfig.set("teams", null);

        for (MinecraftTeam minecraftTeam : teamList) {
            minecraftTeam.saveAsConfig(teamConfig, "teams");
        }

        teamConfig.set("team_create_item", teamCreateItem);
        teamConfig.set("team_upgrade_item", teamUpgradeItem);

        try {
            teamConfig.save(teamConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MinecraftTeam loadAsConfig(FileConfiguration configFile, String path, String name) {
        String customTeamPath = path + "." + name;

        String teamName = configFile.getString(customTeamPath + ".teamName");
        Timestamp createdTime = Timestamp.valueOf(configFile.getString(customTeamPath + ".createdTime"));
        OfflinePlayer admin = configFile.getOfflinePlayer(customTeamPath + ".admin");
        String uniqueId = configFile.getString(customTeamPath + ".uniqueId");
        int teamLevel = configFile.getInt(customTeamPath + ".teamLevel");

        List<String> viceAdminIds = configFile.getStringList(customTeamPath + ".viceAdmins");
        List<String> teamMemberIds = configFile.getStringList(customTeamPath + ".teamMembers");

        List<OfflinePlayer> viceAdmins = new ArrayList<>();
        List<OfflinePlayer> teamMembers = new ArrayList<>();

        for (String uuid : viceAdminIds) {
            viceAdmins.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
        }

        for (String uuid : teamMemberIds) {
            teamMembers.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
        }

        return new MinecraftTeam(teamName, createdTime, admin, viceAdmins, teamMembers, uniqueId, teamLevel);
    }

    @Override
    public void init() {
        setArgsMap();
        setExecutor();
        createConfig();
        getConfigData();

        Main.getInstance().getCommand(TeamCommands.cmd1).setExecutor(new TeamCommands());
    }

    @Override
    public void save() {
        saveConfigData();
    }

    @Override
    public void logMessage(Player p, String s) {
        p.sendMessage("[§a TEAM §f] > " + s);
    }

    @Override
    public String getName() {
        return "Team";
    }
}





