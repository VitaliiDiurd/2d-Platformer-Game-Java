package levels;

import exeptions.ResourceLoadException;
import exeptions.ResourceNotFoundException;
import gamestates.GameState;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The LevelManager class handles the loading, building, and drawing of levels in the game.
 */
public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    protected int lvlIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        importLevelSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    /**
     * Loads the next level in the game, resetting if all levels have been completed.
     */
    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("No more levels! Game Completed!");
            GameState.state = GameState.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
    }

    /**
     * Builds all levels by loading level images and creating corresponding Level objects.
     */
    private void buildAllLevels() {
        try {
            BufferedImage[] allLevels = LoadSave.GetAllLevels();
            for (BufferedImage img : allLevels)
                levels.add(new Level(img));
        } catch (ResourceNotFoundException e) {
            System.err.println("Error building levels: " + e.getMessage());
        }
    }

    /**
     * Imports the level sprite atlas and divides it into individual tile sprites.
     */
    private void importLevelSprites() {
        try {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
            levelSprite = new BufferedImage[48];
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 12; i++) {
                    int index = j * 12 + i;
                    levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
                }
            }
        } catch (ResourceNotFoundException e) {
            System.err.println("Failed to load level sprites: " + e.getMessage());
        } catch (ResourceLoadException e) {
            System.err.println("Error loading level sprites: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Draws the current level on the graphics context with the provided horizontal offset.
     * @param g
     * @param xLvlOffset
     */
    public void draw(Graphics2D g, int xLvlOffset) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLvlData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - xLvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void update(){

    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

}
