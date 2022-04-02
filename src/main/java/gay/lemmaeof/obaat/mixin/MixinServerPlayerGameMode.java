package gay.lemmaeof.obaat.mixin;

import gay.lemmaeof.obaat.CardboardBoxBlockEntity;
import gay.lemmaeof.obaat.hook.BoxHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.entity.BlockEntity;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGameMode {
	@Shadow protected ServerLevel level;

	@Shadow @Final protected ServerPlayer player;

	@Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
	private void hookStoreBoxData(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		BlockEntity be = level.getBlockEntity(pos);

		if (be instanceof CardboardBoxBlockEntity box) {
			((BoxHolder) this.player).obaat$setBoxData(box.saveWithoutMetadata());
		}
	}
}
