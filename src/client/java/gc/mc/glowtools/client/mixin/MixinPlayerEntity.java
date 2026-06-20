package gc.mc.glowtools.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gc.mc.glowtools.client.compat.TweakerooCompatibility;
import gc.mc.glowtools.client.config.Configs;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
            if (TweakerooCompatibility.isTweakerooFakeSneakEnabled()) {
                return true;
            }
        }
        return original.call(instance);
    }

    @Unique
    private boolean isStandable(LocalPlayer player, BlockPos pos) {
        double playerY = player.getY();
        BlockPos basePos = new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ());

        for (int yOffset = -1; yOffset >= -2; yOffset--) {
            BlockPos checkPos = basePos.offset(0, yOffset, 0);
            VoxelShape shape = player.level().getBlockState(checkPos).getCollisionShape(player.level(), checkPos);

            if (!shape.isEmpty()) {
                double blockTopY = checkPos.getY() + shape.max(Direction.Axis.Y);
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