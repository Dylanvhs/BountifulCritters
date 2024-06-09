package net.dylanvhs.bountiful_critters.particles.client;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;

public class NeonShineParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private NeonShineParticle(ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite, SpriteSet spriteSet) {
        super(clientWorld, xPos, yPos, zPos);
        this.gravity = 0.75F;
        this.friction = 0.999F;
        this.xd *= (double)0.8F;
        this.yd *= (double)0.8F;
        this.zd *= (double)0.8F;
        this.yd = (double)(this.random.nextFloat() * 0.4F + 0.05F);
        this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
        this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        this.sprite = sprite;
        this.sprites = spriteSet;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        this.checkForDeletion();
        if (!this.removed && !this.onGround) {
            this.setSpriteFromAge(this.sprites);
        }
    }

    protected void checkForDeletion() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprite) {
            this.sprites = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
            return new NeonShineParticle(clientWorld, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed, sprites.get(clientWorld.random), sprites);
        }
    }
}
