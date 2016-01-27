package de.dkweb.crillionic.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import de.dkweb.crillionic.Crillionic;
import de.dkweb.crillionic.utils.Assets;

/**
 * Created by dirkweber
 */
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
        skin.add("default", assets.getStandardBitmapFont());

        //Create a texture
        Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth()/4, (int) Gdx.graphics.getHeight()/10, Pixmap.Format
                .RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("btnBackground", new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("btnBackground", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("btnBackground", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("btnBackground", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("btnBackground", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Image img = new Image(assets.getTexture(Assets.BACKGROUND));
        img.setFillParent(true);
        stage.addActor(img);

        createBasicSkin();
        TextButton newGameButton = new TextButton("New game", skin);
        newGameButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 2);
        newGameButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.startLevel();
                return true;
            }

        });
        stage.addActor(newGameButton);
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
