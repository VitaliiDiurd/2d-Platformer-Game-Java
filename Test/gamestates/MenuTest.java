package gamestates;

import main.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.MenuButton;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuTest {
    private Menu menu;
    private Game mockGame;
    private MenuButton[] mockButtons;
    private BufferedImage mockBackgroundImg;
    private BufferedImage mockMenuImg;

    @BeforeEach
    void setUp() {
        mockGame = mock(Game.class);
        menu = new Menu(mockGame);

        mockButtons = new MenuButton[2];
        mockButtons[0] = mock(MenuButton.class);
        mockButtons[1] = mock(MenuButton.class);

        mockBackgroundImg = mock(BufferedImage.class);
        mockMenuImg = mock(BufferedImage.class);

        try {
            var buttonsField = Menu.class.getDeclaredField("buttons");
            buttonsField.setAccessible(true);
            buttonsField.set(menu, mockButtons);

            var backgroundField = Menu.class.getDeclaredField("backgroundImg");
            backgroundField.setAccessible(true);
            backgroundField.set(menu, mockBackgroundImg);

            var menuImgField = Menu.class.getDeclaredField("menuImg");
            menuImgField.setAccessible(true);
            menuImgField.set(menu, mockMenuImg);
        } catch (Exception e) {
            fail("Failed to inject mock dependencies");
        }
    }

    @Test
    void testMouseClicked() {
        MouseEvent mockEvent = mock(MouseEvent.class);
        assertDoesNotThrow(() -> menu.mouseClicked(mockEvent));
    }

    @Test
    void testKeyReleased() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        assertDoesNotThrow(() -> menu.keyReleased(mockEvent));
    }

    @Test
    void testMousePressed() {
        MenuButton mockButton = mock(MenuButton.class);
        MouseEvent mockEvent = mock(MouseEvent.class);

        when(mockEvent.getX()).thenReturn(100);
        when(mockEvent.getY()).thenReturn(150);
        when(mockButton.getBounds()).thenReturn(new Rectangle(90, 140, 50, 50));
        mockButtons[0] = mockButton;

        menu.mousePressed(mockEvent);
        verify(mockButton).setMousePressed(true);
    }

    @Test
    void testMouseReleased() {
        MenuButton mockButton = mock(MenuButton.class);
        MouseEvent mockEvent = mock(MouseEvent.class);

        when(mockEvent.getX()).thenReturn(100);
        when(mockEvent.getY()).thenReturn(150);
        when(mockButton.getBounds()).thenReturn(new Rectangle(90, 140, 50, 50));
        when(mockButton.isMousePressed()).thenReturn(true);

        mockButtons[0] = mockButton;

        menu.mouseReleased(mockEvent);
        verify(mockButton).applyGameState();
        verify(mockButton).resetBools();
    }

    @Test
    void testMouseReleasedNotPressed() {
        MenuButton mockButton = mock(MenuButton.class);
        MouseEvent mockEvent = mock(MouseEvent.class);

        when(mockEvent.getX()).thenReturn(100);
        when(mockEvent.getY()).thenReturn(150);
        when(mockButton.getBounds()).thenReturn(new Rectangle(90, 140, 50, 50));
        when(mockButton.isMousePressed()).thenReturn(false);

        mockButtons[0] = mockButton;

        menu.mouseReleased(mockEvent);
        verify(mockButton, never()).applyGameState();
        verify(mockButton).resetBools();
    }

    @Test
    void testUpdate() {
        menu.update();
        verify(mockButtons[0]).update();
        verify(mockButtons[1]).update();
    }

    @Test
    void testKeyPressedEnter() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        menu.keyPressed(mockEvent);
        assertEquals(GameState.PLAYING, GameState.state);
    }

    @Test
    void testKeyPressedOther() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(KeyEvent.VK_SPACE);
        GameState.state = GameState.MENU;
        menu.keyPressed(mockEvent);
        assertEquals(GameState.MENU, GameState.state);
    }

    @Test
    void testIsIn() {
        MenuButton mockButton = mock(MenuButton.class);
        MouseEvent mockEvent = mock(MouseEvent.class);
        when(mockEvent.getX()).thenReturn(100);
        when(mockEvent.getY()).thenReturn(200);
        when(mockButton.getBounds()).thenReturn(new Rectangle(90, 190, 50, 50));
        assertTrue(menu.isIn(mockEvent, mockButton));
    }
}