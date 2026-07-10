package levels;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import entities.EnemyManager;
import entities.Player;
import gamestates.GameState;
import gamestates.Playing;
import main.Game;
import objects.ObjectManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class LevelManagerTest {

    private LevelManager levelManager;
    private Game gameMock;
    private Playing playingMock;
    private EnemyManager enemyManagerMock;
    private Player playerMock;
    private ObjectManager objectManagerMock;
    private List<Level> testLevels;

    @BeforeEach
    void setUp() throws Exception {
        gameMock = mock(Game.class);
        playingMock = mock(Playing.class);
        enemyManagerMock = mock(EnemyManager.class);
        playerMock = mock(Player.class);
        objectManagerMock = mock(ObjectManager.class);

        when(gameMock.getPlaying()).thenReturn(playingMock);
        when(playingMock.getEnemyManager()).thenReturn(enemyManagerMock);
        when(playingMock.getPlayer()).thenReturn(playerMock);
        when(playingMock.getObjectManager()).thenReturn(objectManagerMock);

        Level levelMock1 = mock(Level.class);
        when(levelMock1.getLvlData()).thenReturn(new int[0][0]);
        when(levelMock1.getLvlOffset()).thenReturn(100);

        Level levelMock2 = mock(Level.class);
        when(levelMock2.getLvlData()).thenReturn(new int[0][0]);
        when(levelMock2.getLvlOffset()).thenReturn(200);

        testLevels = new ArrayList<>();
        testLevels.add(levelMock1);
        testLevels.add(levelMock2);

        levelManager = new LevelManager(gameMock) {
            protected void buildAllLevels() {
            }

            protected void importLevelSprites() {
            }
        };

        Field levelsField = LevelManager.class.getDeclaredField("levels");
        levelsField.setAccessible(true);
        levelsField.set(levelManager, testLevels);
    }

    @Test
    void loadNextLevel_ShouldIncrementLevelIndex() throws Exception {
        assertEquals(0, getCurrentLevelIndex());

        levelManager.loadNextLevel();

        assertEquals(1, getCurrentLevelIndex());
        verify(enemyManagerMock).loadEnemies(testLevels.get(1));
        verify(playerMock).loadLvlData(testLevels.get(1).getLvlData());
        verify(objectManagerMock).loadObjects(testLevels.get(1));
        verify(playingMock).setMaxLvlOffset(testLevels.get(1).getLvlOffset());
    }

    @Test
    void loadNextLevel_WhenLastLevel_ShouldResetToFirstLevel() throws Exception {
        setCurrentLevelIndex(1);

        levelManager.loadNextLevel();

        assertEquals(0, getCurrentLevelIndex());
        assertEquals(GameState.MENU, GameState.state);
    }

    private int getCurrentLevelIndex() throws Exception {
        Field lvlIndexField = LevelManager.class.getDeclaredField("lvlIndex");
        lvlIndexField.setAccessible(true);
        return (int) lvlIndexField.get(levelManager);
    }

    private void setCurrentLevelIndex(int index) throws Exception {
        Field lvlIndexField = LevelManager.class.getDeclaredField("lvlIndex");
        lvlIndexField.setAccessible(true);
        lvlIndexField.set(levelManager, index);
    }
}