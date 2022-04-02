package gay.lemmaeof.obaat.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow @Nullable public LocalPlayer player;

	@Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;drop(Z)Z"))
	private void throwItem(CallbackInfo info) {
		this.player.throwCarried();
	}
}
