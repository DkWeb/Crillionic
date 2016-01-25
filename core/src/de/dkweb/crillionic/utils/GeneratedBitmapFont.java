package de.dkweb.crillionic.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GeneratedBitmapFont {
	private BitmapFont font;
	public GeneratedBitmapFont(BitmapFont font) {
		this.font = font;
		float r = 0.0f;
		float g = 0.0f;
		float b = 0.0f;
		float a = 1.0f;
		// We have to fiddle here a little bit around, to make sure,
		// that the cached font color is updated correctly.
		font.setColor(r, g, b, a);
		font.getColor().r = r;
		font.getColor().g = g;
		font.getColor().b = b;
		font.getColor().a = a;
		font.getCache().setColor(r, g, b, a);
	}
	
	public BitmapFont getFont() {
		return font;
	}
}
