package utilz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void testObjectConstantsGetSpriteAmount() {
        assertEquals(7, Constants.ObjectConstants.GetSpriteAmount(Constants.ObjectConstants.RED_POTION));
        assertEquals(7, Constants.ObjectConstants.GetSpriteAmount(Constants.ObjectConstants.BLUE_POTION));
        assertEquals(8, Constants.ObjectConstants.GetSpriteAmount(Constants.ObjectConstants.BARREL));
        assertEquals(1, Constants.ObjectConstants.GetSpriteAmount(-1));
    }

    @Test
    void testEnemyConstantsGetSpriteAmount() {
        assertEquals(2, Constants.EnemyConstants.GetSpriteAmount(Constants.EnemyConstants.ORC, Constants.EnemyConstants.DEAD));
        assertEquals(2, Constants.EnemyConstants.GetSpriteAmount(Constants.EnemyConstants.ORC, Constants.EnemyConstants.IDLE));
        assertEquals(2, Constants.EnemyConstants.GetSpriteAmount(Constants.EnemyConstants.ORC, Constants.EnemyConstants.RUNNING));
        assertEquals(2, Constants.EnemyConstants.GetSpriteAmount(Constants.EnemyConstants.ORC, Constants.EnemyConstants.ATTACK));
        assertEquals(2, Constants.EnemyConstants.GetSpriteAmount(Constants.EnemyConstants.ORC, Constants.EnemyConstants.HIT));

        assertEquals(0, Constants.EnemyConstants.GetSpriteAmount(-1, -1));
    }

    @Test
    void testEnemyConstantsGetMaxHealth() {
        assertEquals(10, Constants.EnemyConstants.GetMaxHealth(Constants.EnemyConstants.ORC));
        assertEquals(1, Constants.EnemyConstants.GetMaxHealth(-1));
    }

    @Test
    void testEnemyConstantsGetEnemyDmg() {
        assertEquals(10, Constants.EnemyConstants.GetEnemyDmg(Constants.EnemyConstants.ORC));
        assertEquals(0, Constants.EnemyConstants.GetEnemyDmg(-1));
    }

    @Test
    void testPlayerConstantsGetSpriteAmount() {
        assertEquals(2, Constants.PlayerConstants.GetSpriteAmount(Constants.PlayerConstants.DEAD));
        assertEquals(2, Constants.PlayerConstants.GetSpriteAmount(Constants.PlayerConstants.RUNNING));
        assertEquals(2, Constants.PlayerConstants.GetSpriteAmount(Constants.PlayerConstants.IDLE));
        assertEquals(2, Constants.PlayerConstants.GetSpriteAmount(Constants.PlayerConstants.JUMP));
        assertEquals(2, Constants.PlayerConstants.GetSpriteAmount(Constants.PlayerConstants.FALLING));
        assertEquals(2, Constants.PlayerConstants.GetSpriteAmount(Constants.PlayerConstants.HIT));
        assertEquals(2, Constants.PlayerConstants.GetSpriteAmount(Constants.PlayerConstants.ATTACK));
        assertEquals(1, Constants.PlayerConstants.GetSpriteAmount(-1));
    }

    @Test
    void testConstantValues() {
        assertEquals(0, Constants.ObjectConstants.RED_POTION);
        assertEquals(1, Constants.ObjectConstants.BLUE_POTION);
        assertEquals(15, Constants.ObjectConstants.RED_POTION_VALUE);
        assertEquals(10, Constants.ObjectConstants.BLUE_POTION_VALUE);

        assertEquals(0, Constants.Directions.LEFT);
        assertEquals(2, Constants.Directions.RIGHT);
        assertEquals(1, Constants.Directions.UP);
        assertEquals(3, Constants.Directions.DOWN);
    }
}