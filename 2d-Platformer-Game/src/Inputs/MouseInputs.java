package Inputs;

import components.AnimationComponent;
import entities.Player;
import gamestates.GameState;
import main.GamePanel;
import gamestates.Menu;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The MouseInputs class handles mouse events and delegates the actions to the corresponding game state.
 */
public class MouseInputs implements MouseListener, MouseMotionListener {

    private final GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * andles mouse click events and forwards the event to the corresponding game state.
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch(GameState.state){
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseClicked(e);
                break;
            default:
                break;
        }
    }

    /**
     * Handles mouse press events and forwards the event to the corresponding game state.
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch(GameState.state){
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            default:
                break;
        }
    }

    /**
     * Handles mouse release events and forwards the event to the corresponding game state.
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch(GameState.state){
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not used
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Not used
    }

    /**
     * Handles mouse movement events and forwards the event to the corresponding game state.
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        switch(GameState.state){
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            default:
                break;
        }
    }
}