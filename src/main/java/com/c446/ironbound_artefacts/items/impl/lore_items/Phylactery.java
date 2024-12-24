package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.components.KillCounterComponent;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.c446.ironbound_artefacts.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.*;


public class Phylactery extends CurioBaseItem {
    public Phylactery(Properties properties) {
        super(properties);
    }
    /*
     * Insight
     * CD
     * mana regen
     * */



    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (!stack.has(ComponentRegistry.KILL_COUNT_COMPONENT)) {
            stack.set(ComponentRegistry.KILL_COUNT_COMPONENT, new KillCounterComponent(1));
            System.out.println("equipped" + stack.getHoverName());
        }

        super.onEquip(slotContext, prevStack, stack);
    }



    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (pStack.has(ComponentRegistry.KILL_COUNT_COMPONENT)) {
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.phylactery.souls", pStack.get(ComponentRegistry.KILL_COUNT_COMPONENT).killCount()));
        }


        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
