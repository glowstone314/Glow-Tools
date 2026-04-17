package gc.mc.glowtools.client.logic;

import gc.mc.glowtools.client.config.Configs;
import gc.mc.glowtools.client.config.EnchantmentsFilterMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

public class TransferEnchantedHandler {

    private static boolean isTarget(ItemStack stack) {
        if (stack.isEmpty()) return false;

        boolean isBook = stack.is(Items.ENCHANTED_BOOK);
        ItemEnchantments enchants = stack.get(isBook
                ? DataComponents.STORED_ENCHANTMENTS
                : DataComponents.ENCHANTMENTS);

        if (enchants == null || enchants.isEmpty()) return false;

        if (!Configs.TransferEnchanted.TRANSFER_ALL.getBooleanValue()) {
            boolean isBow = stack.is(Items.BOW);
            boolean isFishingRod = stack.is(Items.FISHING_ROD);
            if (!isBook && !isBow && !isFishingRod)
                return false;
            boolean transferBook = Configs.TransferEnchanted.TRANSFER_BOOK.getBooleanValue();
            boolean transferBow = Configs.TransferEnchanted.TRANSFER_BOW.getBooleanValue();
            boolean transferFishingRod = Configs.TransferEnchanted.TRANSFER_FISHING_ROD.getBooleanValue();
            if (isBook && !transferBook) return false;
            if (isBow && !transferBow) return false;
            if (isFishingRod && !transferFishingRod) return false;
        }

        boolean hasNonCurse = false;
        boolean invert = Configs.TransferEnchanted.INVERT_FILTER.getBooleanValue();

        for (var entry : enchants.entrySet()) {
            Holder<Enchantment> enchantEntry = entry.getKey();
            Enchantment enchantment = enchantEntry.value();
            int level = entry.getIntValue();

            if (Configs.TransferEnchanted.EXCLUDE_CURSED.getBooleanValue() && enchantEntry.is(EnchantmentTags.CURSE)) {
                return invert;
            }

            if (!suitableEnchant((EnchantmentsFilterMode) Configs.TransferEnchanted.FILTER_MODE.getOptionListValue(), enchantEntry)) {
                continue;
            }

            if (Configs.TransferEnchanted.ONLY_MAX_LEVEL.getBooleanValue()) {
                if (level >= enchantment.getMaxLevel()) {
                    hasNonCurse = true;
                }
            } else {
                hasNonCurse = true;
            }

            if (enchantEntry.is(Enchantments.SWIFT_SNEAK)) {
                hasNonCurse = true;
            }
        }
        if (invert) hasNonCurse = !hasNonCurse;
        return hasNonCurse;
    }

    public static boolean runTransfer() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || !(client.screen instanceof AbstractContainerScreen<?>)) return false;

        AbstractContainerMenu handler = client.player.containerMenu;
        int containerSize = handler.slots.size() - 36;

        for (int i = 0; i < containerSize; i++) {
            Slot slot = handler.getSlot(i);
            if (slot.hasItem() && isTarget(slot.getItem())) {
                transferSlot(i);
            }
        }
        return true;
    }

    private static void transferSlot(int slotIndex) {
        Minecraft client = Minecraft.getInstance();
        if (client.gameMode == null || client.player == null) return;

        client.gameMode.handleContainerInput(
                client.player.containerMenu.containerId,
                slotIndex,
                0,
                ContainerInput.QUICK_MOVE,
                client.player
        );
    }

    public static boolean suitableEnchant(EnchantmentsFilterMode mode, Holder<Enchantment> enchantEntry) {
        return switch (mode) {
            case ALL -> true;
            case SWORD -> matches(swordEnchantments, enchantEntry);
            case ARMOR -> matches(armorEnchantments, enchantEntry);
            case SPECIAL_ARMOR -> {
                if (enchantEntry.is(Enchantments.SWIFT_SNEAK))
                    yield true;
                yield matches(armorSpecialtyEnchantments, enchantEntry);
            }
            case BOW -> matches(bowEnchantments, enchantEntry);
            case TOOL -> matches(toolEnchantments, enchantEntry);
            case CROSSBOW -> matches(crossbowEnchantments, enchantEntry);
            case TRIDENT -> matches(tridentEnchantments, enchantEntry);
            case FISHING -> matches(fishingEnchantments, enchantEntry);
            case MACE -> matches(maceEnchantments, enchantEntry);
            case SPEAR -> matches(spearEnchantments, enchantEntry);
        };
    }

    public static boolean matches(List<ResourceKey<Enchantment>> enchantments, Holder<Enchantment> enchantEntry) {
        for (ResourceKey<Enchantment> enchantment : enchantments) {
            if (enchantEntry.is(enchantment)) {
                return true;
            }
        }
        return false;
    }

    public static final List<ResourceKey<Enchantment>> swordEnchantments = List.of(
            Enchantments.SHARPNESS,
            Enchantments.SMITE,
            Enchantments.BANE_OF_ARTHROPODS,
            Enchantments.FIRE_ASPECT,
            Enchantments.KNOCKBACK,
            Enchantments.LOOTING,
            Enchantments.SWEEPING_EDGE
    );

    public static final List<ResourceKey<Enchantment>> armorEnchantments = List.of(
            Enchantments.PROTECTION,
            Enchantments.FIRE_PROTECTION,
            Enchantments.BLAST_PROTECTION,
            Enchantments.PROJECTILE_PROTECTION
    );

    public static final List<ResourceKey<Enchantment>> armorSpecialtyEnchantments = List.of(
            Enchantments.RESPIRATION,
            Enchantments.AQUA_AFFINITY,
            Enchantments.THORNS,
            Enchantments.DEPTH_STRIDER,
            Enchantments.FROST_WALKER,
            Enchantments.SOUL_SPEED,
            Enchantments.FEATHER_FALLING
    );

    public static final List<ResourceKey<Enchantment>> bowEnchantments = List.of(
            Enchantments.POWER,
            Enchantments.PUNCH,
            Enchantments.FLAME,
            Enchantments.INFINITY
    );

    public static final List<ResourceKey<Enchantment>> toolEnchantments = List.of(
            Enchantments.EFFICIENCY,
            Enchantments.SILK_TOUCH,
            Enchantments.UNBREAKING,
            Enchantments.FORTUNE,
            Enchantments.MENDING
    );

    public static final List<ResourceKey<Enchantment>> crossbowEnchantments = List.of(
            Enchantments.QUICK_CHARGE,
            Enchantments.MULTISHOT,
            Enchantments.PIERCING
    );

    public static final List<ResourceKey<Enchantment>> tridentEnchantments = List.of(
            Enchantments.RIPTIDE,
            Enchantments.IMPALING,
            Enchantments.CHANNELING,
            Enchantments.LOYALTY
    );

    public static final List<ResourceKey<Enchantment>> fishingEnchantments = List.of(
            Enchantments.LURE,
            Enchantments.LUCK_OF_THE_SEA
    );

    public static final List<ResourceKey<Enchantment>> maceEnchantments = List.of(
            Enchantments.DENSITY,
            Enchantments.BREACH,
            Enchantments.WIND_BURST
    );

    public static final List<ResourceKey<Enchantment>> spearEnchantments = List.of(
            Enchantments.LUNGE
    );

}
