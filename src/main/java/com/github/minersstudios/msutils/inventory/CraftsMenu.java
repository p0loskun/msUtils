package com.github.minersstudios.msutils.inventory;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.inventory.CustomInventory;
import com.github.minersstudios.mscore.inventory.ElementListedInventory;
import com.github.minersstudios.mscore.inventory.InventoryButton;
import com.github.minersstudios.mscore.inventory.ListedInventory;
import com.github.minersstudios.mscore.inventory.actions.ButtonClickAction;
import com.github.minersstudios.mscore.utils.InventoryUtils;
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
import java.util.Objects;
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

	public static @NotNull CustomInventory create() {
		CustomInventory customInventory = new CustomInventory("뀂ꀲ", 4);

		InventoryButton blocksButton = new InventoryButton(new ItemStack(Material.AIR), (event, i, b) -> {
			Player player = (Player) event.getWhoClicked();
			player.openInventory(createCraftsInventory(MSCore.getConfigCache().customBlockRecipes));
			playClickSound(player);
		});
		customInventory.setButtons(
				IntStream.of(0, 1, 2, 9, 10, 11, 18, 19, 20, 27, 28, 29)
				.boxed()
				.collect(Collectors.toMap(Function.identity(), slot -> blocksButton))
		);

		InventoryButton decorsButton = new InventoryButton(new ItemStack(Material.AIR), (event, i, b) -> {
			Player player = (Player) event.getWhoClicked();
			player.openInventory(createCraftsInventory(MSCore.getConfigCache().customDecorRecipes));
			playClickSound(player);
		});
		customInventory.setButtons(
				IntStream.of(3, 4, 5, 12, 13, 14, 21, 22, 23, 30, 31, 32)
				.boxed()
				.collect(Collectors.toMap(Function.identity(), slot -> decorsButton))
		);

		InventoryButton itemsButton = new InventoryButton(new ItemStack(Material.AIR), (event, i, b) -> {
			Player player = (Player) event.getWhoClicked();
			player.openInventory(createCraftsInventory(MSCore.getConfigCache().customItemRecipes));
			playClickSound(player);
		});
		customInventory.setButtons(
				IntStream.of(6, 7, 8, 15, 16, 17, 24, 25, 26, 33, 34, 35)
				.boxed()
				.collect(Collectors.toMap(Function.identity(), slot -> itemsButton))
		);

		return customInventory;
	}

	public static boolean open(@NotNull Player player) {
		CustomInventory customInventory = InventoryUtils.getCustomInventory("crafts");
		if (customInventory == null) return false;
		player.openInventory(customInventory);
		return true;
	}

	private static @NotNull CustomInventory createCraftsInventory(@NotNull List<Recipe> recipes) {
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
					craftInventory.setButtonAt(CRAFT_QUIT_BUTTON, new InventoryButton(createQuitButton(), (e, inv, b) -> {
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
		craftsInventory.setStaticButtonAt(36, inventory -> new InventoryButton(createPreviousPageButton()[inventory.getPreviousPageIndex() == -1 ? 1 : 0], previousClick));
		craftsInventory.setStaticButtonAt(37, i -> new InventoryButton(createPreviousPageButton()[1], previousClick));
		craftsInventory.setStaticButtonAt(38, i -> new InventoryButton(createPreviousPageButton()[1], previousClick));
		craftsInventory.setStaticButtonAt(39, i -> new InventoryButton(createPreviousPageButton()[1], previousClick));

		craftsInventory.setStaticButtonAt(CRAFTS_QUIT_BUTTON, i -> new InventoryButton(createQuitButton(), (event, customInventory, inventoryButton) -> {
			Player player = (Player) event.getWhoClicked();
			open(player);
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
		craftsInventory.setStaticButtonAt(41, inventory -> new InventoryButton(createNextPageButton()[inventory.getNextPageIndex() == -1 ? 1 : 0], nextClick));
		craftsInventory.setStaticButtonAt(42, i -> new InventoryButton(createNextPageButton()[1], nextClick));
		craftsInventory.setStaticButtonAt(43, i -> new InventoryButton(createNextPageButton()[1], nextClick));
		craftsInventory.setStaticButtonAt(44, i -> new InventoryButton(createNextPageButton()[1], nextClick));

		craftsInventory.updatePages();

		return Objects.requireNonNull(craftsInventory.getPage(0));
	}

	@Contract(" -> new")
	private static ItemStack @NotNull [] createPreviousPageButton() {
		ItemStack previousPage = new ItemStack(Material.PAPER),
				previousPageNoCMD = new ItemStack(Material.PAPER);
		ItemMeta previousPageMeta = previousPage.getItemMeta(),
				previousPageMetaNoCMD = previousPageNoCMD.getItemMeta();
		previousPageMetaNoCMD.displayName(createDefaultStyledText("Предыдущая страница"));
		previousPageMeta.displayName(createDefaultStyledText("Предыдущая страница"));
		previousPageMeta.setCustomModelData(5001);
		previousPageMetaNoCMD.setCustomModelData(1);
		previousPageNoCMD.setItemMeta(previousPageMetaNoCMD);
		previousPage.setItemMeta(previousPageMeta);
		return new ItemStack[]{previousPage, previousPageNoCMD};
	}

	@Contract(" -> new")
	private static ItemStack @NotNull [] createNextPageButton() {
		ItemStack nextPage = new ItemStack(Material.PAPER),
				nextPageNoCMD = new ItemStack(Material.PAPER);
		ItemMeta nextPageMeta = nextPage.getItemMeta(),
				nextPageMetaNoCMD = nextPageNoCMD.getItemMeta();
		nextPageMetaNoCMD.displayName(createDefaultStyledText("Следующая страница"));
		nextPageMeta.displayName(createDefaultStyledText("Следующая страница"));
		nextPageMeta.setCustomModelData(5002);
		nextPageMetaNoCMD.setCustomModelData(1);
		nextPageNoCMD.setItemMeta(nextPageMetaNoCMD);
		nextPage.setItemMeta(nextPageMeta);
		return new ItemStack[]{nextPage, nextPageNoCMD};
	}

	@Contract(" -> new")
	private static @NotNull ItemStack createQuitButton() {
		ItemStack quit = new ItemStack(Material.PAPER);
		ItemMeta quitMeta = quit.getItemMeta();
		quitMeta.displayName(createDefaultStyledText("Вернуться"));
		quitMeta.setCustomModelData(1);
		quit.setItemMeta(quitMeta);
		return quit;
	}
}