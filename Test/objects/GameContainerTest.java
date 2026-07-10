package objects;

import org.junit.jupiter.api.Test;

import java.awt.Graphics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameContainerTest {

    @Test
    void draw_DoesNotThrow() {
        GameContainer container = new GameContainer(10, 20, 0);
        Graphics graphics = mock(Graphics.class);

        assertDoesNotThrow(() -> container.draw(graphics, 0));
    }

    @Test
    void update_WithAnimationEnabled_UpdatesAnimationTick() {
        GameContainer container = new GameContainer(10, 20, 0);
        container.doAni = true;

        int initialAniTick = container.aniTick;
        container.update();
        assertTrue(container.aniTick == initialAniTick + 1 || container.aniTick == 0);
    }

    @Test
    void update_WithAnimationDisabled_DoesNotUpdateAnimationTick() {
        GameContainer container = new GameContainer(10, 20, 0);
        container.doAni = false;

        int initialAniTick = container.aniTick;
        container.update();
        assertEquals(initialAniTick, container.aniTick);
    }
}
