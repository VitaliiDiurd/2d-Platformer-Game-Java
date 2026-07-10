package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import exeptions.ResourceLoadException;
import exeptions.ResourceNotFoundException;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

/**
 * Manages all enemy entities in the game.
 */
public class EnemyManager {

    private Playing playing;
    public static BufferedImage[][] orcArr;
    private ArrayList<Orc> orcs = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    /**
     * Loads enemies from the current level.
     * @param level
     */
    public void loadEnemies(Level level) {
        orcs = level.getCrabs();
        System.out.println("size of orcs: " + orcs.size());
    }

    /**
     * Updates all active enemies.
     * @param lvlData
     */
    public void update(int [][] lvlData) {
        boolean isAnyActive = false;
        for (Orc c : orcs)
            if (c.isActive()) {
                c.update(lvlData, (Player) playing.getPlayer());
                isAnyActive = true;
            }
        if(!isAnyActive)
            playing.setLevelComp(true);
    }

    /**
     * Draws all active enemies.
     * @param g
     * @param xLvlOffset
     */
    public void draw(Graphics g, int xLvlOffset) {
        drawOrcs(g, xLvlOffset);
    }

    /**
     * Draws all active orcs.
     * @param g
     * @param xLvlOffset
     */
    private void drawOrcs(Graphics g, int xLvlOffset) {
        for (Orc c : orcs)
            if (c.isActive()) {
            g.drawImage(orcArr[c.getEnemyState()][c.getAniIndex()],
                    (int) (c.getHitbox().x - xLvlOffset - ORC_DRAWOFFSET_X + c.flipX()),
                    (int) (c.getHitbox().y - ORC_DRAWOFFSET_Y),
                    ORC_WIDTH * c.flipW(), ORC_HEIGHT,
                    null);
//            c.drawAttackBox(g, xLvlOffset);
        }
    }

    /**
     * Checks if any enemy is hit.
     * @param attackBox
     */
    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Orc c : orcs)
            if (c.isActive())
                if (attackBox.intersects(c.getHitbox())) {
                    c.hurt(20);
                    return;
                }
    }

    /**
     * Loads the sprite sheet for orc enemies.
     */
    private void loadEnemyImgs() {
        try {
            orcArr = new BufferedImage[9][6];
            BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ORC_ATLAS);
            for (int j = 0; j < orcArr.length; j++)
                for (int i = 0; i < orcArr[j].length; i++)
                    orcArr[j][i] = temp.getSubimage(i * ORC_WIDTH_DEFAULT, j * 40, ORC_WIDTH_DEFAULT, ORC_HEIGHT_DEFAULT);
        } catch (ResourceNotFoundException e) {
            System.err.println("Failed to load orc enemy images: " + e.getMessage());
        } catch (ResourceLoadException e) {
            System.err.println("Error loading orc enemy images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Resets all enemies.
     */
    public void resetAllEnemies() {
        for (Orc c : orcs)
            c.resetEnemy();
    }
}