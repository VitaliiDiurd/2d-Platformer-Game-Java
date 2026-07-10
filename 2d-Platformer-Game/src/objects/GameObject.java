package objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.ObjectConstants.*;

/**
 * The GameObject class represents a general game object that has a position, hitbox,
 *  * and animation.
 */
public abstract class GameObject implements IGameObject {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAni, active = true;
    protected int aniTick, aniIndex, enemyState, enemyType;
    protected int xDrawOffset, yDrawOffset;
    protected int aniSpeed = 25;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    /**
     * Updates the animation tick and progresses the animation if necessary.
     */
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objectType)) {
                aniIndex = 0;
                if(objectType == BARREL) {
                    doAni = false;
                    active = false;
                }
            }
        }
    }

    /**
     * Initializes the hitbox of the object with the specified width and height.
     * @param width
     * @param height
     */
    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    /**
     * Resets the animation and object state.
     */
    @Override
    public void reset() {
        aniIndex = 0;
        aniTick = 0;
        active = true;

        if(objectType == BARREL)
            doAni = false;
        else
            doAni = true;
    }

    public void drawHitbox(Graphics g, int lvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x - lvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public int getyDrawOffset() { return yDrawOffset; }
    public int getxDrawOffset() { return xDrawOffset; }
    public int getObjectType() { return objectType; }
    public int getAniIndex() { return aniIndex; }
    public void setAni(boolean doAni) { this.doAni = doAni; }
    public void setActive(boolean active) { this.active = active; }
    @Override
    public boolean isActive() { return active; }
    @Override
    public Rectangle2D.Float getHitbox() { return hitbox; }

    public abstract void draw(Graphics g, int lvlOffset);
}
