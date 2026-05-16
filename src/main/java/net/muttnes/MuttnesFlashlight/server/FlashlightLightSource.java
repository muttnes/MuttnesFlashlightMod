package net.muttnes.MuttnesFlashlight.server;

import atomicstryker.dynamiclights.server.IDynamicLightSource;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.muttnes.MuttnesFlashlight.entities.ModEntities;
import net.muttnes.MuttnesFlashlight.entities.custom.LightEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;

public class FlashlightLightSource implements IDynamicLightSource {

    private final ServerPlayer player;
    private final LightEntity lightEntity;
    private final int lightLevel;
    private static final double FLASHLIGHT_RANGE = 8.0;
    
    private Vec3 cachedPos;
    private int lastRaycastTick = -1;
    private Vec3 previousPosition;
    private double previousHeight;

    public FlashlightLightSource(ServerLevel world, ServerPlayer player, int lightLevel) {
        this.player = player;
        this.lightLevel = lightLevel;

        Vec3 initialPos = getCrosshairPosition(world);
        this.previousPosition = initialPos;
        this.previousHeight = initialPos.y;

        this.lightEntity = new LightEntity(ModEntities.LIGHT_ENTITY.get(), world);
        this.lightEntity.setPos(initialPos.x, initialPos.y, initialPos.z);
        this.lightEntity.setNoGravity(true);
        this.lightEntity.setInvisible(true);
        this.lightEntity.setInvulnerable(true);
        this.lightEntity.setCustomNameVisible(false);
        this.lightEntity.setSilent(true);

        world.addFreshEntity(this.lightEntity);
        world.getServer().getPlayerList().broadcastAll(new ClientboundAddEntityPacket(this.lightEntity));
    }

    @Override
    public int getLightLevel() {
        return lightLevel;
    }

    @Override
    public Entity getAttachmentEntity() {
        return lightEntity;
    }

    public void update() {
        Vec3 targetPos = getCrosshairPosition(player.level());

        if (targetPos.distanceTo(previousPosition) < 0.01) {
            return;
        }

        lightEntity.setPos(targetPos.x, targetPos.y, targetPos.z);

        previousPosition = targetPos;
        previousHeight = targetPos.y;
    }

    public void remove() {
        this.lightEntity.discard();
    }

    private Vec3 getCrosshairPosition(Level world) {

        if (player.tickCount == lastRaycastTick && cachedPos != null) {
            return cachedPos;
        }

        lastRaycastTick = player.tickCount;

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookDirection = player.getLookAngle().normalize();

        ClipContext clipContext = new ClipContext(
            eyePos,
            eyePos.add(lookDirection.scale(FLASHLIGHT_RANGE)),
            ClipContext.Block.OUTLINE,
            ClipContext.Fluid.NONE,
            player
        );

        HitResult hitResult = world.clip(clipContext);

        cachedPos = (hitResult.getType() == HitResult.Type.BLOCK)
            ? ((BlockHitResult) hitResult).getLocation()
            : eyePos.add(lookDirection.scale(FLASHLIGHT_RANGE));

        return cachedPos;
    }
}
