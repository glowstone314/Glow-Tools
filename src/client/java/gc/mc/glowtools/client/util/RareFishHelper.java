package gc.mc.glowtools.client.util;

import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

import java.util.Set;
import java.util.stream.Collectors;

public class RareFishHelper {

    private static final Set<Integer> COMMON_IDS = TropicalFishEntity.COMMON_VARIANTS.stream()
            .map(TropicalFishEntity.Variant::getId)
            .collect(Collectors.toSet());

    public static boolean isRareTropicalFish(int variantId) {
        return !COMMON_IDS.contains(variantId);
    }

    public static Text getTropicalFishVariantName(int variantId) {
        DyeColor baseColor = TropicalFishEntity.getBaseColor(variantId);
        DyeColor patternColor = TropicalFishEntity.getPatternColor(variantId);
        TropicalFishEntity.Pattern variety = TropicalFishEntity.getVariety(variantId);
        Text baseColorName = Text.translatable("color.minecraft." + baseColor.name().toLowerCase());
        Text patternColorName = Text.translatable("color.minecraft." + patternColor.name().toLowerCase());
        Text varietyName = variety.getText();
        return Text.literal(baseColorName.getString() + "-" + patternColorName.getString() + "-" + varietyName.getString());
    }

}