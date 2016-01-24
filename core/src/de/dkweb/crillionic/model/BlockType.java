package de.dkweb.crillionic.model;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by dirkweber
 *
 * The various types of blocks
 */
public enum BlockType {
    NORMAL {
        @Override
        public int getScore() {
            return 0;
        }

        @Override
        public Color getColor() {
            return Color.GRAY;
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
        public Color getColor() {
            return Color.RED;
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
        public Color getColor() {
            return Color.BLUE;
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
        public Color getColor() {
            return Color.GREEN;
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
    RED {
        @Override
        public int getScore() {
            return SCORE_PER_BLOCK;
        }

        @Override
        public Color getColor() {
            return Color.RED;
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
    GREEN {
        @Override
        public int getScore() {
            return SCORE_PER_BLOCK;
        }

        @Override
        public Color getColor() {
            return Color.GREEN;
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
    BLUE {
        @Override
        public int getScore() {
            return SCORE_PER_BLOCK;
        }

        @Override
        public Color getColor() {
            return Color.BLUE;
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
        public Color getColor() {
            return Color.BLACK;
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
    };

    private final static int SCORE_PER_BLOCK = 1000;
    public abstract int getScore();
    public abstract Color getColor();
    public abstract boolean canBeDestroyed();
    public abstract boolean destroysPlayer();
    public abstract boolean isColorizing();
}
