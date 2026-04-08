package gc.mc.glowtools.client.mixin;

import net.minecraft.entity.passive.TropicalFishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TropicalFishEntity.class)
public interface TropicalFishEntityAccessor {

    @Invoker("getTropicalFishVariant")
    int invokeGetTropicalFishVariant();

}