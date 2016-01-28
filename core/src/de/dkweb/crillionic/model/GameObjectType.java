package de.dkweb.crillionic.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import de.dkweb.crillionic.utils.GlobalConstants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dirkweber
 *
 * The various types of game objects
 */
public enum GameObjectType {
    NORMAL_BLOCK {
        @Override
        public int getScore() {
            return 0;
        }

        @Override
        public Color getDefaultColor() {
            return null;
        }

        @Override
        public boolean canBeDestroyed() {
            return false;
        }

        @Override
        public boolean destroysPlayer() {
            return false;
        }

        @Override
        public boolean isColorizing() {
            return false;
        }
    },
    COLORIZE_RED {
        @Override
        public int getScore() {
            return 0;
        }

        @Override
        public Color getDefaultColor() {
            return new Color(1f, 0f, 0f, ALPHA_VALUE);
        }

        @Override
        public boolean canBeDestroyed() {
            return false;
        }

        @Override
        public boolean destroysPlayer() {
            return false;
        }

        @Override
        public boolean isColorizing() {
            return true;
        }
    },
    COLORIZE_BLUE {
        @Override
        public int getScore() {
            return 0;
        }

        @Override
        public Color getDefaultColor() {
            return new Color(0f, 0f, 1f, ALPHA_VALUE);
        }

        @Override
        public boolean canBeDestroyed() {
            return false;
        }

        @Override
        public boolean destroysPlayer() {
            return false;
        }

        @Override
        public boolean isColorizing() {
            return true;
        }
    },
    COLORIZE_GREEN {
        @Override
        public int getScore() {
            return 0;
        }

        @Override
        public Color getDefaultColor() {
            return new Color(0f, 1f, 0f, ALPHA_VALUE);
        }

        @Override
        public boolean canBeDestroyed() {
            return false;
        }

        @Override
        public boolean destroysPlayer() {
            return false;
        }

        @Override
        public boolean isColorizing() {
            return true;
        }
    },
    RED_BLOCK {
        @Override
        public int getScore() {
            return SCORE_PER_BLOCK;
        }

        @Override
        public Color getDefaultColor() {
            return new Color(1f, 0f, 0f, ALPHA_VALUE);
        }

        @Override
        public boolean canBeDestroyed() {
            return true;
        }

        @Override
        public boolean destroysPlayer() {
            return false;
        }

        @Override
        public boolean isColorizing() {
            return false;
        }
    },
    GREEN_BLOCK {
        @Override
        public int getScore() {
            return SCORE_PER_BLOCK;
        }

        @Override
        public Color getDefaultColor() {
            return new Color(0f, 0f, 1f, ALPHA_VALUE);
        }

        @Override
        public boolean canBeDestroyed() {
            return true;
        }

        @Override
        public boolean destroysPlayer() {
            return false;
        }

        @Override
        public boolean isColorizing() {
            return false;
        }
    },
    BLUE_BLOCK {
        @Override
        public int getScore() {
            return SCORE_PER_BLOCK;
        }

        @Override
        public Color getDefaultColor() {
            return new Color(0f, 0f, 0f, ALPHA_VALUE);
        }

        @Override
        public boolean canBeDestroyed() {
            return true;
        }

        @Override
        public boolean destroysPlayer() {
            return false;
        }

        @Override
        public boolean isColorizing() {
            return false;
        }
    },
    KILLER {
        @Override
        public int getScore() {
            return 0;
        }

        @Override
        public Color getDefaultColor() {
            return new Color(0xF44336AA);
        }

        @Override
        public boolean canBeDestroyed() {
            return false;
        }

        @Override
        public boolean destroysPlayer() {
            return true;
        }

        @Override
        public boolean isColorizing() {
            return false;
        }
    },
    PLAYER {
        @Override
        public int getScore() {
            return 0;
        }

        @Override
        public Color getDefaultColor() {
            return null;
        }

        @Override
        public boolean canBeDestroyed() {
            return false;
        }

        @Override
        public boolean destroysPlayer() {
            return false;
        }

        @Override
        public boolean isColorizing() {
            return false;
        }
    },
    BORDER {
        @Override
        public int getScore() {
            return 0;
        }

        @Override
        public Color getDefaultColor() {
            return null;
        }

        @Override
        public boolean canBeDestroyed() {
            return false;
        }

        @Override
        public boolean destroysPlayer() {
            return false;
        }

        @Override
        public boolean isColorizing() {
            return false;
        }
    };
    public final static List<GameObjectType> COLORED_BLOCKS = Arrays.asList(new GameObjectType[] {RED_BLOCK, GREEN_BLOCK, BLUE_BLOCK});
    public final static List<GameObjectType> COLORIZE_BLOCKS = Arrays.asList(new GameObjectType[] { COLORIZE_RED, COLORIZE_GREEN, COLORIZE_BLUE });

    private final static int SCORE_PER_BLOCK = 1000;
    private final static float ALPHA_VALUE = 0.7f;
    public abstract int getScore();
    public abstract Color getDefaultColor();
    public abstract boolean canBeDestroyed();
    public abstract boolean destroysPlayer();
    public abstract boolean isColorizing();

    public static GameObjectType getColoredBlockFor(Color color) {
        for (GameObjectType type : COLORED_BLOCKS) {
            if (type.getDefaultColor().equals(color)) {
                return type;
            }
        }
        Gdx.app.log(GlobalConstants.APP_TAG, "Unable to find colored block for color " + color.toString());
        return null;
    }

    public static GameObjectType getColorizeBlockFor(Color color) {
        for (GameObjectType type : COLORIZE_BLOCKS) {
            if (type.getDefaultColor().equals(color)) {
                return type;
            }
        }
        Gdx.app.log(GlobalConstants.APP_TAG, "Unable to find colorize block for color " + color.toString());
        return null;
    }
}
