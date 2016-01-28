package de.dkweb.crillionic.utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by dirkweber
 */
public class GraphicUtils {
    public int getRelativeWidth(int percent) {
        return (Gdx.graphics.getWidth() * percent) / 100;
    }

    public int getRelativeHeight(int percent) {
        return (Gdx.graphics.getHeight() * percent) / 100;
    }
}
