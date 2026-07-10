package utilz;

import entities.Orc;
import main.Game;
import objects.Coin;
import objects.GameContainer;
import objects.Potion;
import objects.Trap;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static utilz.HelpMethods.*;

class HelpMethodsTest {

    private final int[][] lvlData = {
            {11, 11, 11, 11},
            {11, 0, 0, 11},
            {11, 0, 0, 11},
            {11, 11, 11, 11}
    };

    @Test
    void canMoveHere() {
        assertTrue(CanMoveHere(32, 32, 16, 16, lvlData));
        assertFalse(CanMoveHere(0, 0, 16, 16, lvlData));
    }

    @Test
    void isSolid() {
        assertTrue(IsSolid(0, 0, lvlData));
        assertFalse(IsSolid(32, 32, lvlData));
    }

    @Test
    void isTileSolid() {
        assertTrue(IsTileSolid(0, 0, lvlData));
        assertFalse(IsTileSolid(1, 1, lvlData));
    }

    @Test
    void getEntPosWall() {
        Rectangle2D.Float hitbox = new Rectangle2D.Float(32, 32, 16, 16);
        assertEquals(31, GetEntPosWall(hitbox, 1));
        assertEquals(32, GetEntPosWall(hitbox, -1));
    }

    @Test
    void getEntYPosHitSmth() {
        Rectangle2D.Float hitbox = new Rectangle2D.Float(32, 32, 16, 16);
        assertEquals(31, GetEntYPosHitSmth(hitbox, 1));
        assertEquals(32, GetEntYPosHitSmth(hitbox, -1));
    }

    @Test
    void isEntOnFlour() {
        Rectangle2D.Float hitbox = new Rectangle2D.Float(32, 32, 16, 16);
        assertFalse(IsEntOnFlour(hitbox, lvlData));
        Rectangle2D.Float hitboxWall = new Rectangle2D.Float(0, 0, 16, 16);
        assertTrue(IsEntOnFlour(hitboxWall, lvlData));
    }

    @Test
    void isFloor() {
        Rectangle2D.Float hitbox = new Rectangle2D.Float(32, 32, 16, 16);
        assertFalse(IsFloor(hitbox, 0, lvlData));
    }

    @Test
    void isAllTilesWalkable() {
        assertTrue(IsAllTilesWalkable(1, 3, 1, lvlData));
        assertFalse(IsAllTilesWalkable(0, 2, 0, lvlData));
    }

    @Test
    void isSightClear() {
        Rectangle2D.Float hitbox1 = new Rectangle2D.Float(32, 32, 16, 16);
        Rectangle2D.Float hitbox2 = new Rectangle2D.Float(64, 32, 16, 16);
        assertTrue(IsSightClear(lvlData, hitbox1, hitbox2, 1));
    }

    @Test
    void getLevelData() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, new java.awt.Color(10, 0, 0).getRGB());
        img.setRGB(1, 0, new java.awt.Color(50, 0, 0).getRGB());
        int[][] data = GetLevelData(img);

        assertEquals(10, data[0][0]);
        assertEquals(0, data[0][1]);
    }

    @Test
    void getOrcs() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, new java.awt.Color(0, Constants.EnemyConstants.ORC, 0).getRGB());
        ArrayList<Orc> orcs = GetOrcs(img);
        assertEquals(1, orcs.size());
    }

    @Test
    void getPotions() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, new java.awt.Color(0, 0, Constants.ObjectConstants.RED_POTION).getRGB());
        ArrayList<Potion> potions = GetPotions(img);
        assertEquals(1, potions.size());
    }

    @Test
    void getContainers() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, new java.awt.Color(0, 0, Constants.ObjectConstants.BARREL).getRGB());
        ArrayList<GameContainer> containers = GetContainers(img);
        assertEquals(1, containers.size());
    }

    @Test
    void getTraps() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, new java.awt.Color(0, 0, Constants.ObjectConstants.TRAP).getRGB());
        ArrayList<Trap> traps = GetTraps(img);
        assertEquals(1, traps.size());
    }

    @Test
    void getCoins() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, new java.awt.Color(0, 0, Constants.ObjectConstants.COIN).getRGB());
        ArrayList<Coin> coins = GetCoins(img);
        assertEquals(1, coins.size());
    }
}
