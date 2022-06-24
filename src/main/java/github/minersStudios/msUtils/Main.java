package github.minersStudios.msUtils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import fr.xephi.authme.api.v3.AuthMeApi;
import github.minersStudios.msUtils.classes.*;
import github.minersStudios.msUtils.commands.ban.*;
import github.minersStudios.msUtils.commands.mute.*;
import github.minersStudios.msUtils.commands.other.*;
import github.minersStudios.msUtils.commands.roleplay.*;
import github.minersStudios.msUtils.listeners.RegEvents;
import github.minersStudios.msUtils.tabComplete.*;
import github.minersStudios.msUtils.utils.ChatUtils;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class Main extends JavaPlugin {
    public static Main plugin;
    public static AuthMeApi authmeApi;
    public static World worldDark;
    public static final ChatBubbles bubbles = new ChatBubbles();
    public static final ChatBuffer chatBuffer = new ChatBuffer();
    public static Scoreboard scoreboardHideTags;
    public static Team scoreboardHideTagsTeam;
    public static ProtocolManager protocolManager;

    @Getter private final Map<UUID, ArmorStand> seats = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        authmeApi = AuthMeApi.getInstance();
        worldDark = this.getServer().getWorld("world_dark");
        protocolManager = ProtocolLibrary.getProtocolManager();

        if(!new File(plugin.getDataFolder(), "config.yml").exists()){
            this.saveResource("config.yml", false);
        }
        new RegEvents();
        new RotateSeatTask();
        this.generateWorld();
        this.registerCommands();

        scoreboardHideTags = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        scoreboardHideTags.registerNewTeam("HideTags");
        scoreboardHideTagsTeam = scoreboardHideTags.getTeam("HideTags");
        assert scoreboardHideTagsTeam != null;
        scoreboardHideTagsTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        scoreboardHideTagsTeam.setCanSeeFriendlyInvisibles(false);

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                scoreboardHideTagsTeam.addEntry(player.getName());
                player.setScoreboard(scoreboardHideTags);
            }
        }
    }

    @Override
    public void onDisable() {
        for(UUID uuid : this.seats.keySet()) {
            SitPlayer sitPlayer = new SitPlayer(Bukkit.getPlayer(uuid));
            sitPlayer.setSitting(null);
        }
        for (Player player : Bukkit.getOnlinePlayers()){
            PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
            playerInfo.setLastLeaveLocation(player.getLocation());
            player.kickPlayer("Ну шо грифер, запустил свою лаг машину?");
        }
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("ban")).setExecutor(new BanCommand());
        Objects.requireNonNull(this.getCommand("ban")).setTabCompleter(new AllPlayers());
        Objects.requireNonNull(this.getCommand("unban")).setExecutor(new UnBanCommand());
        Objects.requireNonNull(this.getCommand("unban")).setTabCompleter(new AllPlayers());

        Objects.requireNonNull(this.getCommand("mute")).setExecutor(new MuteCommand());
        Objects.requireNonNull(this.getCommand("mute")).setTabCompleter(new AllPlayers());
        Objects.requireNonNull(this.getCommand("unmute")).setExecutor(new UnMuteCommand());
        Objects.requireNonNull(this.getCommand("unmute")).setTabCompleter(new AllPlayers());

        Objects.requireNonNull(this.getCommand("kick")).setExecutor(new KickCommand());
        Objects.requireNonNull(this.getCommand("kick")).setTabCompleter(new AllLocalPlayers());

        Objects.requireNonNull(this.getCommand("getmaploc")).setExecutor(new GetMapLocationCommand());
        Objects.requireNonNull(this.getCommand("resourcepack")).setExecutor(new ResourcePackCommand());
        Objects.requireNonNull(this.getCommand("rp")).setExecutor(new ResourcePackCommand());
        Objects.requireNonNull(this.getCommand("info")).setExecutor(new InfoCommand());
        Objects.requireNonNull(this.getCommand("info")).setTabCompleter(new AllPlayers());
        Objects.requireNonNull(this.getCommand("whitelist")).setExecutor(new WhitelistCommand());
        Objects.requireNonNull(this.getCommand("whitelist")).setTabCompleter(new WhiteList());
        Objects.requireNonNull(this.getCommand("privatemessage")).setExecutor(new PrivateMessageCommand());
        Objects.requireNonNull(this.getCommand("privatemessage")).setTabCompleter(new AllLocalPlayers());
        Objects.requireNonNull(this.getCommand("pm")).setExecutor(new PrivateMessageCommand());
        Objects.requireNonNull(this.getCommand("pm")).setTabCompleter(new AllLocalPlayers());

        Objects.requireNonNull(this.getCommand("sit")).setExecutor(new SitCommand());
        Objects.requireNonNull(this.getCommand("s")).setExecutor(new SitCommand());
        Objects.requireNonNull(this.getCommand("spit")).setExecutor(new SpitCommand());
        Objects.requireNonNull(this.getCommand("fart")).setExecutor(new FartCommand());
        Objects.requireNonNull(this.getCommand("me")).setExecutor(new MeCommand());
        Objects.requireNonNull(this.getCommand("try")).setExecutor(new TryCommand());
    }

    private void generateWorld(){
        if(Main.worldDark == null){
            this.getServer().createWorld(
                    new WorldCreator("world_dark")
                            .generator("msUtils:empty")
                            .generateStructures(false)
                            .type(WorldType.NORMAL)
                            .environment(World.Environment.NORMAL)
            );
            worldDark = this.getServer().getWorld("world_dark");
            if(worldDark != null) {
                worldDark.setTime(18000);
                worldDark.setDifficulty(Difficulty.PEACEFUL);
                worldDark.setGameRule(GameRule.FALL_DAMAGE, false);
                worldDark.setGameRule(GameRule.FIRE_DAMAGE, false);
                worldDark.setGameRule(GameRule.DROWNING_DAMAGE, false);
                worldDark.setGameRule(GameRule.FREEZE_DAMAGE, false);
                worldDark.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                worldDark.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
                worldDark.setGameRule(GameRule.KEEP_INVENTORY, true);
                worldDark.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            } else {
                ChatUtils.sendError(null, "Main#generateWorld() world_dark = null");
                plugin.getServer().savePlayers();
                plugin.getServer().shutdown();
            }
        }
    }

    @Override
    @Nullable
    public ChunkGenerator getDefaultWorldGenerator(@Nonnull String worldName, String id) {
        return "empty".equals(id) ? new ChunkGenerator() {} : null;
    }
}
