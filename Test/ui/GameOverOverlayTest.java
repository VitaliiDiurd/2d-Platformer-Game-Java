package ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import gamestates.GameState;
import gamestates.Playing;
import main.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameOverOverlayTest {

    private GameOverOverlay gameOverOverlay;
    private Playing playing;
    private BufferedImage mockImage;

    @BeforeEach
    void setUp() {
        playing = mock(Playing.class);
        Game.SCALE = 1.0f;
        Game.GAME_WIDTH = 800;
        Game.GAME_HEIGHT = 600;

        mockImage = mock(BufferedImage.class);
        when(mockImage.getWidth()).thenReturn(200);
        when(mockImage.getHeight()).thenReturn(100);

        gameOverOverlay = new GameOverOverlay(playing) {
            protected void createImg() {
                img = mockImage;
                imgW = 200;
                imgH = 100;
                imgX = 300;
                imgY = 100;
            }
        };
    }

    @Test
    void testConstructorInitializesButtons() {
        assertNotNull(gameOverOverlay.menu);
        assertNotNull(gameOverOverlay.play);
        assertEquals(335, gameOverOverlay.menu.x);
        assertEquals(440, gameOverOverlay.play.x);
        assertEquals(195, gameOverOverlay.menu.y);
        assertEquals(195, gameOverOverlay.play.y);
    }

    @Test
    void testKeyPressedEscape() {
        KeyEvent escapeEvent = new KeyEvent(mock(Component.class),
                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, ' ');

        gameOverOverlay.keyPressed(escapeEvent);

        verify(playing).resetAll();
        assertEquals(GameState.MENU, GameState.state);
    }

    @Test
    void testKeyPressedOtherKey() {
        KeyEvent aKeyEvent = new KeyEvent(mock(Component.class),
                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');

        gameOverOverlay.keyPressed(aKeyEvent);

        verify(playing, never()).resetAll();
    }

    @Test
    void testMouseMovedOverMenu() {
        MouseEvent event = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, 335, 195, 1, false);

        gameOverOverlay.mouseMoved(event);

        assertTrue(gameOverOverlay.menu.isMouseOver());
        assertFalse(gameOverOverlay.play.isMouseOver());
    }

    @Test
    void testMousePressedOnPlay() {
        MouseEvent event = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 440, 195, 1, false);

        gameOverOverlay.mousePressed(event);

        assertTrue(gameOverOverlay.play.isMousePressed());
        assertFalse(gameOverOverlay.menu.isMousePressed());
    }

    @Test
    void testMouseReleasedOnMenu() {
        MouseEvent pressEvent = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 335, 195, 1, false);
        gameOverOverlay.mousePressed(pressEvent);

        MouseEvent releaseEvent = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 335, 195, 1, false);
        gameOverOverlay.mouseReleased(releaseEvent);

        verify(playing).resetAll();
        assertEquals(GameState.MENU, GameState.state);
    }

    @Test
    void testDraw() {
        Graphics g = mock(Graphics.class);

        gameOverOverlay.draw(g);

        verify(g).setColor(new Color(0, 0, 0, 200));
        verify(g).fillRect(0, 0, 800, 600);
        verify(g).drawImage(eq(mockImage), eq(300), eq(100), eq(200), eq(100), isNull());
    }

    @Test
    void testUpdate() {
        assertDoesNotThrow(() -> gameOverOverlay.update());
    }
}