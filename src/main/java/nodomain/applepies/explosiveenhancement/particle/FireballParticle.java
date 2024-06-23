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

public class FireballParticle extends EntityFX {
    private static final ResourceLocation FIREBALL = new ResourceLocation(modid, "fireball.png");
    private static final ResourceLocation FIREBALL_UNDERWATER = new ResourceLocation(modid, "fireball_underwater.png");
    private final TextureManager manager;
    private final boolean underwater;

    protected FireballParticle(TextureManager textureManager, World world, double x, double y, double z, int underwater) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.manager = textureManager;
        this.underwater = underwater == 1;

        this.motionX = this.motionY = this.motionZ = 0.0;
        this.particleMaxAge = 10;
        this.particleScale = 3f * (float) Config.getScale();
    }

    @Override
    public void renderParticle(WorldRenderer wr, Entity entity, float tickDelta, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
        float frames = underwater ? 16 : 9;
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

        manager.bindTexture(underwater ? FIREBALL_UNDERWATER : FIREBALL);

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

    @Override
    public int getFXLayer() {
        return 1;
    }

    public static class Factory implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int id, World world, double x, double y, double z, double motX, double motY, double motZ, int... arr) {
            return new FireballParticle(Minecraft.getMinecraft().getTextureManager(), world, x, y, z, arr[0]);
        }
    }
}
