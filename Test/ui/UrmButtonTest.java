package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class UrmButtonTest {

    private UrmButton urmButton;
    private static final int TEST_X = 10;
    private static final int TEST_Y = 20;
    private static final int TEST_WIDTH = 30;
    private static final int TEST_HEIGHT = 40;
    private static final int TEST_ROW_INDEX = 0;

    @BeforeEach
    void setUp() throws Exception {

        Constructor<UrmButton> constructor = UrmButton.class.getDeclaredConstructor(
                int.class, int.class, int.class, int.class, int.class);
        constructor.setAccessible(true);

        urmButton = constructor.newInstance(TEST_X, TEST_Y, TEST_WIDTH, TEST_HEIGHT, TEST_ROW_INDEX);

        Field imgsField = UrmButton.class.getDeclaredField("imgs");
        imgsField.setAccessible(true);
        BufferedImage[] imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        }
        imgsField.set(urmButton, imgs);
    }

    @Test
    void testIsMouseOver() {
        assertFalse(urmButton.isMouseOver());

        urmButton.setMouseOver(true);
        assertTrue(urmButton.isMouseOver());

        urmButton.setMouseOver(false);
        assertFalse(urmButton.isMouseOver());
    }

    @Test
    void testIsMousePressed() {
        assertFalse(urmButton.isMousePressed());

        urmButton.setMousePressed(true);
        assertTrue(urmButton.isMousePressed());

        urmButton.setMousePressed(false);
        assertFalse(urmButton.isMousePressed());
    }

    @Test
    void testUpdateWithDifferentMouseStates() throws Exception {
        urmButton.update();
        assertEquals(0, getIndexValue(urmButton));

        urmButton.setMouseOver(true);
        urmButton.update();
        assertEquals(1, getIndexValue(urmButton));

        urmButton.setMousePressed(true);
        urmButton.update();
        assertEquals(2, getIndexValue(urmButton));

        urmButton.setMousePressed(false);
        urmButton.update();
        assertEquals(1, getIndexValue(urmButton));
    }

    @Test
    void testResetBools() {
        urmButton.setMouseOver(true);
        urmButton.setMousePressed(true);

        urmButton.resetBools();

        assertFalse(urmButton.isMouseOver());
        assertFalse(urmButton.isMousePressed());
    }

    @Test
    void testExceptionHandling() {

        try {

            UrmButton button = new UrmButton(TEST_X, TEST_Y, TEST_WIDTH, TEST_HEIGHT, TEST_ROW_INDEX);

            assertNotNull(button);
        } catch (Exception e) {
            fail("UrmButton constructor should handle exceptions internally: " + e.getMessage());
        }
    }

    private int getIndexValue(UrmButton button) throws Exception {
        Field field = UrmButton.class.getDeclaredField("index");
        field.setAccessible(true);
        return (int) field.get(button);
    }
}