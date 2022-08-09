package com.github.minersstudios.msUtils.enums;

import com.github.MinersStudios.msBlock.crafts.dumbass.*;
import com.github.MinersStudios.msDecor.crafts.home.*;
import com.github.MinersStudios.msDecor.crafts.home.cameras.OldCamera;
import com.github.MinersStudios.msDecor.crafts.home.chairs.*;
import com.github.MinersStudios.msDecor.crafts.home.clocks.SmallClock;
import com.github.MinersStudios.msDecor.crafts.home.globus.SmallGlobus;
import com.github.MinersStudios.msDecor.crafts.home.lamps.BigLamp;
import com.github.MinersStudios.msDecor.crafts.home.lamps.SmallLamp;
import com.github.MinersStudios.msDecor.crafts.home.plushes.BMOPlush;
import com.github.MinersStudios.msDecor.crafts.home.plushes.BrownBearPlush;
import com.github.MinersStudios.msDecor.crafts.home.plushes.RacoonPlush;
import com.github.MinersStudios.msDecor.crafts.home.tables.BigTable;
import com.github.MinersStudios.msDecor.crafts.home.tables.SmallTable;
import com.github.MinersStudios.msDecor.crafts.street.*;
import com.github.MinersStudios.msDecor.crafts.street.trashcans.IronTrashcan;
import com.github.MinersStudios.msBlock.crafts.planks.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public enum Crafts {
    //<editor-fold desc="Crafts">
    ACACIA_SMALL_CHAIR(SmallChair.craftSmallAcaciaChair()),
    BIRCH_SMALL_CHAIR(SmallChair.craftSmallBirchChair()),
    CRIMSON_SMALL_CHAIR(SmallChair.craftSmallCrimsonChair()),
    DARK_OAK_SMALL_CHAIR(SmallChair.craftSmallDarkOakChair()),
    JUNGLE_SMALL_CHAIR(SmallChair.craftSmallJungleChair()),
    OAK_SMALL_CHAIR(SmallChair.craftSmallOakChair()),
    SPRUCE_SMALL_CHAIR(SmallChair.craftSmallSpruceChair()),
    WARPED_SMALL_CHAIR(SmallChair.craftSmallWarpedChair()),
    MANGROVE_SMALL_CHAIR(SmallChair.craftSmallMangroveChair()),

    ACACIA_CHAIR(Chair.craftAcaciaChair()),
    BIRCH_CHAIR(Chair.craftBirchChair()),
    CRIMSON_CHAIR(Chair.craftCrimsonChair()),
    DARK_OAK_CHAIR(Chair.craftDarkOakChair()),
    JUNGLE_CHAIR(Chair.craftJungleChair()),
    OAK_CHAIR(Chair.craftOakChair()),
    SPRUCE_CHAIR(Chair.craftSpruceChair()),
    WARPED_CHAIR(Chair.craftWarpedChair()),
    MANGROVE_CHAIR(Chair.craftMangroveChair()),

    ACACIA_SMALL_ARMCHAIR(SmallArmchair.craftSmallAcaciaArmchair()),
    BIRCH_SMALL_ARMCHAIR(SmallArmchair.craftSmallBirchArmchair()),
    CRIMSON_SMALL_ARMCHAIR(SmallArmchair.craftSmallCrimsonArmchair()),
    DARK_OAK_SMALL_ARMCHAIR(SmallArmchair.craftSmallDarkOakArmchair()),
    JUNGLE_SMALL_ARMCHAIR(SmallArmchair.craftSmallJungleArmchair()),
    OAK_SMALL_ARMCHAIR(SmallArmchair.craftSmallOakArmchair()),
    SPRUCE_SMALL_ARMCHAIR(SmallArmchair.craftSmallSpruceArmchair()),
    WARPED_SMALL_ARMCHAIR(SmallArmchair.craftSmallWarpedArmchair()),
    MANGROVE_SMALL_ARMCHAIR(SmallArmchair.craftSmallMangroveArmchair()),

    ACACIA_ARMCHAIR(Armchair.craftAcaciaArmchair()),
    BIRCH_ARMCHAIR(Armchair.craftBirchArmchair()),
    CRIMSON_ARMCHAIR(Armchair.craftCrimsonArmchair()),
    DARK_OAK_ARMCHAIR(Armchair.craftDarkOakArmchair()),
    JUNGLE_ARMCHAIR(Armchair.craftJungleArmchair()),
    OAK_ARMCHAIR(Armchair.craftOakArmchair()),
    SPRUCE_ARMCHAIR(Armchair.craftSpruceArmchair()),
    WARPED_ARMCHAIR(Armchair.craftWarpedArmchair()),
    MANGROVE_ARMCHAIR(Armchair.craftMangroveArmchair()),

    ACACIA_ROCKING_CHAIR(RockingChair.craftAcaciaRockingChair()),
    ACACIA_ROCKING_CHAIR_PAINTABLE(RockingChair.craftAcaciaPaintableRockingChair()),
    BIRCH_ROCKING_CHAIR(RockingChair.craftBirchRockingChair()),
    BIRCH_ROCKING_CHAIR_PAINTABLE(RockingChair.craftBirchPaintableRockingChair()),
    CRIMSON_ROCKING_CHAIR(RockingChair.craftCrimsonRockingChair()),
    CRIMSON_ROCKING_CHAIR_PAINTABLE(RockingChair.craftCrimsonPaintableRockingChair()),
    DARK_OAK_ROCKING_CHAIR(RockingChair.craftDarkOakRockingChair()),
    DARK_OAK_ROCKING_CHAIR_PAINTABLE(RockingChair.craftDarkOakPaintableRockingChair()),
    JUNGLE_ROCKING_CHAIR(RockingChair.craftJungleRockingChair()),
    JUNGLE_ROCKING_CHAIR_PAINTABLE(RockingChair.craftJunglePaintableRockingChair()),
    OAK_ROCKING_CHAIR(RockingChair.craftOakRockingChair()),
    OAK_ROCKING_CHAIR_PAINTABLE(RockingChair.craftOakPaintableRockingChair()),
    SPRUCE_ROCKING_CHAIR(RockingChair.craftSpruceRockingChair()),
    SPRUCE_ROCKING_CHAIR_PAINTABLE(RockingChair.craftSprucePaintableRockingChair()),
    WARPED_ROCKING_CHAIR(RockingChair.craftWarpedRockingChair()),
    WARPED_ROCKING_CHAIR_PAINTABLE(RockingChair.craftWarpedPaintableRockingChair()),
    MANGROVE_ROCKING_CHAIR(RockingChair.craftMangroveRockingChair()),
    MANGROVE_ROCKING_CHAIR_PAINTABLE(RockingChair.craftMangrovePaintableRockingChair()),

    ACACIA_BIG_TABLE(BigTable.craftAcaciaBigTable()),
    BIRCH_BIG_TABLE(BigTable.craftBirchBigTable()),
    CRIMSON_BIG_TABLE(BigTable.craftCrimsonBigTable()),
    DARK_OAK_BIG_TABLE(BigTable.craftDarkOakBigTable()),
    JUNGLE_BIG_TABLE(BigTable.craftJungleBigTable()),
    OAK_BIG_TABLE(BigTable.craftOakBigTable()),
    SPRUCE_BIG_TABLE(BigTable.craftSpruceBigTable()),
    WARPED_BIG_TABLE(BigTable.craftWarpedBigTable()),
    MANGROVE_BIG_TABLE(BigTable.craftMangroveBigTable()),

    ACACIA_SMALL_TABLE(SmallTable.craftAcaciaSmallTable()),
    BIRCH_SMALL_TABLE(SmallTable.craftBirchSmallTable()),
    CRIMSON_SMALL_TABLE(SmallTable.craftCrimsonSmallTable()),
    DARK_OAK_SMALL_TABLE(SmallTable.craftDarkOakSmallTable()),
    JUNGLE_SMALL_TABLE(SmallTable.craftJungleSmallTable()),
    OAK_SMALL_TABLE(SmallTable.craftOakSmallTable()),
    SPRUCE_SMALL_TABLE(SmallTable.craftSpruceSmallTable()),
    WARPED_SMALL_TABLE(SmallTable.craftWarpedSmallTable()),
    MANGROVE_SMALL_TABLE(SmallTable.craftMangroveSmallTable()),

    ACACIA_NIGHTSTAND(Nightstand.craftAcaciaNightstand()),
    BIRCH_NIGHTSTAND(Nightstand.craftBirchNightstand()),
    CRIMSON_NIGHTSTAND(Nightstand.craftCrimsonNightstand()),
    DARK_OAK_NIGHTSTAND(Nightstand.craftDarkOakNightstand()),
    JUNGLE_NIGHTSTAND(Nightstand.craftJungleNightstand()),
    OAK_NIGHTSTAND(Nightstand.craftOakNightstand()),
    SPRUCE_NIGHTSTAND(Nightstand.craftSpruceNightstand()),
    WARPED_NIGHTSTAND(Nightstand.craftWarpedNightstand()),
    MANGROVE_NIGHTSTAND(Nightstand.craftMangroveNightstand()),

    BAR_STOOL(BarStool.craftBarStool()),

    COOL_CHAIR(CoolChair.craftCoolChair()),

    COOL_ARMCHAIR(CoolArmchair.craftCoolArmchair()),

    WHEELBARROW(Wheelbarrow.craftWheelbarrow()),

    SMALL_LAMP(SmallLamp.craftSmallLamp()),

    SMALL_GLOBUS(SmallGlobus.craftSmallGlobus()),

    SMALL_CLOCK(SmallClock.craftSmallClock()),

    PATEFON(Patefon.craftPatefon()),

    IRON_TRASHCAN(IronTrashcan.craftIronTrashcan()),

    FIRE_HYDRANT(FireHydrant.craftFireHydrant()),

    OLD_CAMERA(OldCamera.craftOldCamera()),

    BIG_LAMP(BigLamp.craftBigLamp()),

    BMO_PLUSH(BMOPlush.craftBMOPlush()),

    BROWN_BEAR_PLUSH(BrownBearPlush.craftBrownBearPlush()),

    RACOON_PLUSH(RacoonPlush.craftRacoonPlush()),

    PIGGYBANK(Piggybank.craftPiggybank()),
    PIGGYBANK_DIAMOND(Piggybank.craftPiggybankDiamond()),
    PIGGYBANK_EMERALD(Piggybank.craftPiggybankEmerald()),
    PIGGYBANK_GOLD(Piggybank.craftPiggybankGold()),
    PIGGYBANK_IRON(Piggybank.craftPiggybankIron()),
    PIGGYBANK_NETHERITE(Piggybank.craftPiggybankNetherite()),

    DEER_HEAD(Heads.craftDeerHead()),
    HOGLIN_HEAD(Heads.craftHoglinHead()),
    ZOGLIN_HEAD(Heads.craftZoglinHead()),

    COOKING_POT(CookingPot.craftCookingPot()),

    CELL(Cell.craftCell()),

    BRAZIER(Brazier.craftBrazier()),

    VERTICAL_ACACIA_PLANKS(VerticalPlanks.craftVerticalAcaciaPlanks()),
    VERTICAL_BIRCH_PLANKS(VerticalPlanks.craftVerticalBirchPlanks()),
    VERTICAL_CRIMSON_PLANKS(VerticalPlanks.craftVerticalCrimsonPlanks()),
    VERTICAL_DARK_OAK_PLANKS(VerticalPlanks.craftVerticalDarkOakPlanks()),
    VERTICAL_JUNGLE_PLANKS(VerticalPlanks.craftVerticalJunglePlanks()),
    VERTICAL_OAK_PLANKS(VerticalPlanks.craftVerticalOakPlanks()),
    VERTICAL_SPRUCE_PLANKS(VerticalPlanks.craftVerticalSprucePlanks()),
    VERTICAL_WARPED_PLANKS(VerticalPlanks.craftVerticalWarpedPlanks()),
    VERTICAL_MANGROVE_PLANKS(VerticalPlanks.craftVerticalMangrovePlanks()),

    FRAMED_ACACIA_PLANKS(FramedPlanks.craftFramedAcaciaPlanks()),
    FRAMED_BIRCH_PLANKS(FramedPlanks.craftFramedBirchPlanks()),
    FRAMED_CRIMSON_PLANKS(FramedPlanks.craftFramedCrimsonPlanks()),
    FRAMED_DARK_OAK_PLANKS(FramedPlanks.craftFramedDarkOakPlanks()),
    FRAMED_JUNGLE_PLANKS(FramedPlanks.craftFramedJunglePlanks()),
    FRAMED_OAK_PLANKS(FramedPlanks.craftFramedOakPlanks()),
    FRAMED_SPRUCE_PLANKS(FramedPlanks.craftFramedSprucePlanks()),
    FRAMED_WARPED_PLANKS(FramedPlanks.craftFramedWarpedPlanks()),
    FRAMED_MANGROVE_PLANKS(FramedPlanks.craftFramedMangrovePlanks()),

    CARVED_ACACIA_PLANKS(CarvedPlanks.craftCarvedAcaciaPlanks()),
    CARVED_BIRCH_PLANKS(CarvedPlanks.craftCarvedBirchPlanks()),
    CARVED_CRIMSON_PLANKS(CarvedPlanks.craftCarvedCrimsonPlanks()),
    CARVED_DARK_OAK_PLANKS(CarvedPlanks.craftCarvedDarkOakPlanks()),
    CARVED_JUNGLE_PLANKS(CarvedPlanks.craftCarvedJunglePlanks()),
    CARVED_OAK_PLANKS(CarvedPlanks.craftCarvedOakPlanks()),
    CARVED_SPRUCE_PLANKS(CarvedPlanks.craftCarvedSprucePlanks()),
    CARVED_WARPED_PLANKS(CarvedPlanks.craftCarvedWarpedPlanks()),
    CARVED_MANGROVE_PLANKS(CarvedPlanks.craftCarvedMangrovePlanks()),

    HACPAL_BLOCK(HacpalBlock.craftHacpalBlock()),
    SVINSTER_BLOCK(SvinsterBlock.craftSvinsterBlock()),
    //</editor-fold>
    ;

    public static final String
            CRAFTS_NAME = ChatColor.WHITE + "뀂ꀧ",
            CRAFT_NAME = ChatColor.WHITE + "뀂ꀨ";
    private final ShapedRecipe shapedRecipe;

    Crafts(ShapedRecipe shapedRecipe) {
        this.shapedRecipe = shapedRecipe;
    }

    public static int getItemIndex(@Nonnull ItemStack itemStack) {
        for (Crafts craft : Crafts.values()) {
            if (itemStack.isSimilar(craft.shapedRecipe.getResult())) {
                return craft.ordinal();
            }
        }
        return -1;
    }

    public static void openCraft(@Nonnull Player player, @Nonnull ItemStack itemStack, int pageIndex) {
        for (Crafts craft : Crafts.values()) {
            if (itemStack.isSimilar(craft.shapedRecipe.getResult())) {
                Inventory inventory = Bukkit.createInventory(null, 4 * 9, Component.text(CRAFT_NAME));
                String[] shapes = craft.shapedRecipe.getShape();
                int i = 0;
                for (String shape : shapes.length == 1 ? new String[]{"   ", shapes[0], "   "} : shapes) {
                    for (Character character : (shape.length() == 1 ? " " + shape + " " : shape.length() == 2 ? shape + " " : shape).toCharArray()) {
                        ItemStack ingredient = craft.shapedRecipe.getIngredientMap().get(character);
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
                inventory.setItem(14, getArrow(pageIndex));
                inventory.setItem(15, itemStack);
                inventory.setItem(31, getQuitButton());
                player.openInventory(inventory);
            }
        }
    }

    /**
     * @return Crafts GUI
     */
    @Nonnull
    public static Inventory getInventory(int index) {
        Inventory inventory = Bukkit.createInventory(null, 5 * 9, Component.text(CRAFTS_NAME));
        Crafts[] crafts = Crafts.values();
        inventory.setItem(36, getPreviousPageButton()[index == 0 ? 1 : 0]);
        inventory.setItem(37, getPreviousPageButton()[1]);
        inventory.setItem(38, getPreviousPageButton()[1]);
        inventory.setItem(39, getPreviousPageButton()[1]);
        inventory.setItem(40, getQuitButton());
        inventory.setItem(41, getNextPageButton()[index + 37 > Crafts.values().length ? 1 : 0]);
        inventory.setItem(42, getNextPageButton()[1]);
        inventory.setItem(43, getNextPageButton()[1]);
        inventory.setItem(44, getNextPageButton()[1]);
        for (int i = 0; i <= 35 && index < Crafts.values().length; index++, i++) {
            inventory.setItem(i, crafts[index].shapedRecipe.getResult());
        }
        return inventory;
    }

    @Nonnull
    private static ItemStack[] getPreviousPageButton() {
        ItemStack previousPage = new ItemStack(Material.PAPER),
                previousPageNoCMD = new ItemStack(Material.PAPER);
        ItemMeta previousPageMeta = previousPage.getItemMeta(),
                previousPageMetaNoCMD = previousPageNoCMD.getItemMeta();
        assert previousPageMeta != null && previousPageMetaNoCMD != null;
        previousPageMetaNoCMD.displayName(Component.text(ChatColor.WHITE + "Предыдущая страница"));
        previousPageMeta.displayName(Component.text(ChatColor.WHITE + "Предыдущая страница"));
        previousPageMeta.setCustomModelData(5001);
        previousPageMetaNoCMD.setCustomModelData(1);
        previousPageNoCMD.setItemMeta(previousPageMetaNoCMD);
        previousPage.setItemMeta(previousPageMeta);
        return new ItemStack[]{previousPage, previousPageNoCMD};
    }

    @Nonnull
    private static ItemStack[] getNextPageButton() {
        ItemStack nextPage = new ItemStack(Material.PAPER),
                nextPageNoCMD = new ItemStack(Material.PAPER);
        ItemMeta nextPageMeta = nextPage.getItemMeta(),
                nextPageMetaNoCMD = nextPageNoCMD.getItemMeta();
        assert nextPageMeta != null && nextPageMetaNoCMD != null;
        nextPageMetaNoCMD.displayName(Component.text(ChatColor.WHITE + "Следующая страница"));
        nextPageMeta.displayName(Component.text(ChatColor.WHITE + "Следующая страница"));
        nextPageMeta.setCustomModelData(5002);
        nextPageMetaNoCMD.setCustomModelData(1);
        nextPageNoCMD.setItemMeta(nextPageMetaNoCMD);
        nextPage.setItemMeta(nextPageMeta);
        return new ItemStack[]{nextPage, nextPageNoCMD};
    }

    @Nonnull
    private static ItemStack getQuitButton() {
        ItemStack quit = new ItemStack(Material.PAPER);
        ItemMeta quitMeta = quit.getItemMeta();
        assert quitMeta != null;
        quitMeta.displayName(Component.text(ChatColor.WHITE + "Вернуться"));
        quitMeta.setCustomModelData(1);
        quit.setItemMeta(quitMeta);
        return quit;
    }

    @Nonnull
    private static ItemStack getArrow(int pageIndex) {
        ItemStack arrow = new ItemStack(Material.PAPER);
        ItemMeta arrowMeta = arrow.getItemMeta();
        assert arrowMeta != null;
        arrowMeta.displayName(Component.text(ChatColor.GRAY + " -> "));
        arrowMeta.setCustomModelData(pageIndex + 1);
        arrow.setItemMeta(arrowMeta);
        return arrow;
    }
}
