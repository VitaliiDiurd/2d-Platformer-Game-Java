package gamestates;

import exeptions.ResourceLoadException;
import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * The Menu class represents the main menu state of the game.
 */
public class Menu extends State implements StateMethods {

    private MenuButton[] buttons = new MenuButton[2];
    private BufferedImage backgroundImg, menuImg;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }

    /**
     * Loads background images used in the menu.
     */
    private void loadBackground() {
        try {
            menuImg = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_MENU);
        } catch (ResourceLoadException e) {
            throw new RuntimeException(e);
        }
        try {
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        } catch (ResourceLoadException e) {
            throw new RuntimeException(e);
        }
        menuWidth = (int)(backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int)(45 * Game.SCALE);
    }

    /**
     * Loads the buttons for the menu and assigns actions to them.
     */
    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int)(150 * Game.SCALE), 0, GameState.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int)(220 * Game.SCALE), 1, GameState.QUIT);
    }

    /**
     * Updates the state of all the buttons in the menu.
     */
    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    /**
     * Draws the menu background and buttons.
     * @param g
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(menuImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb : buttons) {
            mb.draw((Graphics2D) g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Handles mouse press events, triggers button interactions.
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons){
            if(isIn(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    /**
     * Handles mouse release events, triggers button actions.
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed()) {
                    mb.applyGameState();
                    break;
                }
            }
        }
        resetButtons();
    }

    /**
     * Reset all buttons states.
     */
    private void resetButtons() {
        for (MenuButton mb : buttons){
            mb.resetBools();
        }
    }

    /**
     * Handles mouse movement events to detect hover on buttons.
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons){
            mb.setMouseOver(false);
        }
        for (MenuButton mb : buttons){
            if(isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    /**
     * Handles key press events, e.g., pressing ENTER to start the game.
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
