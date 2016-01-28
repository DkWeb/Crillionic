/**
 * The MIT License (MIT)
 * Copyright (c) 2016 Dirk Weber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.dkweb.crillionic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.dkweb.crillionic.input.SimpleInputProcessor;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.map.MapObject;
import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.screens.HighscoreScreen;
import de.dkweb.crillionic.screens.LevelScreen;
import de.dkweb.crillionic.screens.MenuScreen;
import de.dkweb.crillionic.utils.Assets;
import de.dkweb.crillionic.utils.GlobalConstants;
import de.dkweb.crillionic.utils.JsonManager;

import java.util.ArrayList;
import java.util.List;

public class Crillionic extends Game {
    private Assets assets;

	@Override
	public void create () {
        assets = new Assets();
		setScreen(new MenuScreen(this, assets));
	}

    public void startLevel() {
        setScreen(new LevelScreen(this, assets));
    }

    public void openHighscore() {
        setScreen(new HighscoreScreen(this, assets));
    }

    public void openMainMenu() {
        setScreen(new MenuScreen(this, assets));
    }
}
