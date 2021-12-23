package me.dasuonline.vivace.occupy.system;

import me.dasuonline.vivace.Main;
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
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

enum GameState {
    NO_TEAM_OCCUPYING(0), TEAM_OCCUPYING(1), OCCUPYING_TEAM_LEAVE(-1);

    private int stateCode;

    private GameState(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getStateCode() {
        return this.stateCode;
    }
}

public class OccupyGame extends TeamUtils {

    private static final double OCCUPY_TIME = 20 * 60 * 4;

    private OccupyLand nowLand;
    private boolean flag = false;

    private GameState nowState = GameState.NO_TEAM_OCCUPYING;
    private MinecraftTeam nowOccupyingTeam;
    private double leftTick;

    private BossBar bossBar;

    public OccupyGame() {
        this.bossBar = Bukkit.getServer().createBossBar("[점령전]", BarColor.GREEN, BarStyle.SOLID);
    }

    private void noTeamOccupying() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!playerHasTeam(p)) return;

            if (this.nowLand.isPlayerInside(p)) {
                this.nowState = GameState.TEAM_OCCUPYING;
                this.nowOccupyingTeam = getPlayerTeam(p);
                this.leftTick = OCCUPY_TIME;

                this.bossBar.setTitle("[점령전] " + nowOccupyingTeam.getTeamName() + " "
                        + nowOccupyingTeam.getTeamLevelTitle() + ChatColor.RED + ", 점령 시작!");
                this.bossBar.setVisible(true);
                this.bossBar.setProgress(0);

                for (Player bPlayer : Bukkit.getOnlinePlayers()) {
                    this.bossBar.addPlayer(bPlayer);
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
            this.nowState = GameState.OCCUPYING_TEAM_LEAVE;

            this.bossBar.setTitle("[점령전] " + nowOccupyingTeam.getTeamName() + " "
                    + nowOccupyingTeam.getTeamLevelTitle() + ChatColor.WHITE + ", 점령지에서 이탈했습니다. 카운트 : ");
        }

        this.leftTick--;
        this.bossBar.setProgress(getProgress());
    }
    private void occupyingTeamLeave() {}

    public void startGame(OccupyLand nowLand) {
        this.nowLand = nowLand;
        this.flag = true;

        OccupyGame thisGame = this;

        List<MinecraftTeam> mTeams = TeamManager.teamList;

        BukkitScheduler scheduler = Main.getInstance().getServer().getScheduler();
        scheduler.runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                gameTick();
            }
        }, 0L, 1L);

    }

    private void gameTick() {
        if (this.nowState == GameState.NO_TEAM_OCCUPYING) {
            noTeamOccupying();
        } else if (this.nowState == GameState.TEAM_OCCUPYING) {
            teamOccupying();
        } else if (this.nowState == GameState.OCCUPYING_TEAM_LEAVE) {
            occupyingTeamLeave();
        }
    }

    private double getProgress() {
        double lastedTick = OCCUPY_TIME - this.leftTick;
        return lastedTick / OCCUPY_TIME;
    }
}