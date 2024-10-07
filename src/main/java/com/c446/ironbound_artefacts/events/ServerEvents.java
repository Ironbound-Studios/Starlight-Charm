package com.c446.ironbound_artefacts.events;


import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.DamageSourcesReg;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import com.c446.ironbound_artefacts.registries.IronboundDamageSources;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.events.PlayerSummonsCreature;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.fireball.MagicFireball;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import io.redspace.ironsspellbooks.spells.eldritch.EldritchBlastSpell;
import io.redspace.ironsspellbooks.util.NBT;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.c446.ironbound_artefacts.registries.AttributeRegistry.VOID_DAMAGE_ATTRIBUTE;

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

    @SubscribeEvent
    public static void onEntityDamaged(LivingDamageEvent.Post event) {
        CuriosApi.getCuriosInventory(event.getEntity().getLastAttacker()).ifPresent(inv -> {
            List<SlotResult> result = inv.findCurios(ItemRegistry.DEATH_AMULET.get());
            if (!result.isEmpty()) {
                event.getEntity().addEffect(new MobEffectInstance(EffectsRegistry.VOID_POISON, 3, 1));
            }
        });
    }

    @SubscribeEvent
    public static void tickEntity(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living && living.hasEffect(EffectsRegistry.VOID_POISON)) {
            int amplifier = Objects.requireNonNull(living.getEffect(EffectsRegistry.VOID_POISON)).getAmplifier();
            if (living.tickCount % (40 / ((amplifier * 2 == 0) ? (1) : (amplifier * 2))) == 0) {
                living.hurt(DamageSources.get(event.getEntity().level(), IronboundDamageSources.VOID_DAMAGE), 1);
            }
        }
    }

    @SubscribeEvent
    public static void onSummoningThings(PlayerSummonsCreature event) {
        var spell = SpellRegistry.getSpell(event.getSpellId());
        LivingEntity player = event.getCaster();
        var multiplier = spell.getSpellPower(event.getSpellLevel(), player);
        AtomicBoolean ARE_LICH_ITEMS_GATHERED = new AtomicBoolean(false);
        var quality = 3 * event.getSpellLevel() + 15;
        // full lich set bonus
        if (spell instanceof RaiseDeadSpell raiseDeadSpell) {
            CuriosApi.getCuriosInventory(player).ifPresent(inv -> {
//                if (!(inv.findCurios(ItemRegistry.LICH_CROWN.get()).isEmpty()) && !(inv.findCurios(ItemRegistry.LICH_HAND.get()).isEmpty())) {
//                    ARE_LICH_ITEMS_GATHERED.set(true);
//
//                    if (quality > 40 && quality < 50) {
//                        Mob creature = (Mob) event.getCreature();
//                        creature.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
//                        creature.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_CHESTPLATE));
//                        creature.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_LEGGINGS));
//                        creature.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_BOOTS));
//                        if (creature.getType().equals(EntityRegistry.SUMMONED_ZOMBIE.get())) {
//                            creature.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
//                        }
//                        if (creature.getType().equals(EntityRegistry.SUMMONED_SKELETON.get())) {
//                            creature.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
//                        }
//                        event.setCreature(creature);
//                    } else if (quality > 50) {
//                    }
//                }
            });
            if (ARE_LICH_ITEMS_GATHERED.get()) {
                var mob = event.getCreature();
                if (multiplier >= 0.6) ;
            }
        }

    }

    private void equip(Mob mob, ItemStack[] equipment) {
        mob.setItemSlot(EquipmentSlot.FEET, equipment[0]);
        mob.setItemSlot(EquipmentSlot.LEGS, equipment[1]);
        mob.setItemSlot(EquipmentSlot.CHEST, equipment[2]);
        mob.setItemSlot(EquipmentSlot.HEAD, equipment[3]);
        mob.setDropChance(EquipmentSlot.FEET, 0.0F);
        mob.setDropChance(EquipmentSlot.LEGS, 0.0F);
        mob.setDropChance(EquipmentSlot.CHEST, 0.0F);
        mob.setDropChance(EquipmentSlot.HEAD, 0.0F);
    }


}
