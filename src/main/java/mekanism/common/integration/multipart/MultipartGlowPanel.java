package mekanism.common.integration.multipart;

import mcmultipart.api.container.IPartInfo;
import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import mekanism.common.MekanismBlock;
import mekanism.common.block.BlockGlowPanel;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.tile.TileEntityGlowPanel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MultipartGlowPanel implements IMultipart {

    @Override
    public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
        return EnumFaceSlot.values()[facing.ordinal()];
    }

    @Override
    public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state) {
        return EnumFaceSlot.values()[state.getValue(BlockStateHelper.facingProperty).ordinal()];
    }

    @Override
    public void onPartPlacedBy(IPartInfo part, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = part.getTile().getTileEntity();
        if (tile instanceof TileEntityGlowPanel) {
            EnumFacing facing = EnumFacing.VALUES[((EnumFaceSlot) part.getSlot()).ordinal()];
            //EnumColor col = EnumColor.DYES[stack.getItemDamage()];
            TileEntityGlowPanel glowPanel = (TileEntityGlowPanel) tile;
            glowPanel.setOrientation(facing);
            //TODO: Is this needed or can it work fine due to block. Look at once multipart is reimplemented
            //glowPanel.setColor(col);
        }
    }

    @Override
    public void onPartRemoved(IPartInfo part, IPartInfo otherPart) {
        TileEntity tile = part.getTile().getTileEntity();
        if (tile instanceof TileEntityGlowPanel && !BlockGlowPanel.canStay(part.getPartWorld(), part.getPartPos())) {
            BlockPos pos = part.getPartPos();
            //TileEntityGlowPanel glowPanel = (TileEntityGlowPanel) tile;
            //TODO: Get correct color if it is unaware of it the tile will have to store it
            ItemStack stack = MekanismBlock.BLACK_GLOW_PANEL.getItemStack();
            Block.spawnAsEntity(part.getActualWorld(), pos, stack);
            part.remove();
        }
    }

    @Override
    public void onPartHarvested(IPartInfo part, EntityPlayer player) {
        TileEntity tile = part.getTile().getTileEntity();
        if (tile instanceof TileEntityGlowPanel) {
            IBlockState partState = part.getState();
            partState.getBlock().removedByPlayer(partState, part.getPartWorld(), part.getContainer().getPartPos(), player, true);
        }
        IMultipart.super.onPartHarvested(part, player);
    }

    @Override
    public Block getBlock() {
        //TODO
        return MekanismBlock.BLACK_GLOW_PANEL.getBlock();
    }
}