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
import de.dkweb.crillionic.model.Highscore;
import de.dkweb.crillionic.utils.*;

public class HighscoreScreen implements Screen {
    private Skin skin;
    private Stage stage;
    private Assets assets;
    private Crillionic game;

    public HighscoreScreen(Crillionic game, Assets assets) {
        this.game = game;
        this.assets = assets;
    }

    private void createBasicSkin() {
        skin = new Skin();
        skin.add("default", assets.getBigBitmapFont());

        //Create a texture
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
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Image img = new Image(assets.getTexture(Assets.BACKGROUND));
        img.setFillParent(true);
        stage.addActor(img);

        createBasicSkin();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label("Highscore", skin));
        table.row();
        Highscore currentHighscore = new HighscoreManager(new FileUtils()).getHighscore(new JsonManager());
        for (Integer score : currentHighscore.getScores()) {
            table.add(new Label(String.valueOf(score), skin));
            table.row();
        }

        I18NBundle bundle = assets.getBundle();
        TextButton closeButton = new TextButton(bundle.get("close"), skin);
        closeButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.openMainMenu();
                return true;
            }

        });
        table.add(closeButton).spaceTop(new GraphicUtils().getRelativeHeight(5));
        Gdx.input.setInputProcessor(stage);
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
