package objects;

import java.awt.geom.Rectangle2D;

/**
 * Interface for GameObjects.
 */
public interface IGameObject {
    void update();
    void reset();
    Rectangle2D.Float getHitbox();
    boolean isActive();
}

