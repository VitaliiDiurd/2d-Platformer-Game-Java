package utilz;

import entities.Orc;
import main.Game;
import objects.Coin;
import objects.GameContainer;
import objects.Potion;
import objects.Trap;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;
import static utilz.LoadSave.GetSpriteAtlas;

/**
 * The HelpMethods class contains various utility methods that help with game logic
 */
public class HelpMethods {
    /**
     * Determines if an entity can move to a specified position based on the level's data.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param lvlData
     * @return
     */
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    return !IsSolid(x, y + height, lvlData);
        return false;
    }

    /**
     * Checks if a specified position is solid (collision occurs) in the level's data.
     * @param x
     * @param y
     * @param lvlData
     * @return
     */
    public static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    /**
     * Determines if a tile at the specified coordinates is solid.
     * @param xTile
     * @param yTile
     * @param lvlData
     * @return
     */
    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];

        return value != 11;
    }

    /**
     * Calculates the x-coordinate at which an entity hits a wall while moving horizontally.
     * @param hitbox
     * @param xSpeed
     * @return
     */
    public static float GetEntPosWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * Calculates the y-coordinate at which an entity hits something while falling or jumping.
     * @param hitbox
     * @param airSpeed
     * @return
     */
    public static float GetEntYPosHitSmth(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * Checks if an entity is standing on the floor.
     * @param hitbox
     * @param lvlData
     * @return
     */
    public static boolean IsEntOnFlour(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    /**
     * Checks if there is a solid tile below the entity when moving horizontally.
     * @param hitbox
     * @param xSpeed
     * @param lvlData
     * @return
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if(xSpeed > 0) {
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        } else {
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }
    }

    /**
     * Checks if all tiles in a specified range are walkable.
     * @param xStart
     * @param xEnd
     * @param y
     * @param lvlData
     * @return
     */
    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if (!IsTileSolid(xStart + i, y + 1, lvlData))
                return false;
        }

        return true;
    }

    /**
     * Checks if the line of sight between two hitboxes is clear.
     * @param lvlData
     * @param firstHitbox
     * @param secondHitbox
     * @param yTile
     * @return
     */
    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);

    }

    /**
     * Loads the level data from a level image.
     * @param img
     * @return
     */
    public static int[][] GetLevelData(BufferedImage img) {
        if (img == null) {
            throw new IllegalArgumentException("Level image could not be loaded.");
        }

        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48)
                    value = 0;
                lvlData[j][i] = value;
            }
        }

        return lvlData;
    }

    /**
     * // Methods for extracting various game objects from a level image (Orcs, Potions, Containers, Traps, Coins)
     * @param img
     * @return
     */
    public static ArrayList<Orc> GetOrcs(BufferedImage img) {
        ArrayList<Orc> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == ORC)
                    list.add(new Orc(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img) {
        ArrayList<Potion> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == RED_POTION || value == BLUE_POTION)
                    list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));

            }
        return list;
    }

    public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
        ArrayList<GameContainer> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == BARREL)
                    list.add(new GameContainer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }
        return list;
    }

    public static ArrayList<Trap> GetTraps(BufferedImage img) {
        ArrayList<Trap> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == TRAP)
                    list.add(new Trap(i * Game.TILES_SIZE, j * Game.TILES_SIZE, TRAP));
            }
        return list;
    }

    public static ArrayList<Coin> GetCoins(BufferedImage img) {
        ArrayList<Coin> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == COIN)
                    list.add(new Coin(i * Game.TILES_SIZE, j * Game.TILES_SIZE, TRAP));
            }
        return list;
    }
}