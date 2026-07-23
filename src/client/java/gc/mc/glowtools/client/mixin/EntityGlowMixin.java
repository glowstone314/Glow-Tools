package gc.mc.glowtools.client.mixin;

import gc.mc.glowtools.client.config.Configs;
import gc.mc.glowtools.client.logic.EntityAlertHandler;
import gc.mc.glowtools.client.logic.TropicalFishHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityGlowMixin {

    @Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            return;
        }

        Entity entity = (Entity) (Object) this;

        if (!entity.level().isClientSide()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        boolean shouldGlow = EntityAlertHandler.isTargetEntity(entity) &&
                Configs.EntityAlerts.ENABLE_GLOW.getBooleanValue();

        if (!shouldGlow && TropicalFishHandler.isTargetFish(entity) &&
                Configs.TropicalFish.ENABLE_GLOW.getBooleanValue()) {
            shouldGlow = true;
        }

        if (shouldGlow) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getTeamColor", at = @At("RETURN"), cancellable = true)
    private void onGetTeamColor(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;

        if (!entity.level().isClientSide()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        if (EntityAlertHandler.isTargetEntity(entity) && Configs.EntityAlerts.ENABLE_GLOW.getBooleanValue()) {
            cir.setReturnValue(Configs.EntityAlerts.GLOW_COLOR.getIntegerValue());
            return;
        }
        if (TropicalFishHandler.isTargetFish(entity) && Configs.TropicalFish.ENABLE_GLOW.getBooleanValue()) {
            cir.setReturnValue(Configs.TropicalFish.GLOW_COLOR.getIntegerValue());
        }
    }

}