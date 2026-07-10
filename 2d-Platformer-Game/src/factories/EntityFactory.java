package factories;

import entities.Entity;
import gamestates.Playing;

/**
 * Interface for factories.
 */
public interface EntityFactory {
    Entity createEntity(EntityType type, float x, float y, Playing playing);
}
