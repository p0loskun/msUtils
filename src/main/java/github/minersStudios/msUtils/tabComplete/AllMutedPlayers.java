package github.minersStudios.msUtils.tabComplete;

import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AllMutedPlayers implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 1){
            for(Object ID : new PlayerID().getYamlConfiguration().getValues(true).values()){
                OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(ID.toString()));
                if(offlinePlayer != null){
                    if(new PlayerInfo(offlinePlayer.getUniqueId()).isMuted()){
                        completions.add(ID.toString());
                    }
                }
            }
        }
        return completions;
    }
}
