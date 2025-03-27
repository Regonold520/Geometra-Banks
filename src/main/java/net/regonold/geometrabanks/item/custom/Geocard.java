package net.regonold.geometrabanks.item.custom;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.regonold.geometrabanks.block.entity.ATMBlockEntity;
import net.regonold.geometrabanks.component.ModDataComponents;
import net.regonold.geometrabanks.screen.custom.CardMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Geocard extends Item implements MenuProvider {
    public Geocard(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!level.isClientSide && !stack.has(ModDataComponents.PIN_COMPONENT)) {
            player.swing(usedHand, true);
            ((ServerPlayer) player).openMenu(
                    new SimpleMenuProvider(
                            (containerId, inv, player1) -> new CardMenu(containerId, inv), Component.literal("Card")));
            return InteractionResultHolder.success(stack);
        } else if (!level.isClientSide) {
            System.out.println("Item alr got a pin mate " + stack.get(ModDataComponents.PIN_COMPONENT));
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        // Return a default non-null InteractionResult if conditions aren't met.
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull net.minecraft.world.entity.player.Inventory inv, @NotNull net.minecraft.world.entity.player.Player player) {
        return new CardMenu(containerId, inv);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(ModDataComponents.UUID_COMPONENT) && context.level() != null) {
            UUID storedUUID = stack.get(ModDataComponents.UUID_COMPONENT);
            net.minecraft.world.entity.player.Player owner = context.level().getPlayerByUUID(storedUUID);

            tooltipComponents.add(Component.empty());
            if (owner != null) {
                tooltipComponents.add(Component.literal("§d" + owner.getScoreboardName() + "'s Geocard"));
            } else {
                tooltipComponents.add(Component.literal("§7Unclaimed Geocard"));
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(ModDataComponents.UUID_COMPONENT);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Card Menu");
    }
}
