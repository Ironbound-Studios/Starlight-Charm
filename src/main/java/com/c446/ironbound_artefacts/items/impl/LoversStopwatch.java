package com.c446.ironbound_artefacts.items.impl;

import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.c446.ironbound_artefacts.registries.CustomSpellRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.entity.armor.ArchevokerArmorModel;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class LoversStopwatch extends UserDependantCurios implements IPresetSpellContainer {

    public LoversStopwatch(Properties p) {
        super(p);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        if (itemStack.getItem() instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.CHESTPLATE) {
            if (!ISpellContainer.isSpellContainer(itemStack)) {
                var spellContainer = ISpellContainer.createImbuedContainer(CustomSpellRegistry.TIME_STOP.get(),1, itemStack);

                itemStack.set(ComponentRegistry.SPELL_CONTAINER, spellContainer);
            }
        }

    }
}
