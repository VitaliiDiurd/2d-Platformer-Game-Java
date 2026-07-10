package utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is responsible for loading a sprite sheet.
 */
public class SpriteAtlas {
    private BufferedImage spriteSheet;

    public SpriteAtlas(String spriteSheetPath) {
        loadSpriteSheet(spriteSheetPath);
    }

    /**
     * Loads the sprite sheet from the given path.
     * @param path
     */
    private void loadSpriteSheet(String path) {
        // First try as resource
        InputStream is = getClass().getResourceAsStream(path.startsWith("/") ? path : "/" + path);
        try {
            if (is != null) {
                spriteSheet = ImageIO.read(is);
                is.close();
                return;
            }

            // If resource fails, try as file
            File file = new File(path);
            if (file.exists()) {
                spriteSheet = ImageIO.read(file);
                return;
            }

            System.err.println("Could not find resource at path: " + path);
            createPlaceholderImage();
        } catch (IOException e) {
            e.printStackTrace();
            createPlaceholderImage();
        }
    }

    /**
     * Creates a placeholder image
     */
    private void createPlaceholderImage() {
        spriteSheet = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics2D g = spriteSheet.createGraphics();
        g.setColor(java.awt.Color.PINK);
        g.fillRect(0, 0, 64, 64);
        g.dispose();
    }

    /**
     * Retrieves a sub-image from the sprite sheet based on the provided coordinates and dimensions.
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public BufferedImage getSprite(int x, int y, int width, int height) {
        return spriteSheet.getSubimage(x, y, width, height);
    }
}