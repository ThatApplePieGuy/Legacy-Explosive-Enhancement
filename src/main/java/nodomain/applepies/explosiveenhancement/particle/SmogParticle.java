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

public class SmogParticle extends EntityFX {
    private static final ResourceLocation SMOG = new ResourceLocation(modid, "smog.png");
    private final TextureManager manager;

    protected SmogParticle(TextureManager textureManager, World world, double x, double y, double z, double motX, double motY, double motZ) {
        super(world, x, y, z);
        this.manager = textureManager;

        this.motionX = motX;
        this.motionY = motY;
        this.motionZ = motZ;

        this.particleMaxAge = 35 + this.rand.nextInt(20);
        this.particleScale = 0.8f * (float) Config.getScale();
    }

    @Override
    public void renderParticle(WorldRenderer wr, Entity entity, float tickDelta, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
        float frames = 12;
        int frameIndex = getFrameIndex(particleAge, particleMaxAge, (int) frames);
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

        manager.bindTexture(SMOG);

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
        wr.pos((x + rotX + rotYZ ), (y + rotXZ), (z + rotZ + rotXY))
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

    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (particleAge++ >= particleMaxAge) {
            this.setDead();
        } else {
            if (particleAge == 10) {
                motionX = 0;
                motionY = 0.05;
                motionZ = 0;
            }
            this.moveEntity(motionX, motionY, motionZ);
        }
    }

    @Override
    public int getFXLayer() {
        return 2;
    }

    public static class Factory implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int id, World world, double x, double y, double z, double motX, double motY, double motZ, int... arr) {
            return new SmogParticle(Minecraft.getMinecraft().getTextureManager(), world, x, y, z, motX, motY, motZ);
        }
    }
}
