package gamestates;

import components.*;
import entities.*;
import factories.DefaultEntityFactory;
import factories.EntityFactory;
import factories.EntityType;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Playing class represents the gameplay state, handling player actions, level progression, and overlays.
 */
public class Playing extends State implements StateMethods {
    private static final Logger logger = LoggerFactory.getLogger(Playing.class);

    private Entity player;
    private LevelManager levelManager;
    private final EntityFactory entityFactory;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompOverlay levelCompOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int)(0.3 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.7 * Game.GAME_WIDTH);
    private int maxLvlOffset;

    private BufferedImage backgroundImg;

    private boolean gameOver;
    private boolean lvlComp = false;

    public Playing(Game game, EntityFactory entityFactory) {
        super(game);
        logger.info("Initializing Playing game state with entity factory: {}", entityFactory.getClass().getSimpleName());
        this.entityFactory = entityFactory;

        try {
            initClasses();
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.GAME_BACKGROUND);
            logger.debug("Successfully loaded background image");

            calcLvlOffset();
            loadStartLevel();
            logger.info("Playing state initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize Playing state", e);
            throw new RuntimeException("Playing initialization failed", e);
        }
    }

    /**
     * Loads the next level and resets the necessary game state.
     */
    public void loadNextLevel() {;
        resetAll();
        levelManager.loadNextLevel();;
    }

    /**
     * Loads the starting level's data, including enemies and objects.
     */
    private void loadStartLevel() {
        logger.debug("Loading start level data");
        try {
            enemyManager.loadEnemies(levelManager.getCurrentLevel());
            objectManager.loadObjects(levelManager.getCurrentLevel());
        } catch (Exception e) {
            logger.error("Failed to load start level data", e);
            throw new RuntimeException("Level data loading failed", e);
        }
    }

    /**
     * Calculates the level's horizontal offset based on current level data.
     */
    private void calcLvlOffset() {
        maxLvlOffset = levelManager.getCurrentLevel().getLvlOffset();
        logger.debug("Calculated level offset: {}", maxLvlOffset);
    }

    /**
     * Initializes game components such as the player, enemies, and UI overlays.
     */
    private void initClasses() {
        logger.debug("Initializing game components");
        try {
            levelManager = new LevelManager(game);
            enemyManager = new EnemyManager(this);
            objectManager = new ObjectManager(this);

            player = entityFactory.createEntity(EntityType.PLAYER, 200, 200, this);
            logger.info("Created player entity at (200,200)");

            if (player instanceof Player) {
                ((Player) player).loadLvlData(levelManager.getCurrentLevel().getLvlData());
                logger.debug("Loaded level data for player");
            }

            pauseOverlay = new PauseOverlay(this);
            gameOverOverlay = new GameOverOverlay(this);
            levelCompOverlay = new LevelCompOverlay(this);
            logger.debug("Initialized all UI overlays");
        } catch (Exception e) {
            logger.error("Failed to initialize game components", e);
            throw new RuntimeException("Component initialization failed", e);
        }
    }

    public void windowFocusLost() {
        if (player instanceof Player) {
            ((Player) player).resetDirBooleans();
        }
    }

    /**
     * Overs game.
     * @param gameOver
     */
    public void setGameOver(boolean gameOver) {
        logger.info("Setting game over state to: {}", gameOver);
        this.gameOver = gameOver;
    }

    public Player getPlayer() {
        return (Player) player;
    }

    /**
     * Updates the game state, including player, level, and enemy behaviors.
     */
    @Override
    public void update() {
        if (paused) {
            logger.trace("Game paused - updating pause overlay");
            pauseOverlay.update();
        } else if (lvlComp) {
            logger.trace("Level complete - updating completion overlay");
            levelCompOverlay.update();
        } else if(gameOver) {
            logger.trace("Game over - updating game over overlay");
            gameOverOverlay.update();
        } else if (!gameOver) {
            levelManager.update();
            objectManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData());
            checkCloseToBorder();
            logger.trace("Game state updated");
        }
    }

    /**
     * Check if plaer is close to border.
     */
    private void checkCloseToBorder() {
        int playerX = (int)(player.getHitbox().x);
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        if(xLvlOffset > maxLvlOffset) {
            xLvlOffset = maxLvlOffset;
            logger.trace("Level offset clamped to max: {}", maxLvlOffset);
        } else if(xLvlOffset < 0) {
            xLvlOffset = 0;
            logger.trace("Level offset clamped to 0");
        }
    }

    /**
     * Draws the game elements such as the background, level, entities, and overlays.
     * @param g
     */
    @Override
    public void draw(Graphics g) {
        try {
            g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
            levelManager.draw((Graphics2D) g, xLvlOffset);
            enemyManager.draw(g, xLvlOffset);
            objectManager.draw(g, xLvlOffset);

            if (player instanceof Player) {
                ((Player) player).render(g, xLvlOffset);
            }

            if (paused) {
                g.setColor(new Color(0, 0, 0, 140));
                g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
                pauseOverlay.draw(g);
                logger.trace("Rendered pause overlay");
            } else if (gameOver) {
                gameOverOverlay.draw(g);
                logger.trace("Rendered game over overlay");
            } else if (lvlComp) {
                levelCompOverlay.draw(g);
                logger.trace("Rendered level complete overlay");
            }
        } catch (Exception e) {
            logger.error("Error during rendering", e);
        }
    }

    /**
     * Resets the game state, including player, enemies, and objects.
     */
    public void resetAll() {
        logger.info("Resetting all game state");
        gameOver = false;
        paused = false;
        lvlComp = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
        logger.debug("Reset completed");
    }

    /**
     * Checks if the player's attack hit any enemies.
     * @param attackBox
     */
    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    /**
     * Checks if the player touched potion..
     * @param hitbox
     */
    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    /**
     * Handles mouse clicks for player actions and overlay interactions.
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1 && player instanceof Player) {
                logger.debug("Left mouse click detected - player attack");
                ((Player) player).setAttacking(true);
            }
        }
    }

    /**
     * Handles mouse clicks for player actions and overlay interactions.
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        logger.debug("Mouse released event");
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
                logger.trace("Mouse released on pause overlay");
            } else if (lvlComp) {
                levelCompOverlay.mouseReleased(e);
                logger.trace("Mouse released on level complete overlay");
            }
        } else {
            gameOverOverlay.mouseReleased(e);
            logger.trace("Mouse released on game over overlay");
        }
    }

    /**
     * Handles mouse moved to provide animation.
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        logger.trace("Mouse moved to ({},{})", e.getX(), e.getY());
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            } else if (lvlComp) {
                levelCompOverlay.mouseMoved(e);
            }
        } else {
            gameOverOverlay.mouseMoved(e);
        }
    }

    /**
     * Handles mouse pressed.
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        logger.debug("Mouse pressed at ({},{})", e.getX(), e.getY());
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mousePressed(e);
                logger.trace("Mouse press on pause overlay");
            } else if (lvlComp) {
                levelCompOverlay.mousePressed(e);
                logger.trace("Mouse press on level complete overlay");
            }
        } else {
            gameOverOverlay.mousePressed(e);
            logger.trace("Mouse press on game over overlay");
        }
    }

    /**
     * Handles key pressed to stop player movement and actions.
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
//        logger.debug("Key pressed: {}", KeyEvent.getKeyText(e.getKeyCode()));
        MovementComponent movement = player.getComponent(MovementComponent.class);
        PhysicsComponent physics = player.getComponent(PhysicsComponent.class);

        if (gameOver) {
            gameOverOverlay.keyPressed(e);
            logger.trace("Key press handled by game over overlay");
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    movement.setLeft(true);
                    logger.trace("Left movement activated");
                    break;
                case KeyEvent.VK_D:
                    movement.setRight(true);
                    logger.trace("Right movement activated");
                    break;
                case KeyEvent.VK_SPACE:
                    physics.setJump(true);
                    logger.trace("Jump activated");
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    logger.info("Pause state toggled to: {}", paused);
                    break;
            }
        }
    }

    /**
     * Handles key releases to stop player movement and actions.
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
//        logger.debug("Key released: {}", KeyEvent.getKeyText(e.getKeyCode()));
        MovementComponent movement = player.getComponent(MovementComponent.class);
        PhysicsComponent physics = player.getComponent(PhysicsComponent.class);

        if (!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    movement.setLeft(false);
                    logger.trace("Left movement deactivated");
                    break;
                case KeyEvent.VK_D:
                    movement.setRight(false);
                    logger.trace("Right movement deactivated");
                    break;
                case KeyEvent.VK_SPACE:
                    physics.setJump(false);
                    logger.trace("Jump deactivated");
                    break;
            }
        }
    }

    /**
     * Unpausing game.
     */
    public void unpauseGame() {
        logger.info("Unpausing game");
        paused = false;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public void setMaxLvlOffset(int lvlOffset) {
        logger.debug("Setting max level offset to: {}", lvlOffset);
        this.maxLvlOffset = lvlOffset;
    }

    public void setLevelComp(boolean levelCompleted) {
        logger.info("Setting level completed to: {}", levelCompleted);
        this.lvlComp = levelCompleted;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void checkObhectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkTrapTouched(Player p) {
        objectManager.checkTrapsTouched(p);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isLvlComp() {
        return lvlComp;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public PauseOverlay getPauseOverlay() {
        return pauseOverlay;
    }

    public LevelCompOverlay getLevelCompOverlay() {
        return levelCompOverlay;
    }

    public GameOverOverlay getGameOverOverlay() {
        return gameOverOverlay;
    }

    public void setXLevelOffset(int i) {
    }

    public void setBackgroundImg(BufferedImage dummyImage) {
    }

    public void setPlayer(Player mockPlayer) {
    }

    public int getXLevelOffset() {
        return xLvlOffset;
    }
}