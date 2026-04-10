package gc.mc.glowtools.client.logic;

import gc.mc.glowtools.client.config.Configs;
import gc.mc.glowtools.client.config.EnchantmentsFilterMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.List;

public class TransferEnchantedHandler {

    private static boolean isTarget(net.minecraft.item.ItemStack stack) {
        if (stack.isEmpty()) return false;

        boolean isBook = stack.isOf(Items.ENCHANTED_BOOK);
        ItemEnchantmentsComponent enchants = stack.get(isBook
                ? DataComponentTypes.STORED_ENCHANTMENTS
                : DataComponentTypes.ENCHANTMENTS);

        if (enchants == null || enchants.isEmpty()) return false;

        if (!Configs.TransferEnchanted.TRANSFER_ALL.getBooleanValue()) {
            boolean isBow = stack.isOf(Items.BOW);
            boolean isFishingRod = stack.isOf(Items.FISHING_ROD);
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

        for (var entry : enchants.getEnchantmentEntries()) {
            RegistryEntry<Enchantment> enchantEntry = entry.getKey();
            Enchantment enchantment = enchantEntry.value();
            int level = entry.getIntValue();

            if (Configs.TransferEnchanted.EXCLUDE_CURSED.getBooleanValue() && enchantEntry.isIn(EnchantmentTags.CURSE)) {
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

            if (enchantEntry.matchesKey(Enchantments.SWIFT_SNEAK)) {
                hasNonCurse = true;
            }
        }
        if (invert) hasNonCurse = !hasNonCurse;
        return hasNonCurse;
    }

    public static boolean runTransfer() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !(client.currentScreen instanceof HandledScreen<?>)) return false;

        ScreenHandler handler = client.player.currentScreenHandler;
        int containerSize = handler.slots.size() - 36;

        for (int i = 0; i < containerSize; i++) {
            Slot slot = handler.getSlot(i);
            if (slot.hasStack() && isTarget(slot.getStack())) {
                transferSlot(i);
            }
        }
        return true;
    }

    private static void transferSlot(int slotIndex) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.interactionManager == null || client.player == null) return;

        client.interactionManager.clickSlot(
                client.player.currentScreenHandler.syncId,
                slotIndex,
                0,
                SlotActionType.QUICK_MOVE,
                client.player
        );
    }

    public static boolean suitableEnchant(EnchantmentsFilterMode mode, RegistryEntry<Enchantment> enchantEntry) {
        return switch (mode) {
            case ALL -> true;
            case SWORD -> matches(swordEnchantments, enchantEntry);
            case ARMOR -> matches(armorEnchantments, enchantEntry);
            case SPECIAL_ARMOR -> {
                if (enchantEntry.matchesKey(Enchantments.SWIFT_SNEAK))
                    yield true;
                yield matches(armorSpecialtyEnchantments, enchantEntry);
            }
            case BOW -> matches(bowEnchantments, enchantEntry);
            case TOOL -> matches(toolEnchantments, enchantEntry);
            case CROSSBOW -> matches(crossbowEnchantments, enchantEntry);
            case TRIDENT -> matches(tridentEnchantments, enchantEntry);
            case FISHING -> matches(fishingEnchantments, enchantEntry);
            case MACE -> matches(maceEnchantments, enchantEntry);
        };
    }

    public static boolean matches(List<RegistryKey<Enchantment>> enchantments, RegistryEntry<Enchantment> enchantEntry) {
        for (RegistryKey<Enchantment> enchantment : enchantments) {
            if (enchantEntry.matchesKey(enchantment)) {
                return true;
            }
        }
        return false;
    }

    public static final List<RegistryKey<Enchantment>> swordEnchantments = List.of(
            Enchantments.SHARPNESS,
            Enchantments.SMITE,
            Enchantments.BANE_OF_ARTHROPODS,
            Enchantments.FIRE_ASPECT,
            Enchantments.KNOCKBACK,
            Enchantments.LOOTING,
            Enchantments.SWEEPING_EDGE
    );

    public static final List<RegistryKey<Enchantment>> armorEnchantments = List.of(
            Enchantments.PROTECTION,
            Enchantments.FIRE_PROTECTION,
            Enchantments.BLAST_PROTECTION,
            Enchantments.PROJECTILE_PROTECTION
    );

    public static final List<RegistryKey<Enchantment>> armorSpecialtyEnchantments = List.of(
            Enchantments.RESPIRATION,
            Enchantments.AQUA_AFFINITY,
            Enchantments.THORNS,
            Enchantments.DEPTH_STRIDER,
            Enchantments.FROST_WALKER,
            Enchantments.SOUL_SPEED,
            Enchantments.FEATHER_FALLING
    );

    public static final List<RegistryKey<Enchantment>> bowEnchantments = List.of(
            Enchantments.POWER,
            Enchantments.PUNCH,
            Enchantments.FLAME,
            Enchantments.INFINITY
    );

    public static final List<RegistryKey<Enchantment>> toolEnchantments = List.of(
            Enchantments.EFFICIENCY,
            Enchantments.SILK_TOUCH,
            Enchantments.UNBREAKING,
            Enchantments.FORTUNE,
            Enchantments.MENDING
    );

    public static final List<RegistryKey<Enchantment>> crossbowEnchantments = List.of(
            Enchantments.QUICK_CHARGE,
            Enchantments.MULTISHOT,
            Enchantments.PIERCING
    );

    public static final List<RegistryKey<Enchantment>> tridentEnchantments = List.of(
            Enchantments.RIPTIDE,
            Enchantments.IMPALING,
            Enchantments.CHANNELING,
            Enchantments.LOYALTY
    );

    public static final List<RegistryKey<Enchantment>> fishingEnchantments = List.of(
            Enchantments.LURE,
            Enchantments.LUCK_OF_THE_SEA
    );

    public static final List<RegistryKey<Enchantment>> maceEnchantments = List.of(
            Enchantments.DENSITY,
            Enchantments.BREACH,
            Enchantments.WIND_BURST
    );

}
