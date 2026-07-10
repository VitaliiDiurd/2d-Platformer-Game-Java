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

import static utilz.Constants.UI.URMButtons.URM_SIZE;

/**
 * The LevelCompOverlay class represents the overlay displayed when the player completes a level.
 */
public class LevelCompOverlay{
    private Playing playing;
    protected UrmButton menu;
    protected UrmButton next;
    protected BufferedImage image;
    protected int bgX;
    protected int bgY;
    protected int bgW;
    protected int bgH;

    public LevelCompOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButtons();
}


    /**
     * Initializes the "Menu" and "Next Level" buttons.
     */
    private void initButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    /**
     * Loads the level completion image and scales it according to the game's scale factor.
     */
    private void initImg() {
        try {
            image = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMP);
            bgW = (int) (image.getWidth() * Game.SCALE);
            bgH = (int) (image.getHeight() * Game.SCALE);
            bgX = Game.GAME_WIDTH / 2 - bgW / 2;
            bgY = (int) (75 * Game.SCALE);
        } catch (ResourceNotFoundException e) {
            System.err.println("Failed to load level completion image: " + e.getMessage());
        } catch (ResourceLoadException e) {
            System.err.println("Error loading level completion image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Draws the level completion screen, including the background image and buttons.
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(image, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }

    /**
     * Updates the state of the buttons (e.g., hover and click effects).
     */
    public void update() {
        next.update();
        menu.update();
    }

    /**
     * Checks if the mouse pointer is over a button.
     * @param e
     * @param b
     * @return
     */
    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    /**
     * Handles mouse movement events, updating the hover state of the buttons.
     * @param e
     */
    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(e, menu))
            menu.setMouseOver(true);
        else if (isIn(e, next))
            next.setMouseOver(true);
    }

    /**
     *  Handles mouse press events, updating the pressed state of the buttons.
     * @param e
     */
    public void mousePressed(MouseEvent e){
        if (isIn(e, menu))
            menu.setMousePressed(true);
        else if (isIn(e, next))
            next.setMousePressed(true);
    }

    /**
     * Handles mouse release events, performing actions based on which button was clicked.
     * @param e
     */
    public void mouseReleased(MouseEvent e){
        if (isIn(e, menu)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                GameState.state = GameState.MENU;
            }
        } else if (isIn(e, next))
            if (next.isMousePressed())
                playing.loadNextLevel();

        menu.resetBools();
        next.resetBools();
    }}
