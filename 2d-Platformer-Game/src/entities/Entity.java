package entities;

import components.Component;
import components.MovementComponent;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 *  Abstract base class for all game entities.
 */
public abstract class Entity {

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rectangle2D.Float hitbox;
    protected Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = (int) (width * Game.SCALE);
        this.height = (int) (height * Game.SCALE);
    }

    /**
     * Updates all components attached to the entity.
     */
    public void update() {
        for (Component component : components.values()) {
            component.update();
        }
    }

    /**
     * Adds a component to the entity.
     * @param component
     */
    public void addComponent(Component component) {
        component.setOwner(this);
        components.put(component.getClass(), component);
    }

    /**
     * Retrieves the component of the specified class.
     * @param componentClass
     * @return
     * @param <T>
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        T component = componentClass.cast(components.get(componentClass));
        if (component == null) {
            throw new IllegalStateException("Component not found: " + componentClass.getSimpleName());
        }
        return component;
    }

    protected void drawHitbox(Graphics g, int lvlOffset) {
//        g.setColor(Color.RED);
//        g.drawRect((int) hitbox.x - lvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    /**
     * Initializes the entity's hitbox.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    protected void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    /**
     * Updates entity hitbox.
     */
    protected void updateHitbox() {
        hitbox.x = (int) x;
        hitbox.y = (int) y;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Resets the direction booleans to false.
     */
    public void resetDirBooleans() {
        setLeft(false);
        setRight(false);
    }

    public void setLeft(boolean left) {
        getComponent(MovementComponent.class).setLeft(left);
    }

    public void setRight(boolean right) {
        getComponent(MovementComponent.class).setRight(right);
    }
    public void resetAll() {
    }
}