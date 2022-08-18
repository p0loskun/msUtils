package com.github.minersstudios.msutils.tabCompleters;

import com.github.minersstudios.msutils.classes.PlayerInfo;
import com.github.minersstudios.msutils.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WorldTeleport implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
                int ID = playerInfo.getID(false, false);
                if (ID != -1) {
                    completions.add(String.valueOf(ID));
                }
                completions.add(player.getName());
            }
        }
        if (args.length == 2) {
            for (World world : Bukkit.getWorlds()) {
                if (world != Main.getWorldDark()) {
                    completions.add(world.getName());
                }
            }
        }
        return completions;
    }
}
