package components;

import main.Game;
import static utilz.HelpMethods.*;

/**
 * Component responsible for handling gravity, jumping, and falling physics of an entity.
 */
public class PhysicsComponent extends Component {
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private float airSpeed = 0f;
    private boolean inAir = false;
    private boolean jump = false;
    private int[][] lvlData;

    public PhysicsComponent() {}

    /**
     * Updates the physics state, applying gravity or triggering a jump.
     */
    @Override
    public void update() {
        if (jump) {
            jump();
        }

        if (!inAir) {
            if (!IsEntOnFlour(owner.getHitbox(), lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {
            applyGravity();
        }
    }

    /**
     * Applies gravity to the entity, adjusting vertical position and handling collisions.
     */
    private void applyGravity() {
        if (CanMoveHere(owner.getHitbox().x, owner.getHitbox().y + airSpeed,
                owner.getHitbox().width, owner.getHitbox().height, lvlData)) {
            owner.getHitbox().y += airSpeed;
            airSpeed += gravity;
        } else {
            owner.getHitbox().y = GetEntYPosHitSmth(owner.getHitbox(), airSpeed);
            if (airSpeed > 0) {
                resetInAir();
            } else {
                airSpeed = fallSpeedAfterCollision;
            }
        }
    }

    /**
     * Initiates a jump if the entity is on the ground.
     */
    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    /**
     * Resets the in-air state after landing.
     */
    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    /**
     * Loads the level data.
     * @param lvlData
     */
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntOnFlour(owner.getHitbox(), lvlData))
            inAir = true;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isInAir() {
        return inAir;
    }
}