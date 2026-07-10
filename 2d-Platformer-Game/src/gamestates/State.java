package gamestates;

import main.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

/**
 * The State class represents a base class for different game states, holding a reference to the main game.
 */
public class State {

    protected Game game;
    public State(Game game) {
        this.game = game;
    }

    /**
     * Checks if the mouse event is within the bounds of the provided menu button.
     * @param e
     * @param mb
     * @return
     */
    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }
}
