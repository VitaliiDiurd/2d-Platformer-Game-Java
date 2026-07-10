package gamestates;

import components.MovementComponent;
import components.PhysicsComponent;
import entities.EnemyManager;
import entities.Player;
import factories.DefaultEntityFactory;
import levels.Level;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ui.GameOverOverlay;
import ui.LevelCompOverlay;
import ui.PauseOverlay;


import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
class PlayingTest {

    @Mock private Logger mockLogger;
    @Mock private Game mockGame;
    @Mock private DefaultEntityFactory mockFactory;

    @InjectMocks
    private Playing playing;
    private Game dummyGame;
    private DefaultEntityFactory dummyFactory;

    @BeforeEach
    void setUp() {
        dummyGame = new Game();
        dummyFactory = new DefaultEntityFactory();
        playing = new Playing(dummyGame, dummyFactory);
    }

    @Test
    void testSetGameOver() {
        playing.setGameOver(true);
        assertTrue(playing.isGameOver());
    }

    @Test
    void testUnpauseGame() {
        playing.unpauseGame();
        assertFalse(playing.isPaused());
    }

    @Test
    void testLevelCompleteFlag() {
        playing.setLevelComp(true);
        assertTrue(playing.isLvlComp());

        playing.setLevelComp(false);
        assertFalse(playing.isLvlComp());
    }

    @Test
    void testMouseClicked_LeftButton() {
        MouseEvent mockEvent = mock(MouseEvent.class);
        when(mockEvent.getButton()).thenReturn(MouseEvent.BUTTON1);

        Player mockPlayer = mock(Player.class);
        playing = Mockito.spy(playing);
        when(playing.getPlayer()).thenReturn(mockPlayer);

        playing.mouseClicked(mockEvent);
        verify(mockPlayer).setAttacking(true);
    }

    @Test
    void testMousePressed_DifferentStates() {
        MouseEvent mockEvent = mock(MouseEvent.class);

        playing.setPaused(true);
        playing.mousePressed(mockEvent);
        verify(playing.getPauseOverlay()).mousePressed(mockEvent);

        playing.setPaused(false);
        playing.setLevelComp(true);
        playing.mousePressed(mockEvent);
        verify(playing.getLevelCompOverlay()).mousePressed(mockEvent);

        playing.setLevelComp(false);
        playing.setGameOver(true);
        playing.mousePressed(mockEvent);
        verify(playing.getGameOverOverlay()).mousePressed(mockEvent);
    }



    @Test
    void testMouseReleased_DifferentStates() {
        MouseEvent mockEvent = mock(MouseEvent.class);

        playing.setPaused(true);
        playing.mouseReleased(mockEvent);
        verify(playing.getPauseOverlay()).mouseReleased(mockEvent);

        playing.setPaused(false);
        playing.setLevelComp(true);
        playing.mouseReleased(mockEvent);
        verify(playing.getLevelCompOverlay()).mouseReleased(mockEvent);

        playing.setLevelComp(false);
        playing.setGameOver(true);
        playing.mouseReleased(mockEvent);
        verify(playing.getGameOverOverlay()).mouseReleased(mockEvent);
    }

    @Test
    void testMouseMoved_DifferentStates() {
        MouseEvent mockEvent = mock(MouseEvent.class);

        playing.setPaused(true);
        playing.mouseMoved(mockEvent);
        verify(playing.getPauseOverlay()).mouseMoved(mockEvent);

        playing.setPaused(false);
        playing.setLevelComp(true);
        playing.mouseMoved(mockEvent);
        verify(playing.getLevelCompOverlay()).mouseMoved(mockEvent);

        playing.setLevelComp(false);
        playing.setGameOver(true);
        playing.mouseMoved(mockEvent);
        verify(playing.getGameOverOverlay()).mouseMoved(mockEvent);
    }
    @Test
    void testMouseClicked_LogsAttack() throws Exception {
        MouseEvent mockEvent = mock(MouseEvent.class);
        when(mockEvent.getButton()).thenReturn(MouseEvent.BUTTON1);

        Player mockPlayer = mock(Player.class);
        setPrivateField(playing, "player", mockPlayer);

        playing.mouseClicked(mockEvent);

        verify(mockLogger).debug("Left mouse click detected - player attack");
    }

    @Test
    void testKeyPress_LogsMovement() throws Exception {
        MovementComponent movement = mock(MovementComponent.class);
        PhysicsComponent physics = mock(PhysicsComponent.class);
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getComponent(MovementComponent.class)).thenReturn(movement);
        when(mockPlayer.getComponent(PhysicsComponent.class)).thenReturn(physics);
        setPrivateField(playing, "player", mockPlayer);

        playing.keyPressed(new KeyEvent(mock(java.awt.Component.class),
                KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_A, 'A'));

        verify(mockLogger).trace("Left movement activated");
    }
    @Test
    void testKeyPressed_JumpActivated() throws Exception {
        MovementComponent mockMovement = mock(MovementComponent.class);
        PhysicsComponent mockPhysics = mock(PhysicsComponent.class);
        Player mockPlayer = mock(Player.class);

        when(mockPlayer.getComponent(MovementComponent.class)).thenReturn(mockMovement);
        when(mockPlayer.getComponent(PhysicsComponent.class)).thenReturn(mockPhysics);

        KeyEvent spaceKeyPress = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');

        playing.keyPressed(spaceKeyPress);

        verify(mockPhysics).setJump(true);
        verify(mockLogger).trace("Jump activated");
    }

    @Test
    void testMouseReleased_LevelCompleteState() throws Exception {
        MouseEvent mockEvent = mock(MouseEvent.class);
        LevelCompOverlay mockLevelCompOverlay = mock(LevelCompOverlay.class);

        setPrivateField(playing, "levelCompOverlay", mockLevelCompOverlay);
        playing.setLevelComp(true);

        playing.mouseReleased(mockEvent);

        verify(mockLevelCompOverlay).mouseReleased(mockEvent);
        verify(mockLogger).trace("Mouse released on level complete overlay");
    }
    @Test
    void testMouseReleased_GameOverState() throws Exception {
        MouseEvent mockEvent = mock(MouseEvent.class);
        GameOverOverlay mockGameOverOverlay = mock(GameOverOverlay.class);

        setPrivateField(playing, "gameOverOverlay", mockGameOverOverlay);
        playing.setGameOver(true);

        playing.mouseReleased(mockEvent);

        verify(mockGameOverOverlay).mouseReleased(mockEvent);
        verify(mockLogger).trace("Mouse released on game over overlay");
    }

    @Test
    void testMouseMoved_LevelCompleteState() throws Exception {
        MouseEvent mockEvent = mock(MouseEvent.class);
        LevelCompOverlay mockLevelCompOverlay = mock(LevelCompOverlay.class);

        setPrivateField(playing, "levelCompOverlay", mockLevelCompOverlay);
        playing.setLevelComp(true);

        playing.mouseMoved(mockEvent);

        verify(mockLevelCompOverlay).mouseMoved(mockEvent);
    }
    @Test
    void testMouseMoved_GameOverState() throws Exception {
        MouseEvent mockEvent = mock(MouseEvent.class);
        GameOverOverlay mockGameOverOverlay = mock(GameOverOverlay.class);

        setPrivateField(playing, "gameOverOverlay", mockGameOverOverlay);
        playing.setGameOver(true);

        playing.mouseMoved(mockEvent);

        verify(mockGameOverOverlay).mouseMoved(mockEvent);
    }
    @Test
    void testMousePressed_LevelCompleteState() throws Exception {
        MouseEvent mockEvent = mock(MouseEvent.class);
        LevelCompOverlay mockLevelCompOverlay = mock(LevelCompOverlay.class);

        setPrivateField(playing, "levelCompOverlay", mockLevelCompOverlay);
        playing.setLevelComp(true);

        playing.mousePressed(mockEvent);

        verify(mockLevelCompOverlay).mousePressed(mockEvent);
        verify(mockLogger).trace("Mouse press on level complete overlay");
    }
    @Test
    void testMousePressed_GameOverState() throws Exception {
        MouseEvent mockEvent = mock(MouseEvent.class);
        GameOverOverlay mockGameOverOverlay = mock(GameOverOverlay.class);

        setPrivateField(playing, "gameOverOverlay", mockGameOverOverlay);
        playing.setGameOver(true);

        playing.mousePressed(mockEvent);

        verify(mockGameOverOverlay).mousePressed(mockEvent);
        verify(mockLogger).trace("Mouse press on game over overlay");
    }







    @Test
    void testKeyPressed_MovementKeys() {
        KeyEvent aKey = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        KeyEvent dKey = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        KeyEvent spaceKey = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');

        MovementComponent movement = mock(MovementComponent.class);
        PhysicsComponent physics = mock(PhysicsComponent.class);
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getComponent(MovementComponent.class)).thenReturn(movement);
        when(mockPlayer.getComponent(PhysicsComponent.class)).thenReturn(physics);

        playing = Mockito.spy(playing);
        when(playing.getPlayer()).thenReturn(mockPlayer);

        playing.keyPressed(aKey);
        verify(movement).setLeft(true);

        playing.keyPressed(dKey);
        verify(movement).setRight(true);

        playing.keyPressed(spaceKey);
        verify(physics).setJump(true);
    }

    @Test
    void testKeyReleased_MovementKeys() {
        KeyEvent aKey = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        KeyEvent dKey = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        KeyEvent spaceKey = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');

        MovementComponent movement = mock(MovementComponent.class);
        PhysicsComponent physics = mock(PhysicsComponent.class);
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getComponent(MovementComponent.class)).thenReturn(movement);
        when(mockPlayer.getComponent(PhysicsComponent.class)).thenReturn(physics);

        playing = Mockito.spy(playing);
        when(playing.getPlayer()).thenReturn(mockPlayer);

        playing.keyReleased(aKey);
        verify(movement).setLeft(false);

        playing.keyReleased(dKey);
        verify(movement).setRight(false);

        playing.keyReleased(spaceKey);
        verify(physics).setJump(false);
    }

    @Test
    void testKeyPressed_RightMovementActivated() throws Exception {
        MovementComponent mockMovement = mock(MovementComponent.class);
        PhysicsComponent mockPhysics = mock(PhysicsComponent.class);
        Player mockPlayer = mock(Player.class);

        when(mockPlayer.getComponent(MovementComponent.class)).thenReturn(mockMovement);
        when(mockPlayer.getComponent(PhysicsComponent.class)).thenReturn(mockPhysics);

        KeyEvent dKeyPress = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');

        playing.keyPressed(dKeyPress);

        verify(mockMovement).setRight(true);
        verify(mockLogger).trace("Right movement activated");
    }


    @Test
    void testEscapeKey_TogglesPause() {
        KeyEvent escapeKey = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, (char)KeyEvent.VK_ESCAPE);

        assertFalse(playing.isPaused());
        playing.keyPressed(escapeKey);
        assertTrue(playing.isPaused());
        playing.keyPressed(escapeKey);
        assertFalse(playing.isPaused());
    }

    @Test
    void testCheckCloseToBorder() throws Exception {
        int originalOffset = 100;
        int maxOffset = 1000;
        int leftBorder = (int)(0.3 * Game.GAME_WIDTH);
        int rightBorder = (int)(0.7 * Game.GAME_WIDTH);

        Player mockPlayer = mock(Player.class);
        setPrivateField(playing, "player", mockPlayer);

        setPrivateField(playing, "xLvlOffset", originalOffset);
        setPrivateField(playing, "maxLvlOffset", maxOffset);
        setPrivateField(playing, "leftBorder", leftBorder);
        setPrivateField(playing, "rightBorder", rightBorder);

        when(mockPlayer.getHitbox()).thenReturn(new Rectangle2D.Float(originalOffset + rightBorder + 50, 100, 50, 50));
        invokeCheckCloseToBorder();
        int newOffset = (int) getFieldValue(playing, "xLvlOffset");
        assertEquals(originalOffset + 50, newOffset);

        setPrivateField(playing, "xLvlOffset", originalOffset); // Reset
        when(mockPlayer.getHitbox()).thenReturn(new Rectangle2D.Float(originalOffset + leftBorder - 50, 100, 50, 50));
        invokeCheckCloseToBorder();
        newOffset = (int) getFieldValue(playing, "xLvlOffset");
        assertEquals(originalOffset - 50, newOffset);

        setPrivateField(playing, "xLvlOffset", maxOffset - 10);
        when(mockPlayer.getHitbox()).thenReturn(new Rectangle2D.Float(maxOffset + rightBorder + 100, 100, 50, 50));
        invokeCheckCloseToBorder();
        newOffset = (int) getFieldValue(playing, "xLvlOffset");
        assertEquals(maxOffset, newOffset);

        setPrivateField(playing, "xLvlOffset", 10);
        when(mockPlayer.getHitbox()).thenReturn(new Rectangle2D.Float(leftBorder - 50, 100, 50, 50));
        invokeCheckCloseToBorder();
        newOffset = (int) getFieldValue(playing, "xLvlOffset");
        assertEquals(0, newOffset);
    }



    private void invokeCheckCloseToBorder() throws Exception {
        Method method = Playing.class.getDeclaredMethod("checkCloseToBorder");
        method.setAccessible(true);
        method.invoke(playing);
    }

    private Object getFieldValue(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
    @Test
    void testUpdate_PausedState() throws Exception {
        PauseOverlay mockPauseOverlay = mock(PauseOverlay.class);
        setPrivateField(playing, "pauseOverlay", mockPauseOverlay);
        playing.setPaused(true);

        playing.update();

        verify(mockPauseOverlay).update();
        verifyNoMoreInteractions(mockPauseOverlay);
    }

    @Test
    void testUpdate_LevelCompleteState() throws Exception {
        LevelCompOverlay mockLevelCompOverlay = mock(LevelCompOverlay.class);
        setPrivateField(playing, "levelCompOverlay", mockLevelCompOverlay);
        playing.setLevelComp(true);

        playing.update();

        verify(mockLevelCompOverlay).update();
        verifyNoMoreInteractions(mockLevelCompOverlay);
    }

    @Test
    void testUpdate_GameOverState() throws Exception {
        GameOverOverlay mockGameOverOverlay = mock(GameOverOverlay.class);
        setPrivateField(playing, "gameOverOverlay", mockGameOverOverlay);
        playing.setGameOver(true);

        playing.update();

        verify(mockGameOverOverlay).update();
        verifyNoMoreInteractions(mockGameOverOverlay);
    }

    @Test
    void testUpdate_NormalState() throws Exception {
        LevelManager mockLevelManager = mock(LevelManager.class);
        ObjectManager mockObjectManager = mock(ObjectManager.class);
        Player mockPlayer = mock(Player.class);
        EnemyManager mockEnemyManager = mock(EnemyManager.class);
        Level mockLevel = mock(Level.class);

        setPrivateField(playing, "levelManager", mockLevelManager);
        setPrivateField(playing, "objectManager", mockObjectManager);
        setPrivateField(playing, "player", mockPlayer);
        setPrivateField(playing, "enemyManager", mockEnemyManager);
        when(mockLevelManager.getCurrentLevel()).thenReturn(mockLevel);

        playing.setPaused(false);
        playing.setGameOver(false);
        playing.setLevelComp(false);

        playing.update();

        verify(mockLevelManager).update();
        verify(mockObjectManager).update();
        verify(mockPlayer).update();
        int[][] levelData = mockLevel.getLevelData();
        verify(mockEnemyManager).update(levelData);
    }
    @Test
    void testWindowFocusLost_ResetsPlayerDirections() {
        Player mockPlayer = mock(Player.class);
        playing = Mockito.spy(playing);
        when(playing.getPlayer()).thenReturn(mockPlayer);

        playing.windowFocusLost();
        verify(mockPlayer).resetDirBooleans();
    }

    @Test
    void testResetAll() {
        playing.setGameOver(true);
        playing.setPaused(true);
        playing.setLevelComp(true);

        Player mockPlayer = mock(Player.class);
        playing = Mockito.spy(playing);
        when(playing.getPlayer()).thenReturn(mockPlayer);

        playing.resetAll();

        assertFalse(playing.isGameOver());
        assertFalse(playing.isPaused());
        assertFalse(playing.isLvlComp());
        verify(mockPlayer).resetAll();
    }

    @Test
    void testGetLevelCompOverlay() throws Exception {
        LevelCompOverlay mockLevelCompOverlay = mock(LevelCompOverlay.class);
        setPrivateField(playing, "levelCompOverlay", mockLevelCompOverlay);

        LevelCompOverlay result = playing.getLevelCompOverlay();

        assertEquals(mockLevelCompOverlay, result);
    }

    @Test
    void testGetGameOverOverlay() throws Exception {
        GameOverOverlay mockGameOverOverlay = mock(GameOverOverlay.class);
        setPrivateField(playing, "gameOverOverlay", mockGameOverOverlay);

        GameOverOverlay result = playing.getGameOverOverlay();

        assertEquals(mockGameOverOverlay, result);
    }

    @Test
    void testSetXLevelOffset() throws Exception {
        int newOffset = 200;
        setPrivateField(playing, "xLvlOffset", 100); // Set an initial offset value

        playing.setXLevelOffset(newOffset);

        int updatedOffset = (int) getFieldValue(playing, "xLvlOffset");
        assertEquals(newOffset, updatedOffset);
    }

    @Test
    void testSetBackgroundImg() throws Exception {
        BufferedImage dummyImage = mock(BufferedImage.class);

        playing.setBackgroundImg(dummyImage);

        BufferedImage currentImage = (BufferedImage) getFieldValue(playing, "backgroundImg");
        assertEquals(dummyImage, currentImage);
    }

    @Test
    void testSetPlayer() throws Exception {
        Player mockPlayer = mock(Player.class);

        playing.setPlayer(mockPlayer);

        Player result = (Player) getFieldValue(playing, "player");
        assertEquals(mockPlayer, result);
    }

    @Test
    void testGetXLevelOffset() throws Exception {
        int expectedOffset = 300;
        setPrivateField(playing, "xLvlOffset", expectedOffset);

        int result = playing.getXLevelOffset();

        assertEquals(expectedOffset, result);
    }
    @Test
    void testGetEnemyManager() throws Exception {
        EnemyManager mockEnemyManager = mock(EnemyManager.class);
        setPrivateField(playing, "enemyManager", mockEnemyManager);

        EnemyManager result = playing.getEnemyManager();

        assertEquals(mockEnemyManager, result);
    }
    @Test
    void testGetObjectManager() throws Exception {
        ObjectManager mockObjectManager = mock(ObjectManager.class);
        setPrivateField(playing, "objectManager", mockObjectManager);

        ObjectManager result = playing.getObjectManager();

        assertEquals(mockObjectManager, result);
    }

    @Test
    void testSetMaxLvlOffset() throws Exception {
        int newMaxOffset = 500;

        playing.setMaxLvlOffset(newMaxOffset);

        int updatedMaxOffset = (int) getFieldValue(playing, "maxLvlOffset");
        assertEquals(newMaxOffset, updatedMaxOffset);
    }
    @Test
    void testCheckObjectHit() throws Exception {
        Rectangle2D.Float mockAttackBox = mock(Rectangle2D.Float.class);
        ObjectManager mockObjectManager = mock(ObjectManager.class);
        setPrivateField(playing, "objectManager", mockObjectManager);

        playing.checkObhectHit(mockAttackBox);

        verify(mockObjectManager).checkObjectHit(mockAttackBox);
    }
    @Test
    void testKeyReleased_RightMovementDeactivated() throws Exception {
        MovementComponent mockMovement = mock(MovementComponent.class);
        PhysicsComponent mockPhysics = mock(PhysicsComponent.class);
        Player mockPlayer = mock(Player.class);

        when(mockPlayer.getComponent(MovementComponent.class)).thenReturn(mockMovement);
        when(mockPlayer.getComponent(PhysicsComponent.class)).thenReturn(mockPhysics);

        KeyEvent dKeyRelease = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');

        playing.keyReleased(dKeyRelease);

        verify(mockMovement).setRight(false);
        verify(mockLogger).trace("Right movement deactivated");
    }
    @Test
    void testKeyReleased_JumpDeactivated() throws Exception {
        MovementComponent mockMovement = mock(MovementComponent.class);
        PhysicsComponent mockPhysics = mock(PhysicsComponent.class);
        Player mockPlayer = mock(Player.class);

        when(mockPlayer.getComponent(MovementComponent.class)).thenReturn(mockMovement);
        when(mockPlayer.getComponent(PhysicsComponent.class)).thenReturn(mockPhysics);

        KeyEvent spaceKeyRelease = new KeyEvent(mock(java.awt.Component.class), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');

        playing.keyReleased(spaceKeyRelease);

        verify(mockPhysics).setJump(false);
        verify(mockLogger).trace("Jump deactivated");
    }








    private PauseOverlay getPauseOverlay() {
        return playing.getPauseOverlay();
    }

    private LevelCompOverlay getLevelCompOverlay() {
        return playing.getLevelCompOverlay();
    }

    private GameOverOverlay getGameOverOverlay() {
        return playing.getGameOverOverlay();
    }

    private void setPaused(boolean paused) {
        playing.setPaused(paused);
    }
}