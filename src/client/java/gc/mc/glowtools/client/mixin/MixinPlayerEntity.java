package gc.mc.glowtools.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import gc.mc.glowtools.client.config.Configs;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class MixinPlayerEntity {

    @Shadow protected abstract boolean isStayingOnGroundSurface();

    @WrapOperation(
            method = "maybeBackOffFromEdge",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/entity/player/Player;isStayingOnGroundSurface()Z",
                     ordinal = 0)
    )
    private boolean glowtools_enhancedFakeSneak(Player instance, Operation<Boolean> original) {
        if (((Object) this) instanceof LocalPlayer clientPlayer) {
            if (Configs.Others.TWEAKEROO_FAKE_SNEAK_MODIFY.getBooleanValue()) {
                if (clientPlayer.isShiftKeyDown()) return true;
                BlockPos landingPos = new BlockPos(clientPlayer.getBlockX(), clientPlayer.getBlockY(), clientPlayer.getBlockZ()).below(2);
                return !isStandable(clientPlayer, landingPos);
            }
            if (FeatureToggle.TWEAK_FAKE_SNEAKING.getBooleanValue()) return true;
        }
        return original.call(instance);
    }

    @Unique
    private boolean isStandable(LocalPlayer player, BlockPos pos) {
        VoxelShape shape = player.level().getBlockState(pos).getCollisionShape(player.level(), pos);
        return !shape.isEmpty();
    }

}