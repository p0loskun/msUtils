package com.github.minersstudios.msutils.inventory;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.inventory.CustomInventory;
import com.github.minersstudios.mscore.inventory.CustomInventoryMap;
import com.github.minersstudios.mscore.inventory.InventoryButton;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerInfoMap;
import com.github.minersstudios.msutils.player.PlayerSettings;
import com.github.minersstudios.msutils.player.ResourcePack;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static com.github.minersstudios.mscore.inventory.InventoryButton.playClickSound;
import static com.github.minersstudios.mscore.utils.ChatUtils.*;

public class ResourcePackMenu {

    public static @NotNull CustomInventory create() {
        ItemStack pick = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta pickMeta = pick.getItemMeta();
        pickMeta.displayName(createDefaultStyledText("Ресурспаки"));
        pickMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        pickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        pickMeta.lore(convertStringsToComponents(
                COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
                "Выберите один из",
                "2 видов текстурпаков",
                "или выберите 1 вариант",
                "и играйте без него",
                "(Не рекомендуется)"
        ));
        pick.setItemMeta(pickMeta);

        ItemStack none = new ItemStack(Material.COAL_BLOCK);
        ItemMeta noneMeta = none.getItemMeta();
        noneMeta.displayName(createDefaultStyledText("Без текстурпака"));
        noneMeta.lore(convertStringsToComponents(
                COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
                "Имеет в себе :",
                " - ничего"
        ));
        none.setItemMeta(noneMeta);

        ItemStack lite = new ItemStack(Material.IRON_BLOCK);
        ItemMeta liteMeta = lite.getItemMeta();
        liteMeta.displayName(createDefaultStyledText("Облегчённая версия"));
        liteMeta.lore(convertStringsToComponents(
                COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
                "Имеет в себе :",
                " - кастомные текстуры и модельки",
                " - переименуемые предметы",
                " - изменённая модель головы стива"
        ));
        lite.setItemMeta(liteMeta);

        ItemStack full = new ItemStack(Material.NETHERITE_BLOCK);
        ItemMeta fullMeta = full.getItemMeta();
        fullMeta.displayName(createDefaultStyledText("Полная версия"));
        fullMeta.lore(convertStringsToComponents(
                COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
                "Имеет в себе :",
                " - кастомные текстуры и модельки",
                " - переименуемые предметы",
                " - изменённая модель головы стива",
                " - анимированные текстуры",
                "   блоков/предметов",
                " - изменённые текстуры/модели",
                "   блоков/предметов/интерфейса",
                " - 3D модель фонаря",
                " - OF текстуры и модельки :",
                "   Небо",
                "   Стойка для брони",
                "   CIT предметы"
        ));
        full.setItemMeta(fullMeta);

        PlayerInfoMap playerInfoMap = MSUtils.getConfigCache().playerInfoMap;
        CustomInventory customInventory = new CustomInventory("§8Выберите нужный текстурпак", 1);

        InventoryButton noneButton = InventoryButton.create()
                .item(none)
                .clickAction((event, inventory, button) -> {
                    Player player = (Player) event.getWhoClicked();
                    PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);
                    PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();

                    if (playerSettings.getResourcePackType() != ResourcePack.Type.NULL && playerSettings.getResourcePackType() != ResourcePack.Type.NONE) {
                        playerInfo.kickPlayer("Вы были кикнуты", "Этот параметр требует повторного захода на сервер");
                    }

                    playerSettings.setResourcePackType(ResourcePack.Type.NONE);
                    playerSettings.save();
                    playClickSound(player);
                    player.closeInventory();

                    if (playerInfo.isInWorldDark()) {
                        playerInfo.initJoin();
                    }
                });
        customInventory.setButtonAt(0, noneButton);
        customInventory.setButtonAt(1, noneButton);

        InventoryButton fullButton = InventoryButton.create()
                .item(full)
                .clickAction((event, inventory, button) -> {
                    Player player = (Player) event.getWhoClicked();
                    PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);
                    PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();

                    playerSettings.setResourcePackType(ResourcePack.Type.FULL);
                    playerSettings.save();
                    playClickSound(player);
                    player.closeInventory();
                    ResourcePack.setResourcePack(playerInfo);
                });
        customInventory.setButtonAt(2, fullButton);
        customInventory.setButtonAt(3, fullButton);
        customInventory.setButtonAt(5, fullButton);
        customInventory.setButtonAt(6, fullButton);

        InventoryButton liteButton = InventoryButton.create()
                .item(lite)
                .clickAction((event, inventory, button) -> {
                    Player player = (Player) event.getWhoClicked();
                    PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);
                    PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();

                    playerSettings.setResourcePackType(ResourcePack.Type.LITE);
                    playerSettings.save();
                    playClickSound(player);
                    player.closeInventory();
                    ResourcePack.setResourcePack(playerInfo);
                });
        customInventory.setButtonAt(7, liteButton);
        customInventory.setButtonAt(8, liteButton);

        customInventory.setItem(4, pick);

        customInventory.setCloseAction(((event, inventory) -> {
            Player player = (Player) event.getPlayer();
            PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);
            ResourcePack.Type type = playerInfo.getPlayerFile().getPlayerSettings().getResourcePackType();

            if (type == ResourcePack.Type.NULL) {
                Bukkit.getScheduler().runTask(MSUtils.getInstance(), () -> player.openInventory(inventory));
            }
        }));

        return customInventory;
    }

    public static boolean open(@NotNull Player player) {
        CustomInventoryMap customInventoryMap = MSCore.getConfigCache().customInventoryMap;
        CustomInventory customInventory = customInventoryMap.get("resourcepack");
        if (customInventory == null) return false;
        player.openInventory(customInventory);
        return true;
    }
}
