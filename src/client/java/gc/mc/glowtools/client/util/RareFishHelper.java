package gc.mc.glowtools.client.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.item.DyeColor;

import java.util.Set;
import java.util.stream.Collectors;

public class RareFishHelper {

    private static final Set<Integer> COMMON_IDS = TropicalFish.COMMON_VARIANTS.stream()
            .map(TropicalFish.Variant::getPackedId)
            .collect(Collectors.toSet());

    public static boolean isRareTropicalFish(int variantId) {
        return !COMMON_IDS.contains(variantId);
    }

    public static Component getTropicalFishVariantName(int variantId) {
        DyeColor baseColor = TropicalFish.getBaseColor(variantId);
        DyeColor patternColor = TropicalFish.getPatternColor(variantId);
        TropicalFish.Pattern variety = TropicalFish.getPattern(variantId);
        Component baseColorName = Component.translatable("color.minecraft." + baseColor.name().toLowerCase());
        Component patternColorName = Component.translatable("color.minecraft." + patternColor.name().toLowerCase());
        Component varietyName = variety.displayName();
        return Component.literal(baseColorName.getString() + "-" + patternColorName.getString() + "-" + varietyName.getString());
    }

}