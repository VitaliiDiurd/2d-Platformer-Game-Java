package factories;

import components.*;
import entities.*;
import exeptions.ResourceLoadException;
import gamestates.Playing;

/**
 * Factory class responsible for creating different entities.
 */
public class DefaultEntityFactory implements EntityFactory {

    public Entity createEntity(EntityType type, float x, float y, Playing playing) {
        return switch (type) {
            case PLAYER -> createPlayer(x, y, playing);
            default -> throw new IllegalArgumentException("Unsupported entity type: " + type);
        };
    }

    /**
     * Creates and initializes a Player entity with the necessary components.
     * @param x
     * @param y
     * @param playing
     * @return
     */
    private Player createPlayer(float x, float y, Playing playing) {
        Player player = new Player(x, y, 32, 32, playing);
        try {
            player.addComponent(new AnimationComponent("/gandalf_atlas.png"));
        } catch (ResourceLoadException e) {
            throw new RuntimeException("Failed to load player animation", e);
        }
        player.addComponent(new PhysicsComponent());
        player.addComponent(new MovementComponent());
        player.addComponent(new InputComponent());
        player.addComponent(new RenderComponent());
        return player;
    }
}
