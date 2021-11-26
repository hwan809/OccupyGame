package me.dasuonline.vivace.team.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Help implements CustomExecutor {

    public static ArrayList<String[]> helpmessages = new ArrayList<>();
    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        int helpcode;

        try {
            helpcode = Integer.parseInt(args[1]) + 1; // 0 -> 1
        } catch (NumberFormatException e) {
            e.printStackTrace();
            teamManager.logMessage(player, ChatColor.RED + "도움말 번호를 입력하세요.");

            return true;
        }

        if (helpcode > helpmessages.size()) {
            teamManager.logMessage(player, ChatColor.RED + "도움말 PAGE 1 ~ " + helpmessages.size());
            return true;
        }

        for (String helpm : helpmessages.get(helpcode)) {
            player.sendMessage(helpm);
        }

        return true;
    }

    @Override
    public void init() {
        helpmessages.add(
                new String[]
                        {"&8&m            &f&8[ &aTEAM &f&8]&8&m            ",
                         "",
                         "&a/팀 도움말 [ 페이지 ]",
                         "&a/팀 생성 [ 팀 이름 ]",
                         "&a/팀 해체 [ 팀 이름 ]",
                         "&a/팀 가입 [ 팀 이름 ]",
                         "&a/팀 탈퇴 [ 팀 이름 ]",
                         "&a/팀 초대 [ 플레이어 ]",
                         "                    &epage (1 / 3)"}
        );

        helpmessages.add(
                new String[]
                        {"&8&m            &f&8[ &aTEAM &f&8]&8&m            ",
                                "",
                                "&a/팀 수락 [ 플레이어 ]",
                                "&a/팀 거절 [ 플레이어 ]",
                                "&a/팀 추방 [ 플레이어 ]",
                                "&a/팀 관리자 [ 플레이어 ]",
                                "&a/팀 양도 [ 플레이어 ]",
                                "&a/팀 메세지 [ 할 말 ]",
                                "              &epage (2 / 3)"}
        );

        helpmessages.add(
                new String[]
                        {"&8&m            &f&8[ &aTEAM &f&8]&8&m            ",
                                "",
                                "&a/팀 목록",
                                "",
                                "              &epage (3 / 3)"}
        );

        teamManager = Main.getManager("Team");
    }
}
