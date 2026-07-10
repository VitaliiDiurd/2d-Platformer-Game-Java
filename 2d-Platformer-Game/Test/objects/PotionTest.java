package objects;

import main.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class PotionTest {

    private Potion potion;
    private Graphics mockGraphics;

    @BeforeEach
    void setUp() {
        potion = new Potion(100, 150, 1);

        mockGraphics = mock(Graphics.class);
    }

    @Test
    void testDrawDoesNotThrowException() {
        assertDoesNotThrow(() -> potion.draw(mockGraphics, 0));

        verify(mockGraphics, never()).drawImage(any(), anyInt(), anyInt(), anyInt(), anyInt(), any());
    }

    @Test
    void testUpdateDoesNotThrowException() {
        assertDoesNotThrow(() -> potion.update());
    }

    @Test
    void testUpdateCallsUpdateAnimationTick() {
        Potion spyPotion = spy(potion);

        spyPotion.update();

        verify(spyPotion).updateAnimationTick();
    }
}
