package objects;

import main.Game;

import java.awt.*;

/**
 * The GameContainer class represents a container object in the game
 */
public class GameContainer extends GameObject {
    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(23, 25);
        xDrawOffset = (int) (8 * Game.SCALE);
        yDrawOffset = (int) (5 * Game.SCALE);

        hitbox.y += yDrawOffset + (int) (Game.SCALE * 2);
        hitbox.x += xDrawOffset / 2;
    }

    @Override
    public void draw(Graphics g, int lvlOffset) {

    }

    /**
     * Updates the container's animation state.
     */
    @Override
    public void update() {
        if (doAni)
            updateAnimationTick();
    }
}
