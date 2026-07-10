package objects;

/**
 * The GameObjectFactory class is responsible for creating instances of game objects
 *  * based on their type.
 */
public class GameObjectFactory {

    public static IGameObject createGameObject(int x, int y, int objectType) {
        switch (objectType) {
            case utilz.Constants.ObjectConstants.RED_POTION:
            case utilz.Constants.ObjectConstants.BLUE_POTION:
                return new Potion(x, y, objectType);
            case utilz.Constants.ObjectConstants.BARREL:
                return new GameContainer(x, y, objectType);
            case utilz.Constants.ObjectConstants.TRAP:
                return new Trap(x, y, objectType);
            default:
                throw new IllegalArgumentException("Unknown object type: " + objectType);
        }
    }
}
