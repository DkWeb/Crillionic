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
