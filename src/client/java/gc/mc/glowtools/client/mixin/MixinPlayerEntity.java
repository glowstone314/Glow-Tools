package gc.mc.glowtools.client.mixin;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
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
            if (FeatureToggle.TWEAK_FAKE_SNEAKING.getBooleanValue()) return true;
        }
        return this.clipAtLedge();
    }

    @Unique
    private boolean isStandable(ClientPlayerEntity player, BlockPos pos) {
        VoxelShape shape = player.getWorld().getBlockState(pos).getCollisionShape(player.getWorld(), pos);
        return !shape.isEmpty();
    }

}