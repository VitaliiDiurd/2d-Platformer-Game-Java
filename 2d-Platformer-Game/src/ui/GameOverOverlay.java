package ui;

import exeptions.ResourceLoadException;
import exeptions.ResourceNotFoundException;
import gamestates.GameState;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.URM_SIZE;

/**
 * The GameOverOverlay class represents the overlay displayed when the player loses the game.
 */
public class GameOverOverlay {
    private Playing playing;
    protected BufferedImage img;
    protected int imgX;
    protected int imgY;
    protected int imgW;
    protected int imgH;
    protected UrmButton menu;
    protected UrmButton play;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        createImg();
        createButtons();
    }

    /**
     * Initializes the "Play Again" and "Menu" buttons.
     */
    private void createButtons() {
        int menuX = (int) (335 * Game.SCALE);
        int playX = (int) (440 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    /**
     * Loads the death screen image and scales it according to the game's scale factor.
     */
    private void createImg() {
        try {
            img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
            imgW = (int) (img.getWidth() * Game.SCALE);
            imgH = (int) (img.getHeight() * Game.SCALE);
            imgX = Game.GAME_WIDTH / 2 - imgW / 2;
            imgY = (int) (100 * Game.SCALE);
        } catch (ResourceNotFoundException e) {
            System.err.println("Failed to load death screen image: " + e.getMessage());
        } catch (ResourceLoadException e) {
            System.err.println("Error loading death screen image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Draws the game over screen, including the background, death screen image, and buttons.
     * @param g
     */
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);
        menu.draw(g);
        play.draw(g);
    }

    /**
     * Updates the state of the buttons (e.g., hover and click effects).
     */
    public void update() {
        menu.update();
        play.update();
    }

    /**
     * Handles key press events, such as pressing the ESC key to return to the menu.
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
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
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(e, menu))
            menu.setMouseOver(true);
        else if (isIn(e, play))
            play.setMouseOver(true);
    }

    /**
     * Handles mouse press events, updating the pressed state of the buttons.
     *      *
     * @param e
     */
    public void mousePressed(MouseEvent e){
        if (isIn(e, menu))
            menu.setMousePressed(true);
        else if (isIn(e, play))
            play.setMousePressed(true);
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
        } else if (isIn(e, play))
            if (play.isMousePressed()) {
                playing.resetAll();
            }

        menu.resetBools();
        play.resetBools();
    }
}
