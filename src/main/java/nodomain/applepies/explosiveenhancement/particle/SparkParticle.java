package nodomain.applepies.explosiveenhancement.particle;

import nodomain.applepies.explosiveenhancement.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static nodomain.applepies.explosiveenhancement.ExplosiveEnhancement.getFrameIndex;
import static nodomain.applepies.explosiveenhancement.ExplosiveEnhancement.modid;

public class SparkParticle extends EntityFX {
    private static final ResourceLocation SPARK = new ResourceLocation(modid, "spark.png");
    private final TextureManager manager;
    private final int latentTicks;

    protected SparkParticle(TextureManager textureManager, World world, double x, double y, double z, int latentTicks) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.manager = textureManager;

        this.motionX = this.motionY = this.motionZ = 0.0;
        this.particleMaxAge = latentTicks + 6;
        this.particleScale = 4f * (float) Config.getScale();
        this.particleAlpha = 0.7f;
        this.latentTicks = latentTicks;
    }

    @Override
    public void renderParticle(WorldRenderer wr, Entity entity, float tickDelta, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
        if (particleAge > latentTicks) {
            float frames = 4;
            float frameIndex = getFrameIndex(particleAge - latentTicks + 1, particleMaxAge - latentTicks + 1, (int) frames);
            float minU = (frameIndex + 1) / frames;
            float maxU = frameIndex / frames;
            float minV = 0;
            float maxV = 1;

            int light = this.getBrightnessForRender(tickDelta);
            int lightU = light >> 16 & 65535;
            int lightV = light & 65535;

            float x = (float) (prevPosX + (posX - prevPosX) * (double) tickDelta - interpPosX);
            float y = (float) (prevPosY + (posY - prevPosY) * (double) tickDelta - interpPosY);
            float z = (float) (prevPosZ + (posZ - prevPosZ) * (double) tickDelta - interpPosZ);

            rotX *= particleScale;
            rotXZ *= particleScale;
            rotZ *= particleScale;
            rotYZ *= particleScale;
            rotXY *= particleScale;

            manager.bindTexture(SPARK);

            wr.pos((x - rotX - rotYZ), (y - rotXZ), (z - rotZ - rotXY))
                    .tex(maxU, maxV)
                    .color(particleRed, particleGreen, particleBlue, particleAlpha)
                    .lightmap(lightU, lightV)
                    .endVertex();
            wr.pos((x - rotX + rotYZ), (y + rotXZ), (z - rotZ + rotXY))
                    .tex(maxU, minV)
                    .color(particleRed, particleGreen, particleBlue, particleAlpha)
                    .lightmap(lightU, lightV)
                    .endVertex();
            wr.pos((x + rotX + rotYZ), (y + rotXZ), (z + rotZ + rotXY))
                    .tex(minU, minV)
                    .color(particleRed, particleGreen, particleBlue, particleAlpha)
                    .lightmap(lightU, lightV)
                    .endVertex();
            wr.pos((x + rotX - rotYZ), (y - rotXZ), (z + rotZ - rotXY))
                    .tex(minU, maxV)
                    .color(particleRed, particleGreen, particleBlue, particleAlpha)
                    .lightmap(lightU, lightV)
                    .endVertex();
        }
    }

    @Override
    public int getFXLayer() {
        return 2;
    }

    public static class Factory implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int id, World world, double x, double y, double z, double motX, double motY, double motZ, int... arr) {
            return new SparkParticle(Minecraft.getMinecraft().getTextureManager(), world, x, y, z, arr[0]);
        }
    }
}
