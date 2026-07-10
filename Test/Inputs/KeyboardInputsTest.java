package Inputs;

import gamestates.GameState;
import gamestates.Menu;
import gamestates.Playing;
import main.Game;
import main.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class KeyboardInputsTest {

    private KeyboardInputs keyboardInputs;
    private GamePanel mockGamePanel;
    private Game mockGame;
    private KeyEvent mockKeyEvent;

    @BeforeEach
    void setUp() {
        mockGamePanel = mock(GamePanel.class);
        mockGame = mock(Game.class);
        mockKeyEvent = mock(KeyEvent.class);

        when(mockGamePanel.getGame()).thenReturn(mockGame);
        keyboardInputs = new KeyboardInputs(mockGamePanel);
    }

    @Test
    void constructor_ShouldInitializeKeyboardInputs() {
        assertNotNull(keyboardInputs);
    }

    @Test
    void keyTyped_ShouldNotInteractWithGame() {
        keyboardInputs.keyTyped(mockKeyEvent);
    }

    @Test
    void keyPressed_WhenGameIsNull_ShouldNotThrow() {
        when(mockGamePanel.getGame()).thenReturn(null);

        keyboardInputs.keyPressed(mockKeyEvent);
    }

    @Test
    void keyReleased_WhenGameIsNull_ShouldNotThrow() {
        when(mockGamePanel.getGame()).thenReturn(null);

        keyboardInputs.keyReleased(mockKeyEvent);
    }

    @Test
    void keyPressed_WhenInMenuState_ShouldCallMenuKeyPressed() {
        GameState.state = GameState.MENU;
        Menu mockMenu = mock(Menu.class);
        when(mockGame.getMenu()).thenReturn(mockMenu);

        keyboardInputs.keyPressed(mockKeyEvent);

        verify(mockMenu, times(1)).keyPressed(mockKeyEvent);
    }

    @Test
    void keyPressed_WhenInPlayingState_ShouldCallPlayingKeyPressed() {
        GameState.state = GameState.PLAYING;
        Playing mockPlaying = mock(Playing.class);
        when(mockGame.getPlaying()).thenReturn(mockPlaying);

        keyboardInputs.keyPressed(mockKeyEvent);

        verify(mockPlaying, times(1)).keyPressed(mockKeyEvent);
    }

    @Test
    void keyPressed_WhenInUnknownState_ShouldDoNothing() {
        GameState.state = GameState.OPTIONS;

        keyboardInputs.keyPressed(mockKeyEvent);

        verifyNoMoreInteractions(mockGame);
    }

    @Test
    void keyReleased_WhenInMenuState_ShouldCallMenuKeyReleased() {
        GameState.state = GameState.MENU;
        Menu mockMenu = mock(Menu.class);
        when(mockGame.getMenu()).thenReturn(mockMenu);

        keyboardInputs.keyReleased(mockKeyEvent);

        verify(mockMenu, times(1)).keyReleased(mockKeyEvent);
    }

    @Test
    void keyReleased_WhenInPlayingState_ShouldCallPlayingKeyReleased() {
        GameState.state = GameState.PLAYING;
        Playing mockPlaying = mock(Playing.class);
        when(mockGame.getPlaying()).thenReturn(mockPlaying);

        keyboardInputs.keyReleased(mockKeyEvent);

        verify(mockPlaying, times(1)).keyReleased(mockKeyEvent);
    }

    @Test
    void keyReleased_WhenInUnknownState_ShouldDoNothing() {
        GameState.state = GameState.OPTIONS;

        keyboardInputs.keyReleased(mockKeyEvent);

        verifyNoMoreInteractions(mockGame);
    }
}
