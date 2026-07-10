package components;

import entities.Entity;
/**
 * Abstract base class for all components that can be attached to an entity.
 */
public abstract class Component {
    protected Entity owner;

    /**
     * Sets owner of component.
     * @param owner
     */
    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    /**
     * Updates the component's logic every game tick.
     */
    public abstract void update();

}