package objects;

import entities.Player;
import exeptions.ResourceLoadException;
import exeptions.ResourceNotFoundException;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;

/**
 * The ObjectManager class is responsible for managing and handling game objects such as potions, containers, and traps.
 */
public class ObjectManager {
    private Playing playing;
    protected BufferedImage[][] potionImgs;
    protected BufferedImage[][] containerImgs;
    private BufferedImage[][] coinImg;
    protected BufferedImage trapImg;
    protected ArrayList<Potion> potions;
    protected ArrayList<GameContainer> containers;
    protected ArrayList<Trap> traps;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        initDefaultImages();
        loadImgs();
    }

    /**
     * Initializes the default placeholder images for game objects.
     */
    private void initDefaultImages() {
        BufferedImage defaultImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = defaultImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 1, 1);
        g2d.dispose();

        potionImgs = new BufferedImage[2][7];
        for (int i = 0; i < potionImgs.length; i++) {
            for (int j = 0; j < potionImgs[i].length; j++) {
                potionImgs[i][j] = defaultImage;
            }
        }

        containerImgs = new BufferedImage[2][8];
        for (int i = 0; i < containerImgs.length; i++) {
            for (int j = 0; j < containerImgs[i].length; j++) {
                containerImgs[i][j] = defaultImage;
            }
        }

        trapImg = defaultImage;
    }

    /**
     * Loads objects from the specified level.
     * @param newLevel
     */
    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        traps = new ArrayList<>(newLevel.getTraps());
    }

    /**
     * Updates all active game objects (potions, containers, traps).
     */
    public void update() {
        if (potions != null)
            potions.stream()
                    .filter(Potion::isActive)
                    .forEach(Potion::update);

        if (containers != null)
            containers.stream()
                    .filter(GameContainer::isActive)
                    .forEach(GameContainer::update);
    }

    /**
     * Draws all active game objects (potions, containers, traps) to the screen.
     * @param g
     * @param xLvlOffset
     */
    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
    }

    /**
     * Draws all active potions.
     * @param g
     * @param xLvlOffset
     */
    private void drawPotions(Graphics g, int xLvlOffset) {
        if (potions == null) return;
        for (Potion p : potions)
            if (p.isActive()) {
                int type = (p.getObjectType() == RED_POTION) ? 1 : 0;
                BufferedImage img = potionImgs[type][p.getAniIndex()];
                if (img != null)
                    g.drawImage(img,
                            (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                            (int) (p.getHitbox().y - p.getyDrawOffset()),
                            POTION_WIDTH, POTION_HEIGHT, null);
            }
    }

    /**
     * Draws all active containers..
     * @param g
     * @param xLvlOffset
     */
    private void drawContainers(Graphics g, int xLvlOffset) {
        if (containers == null) return;
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                BufferedImage img = containerImgs[1][gc.getAniIndex()];
                if (img != null)
                    g.drawImage(img,
                            (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                            (int) (gc.getHitbox().y - gc.getyDrawOffset()),
                            CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }
    }

    /**
     * Draws all active traps.
     * @param g
     * @param xLvlOffset
     */
    private void drawTraps(Graphics g, int xLvlOffset) {
        if (traps == null) return;
        for (Trap t : traps)
            if (trapImg != null)
                g.drawImage(trapImg,
                        (int) (t.getHitbox().x - xLvlOffset),
                        (int) (t.getHitbox().y - t.getyDrawOffset()),
                        TRAP_WIDTH, TRAP_HEIGHT, null);
    }

    /**
     * Checks if any of the potions are touched by the specified hitbox.
     * @param hitbox
     */
    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        if (potions == null) return;
        potions.stream()
                .filter(p -> p.isActive() && hitbox.intersects(p.getHitbox()))
                .findFirst()
                .ifPresent(p -> {
                    p.setActive(false);
                    applyEffect(p);
                });
    }

    /**
     * Checks if any containers are hit by the specified attackbox.
     * @param attackbox
     */
    public void checkObjectHit(Rectangle2D.Float attackbox) {
        if (containers == null) return;
        for (GameContainer gc : containers) {
            if (gc.isActive() && gc.getHitbox().intersects(attackbox)) {
                gc.setAni(true);

                if (gc.getObjectType() == BARREL) {
                    int type = Math.random() < 0.25 ? RED_POTION : BLUE_POTION;
                    potions.add((Potion) GameObjectFactory.createGameObject(
                            (int) (gc.getHitbox().x + gc.getHitbox().width / 2),
                            (int) (gc.getHitbox().y + gc.getHitbox().height / 2),
                            type));
                }
                return;
            }
        }
    }

    /**
     * hecks if any traps are touched by the player.
     * @param p
     */
    public void checkTrapsTouched(Player p) {
        if (traps == null) return;
        for (Trap t : traps)
            if (t.getHitbox().intersects(p.getHitbox()))
                p.kill();
    }

    /**
     * Applies the effect of a potion to the player.
     * @param p
     */
    private void applyEffect(Potion p) {
        if (p == null) return;
        if (playing == null) return;
        if (playing.getPlayer() == null) return;

        if (p.getObjectType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else if (p.getObjectType() == BLUE_POTION)
            playing.getPlayer().changeHealth(BLUE_POTION_VALUE);
    }

    /**
     * Loads images for objects (potions, containers, traps).
     */
    private void loadImgs() {
        try {
            BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTIONS_ATLAS);
            for (int j = 0; j < potionImgs.length; j++) {
                for (int i = 0; i < potionImgs[j].length; i++) {
                    potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);
                }
            }

            BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.OBJECTS_ATLAS);
            for (int j = 0; j < containerImgs.length; j++) {
                for (int i = 0; i < containerImgs[j].length; i++) {
                    containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
                }
            }

            trapImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_IMAGE);

        } catch ( ResourceLoadException e) {
            System.err.println("Failed to load object images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Resets all objects in the current level.
     */
    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());
        if (potions != null)
            potions.forEach(Potion::reset);
        if (containers != null)
            containers.forEach(GameContainer::reset);
    }
}
