package net.regonold.geometrabanks.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import net.regonold.geometrabanks.block.bank.BankManager;
import net.regonold.geometrabanks.block.bank.SyncBalancePacket;
import net.regonold.geometrabanks.block.entity.ATMBlockEntity;
import net.regonold.geometrabanks.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class ATMBlock extends BaseEntityBlock {
    public static final MapCodec<ATMBlock> CODEC = simpleCodec(ATMBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape SHAPE = Block.box(0, 0, 1, 15, 16, 15);
    public static final VoxelShape TOP_SHAPE = Block.box(1, 0, 2, 15, 15, 11);

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public ServerPlayer interactor;

    public ATMBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return SHAPE;
        } else {
            return TOP_SHAPE;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // Include both FACING and HALF properties in the block state definition
        builder.add(FACING, HALF);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        // Place the upper half above the lower one.
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ATMBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos abovePos = pos.above();

        // Check if the block above is within build limits and can be replaced.
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(abovePos).canBeReplaced(context)) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            // If the lower half is removed, remove the upper half.
            BlockPos abovePos = pos.above();
            if (level.getBlockState(abovePos).getBlock() == this) {
                level.removeBlock(abovePos, false);
            }
        } else {
            // If the upper half is removed, remove the lower half.
            BlockPos belowPos = pos.below();
            if (level.getBlockState(belowPos).getBlock() == this) {
                level.removeBlock(belowPos, false);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            // The upper half can only survive if the block below is the lower half.
            BlockState belowState = level.getBlockState(pos.below());
            return belowState.getBlock() == this && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
        } else {
            // The lower half must check that the block above is empty.
            BlockPos abovePos = pos.above();
            return pos.getY() < level.getMaxBuildHeight() - 1 && level.isEmptyBlock(abovePos);
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.ATM_BE.get() ? (BlockEntityTicker<T>) ATMBlockEntity::tick : null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // Determine the position of the block entity that should be used.
        BlockPos basePos = pos;
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            basePos = pos.below();
        }

        if (level.getBlockEntity(basePos) instanceof ATMBlockEntity be) {
            if (!level.isClientSide && !be.opened) {
                ((ServerPlayer) player).openMenu(new SimpleMenuProvider(be, Component.literal("Atm")), basePos);
                be.opened = true;
                interactor = (ServerPlayer) player;
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
}
