package com.github.minersstudios.msutils.inventory;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.inventory.*;
import com.github.minersstudios.mscore.inventory.actions.ButtonClickAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.minersstudios.mscore.inventory.InventoryButton.playClickSound;
import static com.github.minersstudios.mscore.utils.ChatUtils.createDefaultStyledText;

public class CraftsMenu {
    public static final int
            RESULT_SLOT = 15,
            CRAFT_QUIT_BUTTON = 31,
            CRAFTS_QUIT_BUTTON = 40;

    @Contract(" -> new")
    public static @NotNull CustomInventory create() {
        CustomInventory customInventory = new CustomInventory("뀂ꀲ", 4);

        InventoryButton blocksButton = new InventoryButton(new ItemStack(Material.AIR), (event, i, b) -> {
            Player player = (Player) event.getWhoClicked();
            open(Type.BLOCKS, player);
            playClickSound(player);
        });
        customInventory.setButtons(
                IntStream.of(0, 1, 2, 9, 10, 11, 18, 19, 20, 27, 28, 29)
                        .boxed()
                        .collect(Collectors.toMap(Function.identity(), slot -> blocksButton))
        );

        InventoryButton decorsButton = new InventoryButton(new ItemStack(Material.AIR), (event, i, b) -> {
            Player player = (Player) event.getWhoClicked();
            open(Type.DECORS, player);
            playClickSound(player);
        });
        customInventory.setButtons(
                IntStream.of(3, 4, 5, 12, 13, 14, 21, 22, 23, 30, 31, 32)
                        .boxed()
                        .collect(Collectors.toMap(Function.identity(), slot -> decorsButton))
        );

        InventoryButton itemsButton = new InventoryButton(new ItemStack(Material.AIR), (event, i, b) -> {
            Player player = (Player) event.getWhoClicked();
            open(Type.ITEMS, player);
            playClickSound(player);
        });
        customInventory.setButtons(
                IntStream.of(6, 7, 8, 15, 16, 17, 24, 25, 26, 33, 34, 35)
                        .boxed()
                        .collect(Collectors.toMap(Function.identity(), slot -> itemsButton))
        );

        return customInventory;
    }

    public static boolean open(@NotNull Type type, @NotNull Player player) {
        CustomInventoryMap customInventoryMap = MSCore.getConfigCache().customInventoryMap;
        CustomInventory customInventory = switch (type) {
            case MAIN -> customInventoryMap.get("crafts");
            case BLOCKS -> customInventoryMap.get("crafts_blocks");
            case DECORS -> customInventoryMap.get("crafts_decors");
            case ITEMS -> customInventoryMap.get("crafts_items");
        };
        if (customInventory == null) return false;
        player.openInventory(customInventory);
        return true;
    }

    @Contract("_ -> new")
    public static @NotNull CustomInventory createCraftsInventory(@NotNull List<Recipe> recipes) {
        ItemStack
                previousPageItem = new ItemStack(Material.PAPER),
                previousPageNoCMD = new ItemStack(Material.PAPER);
        ItemMeta
                previousPageMeta = previousPageItem.getItemMeta(),
                previousPageMetaNoCMD = previousPageNoCMD.getItemMeta();
        previousPageMetaNoCMD.displayName(createDefaultStyledText("Предыдущая страница"));
        previousPageMeta.displayName(createDefaultStyledText("Предыдущая страница"));
        previousPageMeta.setCustomModelData(5001);
        previousPageMetaNoCMD.setCustomModelData(1);
        previousPageNoCMD.setItemMeta(previousPageMetaNoCMD);
        previousPageItem.setItemMeta(previousPageMeta);

        ItemStack
                nextPageItem = new ItemStack(Material.PAPER),
                nextPageNoCMDItem = new ItemStack(Material.PAPER);
        ItemMeta
                nextPageMeta = nextPageItem.getItemMeta(),
                nextPageMetaNoCMD = nextPageNoCMDItem.getItemMeta();
        nextPageMetaNoCMD.displayName(createDefaultStyledText("Следующая страница"));
        nextPageMeta.displayName(createDefaultStyledText("Следующая страница"));
        nextPageMeta.setCustomModelData(5002);
        nextPageMetaNoCMD.setCustomModelData(1);
        nextPageNoCMDItem.setItemMeta(nextPageMetaNoCMD);
        nextPageItem.setItemMeta(nextPageMeta);

        ItemStack quitItem = new ItemStack(Material.PAPER);
        ItemMeta quitMeta = quitItem.getItemMeta();
        quitMeta.displayName(createDefaultStyledText("Вернуться"));
        quitMeta.setCustomModelData(1);
        quitItem.setItemMeta(quitMeta);

        List<InventoryButton> elements = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ItemStack resultItem = recipe.getResult();

            elements.add(new InventoryButton(resultItem, (clickEvent, inventory, button) -> {
                Player player = (Player) clickEvent.getWhoClicked();
                CustomInventory craftInventory = new CustomInventory("뀂ꀨ", 4);

                if (recipe instanceof ShapedRecipe shapedRecipe) {
                    String[] shapes = shapedRecipe.getShape();
                    int i = 0;

                    for (String shape : shapes.length == 1 ? new String[]{"   ", shapes[0], "   "} : shapes) {
                        for (Character character : (shape.length() == 1 ? " " + shape + " " : shape.length() == 2 ? shape + " " : shape).toCharArray()) {
                            ItemStack ingredient = shapedRecipe.getIngredientMap().get(character);

                            if (ingredient == null) {
                                i++;
                                continue;
                            }

                            switch (i) {
                                case 0 -> craftInventory.setItem(2, ingredient);
                                case 1 -> craftInventory.setItem(3, ingredient);
                                case 2 -> craftInventory.setItem(4, ingredient);
                                case 3 -> craftInventory.setItem(11, ingredient);
                                case 4 -> craftInventory.setItem(12, ingredient);
                                case 5 -> craftInventory.setItem(13, ingredient);
                                case 6 -> craftInventory.setItem(20, ingredient);
                                case 7 -> craftInventory.setItem(21, ingredient);
                                case 8 -> craftInventory.setItem(22, ingredient);
                            }

                            i++;
                        }
                    }

                    craftInventory.setItem(RESULT_SLOT, resultItem);
                    craftInventory.setButtonAt(CRAFT_QUIT_BUTTON, new InventoryButton(quitItem, (e, inv, b) -> {
                        player.openInventory(inventory);
                        playClickSound(player);
                    }));
                    player.openInventory(craftInventory);
                }
            }));
        }

        ElementListedInventory craftsInventory = new ElementListedInventory("뀂ꀧ", 5, elements, IntStream.range(0, 36).toArray());

        ButtonClickAction previousClick = (event, customInventory, button) -> {
            if (!(customInventory instanceof ListedInventory listedInventory)) return;

            Player player = (Player) event.getWhoClicked();
            ListedInventory previousPage = craftsInventory.getPage(listedInventory.getPreviousPageIndex());

            if (previousPage != null) {
                player.openInventory(previousPage);
                playClickSound(player);
            }
        };

        craftsInventory.setStaticButtonAt(36, inventory -> new InventoryButton(inventory.getPreviousPageIndex() == -1 ? previousPageNoCMD : previousPageItem, previousClick));
        craftsInventory.setStaticButtonAt(37, i -> new InventoryButton(previousPageNoCMD, previousClick));
        craftsInventory.setStaticButtonAt(38, i -> new InventoryButton(previousPageNoCMD, previousClick));
        craftsInventory.setStaticButtonAt(39, i -> new InventoryButton(previousPageNoCMD, previousClick));
        craftsInventory.setStaticButtonAt(CRAFTS_QUIT_BUTTON, i -> new InventoryButton(quitItem, (event, customInventory, inventoryButton) -> {
            Player player = (Player) event.getWhoClicked();
            open(Type.MAIN, player);
            playClickSound(player);
        }));

        ButtonClickAction nextClick = (event, customInventory, button) -> {
            if (!(customInventory instanceof ListedInventory listedInventory)) return;

            Player player = (Player) event.getWhoClicked();
            ListedInventory nextPage = craftsInventory.getPage(listedInventory.getNextPageIndex());

            if (nextPage != null) {
                player.openInventory(nextPage);
                playClickSound(player);
            }
        };

        craftsInventory.setStaticButtonAt(41, inventory -> new InventoryButton(inventory.getNextPageIndex() == -1 ? nextPageNoCMDItem : nextPageItem, nextClick));
        craftsInventory.setStaticButtonAt(42, i -> new InventoryButton(nextPageNoCMDItem, nextClick));
        craftsInventory.setStaticButtonAt(43, i -> new InventoryButton(nextPageNoCMDItem, nextClick));
        craftsInventory.setStaticButtonAt(44, i -> new InventoryButton(nextPageNoCMDItem, nextClick));
        craftsInventory.updatePages();

        return craftsInventory;
    }

    public enum Type {
        MAIN, BLOCKS, DECORS, ITEMS
    }
}
