package com.c446.ironbound_artefacts.items.impl;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StaffOfPower extends StaffItem {
    public StaffOfPower(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> lines, @NotNull TooltipFlag tooltipFlag) {

            lines.add(Component.empty());
            lines.add(Component.translatable("curios.modifiers.hands").withStyle(ChatFormatting.GOLD));
            var name = Component.translatable("spell.ironbounds_artefacts.all_spell.name").withColor(EffectsRegistry.rgbToInt(255,0,188));
            lines.add(Component.literal(" ").append(
                    (Component.translatable("tooltip.irons_spellbooks.enhance_spell_level_plural", name))
                            .withStyle(ChatFormatting.YELLOW)));


        super.appendHoverText(stack, context, lines, tooltipFlag);
    }
}
