package de.dkweb.crillionic.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.utils.Assets;

/**
 * Created by dirkweber
 */
public class StatisticRenderer {
    public void renderGameStatistics(GameStatistics statistics, SpriteBatch batchWithoutProjection, Assets assets) {
        I18NBundle bundle = assets.getBundle();
        String lifes = bundle.format("lifes", statistics.getLifes());
        String score = bundle.format("score", statistics.getScore());
        String level = bundle.format("level", statistics.getLevel());

        BitmapFont font = assets.getStandardBitmapFont();
        font.setColor(Color.RED);
        GlyphLayout layout = new GlyphLayout(font, lifes, Color.RED, 200, Align.left, false);
        font.draw(batchWithoutProjection, layout, Gdx.graphics.getWidth() / 10,
                Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 20));

        layout = new GlyphLayout(font, score, Color.RED, 200, Align.left, false);
        font.draw(batchWithoutProjection, layout, (Gdx.graphics.getWidth() / 2) - (layout.width / 2),
                Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 20));

        layout = new GlyphLayout(font, level, Color.RED, 200, Align.left, false);
        font.draw(batchWithoutProjection, layout, Gdx.graphics.getWidth() - layout.width - Gdx.graphics.getWidth() / 10,
                Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 20));
    }
}
