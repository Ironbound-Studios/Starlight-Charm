package com.c446.ironbound_artefacts.events;


import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.entities.simulacrum.SimulacrumEntity;
import com.c446.ironbound_artefacts.registries.*;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import com.google.common.collect.HashMultimap;
import io.redspace.ironsspellbooks.api.events.*;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.events.SpellTeleportEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.registries.*;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static com.c446.ironbound_artefacts.registries.ItemRegistry.ARCHMAGE_SPELLBOOK;
import static com.c446.ironbound_artefacts.registries.ItemRegistry.STAFF_OF_POWER;
import static net.minecraft.tags.EntityTypeTags.UNDEAD;

@EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent
    public static void spellLevelEvent(ModifySpellLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            AtomicInteger boost = new AtomicInteger(0);
            CuriosApi.getCuriosInventory(player).ifPresent((curioHandler) -> {
                if (curioHandler.isEquipped(ARCHMAGE_SPELLBOOK.get())) {
                    boost.addAndGet(1);
                }
            });

            if (player != null) {
                if (player.getMainHandItem() != null && player.getMainHandItem().is(STAFF_OF_POWER)) {
                    ISpellContainer mainHandSpellContainer = ISpellContainer.get(player.getMainHandItem());
                    if (mainHandSpellContainer != null && mainHandSpellContainer.getAllSpells() != null) {
                        Arrays.stream(mainHandSpellContainer.getAllSpells()).forEach(spell -> {
                            if (spell != null && spell.getSpell() != null && spell.getSpell().equals(event.getSpell())) {
                                boost.addAndGet(1);
                            }
                        });
                    }
                } else if (player.getOffhandItem() != null && player.getOffhandItem().is(STAFF_OF_POWER)) {
                    ISpellContainer offhandSpellContainer = ISpellContainer.get(player.getOffhandItem());
                    if (offhandSpellContainer != null && offhandSpellContainer.getAllSpells() != null) {
                        Arrays.stream(offhandSpellContainer.getAllSpells()).forEach(spell -> {
                            if (spell != null && spell.getSpell() != null && spell.getSpell().equals(event.getSpell())) {
                                boost.addAndGet(1);
                            }
                        });
                    }
                }
            }

            if (boost.get() > 2) {
                boost.set(2);
            }
            event.setLevel(event.getLevel() + boost.get());
        }
    }

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {
        ServerConfigs.IMBUE_WHITELIST_ITEMS.add(STAFF_OF_POWER.get());

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
    }

    @SubscribeEvent
    public static void entityTriesTP(SpellTeleportEvent event) {
        if (event.getEntity() instanceof LivingEntity living && living.hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onServerStop(ServerStoppingEvent event){
        event.getServer().getAllLevels().forEach(level->{

        });
    }

    @SubscribeEvent
    public static void onManaRegen(ChangeManaEvent event) {
        if (event.getEntity().hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onThrowItem(ItemTossEvent event) {
        if (event.getPlayer().hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemUseLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity().hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
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
    public static void onSummoningThings(SpellSummonEvent<Monster> event) {
        System.out.println(event.getCreature().toString());
        var spell = SpellRegistry.getSpell(event.getSpellId());
        LivingEntity player = event.getCaster();
        var quality = 3 * event.getSpellLevel() + 15;

        // Check if the spell is an instance of RaiseDeadSpell and if the player has the full Lich set equipped
        if (spell instanceof RaiseDeadSpell) {
            boolean hasLichSet = CuriosApi.getCuriosInventory(player).map(inv -> !inv.findCurios(ItemRegistry.LICH_CROWN.get()).isEmpty() || !inv.findCurios(ItemRegistry.LICH_HAND.get()).isEmpty()).orElse(false);

            if (hasLichSet) {
                var canGetNetherite = (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.TAR) || player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.ENDER));
                Monster creature = (equipCreatureBasedOnQuality(event.getCreature(), quality, canGetNetherite));
                System.out.println(creature.toString());
                HashMultimap<Holder<Attribute>, AttributeModifier> summonAttributes = HashMultimap.create();
                summonAttributes.put(Attributes.MAX_HEALTH, new AttributeModifier(IronboundArtefact.prefix("summon_health_boost"), 4 * SpellRegistry.RAISE_DEAD_SPELL.get().getSpellPower(event.getSpellLevel(), player), AttributeModifier.Operation.ADD_VALUE));
                summonAttributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(IronboundArtefact.prefix("summon_damage"), 2 * SpellRegistry.RAISE_DEAD_SPELL.get().getSpellPower(event.getSpellLevel(), player), AttributeModifier.Operation.ADD_VALUE));

                creature.getAttributes().addTransientAttributeModifiers(summonAttributes);
                event.setCreature(creature);
            }
        }
    }

    @SubscribeEvent
    public static void onAttributesAddedToItem(ItemAttributeModifierEvent event) {
        if (event.getItemStack().getItem().equals(ItemRegistry.STAFF_OF_MAGI.get()) && event.getItemStack().has(ComponentRegistry.UPGRADE_DATA)) {
            var data = event.getItemStack().get(ComponentRegistry.UPGRADE_DATA);
            for (var upgrade : data.getUpgrades().keySet()) {
                var amount = data.getUpgrades().get(upgrade);
                AtomicReference<EquipmentSlotGroup> slot = new AtomicReference<>();
                AtomicReference<AttributeModifier> modifier = new AtomicReference<>();

                event.getModifiers().forEach(a -> {
                    if (a.modifier().id().equals(upgrade.getId())) {
                        slot.set(a.slot());
                        modifier.set(a.modifier());
                    }
                });
                if (slot.get() != null && modifier.get() != null) {
                    //event.removeModifier(upgrade.getAttribute(), upgrade.getId());
                    //event.addModifier(upgrade.getAttribute(), modifier.get(), slot.get());
                }
            }
        }
    }



    @SubscribeEvent
    public static void getBonusSpells(SpellSelectionManager.SpellSelectionEvent event) {
        var player = event.getEntity();
        CuriosApi.getCuriosInventory(player).ifPresent(a -> {
            var list = a.findCurios(item -> item != null && item.has(ComponentRegistry.SPELL_CONTAINER) && !(item.getItem() instanceof SpellBook));
            for (var i : list) {
                var spellContainer = i.stack() != null ? i.stack().get(ComponentRegistry.SPELL_CONTAINER) : null;
                if (spellContainer != null) {
                    var spells = spellContainer.getAllSpells();
                    if (spells != null && !Arrays.stream(spells).toList().isEmpty()) {
                        for (var spell : spells) {
                            if (spell == null || spell.getSpell() == null) {
                                return;
                            }
                            event.addSelectionOption(new SpellData(spell.getSpell(), spell.getLevel(), true), i.stack().getItem().getDescriptionId(), spell.index());
                        }
                    }
                }
            }
        });
    }

    private static Monster equipCreatureBasedOnQuality(Monster creature, int quality, boolean canGetNetherite) {
        if (quality > 40) {
            if (quality < 50) {
                equipWithDiamondGear(creature);
            } else {
                equipWithNetheriteGear(creature);
                setDropChancesToZero(creature);
            }
        }
        return (creature);
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

    @SubscribeEvent
    public static void onCast(SpellPreCastEvent preCastEvent) {
        if (preCastEvent.getEntity().hasEffect(EffectsRegistry.TIME_STOP)) {
            preCastEvent.setCanceled(true);
        }
    }
}