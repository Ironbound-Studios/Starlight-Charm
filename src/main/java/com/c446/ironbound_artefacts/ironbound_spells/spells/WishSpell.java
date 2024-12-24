package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.components.UniversalPositionComponent;
import com.c446.ironbound_artefacts.datagen.Tags;
import com.c446.ironbound_artefacts.items.impl.lore_items.Phylactery;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Objects;

import static com.c446.ironbound_artefacts.registries.ComponentRegistry.UNIVERSAL_POS_COMPONENT;
import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.MAX_MANA;

@AutoSpellConfig
public class WishSpell extends AbstractSpell {
    private final ResourceLocation spellId = IronboundArtefact.prefix("wish");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(600)
            .build();

    public WishSpell() {
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 250;
        this.manaCostPerLevel = 250;
        this.castTime = 60;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public float getSpellPower(int spellLevel, @Nullable Entity sourceEntity) {
        return spellLevel;
    }

    protected void consumeOneFromStack(ItemStack stack) {
        stack.setCount(stack.getCount() - 1);
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        return (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.AMON));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        ItemStack item = entity.getMainHandItem();
        if (item.equals(ItemStack.EMPTY)) {
            item = entity.getOffhandItem();
        }

        if (item == ItemStack.EMPTY) {
            entity.heal(entity.getMaxHealth());
            for (MobEffectInstance i : entity.getActiveEffects()) {
                if (!i.getEffect().value().isBeneficial()) {
                    entity.removeEffect(i.getEffect());
                }
            }
        }

        if (item.getItem().equals(ItemRegistry.ARCANE_ESSENCE.get()) && entity instanceof ServerPlayer serverPlayer) {
            IronboundArtefact.LOGGER.debug("trying mana regen.");
            var data = MagicData.getPlayerMagicData(serverPlayer);
            entity.addEffect(new MobEffectInstance(EffectsRegistry.MANA_REGEN, 20*60*10*spellLevel, (int) (1 / 2F * this.getSpellPower(spellLevel, entity))));
            consumeOneFromStack(item);
        } else if (item.getItem().equals(Items.LODESTONE)) {
            IronboundArtefact.LOGGER.debug("trying lodestone");
            CuriosApi.getCuriosInventory(entity).ifPresent(inv -> {
                    var contractorRings = inv.findCurios(stack -> stack.getItem() instanceof Phylactery);
                        contractorRings.forEach(ring->{
                            System.out.println(ring.stack().getHoverName().toString());
                            var pos = UniversalPositionComponent.create(entity);
                            IronboundArtefact.LOGGER.debug("{}{}", pos.getPos().toString(), pos.dimension());
                            ring.stack().set(UNIVERSAL_POS_COMPONENT, UniversalPositionComponent.create(entity));
                            var edfre = ring.stack().get(UNIVERSAL_POS_COMPONENT);
                            IronboundArtefact.LOGGER.debug("{} {}", edfre.getPos(), edfre.dimension());
                        });
                    }
            );
        } else if (item.getItem().equals(Items.PHANTOM_MEMBRANE)) {
            IronboundArtefact.LOGGER.debug("trying slow fall");
            entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20*60*10*spellLevel, (int) (1 / 2 * this.getSpellPower(spellLevel, entity))));
            consumeOneFromStack(item);
        } else if (item.getItem().equals(Items.MAGMA_CREAM)) {
            IronboundArtefact.LOGGER.debug("trying fire res");
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20*60*10*spellLevel, (int) (1 / 2F * this.getSpellPower(spellLevel, entity))));
            consumeOneFromStack(item);
        } else if (item.getItem().equals(Items.GOLD_BLOCK)) {
            IronboundArtefact.LOGGER.debug("trying damage res.");
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20*60*10*spellLevel, Math.min(4, (int) (1 / 2F * this.getSpellPower(spellLevel, entity)))));
            consumeOneFromStack(item);
        } else if (item.getItem().equals(Items.GHAST_TEAR)) {
            IronboundArtefact.LOGGER.debug("trying health regen.");
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20*60*10*spellLevel, (int) (1 / 2F * this.getSpellPower(spellLevel, entity))));
            consumeOneFromStack(item);
        } else if (item.has(ComponentRegistry.SPELL_CONTAINER)) {
            IronboundArtefact.LOGGER.debug("found spell container.");
            var spellContainer = item.get(ComponentRegistry.SPELL_CONTAINER);
            if (spellContainer != null && item.getItem().equals(ItemRegistry.SCROLL.get())) {
                var spells = spellContainer.getActiveSpells();
                if (spells != null && !spells.isEmpty()) {
                    var num = level.random.nextIntBetweenInclusive(0, spells.size() - 1);
                    var selectedSpell = spells.get(num);
                    if (selectedSpell != null && selectedSpell.getSpell() != null && !Objects.equals(selectedSpell.getSpell().getSpellId(), this.getSpellId())) {
                        int spellLevel1 = selectedSpell.getLevel() + spellLevel;
                        System.out.println("casting spell " + selectedSpell.getSpell().getComponentId() + " at level : " + spellLevel1);
                        selectedSpell.getSpell().castSpell(
                                level,
                                spellLevel1,
                                (ServerPlayer) entity,
                                castSource,
                                false
                        );
                    }
                }
            }
        } else if (item.getItem().equals(Items.ZOMBIE_HEAD) && item.getCount() > 3) {
            var entities = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(150), a -> a instanceof IMagicSummon);
            for (LivingEntity l : entities) {
                if (l instanceof IMagicSummon s) {
                    if (s.getSummoner().equals(entity)) {
                        l.addEffect(new MobEffectInstance(MobEffectRegistry.HASTENED, 60 * spellLevel, spellLevel));
                        l.addEffect(new MobEffectInstance(MobEffectRegistry.CHARGED, 60 * spellLevel, spellLevel));
                    }
                }
            }
        } else if (item.getItem().equals(Items.WITHER_SKELETON_SKULL)) {
            var result = Utils.raycastForEntity(level, entity, 150, true);
            if (result instanceof EntityHitResult result1 && result1.getEntity() instanceof LivingEntity l) {
                l.hurt(l.damageSources().wither(), l.getMaxHealth());
            }
        } else if (item.is(Tags.ItemTags.WISH_DUPLICABLE)) {

            var newMax = item.getCount() * 2;
            if (newMax > 64) {
                item.setCount(64);
            } else {
                item.setCount(newMax);
            }
        }

        entity.addEffect(new MobEffectInstance(EffectsRegistry.VOID_POISON, 100, 2));

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
