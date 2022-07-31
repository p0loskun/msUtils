package github.minersStudios.msUtils.listeners;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.listeners.block.PistonListener;
import github.minersStudios.msUtils.listeners.chat.*;
import github.minersStudios.msUtils.listeners.entity.*;
import github.minersStudios.msUtils.listeners.player.*;
import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class RegEvents {

    public static void init(Main plugin) {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PistonListener(), plugin);

        DiscordSRV.api.subscribe(new DiscordSRVListener());
        pluginManager.registerEvents(new AsyncChatListener(), plugin);

        pluginManager.registerEvents(new EntityDamageListener(), plugin);

        pluginManager.registerEvents(new PlayerInteractEntityListener(), plugin);
        pluginManager.registerEvents(new PlayerArmorStandManipulateListener(), plugin);
        pluginManager.registerEvents(new PlayerTeleportListener(), plugin);
        pluginManager.registerEvents(new PlayerChangedWorldListener(), plugin);
        pluginManager.registerEvents(new PlayerMoveListener(), plugin);
        pluginManager.registerEvents(new PlayerDropItemListener(), plugin);
        pluginManager.registerEvents(new PlayerInteractListener(), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(), plugin);
        pluginManager.registerEvents(new InventoryClickListener(), plugin);
        pluginManager.registerEvents(new InventoryCloseListener(), plugin);
        pluginManager.registerEvents(new PlayerSpawnLocationListener(), plugin);
        pluginManager.registerEvents(new PlayerResourcePackStatusListener(), plugin);
        pluginManager.registerEvents(new PlayerDeathListener(), plugin);
        pluginManager.registerEvents(new PlayerJoinListener(), plugin);
        pluginManager.registerEvents(new AsyncPlayerPreLoginListener(), plugin);
    }
}
