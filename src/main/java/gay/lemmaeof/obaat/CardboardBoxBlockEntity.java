package gay.lemmaeof.obaat;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CardboardBoxBlockEntity extends BlockEntity {
	private final SimpleContainer inv = new SimpleContainer(9) {
		@Override
		public int getMaxStackSize() {
			return 1;
		}
	};

	public CardboardBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(OneBoxAtATime.CARDBOARD_BOX_BE, blockPos, blockState);
	}

	public SimpleContainer getInv() {
		return inv;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("Items", inv.createTag());
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		inv.fromTag(tag.getList("Items", Tag.TAG_COMPOUND));
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
