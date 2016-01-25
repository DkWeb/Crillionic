package de.dkweb.crillionic.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

public class FreetypeAssetLoader extends SynchronousAssetLoader<GeneratedBitmapFont, FreetypeAssetLoader.WrappedFreeTypeFontParameter> {
	private static final String FONT_DIR = "fonts/";
	
	public FreetypeAssetLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public GeneratedBitmapFont load(AssetManager assetManager, String fileName,
							FileHandle file, WrappedFreeTypeFontParameter parameter) {
		String[] fontNameAndSizeAndFlipped = fileName.split(";");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_DIR + fontNameAndSizeAndFlipped[0]));
		assert Integer.valueOf(fontNameAndSizeAndFlipped[1]).intValue() == parameter.getFreeTypeFontParameter().size : "Artificial font name and parameter object vary in font size!. Parameter object wins!";
		assert Boolean.valueOf(fontNameAndSizeAndFlipped[2]).booleanValue() == parameter.getFreeTypeFontParameter().flip : "Artificial font name and parameter object vary in flip property!. Parameter object wins!";;
		BitmapFont bitmapFont = generator.generateFont(parameter.getFreeTypeFontParameter());
		generator.dispose();
		return new GeneratedBitmapFont(bitmapFont);		
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, WrappedFreeTypeFontParameter parameter) {
		return null;
	}	
	
	static public class WrappedFreeTypeFontParameter extends AssetLoaderParameters<GeneratedBitmapFont> {
		private FreeTypeFontParameter freeTypeFontParameter;
		
		public WrappedFreeTypeFontParameter(FreeTypeFontParameter freetypeFontParameter) {
			this.freeTypeFontParameter = freetypeFontParameter;
		}
		
		public FreeTypeFontParameter getFreeTypeFontParameter() {
			return freeTypeFontParameter;
		}
	}
}
