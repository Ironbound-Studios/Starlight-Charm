package com.c446.ironbound_artefacts.events;


import com.c446.ironbound_artefacts.items.DevilsFinger;
import com.c446.ironbound_artefacts.items.MagicianMonocle;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent
    public static void spellLevelEvent(ModifySpellLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            AtomicInteger boost = new AtomicInteger(0);
            CuriosApi.getCuriosInventory(player).
                    ifPresent(inv -> inv.findCurios(
                                    stack -> AffinityData.hasAffinityData(stack)
                                            && AffinityData.getAffinityData(stack).getSpell() == event.getSpell())
                            .forEach(slot -> boost.addAndGet(
                                    Objects.requireNonNull(slot.stack().get(ComponentRegistry.AFFINITY_COMPONENT)).bonus()
                            ))
                    );
        }
    }
}
