package com.github.minersstudios.msutils.inventory;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.inventory.CustomInventory;
import com.github.minersstudios.mscore.inventory.InventoryButton;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.github.minersstudios.mscore.inventory.InventoryButton.playClickSound;
import static com.github.minersstudios.mscore.utils.ChatUtils.createDefaultStyledText;
import static net.kyori.adventure.text.Component.text;

public class PronounsMenu {

    public static @NotNull CustomInventory create() {
        ItemStack he = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta heMeta = he.getItemMeta();
        heMeta.displayName(createDefaultStyledText("Он"));
        ArrayList<Component> loreHe = new ArrayList<>();
        loreHe.add(text("К вам будут обращаться как к нему").color(NamedTextColor.GRAY));
        heMeta.lore(loreHe);
        he.setItemMeta(heMeta);

        ItemStack she = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta sheMeta = she.getItemMeta();
        sheMeta.displayName(createDefaultStyledText("Она"));
        ArrayList<Component> loreShe = new ArrayList<>();
        loreShe.add(text("К вам будут обращаться как к ней").color(NamedTextColor.GRAY));
        sheMeta.lore(loreShe);
        she.setItemMeta(sheMeta);

        ItemStack they = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta theyMeta = they.getItemMeta();
        theyMeta.displayName(createDefaultStyledText("Они"));
        ArrayList<Component> loreThey = new ArrayList<>();
        loreThey.add(text("К вам будут обращаться как к ним").color(NamedTextColor.GRAY));
        theyMeta.lore(loreThey);
        they.setItemMeta(theyMeta);

        PlayerInfoMap playerInfoMap = MSUtils.getConfigCache().playerInfoMap;
        CustomInventory customInventory = new CustomInventory("§8Выберите форму обращения", 1);

        InventoryButton heButton = InventoryButton.create()
                .item(he)
                .clickAction((event, inventory, button) -> {
                    Player player = (Player) event.getWhoClicked();
                    PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);
                    PlayerFile playerFile = playerInfo.getPlayerFile();

                    playerFile.setPronouns(Pronouns.HE);
                    playerFile.save();
                    playClickSound(player);
                    player.closeInventory();
                });
        customInventory.setButtonAt(0, heButton);
        customInventory.setButtonAt(1, heButton);
        customInventory.setButtonAt(2, heButton);

        InventoryButton sheButton = InventoryButton.create()
                .item(she)
                .clickAction((event, inventory, button) -> {
                    Player player = (Player) event.getWhoClicked();
                    PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);
                    PlayerFile playerFile = playerInfo.getPlayerFile();

                    playerFile.setPronouns(Pronouns.SHE);
                    playerFile.save();
                    playClickSound(player);
                    player.closeInventory();
                });
        customInventory.setButtonAt(3, sheButton);
        customInventory.setButtonAt(4, sheButton);
        customInventory.setButtonAt(5, sheButton);

        InventoryButton theyButton = InventoryButton.create()
                .item(they)
                .clickAction((event, inventory, button) -> {
                    Player player = (Player) event.getWhoClicked();
                    PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);
                    PlayerFile playerFile = playerInfo.getPlayerFile();

                    playerFile.setPronouns(Pronouns.THEY);
                    playerFile.save();
                    playClickSound(player);
                    player.closeInventory();
                });
        customInventory.setButtonAt(6, theyButton);
        customInventory.setButtonAt(7, theyButton);
        customInventory.setButtonAt(8, theyButton);

        customInventory.setCloseAction(((event, inventory) -> {
            Player player = (Player) event.getPlayer();
            PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);

            if (playerInfo.getPlayerFile().getYamlConfiguration().getString("pronouns") == null) {
                Bukkit.getScheduler().runTask(
                        MSUtils.getInstance(),
                        () -> player.openInventory(customInventory)
                );
            } else {
                new RegistrationProcess().setPronouns(player, playerInfo);
            }
        }));

        return customInventory;
    }

    public static void open(@NotNull Player player) {
        CustomInventory customInventory = MSCore.getConfigCache().customInventoryMap.get("pronouns");
        if (customInventory == null) return;
        player.openInventory(customInventory);
    }
}
