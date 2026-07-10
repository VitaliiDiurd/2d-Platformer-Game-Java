package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PauseButtonTest {

    private PauseButton pauseButton;
    private final int initialX = 10;
    private final int initialY = 20;
    private final int initialWidth = 100;
    private final int initialHeight = 50;

    @BeforeEach
    void setUp() {
        pauseButton = new PauseButton(initialX, initialY, initialWidth, initialHeight);
    }

    @Test
    void testGetX() {
        assertEquals(initialX, pauseButton.getX());
    }

    @Test
    void testSetX() {
        int newX = 50;
        pauseButton.setX(newX);
        assertEquals(newX, pauseButton.getX());
    }

    @Test
    void testGetY() {
        assertEquals(initialY, pauseButton.getY());
    }

    @Test
    void testSetY() {
        int newY = 75;
        pauseButton.setY(newY);
        assertEquals(newY, pauseButton.getY());
    }

    @Test
    void testGetWidth() {
        assertEquals(initialWidth, pauseButton.getWidth());
    }

    @Test
    void testSetWidth() {
        int newWidth = 200;
        pauseButton.setWidth(newWidth);
        assertEquals(newWidth, pauseButton.getWidth());
    }

    @Test
    void testGetHeight() {
        assertEquals(initialHeight, pauseButton.getHeight());
    }

    @Test
    void testSetHeight() {
        int newHeight = 80;
        pauseButton.setHeight(newHeight);
        assertEquals(newHeight, pauseButton.getHeight());
    }

    @Test
    void testBoundsAfterSetters() {
        int newX = 30;
        int newY = 40;
        int newWidth = 150;
        int newHeight = 75;

        pauseButton.setX(newX);
        pauseButton.setY(newY);
        pauseButton.setWidth(newWidth);
        pauseButton.setHeight(newHeight);

        assertEquals(initialX, pauseButton.getBounds().x);
        assertEquals(initialY, pauseButton.getBounds().y);
        assertEquals(initialWidth, pauseButton.getBounds().width);
        assertEquals(initialHeight, pauseButton.getBounds().height);
    }

    @Test
    void testSettersWithZeroValues() {
        pauseButton.setX(0);
        pauseButton.setY(0);
        pauseButton.setWidth(0);
        pauseButton.setHeight(0);

        assertEquals(0, pauseButton.getX());
        assertEquals(0, pauseButton.getY());
        assertEquals(0, pauseButton.getWidth());
        assertEquals(0, pauseButton.getHeight());
    }

    @Test
    void testSettersWithNegativeValues() {
        pauseButton.setX(-10);
        pauseButton.setY(-20);
        pauseButton.setWidth(-30);
        pauseButton.setHeight(-40);

        assertEquals(-10, pauseButton.getX());
        assertEquals(-20, pauseButton.getY());
        assertEquals(-30, pauseButton.getWidth());
        assertEquals(-40, pauseButton.getHeight());
    }
}