package gc.mc.glowtools.client.mixin;

import net.minecraft.world.entity.animal.fish.TropicalFish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TropicalFish.class)
public interface TropicalFishAccessor {

    @Invoker("getPackedVariant")
    int invokeGetPackedVariant();

}