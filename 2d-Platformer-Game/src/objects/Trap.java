package objects;

import main.Game;

import java.awt.*;

/**
 * The Trap class represents a trap object in the game.
 */
public class Trap extends GameObject {
    public Trap(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16);
        hitbox.y += yDrawOffset;
    }

    @Override
    public void draw(Graphics g, int lvlOffset) {

    }

    @Override
    public void update() {
    }
}
