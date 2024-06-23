package nodomain.applepies.explosiveenhancement.mixin;

import nodomain.applepies.explosiveenhancement.CustomParticle;
import nodomain.applepies.explosiveenhancement.particle.*;
import net.minecraft.client.particle.EffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderer.class)
public class EffectRendererMixin {

    @Inject(method = "registerVanillaParticles()V", at = @At("TAIL"))
    private void explosions$registerParticles(CallbackInfo ci) {
        EffectRenderer particleManager = (EffectRenderer) (Object) this;

        particleManager.registerParticle(CustomParticle.BLASTWAVE.getParticleID(), new BlastwaveParticle.Factory());
        particleManager.registerParticle(CustomParticle.FIREBALL.getParticleID(), new FireballParticle.Factory());
        particleManager.registerParticle(CustomParticle.SPARK.getParticleID(), new SparkParticle.Factory());
        particleManager.registerParticle(CustomParticle.SMOG.getParticleID(), new SmogParticle.Factory());
    }
}
