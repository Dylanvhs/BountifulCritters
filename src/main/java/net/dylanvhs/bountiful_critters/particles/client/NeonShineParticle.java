package net.dylanvhs.bountiful_critters.particles.client;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class NeonShineParticle extends TextureSheetParticle {
    static final RandomSource RANDOM = RandomSource.create();
    private final SpriteSet sprites;

    NeonShineParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, TextureAtlasSprite sprite, SpriteSet spriteSet) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.friction = 0.96F;
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.5F;
        this.xd *= (double)0.02F;
        this.yd *= (double)0.02F;
        this.zd *= (double)0.02F;
        this.hasPhysics = false;
        this.sprite = sprite;
        this.sprites = spriteSet;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getLightColor(float pPartialTick) {
        float f = ((float)this.age + pPartialTick) / (float)this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(pPartialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public void tick() {
        super.tick();
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.99D;
        this.yd *= 0.99D;
        this.zd *= 0.99D;
        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprite) {
            this.sprites = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
            NeonShineParticle shineParticle = new NeonShineParticle(clientWorld, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed, sprites.get(clientWorld.random), sprites);
            shineParticle.setAlpha(1.0F - clientWorld.random.nextFloat() * 0.7F);
            return shineParticle;
        }
    }
}

