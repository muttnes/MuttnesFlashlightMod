package net.muttnes.MuttnesFlashlight.items.custom;

import atomicstryker.dynamiclights.server.IDynamicLightSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;

public class FlashlightLightSource implements IDynamicLightSource {

    private final Player player;
    private final ArmorStand lightEntity;
    private final int lightLevel;
    private static final double FLASHLIGHT_RANGE = 10.0;

    private Vec3 previousPosition;
    private double previousHeight;

    public FlashlightLightSource(Level world, Player player, int lightLevel) {
        this.player = player;
        this.lightLevel = lightLevel;

        Vec3 initialPos = getCrosshairPosition(world);
        this.previousPosition = initialPos;
        this.previousHeight = initialPos.y;

        this.lightEntity = new ArmorStand((ServerLevel) world, initialPos.x, initialPos.y, initialPos.z);
        this.lightEntity.setNoGravity(true);
        this.lightEntity.setInvisible(true);
        this.lightEntity.setInvulnerable(true);
        this.lightEntity.setCustomNameVisible(false);
        this.lightEntity.setSilent(true);

        world.addFreshEntity(this.lightEntity);
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
        double targetHeight = targetPos.y;

        double interpolatedX = previousPosition.x + (targetPos.x - previousPosition.x) * 0.1;
        double interpolatedY = previousHeight + (targetHeight - previousHeight) * 0.1;
        double interpolatedZ = previousPosition.z + (targetPos.z - previousPosition.z) * 0.1;

        lightEntity.setPos(interpolatedX, interpolatedY, interpolatedZ);

        previousPosition = new Vec3(interpolatedX, interpolatedY, interpolatedZ);
        previousHeight = interpolatedY;
    }

    public void remove() {
        this.lightEntity.discard();
    }

    private Vec3 getCrosshairPosition(Level world) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookDirection = player.getLookAngle().normalize();

        ClipContext clipContext = new ClipContext(eyePos, eyePos.add(lookDirection.scale(FLASHLIGHT_RANGE)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);
        HitResult hitResult = world.clip(clipContext);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            if (world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                return eyePos.add(lookDirection.x * FLASHLIGHT_RANGE, lookDirection.y * FLASHLIGHT_RANGE, lookDirection.z * FLASHLIGHT_RANGE);
            }
            return blockHitResult.getLocation();
        }

        return eyePos.add(lookDirection.x * FLASHLIGHT_RANGE, lookDirection.y * FLASHLIGHT_RANGE, lookDirection.z * FLASHLIGHT_RANGE);
    }
}
