package components;

import exeptions.ResourceLoadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimationComponentTest {

    private AnimationComponent animationComponent;

    @BeforeEach
    void setUp() throws ResourceLoadException {
        animationComponent = new AnimationComponent("path/to/atlas");
    }

    @Test
    void testResetAniTick() {
        animationComponent.aniTick = 10;
        animationComponent.aniIndex = 3;

        animationComponent.resetAniTick();

        assertEquals(0, animationComponent.aniTick);
        assertEquals(0, animationComponent.aniIndex);
    }

    @Test
    void testGetCurrentAction() {
        assertEquals(0, animationComponent.getCurrentAction());

        animationComponent.setAnimation(1, 25);
        assertEquals(1, animationComponent.getCurrentAction());
    }

    @Test
    void testGetAniIndex() {
        assertEquals(0, animationComponent.getAniIndex());

        animationComponent.aniIndex = 3;

        assertEquals(3, animationComponent.getAniIndex());
    }

    @Test
    void testSetAttacking() {
        assertFalse(animationComponent.isAttacking());

        animationComponent.setAttacking(true);

        assertTrue(animationComponent.isAttacking());

        animationComponent.setAttacking(false);

        assertFalse(animationComponent.isAttacking());
    }

    @Test
    void testChangeHealth() {
        animationComponent.changeHealth(-50);
        assertEquals(50, animationComponent.getCurrentHealth());

        animationComponent.changeHealth(100);
        assertEquals(100, animationComponent.getCurrentHealth());

        animationComponent.changeHealth(-150);
        assertEquals(0, animationComponent.getCurrentHealth());
    }
}
