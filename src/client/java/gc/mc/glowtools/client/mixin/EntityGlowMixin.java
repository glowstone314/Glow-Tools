package gc.mc.glowtools.client.mixin;

import gc.mc.glowtools.client.config.Configs;
import gc.mc.glowtools.client.logic.EntityAlertHandler;
import gc.mc.glowtools.client.logic.TropicalFishHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static gc.mc.glowtools.client.util.Tools.addDefaultPrefix;

@Mixin(Entity.class)
public abstract class EntityGlowMixin {

    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        boolean b = true;
        Entity entity = (Entity) (Object) this;
        if (Configs.EntityAlerts.ENABLE_GLOW.getBooleanValue()) {
            if (EntityAlertHandler.isTargetEntity(entity)) {
                if ((Configs.EntityAlerts.ENABLE_WANDERING_TRADER.getBooleanValue() && entity instanceof net.minecraft.entity.passive.WanderingTraderEntity) ||
                        (Configs.EntityAlerts.ENABLE_PILLAGER.getBooleanValue() && entity instanceof net.minecraft.entity.mob.PillagerEntity) ||
                        (Configs.EntityAlerts.ENABLE_FOX_EMERALD.getBooleanValue() && entity instanceof net.minecraft.entity.passive.FoxEntity) ||
                        (Configs.EntityAlerts.ENABLE_DROWNED_SNIFFER.getBooleanValue() && entity instanceof net.minecraft.entity.mob.DrownedEntity) ||
                        (Configs.EntityAlerts.ENABLE_CUSTOM_ENTITY.getBooleanValue() && addDefaultPrefix(Configs.EntityAlerts.CUSTOM_ENTITY_ID.getStringValue()).equals(Registries.ENTITY_TYPE.getId(entity.getType()).toString()))) {
                    cir.setReturnValue(true);
                    b = false;
                }
            }
        }
        if (Configs.TropicalFish.ENABLE_TROPICAL_FISH_ALERTS.getBooleanValue() && Configs.TropicalFish.ENABLE_GLOW.getBooleanValue()) {
            if (entity instanceof TropicalFishEntity fish) {
                if (TropicalFishHandler.isTargetFish(fish)) {
                    cir.setReturnValue(true);
                    b = false;
                }
            }
        }
        if (b) cir.setReturnValue(false);
    }

    @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
    private void onGetTeamColorValue(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;
        if (Configs.EntityAlerts.ENABLE_GLOW.getBooleanValue()) {
            if (EntityAlertHandler.isTargetEntity(entity)) {
                if ((Configs.EntityAlerts.ENABLE_WANDERING_TRADER.getBooleanValue() && entity instanceof net.minecraft.entity.passive.WanderingTraderEntity) ||
                        (Configs.EntityAlerts.ENABLE_PILLAGER.getBooleanValue() && entity instanceof net.minecraft.entity.mob.PillagerEntity) ||
                        (Configs.EntityAlerts.ENABLE_FOX_EMERALD.getBooleanValue() && entity instanceof net.minecraft.entity.passive.FoxEntity) ||
                        (Configs.EntityAlerts.ENABLE_DROWNED_SNIFFER.getBooleanValue() && entity instanceof net.minecraft.entity.mob.DrownedEntity) ||
                        (Configs.EntityAlerts.ENABLE_CUSTOM_ENTITY.getBooleanValue() && addDefaultPrefix(Configs.EntityAlerts.CUSTOM_ENTITY_ID.getStringValue()).equals(Registries.ENTITY_TYPE.getId(entity.getType()).toString()))) {
                    cir.setReturnValue(Configs.EntityAlerts.GLOW_COLOR.getIntegerValue());
                }
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