package net.muttnes.MuttnesFlashlight.entities.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class LightEntity extends Entity {

    public LightEntity(EntityType<?> pType, Level pLevel) {
        super(pType, pLevel);
        this.setNoGravity(true);
        this.setInvisible(true);
        this.setInvulnerable(true);
        this.setCustomNameVisible(false);
        this.setSilent(true);
        this.noCulling = true;
        this.noPhysics = true;
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void setPos(double pX, double pY, double pZ) {
        super.setPos(pX, pY, pZ);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }
    
}
