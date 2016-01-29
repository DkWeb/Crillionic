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
package de.dkweb.crillionic.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class GraphicUtils {
    public int getRelativeWidth(int percent) {
        return (Gdx.graphics.getWidth() * percent) / 100;
    }

    public int getRelativeHeight(int percent) {
        return (Gdx.graphics.getHeight() * percent) / 100;
    }

    /*
    * See also:
    * https://developerover30.wordpress.com/2014/11/11/libgdx-creating-a-rounded-rectangle-pixmap/
    */
    public Pixmap createRoundedCornerPixmap(int radius) {
        Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth() / 2, (int) Gdx.graphics.getHeight() / 5, Pixmap.Format
                .RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight() - 2 * radius);
        pixmap.fillRectangle(radius, 0, pixmap.getWidth() - 2 * radius, pixmap.getHeight());
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(radius, pixmap.getHeight() - radius, radius);
        pixmap.fillCircle(pixmap.getWidth() - radius, radius, radius);
        pixmap.fillCircle(pixmap.getWidth() - radius, pixmap.getHeight() - radius, radius);
        return pixmap;
    }
}
