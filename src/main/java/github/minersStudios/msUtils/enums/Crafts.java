package github.minersStudios.msUtils.enums;

import github.minersStudios.msBlock.crafts.planks.VerticalPlanks;
import github.minersStudios.msDecor.crafts.home.*;
import github.minersStudios.msDecor.crafts.home.cameras.OldCamera;
import github.minersStudios.msDecor.crafts.home.chairs.*;
import github.minersStudios.msDecor.crafts.home.clocks.SmallClock;
import github.minersStudios.msDecor.crafts.home.globus.SmallGlobus;
import github.minersStudios.msDecor.crafts.home.lamps.BigLamp;
import github.minersStudios.msDecor.crafts.home.lamps.SmallLamp;
import github.minersStudios.msDecor.crafts.home.plushes.BMOPlush;
import github.minersStudios.msDecor.crafts.home.plushes.BrownBearPlush;
import github.minersStudios.msDecor.crafts.home.plushes.RacoonPlush;
import github.minersStudios.msDecor.crafts.home.tables.BigTable;
import github.minersStudios.msDecor.crafts.home.tables.SmallTable;
import github.minersStudios.msDecor.crafts.street.Brazier;
import github.minersStudios.msDecor.crafts.street.FireHydrant;
import github.minersStudios.msDecor.crafts.street.Wheelbarrow;
import github.minersStudios.msDecor.crafts.street.trashcans.IronTrashcan;
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
    ACACIA_SMALL_CHAIR(SmallChair.craftSmallAcaciaChair()),
    BIRCH_SMALL_CHAIR(SmallChair.craftSmallBirchChair()),
    CRIMSON_SMALL_CHAIR(SmallChair.craftSmallCrimsonChair()),
    DARK_OAK_SMALL_CHAIR(SmallChair.craftSmallDarkOakChair()),
    JUNGLE_SMALL_CHAIR(SmallChair.craftSmallJungleChair()),
    OAK_SMALL_CHAIR(SmallChair.craftSmallOakChair()),
    SPRUCE_SMALL_CHAIR(SmallChair.craftSmallSpruceChair()),
    WARPED_SMALL_CHAIR(SmallChair.craftSmallWarpedChair()),

    ACACIA_CHAIR(Chair.craftAcaciaChair()),
    BIRCH_CHAIR(Chair.craftBirchChair()),
    CRIMSON_CHAIR(Chair.craftCrimsonChair()),
    DARK_OAK_CHAIR(Chair.craftDarkOakChair()),
    JUNGLE_CHAIR(Chair.craftJungleChair()),
    OAK_CHAIR(Chair.craftOakChair()),
    SPRUCE_CHAIR(Chair.craftSpruceChair()),
    WARPED_CHAIR(Chair.craftWarpedChair()),

    COOL_ARMCHAIR(CoolArmchair.craftCoolArmchair()),

    ACACIA_SMALL_ARMCHAIR(SmallArmchair.craftSmallAcaciaArmchair()),
    BIRCH_SMALL_ARMCHAIR(SmallArmchair.craftSmallBirchArmchair()),
    CRIMSON_SMALL_ARMCHAIR(SmallArmchair.craftSmallCrimsonArmchair()),
    DARK_OAK_SMALL_ARMCHAIR(SmallArmchair.craftSmallDarkOakArmchair()),
    JUNGLE_SMALL_ARMCHAIR(SmallArmchair.craftSmallJungleArmchair()),
    OAK_SMALL_ARMCHAIR(SmallArmchair.craftSmallOakArmchair()),
    SPRUCE_SMALL_ARMCHAIR(SmallArmchair.craftSmallSpruceArmchair()),
    WARPED_SMALL_ARMCHAIR(SmallArmchair.craftSmallWarpedArmchair()),

    ACACIA_ARMCHAIR(Armchair.craftAcaciaArmchair()),
    BIRCH_ARMCHAIR(Armchair.craftBirchArmchair()),
    CRIMSON_ARMCHAIR(Armchair.craftCrimsonArmchair()),
    DARK_OAK_ARMCHAIR(Armchair.craftDarkOakArmchair()),
    JUNGLE_ARMCHAIR(Armchair.craftJungleArmchair()),
    OAK_ARMCHAIR(Armchair.craftOakArmchair()),
    SPRUCE_ARMCHAIR(Armchair.craftSpruceArmchair()),
    WARPED_ARMCHAIR(Armchair.craftWarpedArmchair()),

    BAR_STOOL(BarStool.craftBarStool()),

    COOL_CHAIR(CoolChair.craftCoolChair()),

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

    ACACIA_BIG_TABLE(BigTable.craftAcaciaBigTable()),
    BIRCH_BIG_TABLE(BigTable.craftBirchBigTable()),
    CRIMSON_BIG_TABLE(BigTable.craftCrimsonBigTable()),
    DARK_OAK_BIG_TABLE(BigTable.craftDarkOakBigTable()),
    JUNGLE_BIG_TABLE(BigTable.craftJungleBigTable()),
    OAK_BIG_TABLE(BigTable.craftOakBigTable()),
    SPRUCE_BIG_TABLE(BigTable.craftSpruceBigTable()),
    WARPED_BIG_TABLE(BigTable.craftWarpedBigTable()),

    ACACIA_SMALL_TABLE(SmallTable.craftAcaciaSmallTable()),
    BIRCH_SMALL_TABLE(SmallTable.craftBirchSmallTable()),
    CRIMSON_SMALL_TABLE(SmallTable.craftCrimsonSmallTable()),
    DARK_OAK_SMALL_TABLE(SmallTable.craftDarkOakSmallTable()),
    JUNGLE_SMALL_TABLE(SmallTable.craftJungleSmallTable()),
    OAK_SMALL_TABLE(SmallTable.craftOakSmallTable()),
    SPRUCE_SMALL_TABLE(SmallTable.craftSpruceSmallTable()),
    WARPED_SMALL_TABLE(SmallTable.craftWarpedSmallTable()),

    ACACIA_NIGHTSTAND(Nightstand.craftAcaciaNightstand()),
    BIRCH_NIGHTSTAND(Nightstand.craftBirchNightstand()),
    CRIMSON_NIGHTSTAND(Nightstand.craftCrimsonNightstand()),
    DARK_OAK_NIGHTSTAND(Nightstand.craftDarkOakNightstand()),
    JUNGLE_NIGHTSTAND(Nightstand.craftJungleNightstand()),
    OAK_NIGHTSTAND(Nightstand.craftOakNightstand()),
    SPRUCE_NIGHTSTAND(Nightstand.craftSpruceNightstand()),
    WARPED_NIGHTSTAND(Nightstand.craftWarpedNightstand()),

    WHEELBARROW(Wheelbarrow.craftWheelbarrow()),

    SMALL_LAMP(SmallLamp.craftSmallLamp()),

    SMALL_GLOBUS(SmallGlobus.craftSmallGlobus()),

    SMALL_CLOCK(SmallClock.craftSmallClock()),

    PATEFON(Patefon.craftPatefon()),

    IRON_TRASHCAN(IronTrashcan.craftIronTrashcan()),

    FIRE_HYDRANT(FireHydrant.craftFireHydrant()),

    CAMERA(OldCamera.craftOldCamera()),

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
    VERTICAL_WARPED_PLANKS(VerticalPlanks.craftVerticalWarpedPlanks());

    public static final String CRAFTS_NAME = ChatColor.WHITE + "\uB002\uA027", CRAFT_NAME = ChatColor.WHITE + "\uB002\uA028";
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
                Inventory inventory = Bukkit.createInventory(null, 4 * 9, CRAFT_NAME);
                int i = 0;
                for (String shape : craft.shapedRecipe.getShape()) {
                    for (Character character : shape.toCharArray()) {
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
        Inventory inventory = Bukkit.createInventory(null, 5 * 9, CRAFTS_NAME);
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
        for (int i = 0; i <= 35 && index < Crafts.values().length;) {
            inventory.setItem(i, crafts[index].shapedRecipe.getResult());
            i++;
            index++;
        }
        return inventory;
    }

    @Nonnull
    private static ItemStack[] getPreviousPageButton() {
        ItemStack previousPage = new ItemStack(Material.PAPER), previousPageNoCMD = new ItemStack(Material.PAPER);
        ItemMeta previousPageMeta = previousPage.getItemMeta(), previousPageMetaNoCMD = previousPageNoCMD.getItemMeta();
        assert previousPageMeta != null && previousPageMetaNoCMD != null;
        previousPageMetaNoCMD.setDisplayName(ChatColor.WHITE + "Предыдущая страница");
        previousPageMeta.setDisplayName(ChatColor.WHITE + "Предыдущая страница");
        previousPageMeta.setCustomModelData(5001);
        previousPageMetaNoCMD.setCustomModelData(0);
        previousPageNoCMD.setItemMeta(previousPageMetaNoCMD);
        previousPage.setItemMeta(previousPageMeta);
        return new ItemStack[]{previousPage, previousPageNoCMD};
    }

    @Nonnull
    private static ItemStack[] getNextPageButton() {
        ItemStack nextPage = new ItemStack(Material.PAPER), nextPageNoCMD = new ItemStack(Material.PAPER);
        ItemMeta nextPageMeta = nextPage.getItemMeta(), nextPageMetaNoCMD = nextPageNoCMD.getItemMeta();
        assert nextPageMeta != null && nextPageMetaNoCMD != null;
        nextPageMetaNoCMD.setDisplayName(ChatColor.WHITE + "Следующая страница");
        nextPageMeta.setDisplayName(ChatColor.WHITE + "Следующая страница");
        nextPageMeta.setCustomModelData(5002);
        nextPageMetaNoCMD.setCustomModelData(0);
        nextPageNoCMD.setItemMeta(nextPageMetaNoCMD);
        nextPage.setItemMeta(nextPageMeta);
        return new ItemStack[]{nextPage, nextPageNoCMD};
    }

    @Nonnull
    private static ItemStack getQuitButton() {
        ItemStack quit = new ItemStack(Material.PAPER);
        ItemMeta quitMeta = quit.getItemMeta();
        assert quitMeta != null;
        quitMeta.setDisplayName(ChatColor.WHITE + "Вернуться");
        quitMeta.setCustomModelData(0);
        quit.setItemMeta(quitMeta);
        return quit;
    }

    @Nonnull
    private static ItemStack getArrow(int pageIndex) {
        ItemStack arrow = new ItemStack(Material.PAPER);
        ItemMeta arrowMeta = arrow.getItemMeta();
        assert arrowMeta != null;
        arrowMeta.setDisplayName(ChatColor.GRAY + "->");
        arrowMeta.setCustomModelData(pageIndex);
        arrow.setItemMeta(arrowMeta);
        return arrow;
    }
}
