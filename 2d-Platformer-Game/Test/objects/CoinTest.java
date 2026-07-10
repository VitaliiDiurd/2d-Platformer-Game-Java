package objects;

import org.junit.jupiter.api.Test;

import java.awt.Graphics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoinTest {

    @Test
    void draw_DoesNotThrow() {
        Coin coin = new Coin(10, 20, 0);
        Graphics graphics = mock(Graphics.class);

        assertDoesNotThrow(() -> coin.draw(graphics, 0));
    }

    @Test
    void update_UpdatesAnimationTick() {
        Coin coin = new Coin(10, 20, 0);

        int initialAniTick = coin.aniTick;
        coin.update();
        assertTrue(coin.aniTick == initialAniTick + 1 || coin.aniTick == 0);
    }
}
