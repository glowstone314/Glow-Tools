package gc.mc.glowtools.client.mixin;

import gc.mc.glowtools.client.config.Configs;
import gc.mc.glowtools.client.logic.EntityAlertHandler;
import gc.mc.glowtools.client.logic.TropicalFishHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static gc.mc.glowtools.client.util.Tools.addDefaultPrefix;

@Mixin(Entity.class)
public abstract class EntityGlowMixin {

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        boolean b = true;
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;
        Entity entity = (Entity) (Object) this;
        if (Configs.EntityAlerts.ENABLE_ALERTS.getBooleanValue() && Configs.EntityAlerts.ENABLE_GLOW.getBooleanValue()) {
            if (EntityAlertHandler.isTargetEntity(entity)) {
                Identifier identifier = Identifier.parse(addDefaultPrefix(Configs.EntityAlerts.CUSTOM_ENTITY_ID.getStringValue()));
                EntityType<?> entityType = client.level.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).getValue(identifier);
                if ((entityType != null) && (
                        (Configs.EntityAlerts.ENABLE_WANDERING_TRADER.getBooleanValue() && entity instanceof WanderingTrader) ||
                                (Configs.EntityAlerts.ENABLE_PILLAGER.getBooleanValue() && entity instanceof Pillager) ||
                                (Configs.EntityAlerts.ENABLE_FOX_EMERALD.getBooleanValue() && entity instanceof Fox) ||
                                (Configs.EntityAlerts.ENABLE_DROWNED_SNIFFER.getBooleanValue() && entity instanceof Drowned) ||
                                (Configs.EntityAlerts.ENABLE_CUSTOM_ENTITY.getBooleanValue() &&
                                        Objects.equals(entityType, entity.getType())))
                ) {
                    cir.setReturnValue(true);
                    b = false;
                }
            }
        }
        if (Configs.TropicalFish.ENABLE_TROPICAL_FISH_ALERTS.getBooleanValue() && Configs.TropicalFish.ENABLE_GLOW.getBooleanValue()) {
            if (entity instanceof TropicalFish fish) {
                if (TropicalFishHandler.isTargetFish(fish)) {
                    cir.setReturnValue(true);
                    b = false;
                }
            }
        }
        if (b) cir.setReturnValue(false);
    }

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void onGetTeamColor(CallbackInfoReturnable<Integer> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;
        Entity entity = (Entity) (Object) this;
        if (Configs.EntityAlerts.ENABLE_ALERTS.getBooleanValue() && Configs.EntityAlerts.ENABLE_GLOW.getBooleanValue()) {
            if (EntityAlertHandler.isTargetEntity(entity)) {
                Identifier identifier = Identifier.parse(addDefaultPrefix(Configs.EntityAlerts.CUSTOM_ENTITY_ID.getStringValue()));
                EntityType<?> entityType = client.level.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).getValue(identifier);
                if ((entityType != null) && (
                        (Configs.EntityAlerts.ENABLE_WANDERING_TRADER.getBooleanValue() && entity instanceof WanderingTrader) ||
                                (Configs.EntityAlerts.ENABLE_PILLAGER.getBooleanValue() && entity instanceof Pillager) ||
                                (Configs.EntityAlerts.ENABLE_FOX_EMERALD.getBooleanValue() && entity instanceof Fox) ||
                                (Configs.EntityAlerts.ENABLE_DROWNED_SNIFFER.getBooleanValue() && entity instanceof Drowned) ||
                                (Configs.EntityAlerts.ENABLE_CUSTOM_ENTITY.getBooleanValue() &&
                                        Objects.equals(entityType, entity.getType())))
                ) {
                    cir.setReturnValue(Configs.EntityAlerts.GLOW_COLOR.getIntegerValue());
                }
            }
        }
        if (Configs.TropicalFish.ENABLE_TROPICAL_FISH_ALERTS.getBooleanValue() && Configs.TropicalFish.ENABLE_GLOW.getBooleanValue()) {
            if (entity instanceof TropicalFish fish) {
                if (TropicalFishHandler.isTargetFish(fish)) {
                    cir.setReturnValue(Configs.TropicalFish.GLOW_COLOR.getIntegerValue());
                }
            }
        }
    }

}