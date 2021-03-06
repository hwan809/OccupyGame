package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Help implements CustomExecutor {

    private final ArrayList<String[]> helpMessages = new ArrayList<>();
    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        int helpcode;

        try {
            helpcode = Integer.parseInt(args[1]);
        } catch (Exception e) {
            teamManager.logMessage(player, ChatColor.RED + "도움말 번호를 입력하세요.");

            return true;
        }

        if (helpcode > helpMessages.size()) {
            teamManager.logMessage(player, ChatColor.RED + "도움말 PAGE 1 ~ " + helpMessages.size());
            return true;
        }

        for (String helpm : helpMessages.get(helpcode - 1)) {
            player.sendMessage(helpm);
        }

        return true;
    }

    @Override
    public void init() {
        helpMessages.add(
                new String[]
                        {"§8§m            §f§8[ §aTEAM §f§8]§8§m            ",
                                 "",
                                 "§a/팀 도움말 [ 페이지 ]",
                                 "§a/팀 생성 [ 팀 이름 ]",
                                 "§a/팀 해체 [ 팀 이름 ]",
                                 "§a/팀 탈퇴 [ 플레이어 ]",
                                 "§a/팀 초대 [ 팀 이름 ]",
                                 "                    §epage (1 / 2)"}
        );

        helpMessages.add(
                new String[]
                        {"§8§m            §f§8[ §aTEAM §f§8]§8§m            ",
                                "",
                                "§a/팀 추방 [ 플레이어 ]",
                                "§a/팀 관리자 [추가 | 삭제] [ 플레이어 ]",
                                "§a/팀 채팅 [ 메시지 ]",
                                "§a/팀 목록 [ 플레이어 ]",
                                "                    §epage (2 / 2)"}
        );

        teamManager = Main.getManager("Team");
    }
}
