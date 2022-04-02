package gay.lemmaeof.obaat.hook;

import net.minecraft.nbt.CompoundTag;

public interface BoxHolder {
	void obaat$setBoxData(CompoundTag tag);
	CompoundTag obaat$getBoxData();
	void obaat$clearBoxData();
}
