package components;

import main.Game;
import static utilz.HelpMethods.*;

/**
 * Component responsible for handling horizontal movement and direction of an entity.
 */
public class MovementComponent extends Component {
    private float speed = 1.0f * Game.SCALE;
    private boolean left = false;
    private boolean right = false;
    private boolean moving = false;
    private int[][] lvlData;
    private int flipX = 0;
    private int flipW = 1;

    public MovementComponent() {}

    /**
     * Updates the entity's horizontal position based on movement input.
     */
    @Override
    public void update() {
        moving = false;

        if (!left && !right && !owner.getComponent(PhysicsComponent.class).isInAir())
            return;

        if (left && right)
            return;

        float xSpeed = 0;

        if (left) {
            xSpeed -= speed;
        }

        if (right && !left)
            xSpeed += speed;

        if (xSpeed < 0) {
            flipX = 1;
            flipW = -1;
        } else if (xSpeed > 0) {
            flipX = 0;
            flipW = 1;
        }


        updateXPos(xSpeed);
        moving = (xSpeed != 0);
    }

    /**
     * Updates the x-position of the entity.
     * @param xSpeed
     */
    private void updateXPos(float xSpeed) {
        if (CanMoveHere(owner.getHitbox().x + xSpeed, owner.getHitbox().y,
                owner.getHitbox().width, owner.getHitbox().height, lvlData)) {
            owner.getHitbox().x += xSpeed;
        } else {
            owner.getHitbox().x = GetEntPosWall(owner.getHitbox(), xSpeed);
        }
    }

    /**
     * Loads the level data used for collision checks.
     * @param lvlData
     */
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    /**
     * Resets the left and right movement flags.
     */
    public void resetDirBooleans() {
        left = false;
        right = false;
    }


    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
    public int getFlipX() {
        return flipX;
    }

    public int getFlipW() {
        return flipW;
    }

    public boolean isMoving() {
        return moving;
    }
}