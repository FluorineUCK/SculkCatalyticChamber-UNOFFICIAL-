package net.dragonegg.sculkcatalyticchamber.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.dragonegg.sculkcatalyticchamber.SculkCatalyticChamber;
import net.dragonegg.sculkcatalyticchamber.content.shrieker.MechanicalShriekerBlock;
import net.dragonegg.sculkcatalyticchamber.content.shrieker.MechanicalShriekerBlockEntity;
import net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberBottomBlock;
import net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberMiddleBlock;

import net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberTopBlock;
import net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberBottomBlockEntity;
import net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberMiddleBlockEntity;
import net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberTopBlockEntity;
import net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.PushReaction;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static net.dragonegg.sculkcatalyticchamber.SculkCatalyticChamber.MODID;

public class BlockRegistry {

    private static final CreateRegistrate REGISTRATE = SculkCatalyticChamber.registrate();

    public static final RegistryEntry<CreativeModeTab> TAB = REGISTRATE.defaultCreativeTab(MODID,
            c -> c.icon(() -> new ItemStack(BlockRegistry.CHAMBER_BOTTOM_BLOCK.get()))
    ).register();

    public static final BlockEntry<ChamberTopBlock> CHAMBER_TOP_BLOCK = REGISTRATE
            .block("chamber_top", ChamberTopBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noLootTable().pushReaction(PushReaction.BLOCK))
            .transform(TagGen.pickaxeOnly())
            .register();

    public static final BlockEntityEntry<ChamberTopBlockEntity> CHAMBER_TOP_BLOCK_TILE = REGISTRATE
            .blockEntity("chamber_top", ChamberTopBlockEntity::new)
            .validBlocks(CHAMBER_TOP_BLOCK)
            .register();

    public static final BlockEntry<ChamberMiddleBlock> CHAMBER_MIDDLE_BLOCK = REGISTRATE
            .block("chamber_middle", ChamberMiddleBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noLootTable().pushReaction(PushReaction.BLOCK))
            .transform(TagGen.pickaxeOnly())
            .register();

    public static final BlockEntityEntry<ChamberMiddleBlockEntity> CHAMBER_MIDDLE_BLOCK_TILE = REGISTRATE
            .blockEntity("chamber_middle", ChamberMiddleBlockEntity::new)
            .validBlocks(CHAMBER_MIDDLE_BLOCK)
            .register();

    public static final BlockEntry<ChamberBottomBlock> CHAMBER_BOTTOM_BLOCK = REGISTRATE
            .block("chamber", ChamberBottomBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.pickaxeOnly())
            .item(ChamberItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntityEntry<ChamberBottomBlockEntity> CHAMBER_BOTTOM_BLOCK_TILE = REGISTRATE
            .blockEntity("chamber", ChamberBottomBlockEntity::new)
            .validBlocks(CHAMBER_BOTTOM_BLOCK)
            .register();

    public static final BlockEntry<MechanicalShriekerBlock> MECHANICAL_SHRIEKER_BLOCK = REGISTRATE
            .block("mechanical_shrieker", MechanicalShriekerBlock::new)
            .initialProperties(() -> Blocks.SCULK_SHRIEKER)
            .transform(TagGen.axeOrPickaxe())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntityEntry<MechanicalShriekerBlockEntity> MECHANICAL_SHRIEKER_BLOCK_TILE = REGISTRATE
            .blockEntity("mechanical_shrieker", MechanicalShriekerBlockEntity::new)
            .validBlocks(MECHANICAL_SHRIEKER_BLOCK)
            .register();


}
