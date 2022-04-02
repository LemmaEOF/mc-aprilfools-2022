package gay.lemmaeof.obaat.mixin;

import gay.lemmaeof.obaat.OneBoxAtATime;
import gay.lemmaeof.obaat.hook.BoxHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
public class MixinPlayer {
	@Inject(method = "getMainHandItem", at = @At("RETURN"), cancellable = true)
	private void hookHeldItem(CallbackInfoReturnable<ItemStack> info) {
		ItemStack ret = info.getReturnValue();
		if (ret.getItem() == OneBoxAtATime.CARDBOARD_BOX_ITEM && this instanceof BoxHolder holder) {
			if (holder.obaat$getBoxData() != null) {
				ret.getOrCreateTag().put("BlockEntityTag", holder.obaat$getBoxData());
			}
		}
	}
}
