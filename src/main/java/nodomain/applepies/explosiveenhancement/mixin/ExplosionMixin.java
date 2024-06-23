package nodomain.applepies.explosiveenhancement.mixin;

import nodomain.applepies.explosiveenhancement.config.Config;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static nodomain.applepies.explosiveenhancement.CustomParticle.*;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Inject(method = "Lnet/minecraft/world/Explosion;<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDFLjava/util/List;)V", at = @At("TAIL"))
    private void explosions$addParticles(World world, Entity entity, double x, double y, double z, float power, List list, CallbackInfo ci) {
        if (Config.getEnabled()) {
            int underwater = (world.getBlockState(new BlockPos(x, y, z)).getBlock().getMaterial() == Material.water) ? 1 : 0;

            world.spawnParticle(BLASTWAVE, x, y + 0.5, z, 0, 0, 0, underwater);
            world.spawnParticle(FIREBALL, x, y + 0.5, z, 0, 0, 0, underwater);

            if (underwater != 1) {
                for (int i = 0; i < 4; i++) {
                    world.spawnParticle(SPARK, x, y + 0.5, z, 0, 0, 0, i + 7);
                }

                if (Config.getSmoke()) {
                    world.spawnParticle(SMOG, x, y, z, 0, 0.4, 0);
                    world.spawnParticle(SMOG, x, y, z, 0, 0.6, 0);
                    world.spawnParticle(SMOG, x, y, z, 0.25, 0.6, 0);
                    world.spawnParticle(SMOG, x, y, z, -0.25, 0.6, 0);
                    world.spawnParticle(SMOG, x, y, z, 0, 0.6, 0.25);
                    world.spawnParticle(SMOG, x, y, z, 0, 0.6, -0.25);
                }
            }
        } else { // vanilla logic
            if (power >= 2f) {
                world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, x, y, z, 1.0, 0.0, 0.0, new int[0]);
            } else {
                world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 1.0, 0.0, 0.0, new int[0]);
            }
        }
    }

    @Redirect(method = "doExplosionB", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;DDDDDD[I)V"))
    private void explosions$skipParticles(World instance, EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int[] p_175688_14_) {
        ;
    }
}