package gay.lemmaeof.obaat.mixin;

import gay.lemmaeof.obaat.CardboardBoxBlock;
import gay.lemmaeof.obaat.hook.BoxHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer implements BoxHolder {
	private CompoundTag boxTag;

	@Inject(method = "tryThrowBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void hookThrow(Vec3 throwAngle, CallbackInfo info, BlockState state, FallingBlockEntity thrown) {
		if (state.getBlock() instanceof CardboardBoxBlock && obaat$getBoxData() != null) {
			thrown.blockData = obaat$getBoxData();
			obaat$clearBoxData();
		}
	}

	@Inject(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setCarriedBlock(Lnet/minecraft/world/level/block/state/BlockState;)V"))
	private void hookRestore(ServerPlayer player, boolean connecting, CallbackInfo info) {
		this.obaat$setBoxData(((BoxHolder) player).obaat$getBoxData());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void hookLoad(CompoundTag tag, CallbackInfo info) {
		if (tag.contains("BoxData", Tag.TAG_COMPOUND)) {
			obaat$setBoxData(tag.getCompound("BoxData"));
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void hookSave(CompoundTag tag, CallbackInfo info) {
		if (obaat$getBoxData() != null) {
			tag.put("BoxData", obaat$getBoxData());
		}
	}

	@Override
	public void obaat$setBoxData(CompoundTag tag) {
		this.boxTag = tag;
	}

	@Override
	public CompoundTag obaat$getBoxData() {
		return boxTag;
	}

	@Override
	public void obaat$clearBoxData() {
		this.boxTag = null;
	}
}
