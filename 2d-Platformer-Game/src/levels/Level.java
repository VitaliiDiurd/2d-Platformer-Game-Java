package levels;

import entities.Orc;
import main.Game;
//import objects.Coin;
import objects.GameContainer;
import objects.Potion;
import objects.Trap;
import utilz.HelpMethods;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.*;

/**
 * The Level class represents a game level, including its data, enemies, items, and environmental objects.
 */
public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Orc> orcs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Trap> traps;
//    private ArrayList<Coin> coins;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;

public Level(BufferedImage img) {
    this.img = img;
    createLevelData();
    createEnemies();
    createPotions();
    createContainers();
    createTraps();
    calcLvlOffsets();
}

    /**
     * Creates traps for the level.
     */
    private void createTraps() {
        traps = HelpMethods.GetTraps(img);
    }

    /**
     * Creates containers for the level.
     */
    private void createContainers() {
        containers = HelpMethods.GetContainers(img);
    }

    /**
     * Creates potions for the level
     */
    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }

    /**
     * Calculates level offsets to determine how much the level can be scrolled horizontally.
     */
    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    /**
     * Creates enemies (Orcs) for the level by extracting data from the level image.
     */
    private void createEnemies() {
        orcs = GetOrcs(img);
    }

    /**
     * reates level data by extracting data from the level image.
     */
    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Orc> getCrabs() {
        return orcs;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Trap> getTraps() {
        return traps;
    }


    public int[][] getLevelData() {
        return lvlData;
    }
}
