package objects;

import main.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static utilz.Constants.ObjectConstants.*;

class GameObjectTest {
    private static class MockGameObject extends GameObject {
        public MockGameObject(int x, int y, int objectType) {
            super(x, y, objectType);
        }

        private boolean updateAnimationTickCalled = false;

        @Override
        public void update() {
        }

        @Override
        public void draw(Graphics g, int lvlOffset) {
        }

        public void testUpdateAnimationTick() {
            updateAnimationTick();
            updateAnimationTickCalled = true;
        }

        public void testInitHitbox(int width, int height) {
            initHitbox(width, height);
        }

        public boolean wasUpdateAnimationTickCalled() {
            return updateAnimationTickCalled;
        }

        public int getAniTick() {
            return aniTick;
        }

        public void setAniTick(int aniTick) {
            this.aniTick = aniTick;
        }

        public void setAniIndex(int aniIndex) {
            this.aniIndex = aniIndex;
        }

        public void setAniSpeed(int aniSpeed) {
            this.aniSpeed = aniSpeed;
        }

        public boolean isDoAni() {
            return doAni;
        }

        public void setDoAni(boolean doAni) {
            this.doAni = doAni;
        }
    }

    private MockGameObject testObject;
    private static final int TEST_X = 100;
    private static final int TEST_Y = 200;
    private static final int TEST_TYPE = BARREL;

    @BeforeEach
    void setUp() {
        testObject = new MockGameObject(TEST_X, TEST_Y, TEST_TYPE);
    }

    @Test
    void constructorShouldInitializeBasicProperties() {
        assertEquals(TEST_X, testObject.x);
        assertEquals(TEST_Y, testObject.y);
        assertEquals(TEST_TYPE, testObject.objectType);
        assertTrue(testObject.active);
        assertFalse(testObject.doAni);
    }

    @Test
    void updateAnimationTickShouldIncrementCounter() {
        int initialAniTick = testObject.getAniTick();
        testObject.testUpdateAnimationTick();
        assertTrue(testObject.getAniTick() > initialAniTick);
    }

    @Test
    void updateAnimationTickShouldResetCounterWhenReachingAniSpeed() {
        testObject.setAniTick(testObject.aniSpeed - 1);
        testObject.testUpdateAnimationTick();
        assertEquals(0, testObject.getAniTick());
    }

    @Test
    void updateAnimationTickShouldIncrementAniIndexWhenResetTick() {
        testObject.setAniTick(testObject.aniSpeed - 1);
        int initialAniIndex = testObject.aniIndex;
        testObject.testUpdateAnimationTick();
        assertEquals(initialAniIndex + 1, testObject.aniIndex);
    }

    @Test
    void updateAnimationTickShouldResetAniIndexWhenReachingMaxFrames() {
        try (MockedStatic<utilz.Constants.ObjectConstants> mockedConstants = mockStatic(utilz.Constants.ObjectConstants.class)) {
            mockedConstants.when(() -> GetSpriteAmount(TEST_TYPE)).thenReturn(5);

            testObject.setAniTick(testObject.aniSpeed - 1);
            testObject.setAniIndex(4);

            testObject.testUpdateAnimationTick();
            assertEquals(0, testObject.aniIndex);
        }
    }

    @Test
    void updateAnimationTickShouldDeactivateBarrelObjectWhenAnimationCompletes() {
        try (MockedStatic<utilz.Constants.ObjectConstants> mockedConstants = mockStatic(utilz.Constants.ObjectConstants.class)) {
            mockedConstants.when(() -> GetSpriteAmount(TEST_TYPE)).thenReturn(5);
            testObject.setDoAni(true);
            testObject.setAniTick(testObject.aniSpeed - 1);
            testObject.setAniIndex(4);

            testObject.testUpdateAnimationTick();

            assertFalse(testObject.isDoAni());
            assertFalse(testObject.isActive());
        }
    }


    @Test
    void resetShouldSetDefaultValuesForBarrelObject() {
        testObject.setAniIndex(3);
        testObject.setAniTick(10);
        testObject.setActive(false);
        testObject.setDoAni(true);

        testObject.reset();

        assertEquals(0, testObject.aniIndex);
        assertEquals(0, testObject.getAniTick());
        assertTrue(testObject.isActive());
        assertFalse(testObject.isDoAni());
    }

    @Test
    void resetShouldSetDefaultValuesForNonBarrelObject() {
        MockGameObject nonBarrelObject = new MockGameObject(TEST_X, TEST_Y, TRAP);

        nonBarrelObject.setAniIndex(3);
        nonBarrelObject.setAniTick(10);
        nonBarrelObject.setActive(false);
        nonBarrelObject.setDoAni(false);

        nonBarrelObject.reset();

        assertEquals(0, nonBarrelObject.aniIndex);
        assertEquals(0, nonBarrelObject.getAniTick());
        assertTrue(nonBarrelObject.isActive());
        assertTrue(nonBarrelObject.isDoAni());
    }

    @Test
    void drawHitboxShouldSetColorAndDrawRectangle() {
        Graphics mockGraphics = mock(Graphics.class);

        testObject.hitbox = new Rectangle2D.Float(TEST_X, TEST_Y, 50, 60);

        int lvlOffset = 20;
        testObject.drawHitbox(mockGraphics, lvlOffset);

        verify(mockGraphics).setColor(Color.RED);
        verify(mockGraphics).drawRect(
                (int) testObject.hitbox.x - lvlOffset,
                (int) testObject.hitbox.y,
                (int) testObject.hitbox.width,
                (int) testObject.hitbox.height
        );
    }

    @Test
    void getterMethodsShouldReturnCorrectValues() {
        testObject.xDrawOffset = 10;
        testObject.yDrawOffset = 15;
        testObject.setAniIndex(3);

        assertEquals(10, testObject.getxDrawOffset());
        assertEquals(15, testObject.getyDrawOffset());
        assertEquals(TEST_TYPE, testObject.getObjectType());
        assertEquals(3, testObject.getAniIndex());
    }

    @Test
    void setterMethodsShouldUpdateValues() {
        testObject.setAni(true);
        testObject.setActive(false);

        assertTrue(testObject.isDoAni());
        assertFalse(testObject.isActive());
    }

    @Test
    void getHitboxShouldReturnHitboxReference() {
        Rectangle2D.Float testHitbox = new Rectangle2D.Float(TEST_X, TEST_Y, 50, 60);
        testObject.hitbox = testHitbox;

        assertSame(testHitbox, testObject.getHitbox());
    }
}