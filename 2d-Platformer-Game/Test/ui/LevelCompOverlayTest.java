package ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import exeptions.ResourceLoadException;
import exeptions.ResourceNotFoundException;
import gamestates.GameState;
import gamestates.Playing;
import main.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LevelCompOverlayTest {

    private LevelCompOverlay overlay;
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

        overlay = new LevelCompOverlay(playing) {
            protected void initImg() {
                image = mockImage;
                bgW = 200;
                bgH = 100;
                bgX = 300;
                bgY = 75;
            }
        };
    }

    @Test
    void testConstructorInitializesButtons() {
        assertNotNull(overlay.menu);
        assertNotNull(overlay.next);
        assertEquals(330, overlay.menu.x);
        assertEquals(445, overlay.next.x);
        assertEquals(195, overlay.menu.y);
        assertEquals(195, overlay.next.y);
    }

    @Test
    void testInitImgSuccess() {
        assertEquals(mockImage, overlay.image);
        assertEquals(300, overlay.bgX);
        assertEquals(75, overlay.bgY);
        assertEquals(200, overlay.bgW);
        assertEquals(100, overlay.bgH);
    }

    @Test
    void testInitImgWithResourceNotFoundException() {
        LevelCompOverlay overlayWithException = new LevelCompOverlay(playing) {
            protected void initImg() {
                try {
                    throw new ResourceNotFoundException("Test exception");
                } catch (ResourceNotFoundException e) {
                    System.err.println("Caught expected exception: " + e.getMessage());
                }
            }
        };
        assertNull(overlayWithException.image);
    }

    @Test
    void testDraw() {
        Graphics g = mock(Graphics.class);
        overlay.draw(g);

        verify(g).drawImage(eq(mockImage), eq(300), eq(75), eq(200), eq(100), isNull());
        verify(overlay.menu).draw(g);
        verify(overlay.next).draw(g);
    }

    @Test
    void testUpdate() {
        assertDoesNotThrow(() -> overlay.update());
    }

    @Test
    void testMouseMovedOverMenu() {
        MouseEvent event = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, 330, 195, 1, false);

        overlay.mouseMoved(event);

        assertTrue(overlay.menu.isMouseOver());
        assertFalse(overlay.next.isMouseOver());
    }

    @Test
    void testMousePressedOnNext() {
        MouseEvent event = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 445, 195, 1, false);

        overlay.mousePressed(event);

        assertTrue(overlay.next.isMousePressed());
        assertFalse(overlay.menu.isMousePressed());
    }

    @Test
    void testMouseReleasedOnMenu() {
        MouseEvent pressEvent = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 330, 195, 1, false);
        overlay.mousePressed(pressEvent);

        MouseEvent releaseEvent = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 330, 195, 1, false);
        overlay.mouseReleased(releaseEvent);

        verify(playing).resetAll();
        assertEquals(GameState.MENU, GameState.state);
    }

    @Test
    void testMouseReleasedOnNext() {
        MouseEvent pressEvent = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 445, 195, 1, false);
        overlay.mousePressed(pressEvent);

        MouseEvent releaseEvent = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 445, 195, 1, false);
        overlay.mouseReleased(releaseEvent);

        verify(playing).loadNextLevel();
    }

    @Test
    void testMouseReleasedOutsideButtons() {
        MouseEvent pressEvent = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        overlay.mousePressed(pressEvent);

        MouseEvent releaseEvent = new MouseEvent(mock(Component.class),
                MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        overlay.mouseReleased(releaseEvent);

        verify(playing, never()).resetAll();
        verify(playing, never()).loadNextLevel();
    }
}