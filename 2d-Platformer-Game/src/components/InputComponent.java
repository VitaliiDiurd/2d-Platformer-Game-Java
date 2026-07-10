package components;

import static utilz.Constants.PlayerConstants.*;
/**
 * Component that handles player input and updates the entity's animation.
 */
public class InputComponent extends Component {
    private MovementComponent movement;
    private AnimationComponent animation;
    private PhysicsComponent physics;

    /**
     * Updates the entity's animation based on its movement and attack state.
     */
    @Override
    public void update() {
        if (animation.isAttacking()) {
            animation.setAnimation(ATTACK, 30);
            return;
        }
        if (movement.isMoving()) {
            animation.setAnimation(RUNNING, 40);
        } else {
            animation.setAnimation(IDLE, 50);
        }

    }

    /**
     * Sets the owner entity and retrieves necessary components.
     * @param owner
     */
    @Override
    public void setOwner(entities.Entity owner) {
        super.setOwner(owner);
        this.movement = owner.getComponent(MovementComponent.class);
        this.animation = owner.getComponent(AnimationComponent.class);
        this.physics = owner.getComponent(PhysicsComponent.class);
    }
}
