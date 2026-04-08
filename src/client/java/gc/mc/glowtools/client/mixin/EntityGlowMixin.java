package gc.mc.glowtools.client.mixin;

import gc.mc.glowtools.client.config.Configs;
import gc.mc.glowtools.client.logic.EntityAlertHandler;
import gc.mc.glowtools.client.logic.TropicalFishHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TropicalFishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityGlowMixin {

    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (Configs.EntityAlerts.ENABLE_ALERTS.getBooleanValue() && Configs.EntityAlerts.ENABLE_GLOW.getBooleanValue()) {
            if (EntityAlertHandler.isTargetEntity(entity)) {
                cir.setReturnValue(true);
            }

        }
        if (Configs.TropicalFish.ENABLE_TROPICAL_FISH_ALERTS.getBooleanValue() && Configs.TropicalFish.ENABLE_GLOW.getBooleanValue()) {
            if (entity instanceof TropicalFishEntity fish) {
                if (TropicalFishHandler.isTargetFish(fish)) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
    private void onGetTeamColorValue(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;
        if (Configs.EntityAlerts.ENABLE_ALERTS.getBooleanValue() && Configs.EntityAlerts.ENABLE_GLOW.getBooleanValue()) {
            if (EntityAlertHandler.isTargetEntity(entity)) {
                cir.setReturnValue(Configs.EntityAlerts.GLOW_COLOR.getIntegerValue());
            }
        }
        if (Configs.TropicalFish.ENABLE_TROPICAL_FISH_ALERTS.getBooleanValue() && Configs.TropicalFish.ENABLE_GLOW.getBooleanValue()) {
            if (entity instanceof TropicalFishEntity fish) {
                if (TropicalFishHandler.isTargetFish(fish)) {
                    cir.setReturnValue(Configs.TropicalFish.GLOW_COLOR.getIntegerValue());
                }
            }
        }
    }

}