package main;

import factories.DefaultEntityFactory;
import factories.EntityFactory;
import gamestates.GameState;
import gamestates.Playing;
import gamestates.Menu;
import utilz.LoadSave;

import java.awt.*;

/**
 * The Game class is the main game loop and window manager, handling the initialization and execution of the game.
 */
public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;
    private EntityFactory entityFactory;

    public final static int TILES_DEFAULT_SIZE = 32;
    public static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game(){
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGameLoop();
    }

    /**
     * Initializes essential game classes (e.g., entity factory, menu, and playing state).
     */
    private void initClasses() {
        entityFactory = new DefaultEntityFactory(); // або інший твій конкретний Factory
        menu = new Menu(this);
        playing = new Playing(this, entityFactory);
    }

    /**
     * Starts the game loop in a separate thread.
     */
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Updates the current game state, based on the active game state (MENU, PLAYING, etc.).
     */
    public void update(){

        switch (GameState.state) {
            case MENU:
              menu.update();
              break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
            case QUIT:
            default:
                System.exit(0);
                break;

        }
    }

    /**
     * Renders the current game state to the provided graphics context.
     * @param g
     */
    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;

        }
    }

    /**
     * The main game loop, controlling the FPS and UPS, updating and rendering the game accordingly.
     */
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        double deltaUpdate = 0;
        double deltaFrame = 0;

        long lastTimer = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;

        while (true) {
            long currentTime = System.nanoTime();
            double elapsed = currentTime - previousTime;
            previousTime = currentTime;

            deltaUpdate += elapsed / timePerUpdate;
            deltaFrame += elapsed / timePerFrame;

            while (deltaUpdate >= 1) {
                update();
                updates++;
                deltaUpdate--;
            }

            if (deltaFrame >= 1) {
                gamePanel.repaint();
                frames++;
                deltaFrame--;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
                lastTimer += 1000;
            }
        }
    }

    /**
     * Resets the player's directional input booleans when the window loses focus.
     */
    public void WindowFocusLost(){
            if(GameState.state == GameState.PLAYING)
                playing.getPlayer().resetDirBooleans();

    }
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Object getWidth() {
        return gamePanel.getWidth();
    }

    public Object getHeight() {
        return gamePanel.getHeight();
    }
}