package de.dkweb.crillionic.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Assets {
	public static final String BALL_TEXTURE = "ball_less_color.png";
	public static final String BLOCK_TEXTURE = "block.png";
	public static final String BORDER_TEXTURE = "wood.jpg";
    public static final String BACKGROUND = "background.jpg";

    private static final String EFFECTS_FOLDER = "effects";
	private static final String EXPLOSION_EFFECT = "explosion.p";
	private static final String STANDARD_FONT_NAME = "Roboto-Regular.ttf";
	private static final int FONT_SIZE = 26;
	private AssetManager assetManager;
	private boolean allLoaded;
    private ParticleEffectPool explosionEffectPool;

	public Assets() {
		allLoaded = false;
		assetManager = new AssetManager();
		assetManager.setLoader(GeneratedBitmapFont.class, new FreetypeAssetLoader(new InternalFileHandleResolver()));
		loadAllFonts();
		loadTexture(BALL_TEXTURE);
		loadTexture(BLOCK_TEXTURE);
		loadTexture(BORDER_TEXTURE);
		loadTexture(BACKGROUND);
        loadParticleEffects();
		assetManager.finishLoading();
		allLoaded = true;
	}
	
    public void loadParticleEffects() {
        ParticleEffect explosionEffect = new ParticleEffect();
        explosionEffect.load(Gdx.files.internal(EFFECTS_FOLDER + "/" + EXPLOSION_EFFECT), Gdx.files.internal
                (EFFECTS_FOLDER));
        explosionEffectPool = new ParticleEffectPool(explosionEffect, 1, 50);
    }

	private void loadTexture(String path) {
		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.Linear;
		param.magFilter = TextureFilter.Linear;	
		assetManager.load(path, Texture.class);
	}
	
	public BitmapFont getStandardBitmapFont() {
		return assetManager.get(STANDARD_FONT_NAME + ";" + String.valueOf(FONT_SIZE) + ";" + String.valueOf(false), GeneratedBitmapFont.class).getFont();		
	}
	
	private void createFontInMemory(int pixelSize, String fontName, boolean flipped) {
		FreeTypeFontGenerator.FreeTypeFontParameter fontDesc = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontDesc.size = pixelSize;
		fontDesc.flip = flipped;
		assetManager.load(fontName + ";" + pixelSize + ";" + String.valueOf(flipped), GeneratedBitmapFont.class, new FreetypeAssetLoader.WrappedFreeTypeFontParameter(fontDesc));
	}	
	
	private void loadAllFonts() {
		createFontInMemory(FONT_SIZE, STANDARD_FONT_NAME, false);
	}
	
	public Texture getTexture(String name) {
		return assetManager.get(name);
	}

	public boolean finishedLoading() {
		return allLoaded && assetManager.update();
    }

    public ParticleEffectPool.PooledEffect getExplosionEffect() {
        return explosionEffectPool.obtain();
	}

    public void freeEffect(ParticleEffectPool.PooledEffect effect) {
        explosionEffectPool.free(effect);
    }
}
