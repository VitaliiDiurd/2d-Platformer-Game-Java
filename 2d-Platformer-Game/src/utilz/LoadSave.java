package utilz;

import exeptions.ResourceLoadException;
import exeptions.ResourceNotFoundException;
import exeptions.ResourceReadException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class is responsible for loading various game resources, such as sprite atlases, level images, and other assets.
 */
public class LoadSave {

    public static final String PLAYER_ATLAS = "gandalf_atlas.png";
    public static final String LEVEL_ATLAS = "level_atlas.png";
    public static final String ORC_ATLAS = "orc_atlas.png";
    public static final String GAME_BACKGROUND = "background_game.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_back.png";
    public static final String BACKGROUND_MENU = "background_menu.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String LEVEL_COMP = "level_completed.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String OBJECTS_ATLAS = "objects_atlas.png";
    public static final String POTIONS_ATLAS = "potions_atlas.png";
    public static final String TRAP_IMAGE = "trap_image.png";
    public static final String COIN_ATLAS = "coins_atlas.png";


    /**
     * Loads a sprite atlas from the resources directory.
     * @param fileName
     * @return
     * @throws ResourceLoadException
     */
    public static BufferedImage GetSpriteAtlas(String fileName) throws ResourceLoadException {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        if (is == null) {
            throw new ResourceNotFoundException(fileName);
        }

        try {
            img = ImageIO.read(is);
            if (img == null) {
                throw new ResourceReadException(fileName, new IOException("ImageIO returned null"));
            }
        } catch (IOException e) {
            throw new ResourceReadException(fileName, e);
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                System.err.println("Failed to close input stream for " + fileName);
            }
        }
        return img;
    }

    /**
     * Loads all level images.
     * @return
     * @throws ResourceNotFoundException
     */
    public static BufferedImage[] GetAllLevels() throws ResourceNotFoundException {
        URL url = LoadSave.class.getResource("/lvls");
        if (url == null) {
            throw new ResourceNotFoundException("levels directory");
        }
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];

            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for (int i = 0; i < imgs.length; i++)
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return imgs;
    }
}
