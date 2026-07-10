package objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import levels.LevelManager;
import objects.ObjectManager;
import objects.Potion;
import objects.Trap;
import objects.GameContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ObjectManagerTest {
    private ObjectManager objectManager;
    private Playing playingMock;
    private Level levelMock;

    @BeforeEach
    void setUp() {
        playingMock = mock(Playing.class);
        objectManager = new ObjectManager(playingMock);
        levelMock = mock(Level.class);
    }

    @Test
    void testInitialization_NotNullFields() {
        assertNotNull(objectManager);
        assertNotNull(objectManager.potionImgs);
        assertNotNull(objectManager.containerImgs);
        assertNotNull(objectManager.trapImg);
    }

    @Test
    void testLoadObjects_ShouldNotThrow() {
        when(levelMock.getPotions()).thenReturn(new ArrayList<>());
        when(levelMock.getContainers()).thenReturn(new ArrayList<>());
        when(levelMock.getTraps()).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> objectManager.loadObjects(levelMock));
    }

    @Test
    void testCheckTrapsTouched_ShouldKillPlayer() {
        Player playerMock = mock(Player.class);
        Trap trapMock = mock(Trap.class);

        Rectangle2D.Float playerHitbox = new Rectangle2D.Float(0, 0, 10, 10);
        Rectangle2D.Float trapHitbox = new Rectangle2D.Float(0, 0, 10, 10);

        when(playerMock.getHitbox()).thenReturn(playerHitbox);
        when(trapMock.getHitbox()).thenReturn(trapHitbox);

        ArrayList<Trap> traps = new ArrayList<>();
        traps.add(trapMock);

        objectManager.loadObjects(mock(Level.class));
        objectManager.traps = traps;

        objectManager.checkTrapsTouched(playerMock);

        verify(playerMock).kill();
    }


}
