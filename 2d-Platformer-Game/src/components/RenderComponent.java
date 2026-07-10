package components;

import java.awt.Graphics;

/**
 * Component responsible for rendering an entity's animations.
 */
public class RenderComponent extends Component {
    private AnimationComponent animation;

    /**
     * Updates the render component.
     */
    @Override
    public void update() {
    }

    /**
     * Sets the owner of this component.
     * @param owner
     */
    @Override
    public void setOwner(entities.Entity owner) {
        super.setOwner(owner);
        this.animation = owner.getComponent(AnimationComponent.class);
    }

    /**
     * Renders the entity's animation.
     * @param g
     * @param lvlOffset
     */
    public void render(Graphics g, int lvlOffset) {
        animation.render(g, lvlOffset);
    }
}