package utilz;

import exeptions.ResourceLoadException;
import exeptions.ResourceNotFoundException;
import exeptions.ResourceReadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LoadSaveTest {

    @Test
    void getSpriteAtlas_ShouldReturnImage_ForValidResource() {
        assertDoesNotThrow(() -> {
            BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
            assertNotNull(image, "Image should not be null");
            assertTrue(image.getWidth() > 0, "Image should have width");
            assertTrue(image.getHeight() > 0, "Image should have height");
        }, "Should not throw for existing resource");
    }

    @Test
    void getSpriteAtlas_ShouldThrowResourceReadException_ForCorruptedResource() {
        assertThrows(
                ResourceReadException.class,
                () -> LoadSave.GetSpriteAtlas("corrupted_image.png"),
                "Should throw for corrupted resource"
        );
    }

    @Test
    void getAllLevels_ShouldReturnImages_ForValidLevelsDirectory(@TempDir Path tempDir) throws IOException {
        Path lvlsDir = tempDir.resolve("lvls");
        Files.createDirectory(lvlsDir);

        for (int i = 1; i <= 3; i++) {
            Path levelFile = lvlsDir.resolve(i + ".png");
            BufferedImage dummyImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(dummyImage, "png", levelFile.toFile());
        }

        BufferedImage[] levels = null;
        try {
            var originalClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(new ClassLoader() {
                    @Override
                    public URL getResource(String name) {
                        if (name.equals("/lvls")) {
                            try {
                                return lvlsDir.toUri().toURL();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return super.getResource(name);
                    }
                });

                levels = LoadSave.GetAllLevels();
            } finally {
                Thread.currentThread().setContextClassLoader(originalClassLoader);
            }

            assertNotNull(levels, "Levels array should not be null");
            assertEquals(3, levels.length, "Should return 3 level images");
            for (BufferedImage img : levels) {
                assertNotNull(img, "Level image should not be null");
            }
        } catch (ResourceNotFoundException e) {
            fail("Should not throw ResourceNotFoundException for existing levels directory");
        }
    }

    @Test
    void getAllLevels_ShouldHandleEmptyDirectory(@TempDir Path tempDir) throws IOException {
        Path lvlsDir = tempDir.resolve("lvls");
        Files.createDirectory(lvlsDir);

        BufferedImage[] levels = null;
        try {
            var originalClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(new ClassLoader() {
                    @Override
                    public URL getResource(String name) {
                        if (name.equals("/lvls")) {
                            try {
                                return lvlsDir.toUri().toURL();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return super.getResource(name);
                    }
                });

                levels = LoadSave.GetAllLevels();
            } finally {
                Thread.currentThread().setContextClassLoader(originalClassLoader);
            }

            assertNotNull(levels, "Levels array should not be null");
            assertEquals(0, levels.length, "Should return empty array for empty directory");
        } catch (ResourceNotFoundException e) {
            fail("Should not throw ResourceNotFoundException for empty directory");
        }
    }

    private void createTestImage(File file) throws IOException {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(image, "png", file);
    }
}