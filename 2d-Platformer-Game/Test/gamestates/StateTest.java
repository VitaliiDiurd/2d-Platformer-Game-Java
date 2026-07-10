package gamestates;

import main.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ui.MenuButton;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StateTest {

    @Mock
    private Game mockGame;

    @Mock
    private MenuButton mockMenuButton;

    @Mock
    private MouseEvent mockMouseEvent;

    @Mock
    private Rectangle mockBounds;

    private State state;

    @BeforeEach
    void setUp() {
        state = new State(mockGame) {};
    }

    @Test
    void isIn_ShouldReturnTrue_WhenMouseInButtonBounds() {
        when(mockMenuButton.getBounds()).thenReturn(mockBounds);
        when(mockBounds.contains(anyInt(), anyInt())).thenReturn(true);
        when(mockMouseEvent.getX()).thenReturn(100);
        when(mockMouseEvent.getY()).thenReturn(50);

        boolean result = state.isIn(mockMouseEvent, mockMenuButton);

        assertTrue(result);
        verify(mockMenuButton).getBounds();
        verify(mockBounds).contains(100, 50);
    }

    @Test
    void isIn_ShouldReturnFalse_WhenMouseOutsideButtonBounds() {
        when(mockMenuButton.getBounds()).thenReturn(mockBounds);
        when(mockBounds.contains(anyInt(), anyInt())).thenReturn(false);
        when(mockMouseEvent.getX()).thenReturn(10);
        when(mockMouseEvent.getY()).thenReturn(10);

        boolean result = state.isIn(mockMouseEvent, mockMenuButton);

        assertFalse(result);
        verify(mockMenuButton).getBounds();
        verify(mockBounds).contains(10, 10);
    }

    @Test
    void isIn_ShouldHandleNullButton() {
        assertThrows(NullPointerException.class,
                () -> state.isIn(mockMouseEvent, null),
                "Should throw NPE for null button");
    }

    @Test
    void isIn_ShouldHandleNullEvent() {
        assertThrows(NullPointerException.class,
                () -> state.isIn(null, mockMenuButton),
                "Should throw NPE for null event");
    }

    @Test
    void getGame_ShouldReturnGameInstance() {
        Game result = state.getGame();

        assertSame(mockGame, result, "Should return the game instance provided in constructor");
    }

    @Test
    void isIn_ShouldHandleEdgeCases() {
        when(mockMenuButton.getBounds()).thenReturn(mockBounds);

        when(mockMouseEvent.getX()).thenReturn(Integer.MIN_VALUE);
        when(mockMouseEvent.getY()).thenReturn(Integer.MIN_VALUE);
        when(mockBounds.contains(Integer.MIN_VALUE, Integer.MIN_VALUE)).thenReturn(false);
        assertFalse(state.isIn(mockMouseEvent, mockMenuButton));

        when(mockMouseEvent.getX()).thenReturn(Integer.MAX_VALUE);
        when(mockMouseEvent.getY()).thenReturn(Integer.MAX_VALUE);
        when(mockBounds.contains(Integer.MAX_VALUE, Integer.MAX_VALUE)).thenReturn(true);
        assertTrue(state.isIn(mockMouseEvent, mockMenuButton));
    }

    @Test
    void isIn_ShouldVerifyButtonBoundsUsage() {
        Rectangle testBounds = new Rectangle(50, 50, 100, 50);
        when(mockMenuButton.getBounds()).thenReturn(testBounds);

        when(mockMouseEvent.getX()).thenReturn(75);
        when(mockMouseEvent.getY()).thenReturn(75);
        assertTrue(state.isIn(mockMouseEvent, mockMenuButton));

        when(mockMouseEvent.getX()).thenReturn(25);
        when(mockMouseEvent.getY()).thenReturn(25);
        assertFalse(state.isIn(mockMouseEvent, mockMenuButton));
    }
}