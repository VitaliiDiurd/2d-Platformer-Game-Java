package objects;

import main.Game;

import java.awt.*;

/**
 * The Coin class represents a collectible coin object in the game.
 */
public class Coin extends GameObject {
    public Coin(int x, int y, int objectType) {
        super(x, y, objectType);
        doAni = true;
        initHitbox(7, 14);
        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);
    }

    @Override
    public void draw(Graphics g, int lvlOffset) {

    }

    @Override
    public void update() {
        updateAnimationTick();
    }
}
