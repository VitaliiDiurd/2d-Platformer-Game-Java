package objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameObjectFactoryTest {

    @Test
    void createPotionRed() {
        IGameObject obj = GameObjectFactory.createGameObject(10, 20, utilz.Constants.ObjectConstants.RED_POTION);
        assertTrue(obj instanceof Potion);
    }

    @Test
    void createPotionBlue() {
        IGameObject obj = GameObjectFactory.createGameObject(15, 25, utilz.Constants.ObjectConstants.BLUE_POTION);
        assertTrue(obj instanceof Potion);
    }

    @Test
    void createGameContainer() {
        IGameObject obj = GameObjectFactory.createGameObject(30, 40, utilz.Constants.ObjectConstants.BARREL);
        assertTrue(obj instanceof GameContainer);
    }

    @Test
    void createTrap() {
        IGameObject obj = GameObjectFactory.createGameObject(50, 60, utilz.Constants.ObjectConstants.TRAP);
        assertTrue(obj instanceof Trap);
    }

    @Test
    void createUnknownObjectThrowsException() {
        int invalidType = -1;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            GameObjectFactory.createGameObject(0, 0, invalidType);
        });
        assertEquals("Unknown object type: " + invalidType, exception.getMessage());
    }
}
