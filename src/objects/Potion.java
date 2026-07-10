package objects;

import main.Game;

import java.awt.*;

/**
 * The Potion class represents a potion object in the game.
 */
public class Potion extends GameObject {
    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAni = true;
        initHitbox(7, 14);
        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);
    }

    @Override
    public void draw(Graphics g, int lvlOffset) {

    }

    /**
     * Updates the potion's animation state.
     */
    @Override
    public void update() {
        updateAnimationTick();
    }
}
