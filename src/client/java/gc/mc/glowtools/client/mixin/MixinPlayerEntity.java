package gc.mc.glowtools.client.mixin;

import gc.mc.glowtools.client.compat.TweakerooCompatibility;
import gc.mc.glowtools.client.config.Configs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @Shadow protected abstract boolean clipAtLedge();

    @Redirect(method = "adjustMovementForSneaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;clipAtLedge()Z"))
    private boolean glowtools_enhancedFakeSneak(PlayerEntity entity) {
        if (((Object) this) instanceof ClientPlayerEntity clientPlayer) {
            if (Configs.Others.TWEAKEROO_FAKE_SNEAK_MODIFY.getBooleanValue()) {
                if (clientPlayer.isSneaking()) return true;
                BlockPos landingPos = clientPlayer.getBlockPos().down(2);
                return !isStandable(clientPlayer, landingPos);
            }
            if (TweakerooCompatibility.isTweakerooFakeSneakEnabled()) {
                return true;
            }
        }
        return this.clipAtLedge();
    }

    @Unique
    private boolean isStandable(ClientPlayerEntity player, BlockPos pos) {
        double playerY = player.getY();
        BlockPos basePos = player.getBlockPos();

        for (int yOffset = -1; yOffset >= -2; yOffset--) {
            BlockPos checkPos = basePos.add(0, yOffset, 0);
            VoxelShape shape = player.getEntityWorld().getBlockState(checkPos).getCollisionShape(player.getEntityWorld(), checkPos);

            if (!shape.isEmpty()) {
                double blockTopY = checkPos.getY() + shape.getMax(net.minecraft.util.math.Direction.Axis.Y);
                double diff = playerY - blockTopY;
                if (diff <= 1.25) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

}