package com.github.minersstudios.msutils.player;

import com.github.minersstudios.msblock.utils.BlockUtils;
import com.github.minersstudios.msdecor.utils.CustomDecorUtils;
import com.github.minersstudios.msitems.utils.ItemUtils;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;

public class CraftsMenu {
	public static final Component
			CATEGORY_NAME = ChatUtils.createDefaultStyledName("뀂ꀲ"),
			CRAFTS_NAME = ChatUtils.createDefaultStyledName("뀂ꀧ"),
			CRAFT_NAME = ChatUtils.createDefaultStyledName("뀂ꀨ");

	public static final int
			ARROW_SLOT = 14,
			RESULT_SLOT = 15,
			CRAFT_QUIT_BUTTON = 31,
			CRAFTS_QUIT_BUTTON = 40;

	public static final List<Integer>
			PREVIOUS_PAGE_BUTTON_SLOTS = Lists.newArrayList(36, 37, 38, 39),
			NEXT_PAGE_BUTTON_SLOTS = Lists.newArrayList(41, 42, 43, 44),
			BLOCKS_CATEGORY_SLOTS = Lists.newArrayList(0, 1, 2, 9, 10, 11, 18, 19, 20, 27, 28, 29),
			DECORS_CATEGORY_SLOTS = Lists.newArrayList(3, 4, 5, 12, 13, 14, 21, 22, 23, 30, 31, 32),
			ITEMS_CATEGORY_SLOTS = Lists.newArrayList(6, 7, 8, 15, 16, 17, 24, 25, 26, 33, 34, 35);

	protected static final List<Recipe> customDecorRecipes = CustomDecorUtils.CUSTOM_DECOR_RECIPES;
	protected static final List<Recipe> customBlockRecipes = BlockUtils.CUSTOM_BLOCK_RECIPES;
	protected static final List<Recipe> customItemRecipes =ItemUtils.CUSTOM_ITEM_RECIPES;

	public static int getItemIndex(@NotNull ItemStack itemStack, @NotNull Category category) {
		int index = 0;
		for (Recipe recipe : category.recipes) {
			if (itemStack.isSimilar(recipe.getResult())) {
				return index;
			}
			index++;
		}
		return -1;
	}

	public static void openCraft(@NotNull Player player, @NotNull ItemStack itemStack, @Range(from = 0, to = Integer.MAX_VALUE) int pageIndex, @NotNull Category category) {
		for (Recipe recipe : category.recipes) {
			if (recipe instanceof ShapedRecipe shapedRecipe && itemStack.isSimilar(shapedRecipe.getResult())) {
				Inventory inventory = Bukkit.createInventory(null, 4 * 9, CRAFT_NAME);
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
							case 0 -> inventory.setItem(2, ingredient);
							case 1 -> inventory.setItem(3, ingredient);
							case 2 -> inventory.setItem(4, ingredient);
							case 3 -> inventory.setItem(11, ingredient);
							case 4 -> inventory.setItem(12, ingredient);
							case 5 -> inventory.setItem(13, ingredient);
							case 6 -> inventory.setItem(20, ingredient);
							case 7 -> inventory.setItem(21, ingredient);
							case 8 -> inventory.setItem(22, ingredient);
						}
						i++;
					}
				}
				inventory.setItem(ARROW_SLOT, getArrow(pageIndex));
				inventory.setItem(RESULT_SLOT, itemStack);
				inventory.setItem(CRAFT_QUIT_BUTTON, getQuitButton());
				player.openInventory(inventory);
			}
		}
	}

	public static void openCategory(@NotNull Player player, @Range(from = 0, to = Integer.MAX_VALUE) int index, @NotNull Category category) {
		List<Recipe> recipes = category.recipes;
		Inventory inventory = Bukkit.createInventory(null, 5 * 9, CRAFTS_NAME);
		inventory.setItem(PREVIOUS_PAGE_BUTTON_SLOTS.get(0), getPreviousPageButton()[index == 0 ? 1 : 0]);
		inventory.setItem(PREVIOUS_PAGE_BUTTON_SLOTS.get(1), getPreviousPageButton()[1]);
		inventory.setItem(PREVIOUS_PAGE_BUTTON_SLOTS.get(2), getPreviousPageButton()[1]);
		inventory.setItem(PREVIOUS_PAGE_BUTTON_SLOTS.get(3), getPreviousPageButton()[1]);
		inventory.setItem(CRAFTS_QUIT_BUTTON, getQuitButton());
		inventory.setItem(NEXT_PAGE_BUTTON_SLOTS.get(0), getNextPageButton()[index + 37 > recipes.size() ? 1 : 0]);
		inventory.setItem(NEXT_PAGE_BUTTON_SLOTS.get(1), getNextPageButton()[1]);
		inventory.setItem(NEXT_PAGE_BUTTON_SLOTS.get(2), getNextPageButton()[1]);
		inventory.setItem(NEXT_PAGE_BUTTON_SLOTS.get(3), getNextPageButton()[1]);
		for (int i = 0, page = index; i <= 35 && page < recipes.size(); page++, i++) {
			inventory.setItem(i, recipes.get(page).getResult());
		}
		player.openInventory(inventory);
	}

	public static void openCategories(@NotNull Player player) {
		player.openInventory(Bukkit.createInventory(null, 4 * 9, CATEGORY_NAME));
	}

	private static ItemStack @NotNull [] getPreviousPageButton() {
		ItemStack previousPage = new ItemStack(Material.PAPER),
				previousPageNoCMD = new ItemStack(Material.PAPER);
		ItemMeta previousPageMeta = previousPage.getItemMeta(),
				previousPageMetaNoCMD = previousPageNoCMD.getItemMeta();
		previousPageMetaNoCMD.displayName(Component.text(ChatColor.WHITE + "Предыдущая страница"));
		previousPageMeta.displayName(Component.text(ChatColor.WHITE + "Предыдущая страница"));
		previousPageMeta.setCustomModelData(5001);
		previousPageMetaNoCMD.setCustomModelData(1);
		previousPageNoCMD.setItemMeta(previousPageMetaNoCMD);
		previousPage.setItemMeta(previousPageMeta);
		return new ItemStack[]{previousPage, previousPageNoCMD};
	}

	private static ItemStack @NotNull [] getNextPageButton() {
		ItemStack nextPage = new ItemStack(Material.PAPER),
				nextPageNoCMD = new ItemStack(Material.PAPER);
		ItemMeta nextPageMeta = nextPage.getItemMeta(),
				nextPageMetaNoCMD = nextPageNoCMD.getItemMeta();
		nextPageMetaNoCMD.displayName(Component.text(ChatColor.WHITE + "Следующая страница"));
		nextPageMeta.displayName(Component.text(ChatColor.WHITE + "Следующая страница"));
		nextPageMeta.setCustomModelData(5002);
		nextPageMetaNoCMD.setCustomModelData(1);
		nextPageNoCMD.setItemMeta(nextPageMetaNoCMD);
		nextPage.setItemMeta(nextPageMeta);
		return new ItemStack[]{nextPage, nextPageNoCMD};
	}

	private static @NotNull ItemStack getQuitButton() {
		ItemStack quit = new ItemStack(Material.PAPER);
		ItemMeta quitMeta = quit.getItemMeta();
		quitMeta.displayName(Component.text(ChatColor.WHITE + "Вернуться"));
		quitMeta.setCustomModelData(1);
		quit.setItemMeta(quitMeta);
		return quit;
	}

	private static @NotNull ItemStack getArrow(@Range(from = 0, to = Integer.MAX_VALUE) int pageIndex) {
		ItemStack arrow = new ItemStack(Material.PAPER);
		ItemMeta arrowMeta = arrow.getItemMeta();
		arrowMeta.displayName(Component.text(ChatColor.GRAY + " -> "));
		arrowMeta.setCustomModelData(pageIndex + 1);
		arrow.setItemMeta(arrowMeta);
		return arrow;
	}

	public enum Category {
		BLOCKS(customBlockRecipes),
		DECORS(customDecorRecipes),
		ITEMS(customItemRecipes);

		private final @NotNull List<Recipe> recipes;

		Category(@NotNull List<Recipe> recipes) {
			this.recipes = recipes;
		}

		public @NotNull List<Recipe> getRecipes() {
			return recipes;
		}

		public static @Nullable Category getCategory(@Nullable ItemStack itemStack) {
			if (itemStack == null) return null;
			for (Recipe recipe : customBlockRecipes) {
				if (recipe.getResult().isSimilar(itemStack)) {
					return BLOCKS;
				}
			}
			for (Recipe recipe : customDecorRecipes) {
				if (recipe.getResult().isSimilar(itemStack)) {
					return DECORS;
				}
			}
			for (Recipe recipe : customItemRecipes) {
				if (recipe.getResult().isSimilar(itemStack)) {
					return ITEMS;
				}
			}
			return null;
		}
	}
}
