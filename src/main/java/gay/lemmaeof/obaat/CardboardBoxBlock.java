package gay.lemmaeof.obaat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.GenericItemBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CardboardBoxBlock extends Block implements EntityBlock {
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	public static final VoxelShape OPEN_SHAPE = Shapes.or(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 0, 0, 16, 16, 1),
			Block.box(0, 0, 0, 1, 16, 16),
			Block.box(15, 0, 0, 16, 16, 16),
			Block.box(0, 0, 15, 16, 16, 16)
	);

	public CardboardBoxBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, true));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return state.getValue(OPEN)? OPEN_SHAPE : Shapes.block();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(OPEN);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (!level.isClientSide) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof CardboardBoxBlockEntity box && player.getCarried() == LivingEntity.Carried.NONE && interactionHand == InteractionHand.MAIN_HAND) {
				if (state.getValue(OPEN)) {
					BlockPos.MutableBlockPos mutable = pos.mutable();
					List<BlockPos> toBreak = new ArrayList<>();
					for (int i = 0; i < 9; i++) {
						BlockState grabState = level.getBlockState(mutable.move(Direction.UP));
						if (grabState.isAir()){
							box.getInv().setItem(i, ItemStack.EMPTY);
							continue;
						}
						Item item;
						if (grabState.getBlock() instanceof GenericItemBlock) {
							item = GenericItemBlock.itemFromGenericBlock(grabState);
						} else {
							item = grabState.getBlock().asItem();
						}
						box.getInv().setItem(i, new ItemStack(item));
						toBreak.add(mutable.immutable());
					}
					level.playSound(null, pos, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 1f, 1f);
					box.setChanged();
					level.setBlockAndUpdate(pos, state.setValue(OPEN, false));
					Collections.reverse(toBreak);
					for (BlockPos breakPos : toBreak) { level.removeBlock(breakPos, true); }
					return InteractionResult.SUCCESS;
				} else {
					BlockPos.MutableBlockPos mutable = pos.mutable();
					for (int i = 0; i < 9; i++) {
						BlockState placeState = level.getBlockState(mutable.move(Direction.UP));
						ItemStack stack = box.getInv().getItem(i);
						if (!stack.isEmpty()) {
							if (placeState.isAir()) {
								BlockState toPlace;
								if (stack.getItem() instanceof BlockItem bi) {
									 toPlace = bi.getBlock().defaultBlockState();
								} else {
									toPlace = GenericItemBlock.genericBlockFromItem(stack.getItem());
								}
								if (toPlace != null) {
									level.setBlockAndUpdate(mutable, toPlace);
								}
							} else {
								player.drop(stack, true);
							}
						}
					}
					level.playSound(null, pos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 1f, 1f);
					box.setChanged();
					level.setBlockAndUpdate(pos, state.setValue(OPEN, true));
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.FAIL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CardboardBoxBlockEntity(pos, state);
	}
}
