package objects;

import main.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class TrapTest {

    private Trap trap;
    private Graphics mockGraphics;

    @BeforeEach
    void setUp() {
        trap = new Trap(100, 150, 1);

        mockGraphics = mock(Graphics.class);
    }

    @Test
    void testDrawDoesNotThrowException() {
        assertDoesNotThrow(() -> trap.draw(mockGraphics, 0));

        verify(mockGraphics, never()).drawImage(any(), anyInt(), anyInt(), anyInt(), anyInt(), any());
    }

    @Test
    void testUpdateDoesNotThrowException() {
        assertDoesNotThrow(() -> trap.update());
    }
}
