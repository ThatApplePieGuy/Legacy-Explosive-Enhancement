package nodomain.applepies.explosiveenhancement.mixin;

import nodomain.applepies.explosiveenhancement.CustomParticle;
import net.minecraft.util.EnumParticleTypes;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(EnumParticleTypes.class)
public abstract class EnumParticleTypesMixin {
    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static EnumParticleTypes explosions$newParticle(String internalName, int arguments, String name, int internalId, boolean alwaysShow) {
        throw new AssertionError();
    }

    @SuppressWarnings("ShadowTarget")
    @Shadow
    private static @Final
    @Mutable
    EnumParticleTypes[] $VALUES;

    @Inject(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, target = "Lnet/minecraft/util/EnumParticleTypes;$VALUES:[Lnet/minecraft/util/EnumParticleTypes;", shift = At.Shift.AFTER))
    private static void explosions$addParticles(CallbackInfo ci) {
        ArrayList<EnumParticleTypes> variants = new ArrayList<>(Arrays.asList($VALUES));
        int last = variants.get(variants.size() - 1).ordinal();

        EnumParticleTypes blastwave = explosions$newParticle("BLASTWAVE", 0, "blastwave", ++last, true);
        CustomParticle.BLASTWAVE = blastwave;
        variants.add(blastwave);

        EnumParticleTypes fireball = explosions$newParticle("FIREBALL", 0, "fireball", ++last, true);
        CustomParticle.FIREBALL = fireball;
        variants.add(fireball);

        EnumParticleTypes spark = explosions$newParticle("SPARK", 0, "spark", ++last, true);
        CustomParticle.SPARK = spark;
        variants.add(spark);

        EnumParticleTypes smog = explosions$newParticle("SMOG", 0, "smog", ++last, true);
        CustomParticle.SMOG = smog;
        variants.add(smog);

        $VALUES = variants.toArray(new EnumParticleTypes[0]);
    }
}
