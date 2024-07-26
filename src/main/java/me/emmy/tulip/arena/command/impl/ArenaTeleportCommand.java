package me.emmy.tulip.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.annotation.Command;
import me.emmy.tulip.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 20:35
 */
public class ArenaTeleportCommand extends BaseCommand {
    @Override
    @Command(name = "arena.teleport", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /arena teleport <name>"));
            return;
        }

        String arenaName = args[0];

        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);
        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe center of the arena " + arenaName + " is not set!"));
            return;
        }

        player.teleport(Tulip.getInstance().getArenaRepository().getArena(arenaName).getCenter());
        player.sendMessage(CC.translate("&aTeleported to the center of arena " + arenaName + "!"));
    }
}
