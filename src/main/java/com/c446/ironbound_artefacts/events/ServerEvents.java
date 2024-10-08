package com.c446.ironbound_artefacts.events;


import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import com.c446.ironbound_artefacts.registries.IronboundDamageSources;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.events.PlayerSummonsCreature;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.tags.EntityTypeTags.UNDEAD;

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
    public static void entityGoalChanges(LivingChangeTargetEvent event) {
        AtomicBoolean isCrownPresent = new AtomicBoolean(false);
        AtomicBoolean isHandPresent = new AtomicBoolean(false);
        if (event.getNewAboutToBeSetTarget() instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(inv -> {
                isCrownPresent.set(inv.findCurios((new ItemStack(ItemRegistry.LICH_CROWN)).getItem()).isEmpty());
                isHandPresent.set(inv.findCurios((new ItemStack(ItemRegistry.LICH_HAND)).getItem()).isEmpty());
            });
            if (isCrownPresent.get() || isHandPresent.get()) {
                if (event.getEntity().getType().is(UNDEAD)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSummoningThings(PlayerSummonsCreature event) {
        var spell = SpellRegistry.getSpell(event.getSpellId());
        LivingEntity player = event.getCaster();
        var multiplier = spell.getSpellPower(event.getSpellLevel(), player);
        var quality = 3 * event.getSpellLevel() + 15;

        // Check if the spell is an instance of RaiseDeadSpell and if the player has the full Lich set equipped
        if (spell instanceof RaiseDeadSpell) {
            boolean hasLichSet = CuriosApi.getCuriosInventory(player)
                    .map(inv -> !inv.findCurios(ItemRegistry.LICH_CROWN.get()).isEmpty() ||
                            !inv.findCurios(ItemRegistry.LICH_HAND.get()).isEmpty())
                    .orElse(false);

            if (hasLichSet) {
                equipCreatureBasedOnQuality(event, quality);
            }
        }
    }

    private static void equipCreatureBasedOnQuality(PlayerSummonsCreature event, int quality) {
        Mob creature = (Mob) event.getCreature();

        // Equip the creature based on quality value
        if (quality > 40) {
            if (quality < 50) {
                equipWithDiamondGear(creature);
            } else {
                equipWithNetheriteGear(creature);
                setDropChancesToZero(creature);
            }
            event.setCreature(creature);
        }
    }

    private static void equipWithDiamondGear(Mob creature) {
        creature.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        creature.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
        creature.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
        creature.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        setWeaponBasedOnType(creature, Items.DIAMOND_AXE, Items.BOW);
    }

    private static void equipWithNetheriteGear(Mob creature) {
        creature.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
        creature.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
        creature.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
        creature.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
        setWeaponBasedOnType(creature, Items.NETHERITE_AXE, Items.BOW);
    }

    private static void setWeaponBasedOnType(Mob creature, Item meleeWeapon, Item rangedWeapon) {
        if (creature.getType().equals(EntityRegistry.SUMMONED_ZOMBIE.get())) {
            creature.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(meleeWeapon));
        } else if (creature.getType().equals(EntityRegistry.SUMMONED_SKELETON.get())) {
            creature.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(rangedWeapon));
        }
    }

    private static void setDropChancesToZero(Mob creature) {
        creature.setDropChance(EquipmentSlot.HEAD, 0.0F);
        creature.setDropChance(EquipmentSlot.CHEST, 0.0F);
        creature.setDropChance(EquipmentSlot.LEGS, 0.0F);
        creature.setDropChance(EquipmentSlot.FEET, 0.0F);
    }
}