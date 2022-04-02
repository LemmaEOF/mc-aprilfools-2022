package gay.lemmaeof.obaat;

import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;

import net.fabricmc.loader.api.ModContainer;

public class OneBoxAtATime implements ModInitializer {
	public static final String MODID = "oneboxatatime";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static Block CARDBOARD_BOX;

	public static Item CARDBOARD_BOX_ITEM;

	public static BlockEntityType<CardboardBoxBlockEntity> CARDBOARD_BOX_BE;

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("lemmaeBw lemmaeOp");

		CARDBOARD_BOX = Registry.register(Registry.BLOCK, new ResourceLocation(MODID, "cardboard_box"), new CardboardBoxBlock(QuiltBlockSettings.of(Material.WOOD).strength(2f)));
		CARDBOARD_BOX_ITEM = Registry.register(Registry.ITEM, new ResourceLocation(MODID, "cardboard_box"), new BlockItem(CARDBOARD_BOX, new Item.Properties().stacksTo(1)));
		CARDBOARD_BOX_BE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(MODID, "cardboard_box"), BlockEntityType.Builder.of(CardboardBoxBlockEntity::new, CARDBOARD_BOX).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "oneboxatatime:cardboard_box")));
	}
}
