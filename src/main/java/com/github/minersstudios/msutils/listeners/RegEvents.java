package com.github.minersstudios.msutils.listeners;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.listeners.block.BlockPistonExtendListener;
import com.github.minersstudios.msutils.listeners.block.BlockPistonRetractListener;
import com.github.minersstudios.msutils.listeners.chat.AsyncChatListener;
import com.github.minersstudios.msutils.listeners.chat.DiscordSRVListener;
import com.github.minersstudios.msutils.listeners.entity.EntityDamageByEntityListener;
import com.github.minersstudios.msutils.listeners.entity.EntityDamageListener;
import com.github.minersstudios.msutils.listeners.entity.HangingBreakByEntityListener;
import com.github.minersstudios.msutils.listeners.inventory.InventoryClickListener;
import com.github.minersstudios.msutils.listeners.inventory.InventoryCloseListener;
import com.github.minersstudios.msutils.listeners.inventory.InventoryDragListener;
import com.github.minersstudios.msutils.listeners.player.*;
import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nonnull;

public final class RegEvents {

    private RegEvents() {
        throw new IllegalStateException();
    }

    public static void init(@Nonnull Main plugin) {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new BlockPistonExtendListener(), plugin);
        pluginManager.registerEvents(new BlockPistonRetractListener(), plugin);

        DiscordSRV.api.subscribe(new DiscordSRVListener());
        pluginManager.registerEvents(new AsyncChatListener(), plugin);

        pluginManager.registerEvents(new EntityDamageListener(), plugin);
        pluginManager.registerEvents(new EntityDamageByEntityListener(), plugin);
        pluginManager.registerEvents(new HangingBreakByEntityListener(), plugin);

        pluginManager.registerEvents(new InventoryClickListener(), plugin);
        pluginManager.registerEvents(new InventoryCloseListener(), plugin);
        pluginManager.registerEvents(new InventoryDragListener(), plugin);

        pluginManager.registerEvents(new PlayerInteractEntityListener(), plugin);
        pluginManager.registerEvents(new PlayerArmorStandManipulateListener(), plugin);
        pluginManager.registerEvents(new PlayerTeleportListener(), plugin);
        pluginManager.registerEvents(new PlayerChangedWorldListener(), plugin);
        pluginManager.registerEvents(new PlayerMoveListener(), plugin);
        pluginManager.registerEvents(new PlayerDropItemListener(), plugin);
        pluginManager.registerEvents(new PlayerInteractListener(), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(), plugin);
        pluginManager.registerEvents(new PlayerSpawnLocationListener(), plugin);
        pluginManager.registerEvents(new PlayerResourcePackStatusListener(), plugin);
        pluginManager.registerEvents(new PlayerDeathListener(), plugin);
        pluginManager.registerEvents(new PlayerJoinListener(), plugin);
        pluginManager.registerEvents(new AsyncPlayerPreLoginListener(), plugin);
    }
}
