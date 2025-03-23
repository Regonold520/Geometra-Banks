package net.regonold.geometrabanks.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.regonold.geometrabanks.component.ModDataComponents;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Geocard extends Item {
    public Geocard(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        // Ensure the Geocard is only registered once
        if (!stack.has(ModDataComponents.UUID_COMPONENT)) {
            UUID playerUUID = player.getUUID();
            stack.set(ModDataComponents.UUID_COMPONENT, playerUUID);

            // Trigger hand swing animation
            player.swing(usedHand, true);

            // Feedback for debugging
            System.out.println(player.getScoreboardName() + " registered Geocard");

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(ModDataComponents.UUID_COMPONENT) && context.level() != null) {
            UUID storedUUID = stack.get(ModDataComponents.UUID_COMPONENT);
            Player owner = context.level().getPlayerByUUID(storedUUID);

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


}
