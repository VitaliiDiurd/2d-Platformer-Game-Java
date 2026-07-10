package ui;

import exeptions.ResourceLoadException;
import exeptions.ResourceNotFoundException;
import gamestates.GameState;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.*;

/**
 * The PauseOverlay class represents the overlay displayed when the game is paused.
 */
public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgWidth, bgHeight;
    private UrmButton menuB, replayB, unpauseB;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createUrmButtons();

    }

    /**
     * Initializes the buttons (menu, replay, unpause) with their positions.
     */
    private void createUrmButtons() {
        int menuX = (int)(313 * Game.SCALE);
        int replayX = (int)(387 * Game.SCALE);
        int unpauseX = (int)(462 * Game.SCALE);
        int bY = (int)(325 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    /**
     * Loads the background image for the pause overlay.
     */
    private void loadBackground() {
        try {
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
            bgWidth = (int)(backgroundImg.getWidth() * Game.SCALE);
            bgHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
            bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
            bgY = (int)(25 * Game.SCALE);
        } catch (ResourceNotFoundException e) {
            System.err.println("Failed to load pause background: " + e.getMessage());
            // Handle missing background - perhaps use a solid color as fallback
        } catch (ResourceLoadException e) {
            System.err.println("Error loading pause background: " + e.getMessage());
            e.printStackTrace();
            // Handle other loading errors
        }
    }

    /**
     * Updates the buttons' states (hover, pressed).
     */
    public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();
    }
    /**
     * Draws the pause overlay, including the background and buttons.
     */
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

    }

    /**
     * Handles mouse press events for the buttons.
     * @param e
     */
    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB))
            menuB.setMousePressed(true);
        else if(isIn(e, replayB))
            replayB.setMousePressed(true);
        else if(isIn(e, unpauseB))
            unpauseB.setMousePressed(true);

    }

    /**
     * Handles mouse release events for the buttons, triggering their actions.
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                GameState.state = GameState.MENU;
                playing.unpauseGame();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed())
            {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        }

        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    /**
     * Handles mouse movement, setting the hover state for buttons.
     * @param e
     */
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMouseOver(true);

    }

    /**
     * Checks if the mouse is inside the bounds of a given button.
     * @param e
     * @param b
     * @return
     */
    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}