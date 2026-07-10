package Inputs;

import components.MovementComponent;
import components.PhysicsComponent;
import entities.Player;
import gamestates.GameState;
import main.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The KeyboardInputs class handles key events and delegates the actions to the corresponding game state.
 */
public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Handles key typed events but does nothing in this implementation.
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Handles key press events and forwards the event to the corresponding game state.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
            default:
                break;
        }
    }

    /**
     * Handles key release events and forwards the event to the corresponding game state.
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
            default:
                break;
        }
    }
}