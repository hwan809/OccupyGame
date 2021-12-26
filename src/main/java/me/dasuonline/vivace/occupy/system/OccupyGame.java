package me.dasuonline.vivace.occupy.system;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.occupy.manager.OccupyManager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamManager;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum GameState {
    NO_TEAM_OCCUPYING(0), TEAM_OCCUPYING(1),
    OCCUPYING_TEAM_LEAVE(2), AFTER_TEAM_LEAVE(3);

    private int stateCode;

    private GameState(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getStateCode() {
        return this.stateCode;
    }
}

public class OccupyGame extends TeamUtils {

    private static final double OCCUPY_TIME = 20 * 60 * 0.2;
    private static final double LEAVE_TIME = 20 * 5;
    private static final double GAME_INTERVAL = 20 * 60 * 0.1;
    private static final double TICKS_IN_A_SECOND = 20;

    private static final Random random = new Random();

    private BukkitScheduler scheduler = Main.getInstance().getServer().getScheduler();

    private OccupyLand nowLand;
    private boolean flag = false;

    private GameState nowState = GameState.NO_TEAM_OCCUPYING;
    private MinecraftTeam nowOccupyingTeam;
    private double occupyingLeftTick;

    private double leaveLeftTick;

    private BossBar bossBar;

    public OccupyGame() {
        this.bossBar = Bukkit.createBossBar(".", BarColor.WHITE, BarStyle.SOLID);
    }

    private void noTeamOccupying() {
        for (MinecraftTeam team : TeamManager.teamList) {
            for (OfflinePlayer offlinePlayer :team.getAllTeamPlayers()) {
                if (!offlinePlayer.isOnline()) return;

                Player onlinePlayer = offlinePlayer.getPlayer();

                if (this.nowLand.isPlayerInside(onlinePlayer)) {
                    this.nowOccupyingTeam = team;
                    this.occupyingLeftTick = OCCUPY_TIME;

                    changeStateTo(GameState.TEAM_OCCUPYING);
                }
            }
        }
    }

    private void teamOccupying() {
        boolean isStillInside = false;

        for (OfflinePlayer offlinePlayer : this.nowOccupyingTeam.getAllTeamPlayers()) {
            if (!offlinePlayer.isOnline()) continue;

            Player occupyTeamPlayer = offlinePlayer.getPlayer();

            if (this.nowLand.isPlayerInside(occupyTeamPlayer)) {
                isStillInside = true;
                break;
            }
        }

        if (!isStillInside) {
            changeStateTo(GameState.OCCUPYING_TEAM_LEAVE);
            return;
        }

        if (occupyingLeftTick == 0) {
            this.flag = false;
            return;
        }

        this.occupyingLeftTick--;
        this.bossBar.setProgress(getProgress());
    }

    private void occupyingTeamLeave() {
        boolean isStillOutside = true;

        for (OfflinePlayer offlinePlayer : this.nowOccupyingTeam.getAllTeamPlayers()) {
            if (!offlinePlayer.isOnline()) continue;

            Player occupyTeamPlayer = offlinePlayer.getPlayer();

            if (this.nowLand.isPlayerInside(occupyTeamPlayer)) {
                isStillOutside = false;
                break;
            }
        }

        if (!isStillOutside) {
            changeStateTo(GameState.TEAM_OCCUPYING);
            return;
        }

        if (this.leaveLeftTick % TICKS_IN_A_SECOND == 0) {
            int leftSecond = (int) (this.leaveLeftTick / TICKS_IN_A_SECOND);

            if (leftSecond == 0) {
                progressAfterTeamLeave();
            }

            updateBossBar();
        }

        this.leaveLeftTick--;
    }

    private void progressAfterTeamLeave() {
        List<MinecraftTeam> landInsideTeams = getTeamsInside();
        int insideTeamAmount = landInsideTeams.size();

        if (insideTeamAmount == 0) {
            changeStateTo(GameState.NO_TEAM_OCCUPYING);
        } else if (insideTeamAmount == 1) {
            this.nowOccupyingTeam = landInsideTeams.get(0);
            changeStateTo(GameState.TEAM_OCCUPYING);
        } else {
            changeStateTo(GameState.AFTER_TEAM_LEAVE);
        }
    }

    private void teamFightAfterLeave() {
        List<MinecraftTeam> landInsideTeams = getTeamsInside();
        int insideTeamAmount = landInsideTeams.size();

        if (insideTeamAmount == 0) {
            changeStateTo(GameState.NO_TEAM_OCCUPYING);
        } else if (insideTeamAmount == 1) {
            this.nowOccupyingTeam = landInsideTeams.get(0);
            changeStateTo(GameState.TEAM_OCCUPYING);
        }

        updateBossBar();
    }

    private void changeStateTo(GameState state) {
        this.nowState = state;

        if (state == GameState.NO_TEAM_OCCUPYING) {
            this.nowOccupyingTeam = null;
            this.occupyingLeftTick = 0;
        } else if (state == GameState.TEAM_OCCUPYING) {
            //pass
        } else if (state == GameState.OCCUPYING_TEAM_LEAVE) {
            this.leaveLeftTick = LEAVE_TIME;
        } else if (state == GameState.AFTER_TEAM_LEAVE) {
            this.nowOccupyingTeam = null;
        }

        updateBossBar();
    }

    private void updateBossBar() {
        if (this.nowState == GameState.NO_TEAM_OCCUPYING) {

            this.bossBar.setProgress(1);
            this.bossBar.setTitle(ChatColor.GOLD + "[ 점령전 ]");
            this.bossBar.setColor(BarColor.BLUE);
            this.bossBar.setStyle(BarStyle.SOLID);

        } else if (this.nowState == GameState.TEAM_OCCUPYING) {

            this.bossBar.setProgress(getProgress());
            this.bossBar.setTitle(ChatColor.GOLD + "[ 점령전 ] " + ChatColor.WHITE + nowOccupyingTeam.getTeamName() + " "
                    + nowOccupyingTeam.getTeamLevelTitle() + ChatColor.RED + ", 점령 중...");
            this.bossBar.setColor(BarColor.WHITE);
            this.bossBar.setStyle(BarStyle.SEGMENTED_20);

        } else if (this.nowState == GameState.OCCUPYING_TEAM_LEAVE) {

            int leftSecond = (int) (this.leaveLeftTick / TICKS_IN_A_SECOND);

            this.bossBar.setProgress(0);
            this.bossBar.setTitle(ChatColor.GOLD + "[ 점령전 ] " + ChatColor.WHITE + nowOccupyingTeam.getTeamName() + " "
                    + nowOccupyingTeam.getTeamLevelTitle() + ChatColor.RED + ", 점령지에서 이탈했습니다. 카운트 : " + leftSecond);
            this.bossBar.setColor(BarColor.BLUE);
            this.bossBar.setStyle(BarStyle.SOLID);

        } else if (this.nowState == GameState.AFTER_TEAM_LEAVE) {
            List<MinecraftTeam> landInsideTeams = getTeamsInside();
            int insideTeamAmount = landInsideTeams.size();

            StringBuilder teamNames = new StringBuilder();

            if (insideTeamAmount > 3) {
                teamNames.append("여러 팀");
            } else {
                for (int teamCount = 0; teamCount < insideTeamAmount; teamCount++) {
                    MinecraftTeam insideTeam = landInsideTeams.get(teamCount);

                    teamNames.append(ChatColor.WHITE).append(insideTeam.getTeamName())
                            .append(" ").append(insideTeam.getTeamLevelTitle());

                    if (teamCount != insideTeamAmount - 1) {
                        teamNames.append(ChatColor.WHITE).append(", ");
                    }
                }
            }

            this.bossBar.setProgress(0);
            this.bossBar.setTitle(ChatColor.GOLD + "[ 점령전 ] " + teamNames + "" +
                    ChatColor.RED + "가 구역의 점령을 두고 전투 중...");
            this.bossBar.setColor(BarColor.BLUE);
            this.bossBar.setStyle(BarStyle.SOLID);
        }
    }

    public void startGame() {
        this.nowState = GameState.NO_TEAM_OCCUPYING;
        this.nowOccupyingTeam = null;
        this.occupyingLeftTick = 0;
        this.leaveLeftTick = 0;
        this.nowLand = OccupyManager.landList.get(random.nextInt(OccupyManager.landList.size()));
        this.flag = true;
        this.bossBar.setVisible(true);

        updateBossBar();

        for (Player bPlayer : Bukkit.getOnlinePlayers()) {
            this.bossBar.addPlayer(bPlayer);
        }

        OccupyGame thisGame = this;
        List<MinecraftTeam> mTeams = TeamManager.teamList;

        scheduler = Main.getInstance().getServer().getScheduler();
        scheduler.runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (!thisGame.flag) {
                    scheduler.cancelAllTasks();
                    stopGame();
                    Bukkit.broadcastMessage("ENDGAME");
                    return;
                }
                gameTick();
            }
        }, 0L, 1L);
    }

    private void stopGame() {
        for (OfflinePlayer winPlayer : this.nowOccupyingTeam.getAllTeamPlayers()) {
            for (ItemStack item : this.nowLand.occupyRewards) {
                winPlayer.getPlayer().getInventory().addItem(item);
            }

            if (winPlayer.isOnline()) {
                winPlayer.getPlayer().sendMessage(ChatColor.GOLD + "[ 점령전 ] " + ChatColor.GREEN + "점령전에서 승리하셨습니다!");
                winPlayer.getPlayer().sendMessage(ChatColor.GOLD + "[ 점령전 ] " + ChatColor.GRAY + "보상이 지급되었습니다.");
            }
        }

        this.nowLand = null;
        this.nowOccupyingTeam = null;
        this.bossBar.setVisible(false);

        scheduler.runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                startGame();
            }
        },(long) GAME_INTERVAL);
    }

    private void gameTick() {
        if (this.nowState == GameState.NO_TEAM_OCCUPYING) {
            noTeamOccupying();
        } else if (this.nowState == GameState.TEAM_OCCUPYING) {
            teamOccupying();
        } else if (this.nowState == GameState.OCCUPYING_TEAM_LEAVE) {
            occupyingTeamLeave();
        } else if (this.nowState == GameState.AFTER_TEAM_LEAVE) {
            teamFightAfterLeave();
        }
    }

    private double getProgress() {
        double lastedTick = OCCUPY_TIME - this.occupyingLeftTick;
        return lastedTick / OCCUPY_TIME;
    }

    private List<MinecraftTeam> getTeamsInside() {
        List<MinecraftTeam> landInsideTeams = new ArrayList<>();

        for (MinecraftTeam team : TeamManager.teamList) {
            for (OfflinePlayer offlinePlayer :team.getAllTeamPlayers()) {
                if (!offlinePlayer.isOnline()) continue;

                Player onlinePlayer = offlinePlayer.getPlayer();

                if (this.nowLand.isPlayerInside(onlinePlayer)) {
                    landInsideTeams.add(team);
                    break;
                }
            }
        }

        return landInsideTeams;
    }

    public boolean isStarted() {
        return this.flag;
    }
}