package nodomain.applepies.explosiveenhancement.particle;

import nodomain.applepies.explosiveenhancement.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static nodomain.applepies.explosiveenhancement.ExplosiveEnhancement.getFrameIndex;
import static nodomain.applepies.explosiveenhancement.ExplosiveEnhancement.modid;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class BlastwaveParticle extends EntityFX {
    private static final ResourceLocation BLASTWAVE = new ResourceLocation(modid, "blastwave.png");
    private static final ResourceLocation BLASTWAVE_UNDERWATER = new ResourceLocation(modid, "blastwave_underwater.png");
    private final TextureManager manager;
    private final boolean underwater;

    protected BlastwaveParticle(TextureManager textureManager, World world, double x, double y, double z, int underwater) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.manager = textureManager;
        this.underwater = underwater == 1;

        this.motionX = this.motionY = this.motionZ = 0.0;
        this.particleMaxAge = 16;
        this.particleScale = 4f * (float) Config.getScale();
    }

    @Override
    public void renderParticle(WorldRenderer wr, Entity entity, float tickDelta, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
        float frames = 21;
        int frameIndex = getFrameIndex(particleAge, particleMaxAge, (int) frames);
        float minU = (frameIndex + 1) / frames;
        float maxU = frameIndex / frames;
        float minV = 0;
        float maxV = 1;

        float x = (float) (posX - interpPosX);
        float y = (float) (posY - interpPosY);
        float z = (float) (posZ - interpPosZ);

        manager.bindTexture(underwater ? BLASTWAVE_UNDERWATER : BLASTWAVE);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        wr.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        wr.pos(x - particleScale, y, z + particleScale)
                .tex(maxU, maxV)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .endVertex();
        wr.pos(x + particleScale, y, z + particleScale)
                .tex(maxU, minV)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .endVertex();
        wr.pos(x + particleScale, y, z - particleScale)
                .tex(minU, minV)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .endVertex();
        wr.pos(x - particleScale, y, z - particleScale)
                .tex(minU, maxV).color(particleRed, particleGreen, particleBlue, particleAlpha)
                .endVertex();

        wr.pos(x - particleScale, y, z - particleScale)
                .tex(minU, maxV)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .endVertex();
        wr.pos(x + particleScale, y, z - particleScale)
                .tex(minU, minV)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .endVertex();
        wr.pos(x + particleScale, y, z + particleScale)
                .tex(maxU, minV)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .endVertex();
        wr.pos(x - particleScale, y, z + particleScale)
                .tex(maxU, maxV)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .endVertex();

        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    public static class Factory implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int id, World world, double x, double y, double z, double motX, double motY, double motZ, int... arr) {
            return new BlastwaveParticle(Minecraft.getMinecraft().getTextureManager(), world, x, y, z, arr[0]);
        }
    }
}
