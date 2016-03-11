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
package de.dkweb.crillionic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.dkweb.crillionic.Crillionic;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.utils.*;

public class MenuScreen implements Screen {
    private Skin skin;
    private Stage stage;
    private Assets assets;
    private Crillionic game;

    public MenuScreen(Crillionic game, Assets assets) {
        this.game = game;
        this.assets = assets;
    }

    private void createBasicSkin() {
        skin = new Skin();
        skin.add("default", assets.getBigBitmapFont());

        Pixmap pixmap = new GraphicUtils().createRoundedCornerPixmap(GlobalConstants.BUTTON_ROUNDED_CORNER_RADIUS);
        skin.add("btnBackground", new Texture(pixmap));

        // Create a Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("btnBackground", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("btnBackground", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("btnBackground", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("btnBackground", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        skin.add("default", labelStyle);
    }



    @Override
    public void show() {
        GraphicUtils graphicUtils = new GraphicUtils();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Image img = new Image(assets.getTexture(Assets.BACKGROUND));
        img.setFillParent(true);
        stage.addActor(img);

        createBasicSkin();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label("Crillionic", skin)).spaceBottom(graphicUtils.getRelativeHeight(10));
        table.row();

        I18NBundle bundle = assets.getBundle();
        TextButton newGameButton = new TextButton(bundle.get("new_game"), skin);
        newGameButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.startLevel(getInitialGameStatistics());
                return true;
            }

        });
        table.add(newGameButton).width(graphicUtils.getRelativeWidth(80)).spaceBottom(graphicUtils.getRelativeHeight
                (5));
        table.row();

        TextButton openHighscoreButton = new TextButton(bundle.get("open_highscore"), skin);
        openHighscoreButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.openHighscore();
                return true;
            }

        });
        table.add(openHighscoreButton).width(graphicUtils.getRelativeWidth(80));
        Gdx.input.setInputProcessor(stage);
    }

    private GameStatistics getInitialGameStatistics() {
        LevelMap map = new LevelFactory().createLevel(1, new JsonManager());
        return new GameStatistics(0, map.getLevelId(), GlobalConstants.INITIAL_LIFES,
                map.getColoredBlocks().size(), GlobalConstants.TIME_PER_LEVEL);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }



    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
