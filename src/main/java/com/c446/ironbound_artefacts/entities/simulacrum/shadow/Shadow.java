package com.c446.ironbound_artefacts.entities.simulacrum.shadow;

import com.c446.ironbound_artefacts.entities.simulacrum.SimulacrumEntity;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardSupportGoal;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Shadow extends SimulacrumEntity {
    private static final EntityDataAccessor<Optional<UUID>> CLONE_OF = SynchedEntityData.defineId(Shadow.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(CLONE_OF, Optional.of(Util.NIL_UUID));

    }

    public void setCloneOf(Player cloneTarget) {
        this.entityData.set(CLONE_OF, Optional.of(cloneTarget.getUUID()));
    }

    public Optional<Player> getClone() {
        if (this.entityData.get(CLONE_OF).isPresent()) {
            return Optional.ofNullable(this.level().getPlayerByUUID(this.entityData.get(CLONE_OF).get()));
        }
        return Optional.empty();
    }

    public Shadow(Level pLevel, @NotNull Player player, float quality, Player cloneTarget) {
        super(pLevel, player, quality);
        this.setCloneOf(cloneTarget);
    }

    public Shadow(Level pLevel) {
        super(pLevel);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    protected PlayerInfo getPlayerInfo() {
        if (this.playerInfo == null && this.getEntityData().get(CLONE_OF).isPresent() && this.getClone().isPresent()) {
            var uuid = this.getOwnerUUID().get();
            this.playerInfo = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(uuid);
            System.out.println("player info was missing.");
            if (this.getSummoner() == null) {
                this.discard();
            }
        }
        return this.playerInfo;
    }

    @Override
    protected void registerWizardGoals() {
        this.goalSelector.removeAllGoals(a -> a instanceof WizardAttackGoal || a instanceof WizardSupportGoal<?>);

        if (this.getClone().isPresent() && this.getClone().get() instanceof Player player) {
            this.goalSelector.addGoal(1, new WizardAttackGoal(this, 1.25f, 25, 80).setSpells(getOffensiveSpellsFromList(simpleGetSpells(player), player), getDefensiveSpells(simpleGetSpells(player), player), getMovementSpells(simpleGetSpells(player), player), getUtilSpells(simpleGetSpells(player), player)).setSpellQuality(this.quality * 0.75f, this.quality));
            System.out.println("min quality : " + this.quality * 0.75 + "max quality : " + this.quality);
            this.goalSelector.addGoal(2, new WizardSupportGoal<SimulacrumEntity>(this, 1.25f, 100, 180).setSpells(getDefensiveSpells(simpleGetSpells(player), player), (getUtilSpells(simpleGetSpells(player), player))).setSpellQuality(this.quality * 0.75f, this.quality));
        }
    }
}
