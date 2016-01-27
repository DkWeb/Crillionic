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
}
