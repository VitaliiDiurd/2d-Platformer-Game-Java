package entities;

import components.*;
import gamestates.Playing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerTest {
    private Player player;
    private Playing mockPlaying;
    private AnimationComponent mockAnim;
    private MovementComponent mockMove;
    private PhysicsComponent mockPhys;
    private RenderComponent mockRender;

    @BeforeEach
    void setUp() {
        mockPlaying = mock(Playing.class);
        mockAnim = mock(AnimationComponent.class);
        mockMove = mock(MovementComponent.class);
        mockPhys = mock(PhysicsComponent.class);
        mockRender = mock(RenderComponent.class);

        player = spy(new Player(100, 100, 50, 50, mockPlaying));

        doReturn(mockAnim).when(player).getComponent(AnimationComponent.class);
        doReturn(mockMove).when(player).getComponent(MovementComponent.class);
        doReturn(mockPhys).when(player).getComponent(PhysicsComponent.class);
        doReturn(mockRender).when(player).getComponent(RenderComponent.class);
    }

    @Test
    void kill() {
        player.kill();
        assertEquals(0, player.getCurrentHealth());
    }

    @Test
    void getAttackBox() {
        assertNotNull(player.getAttackBox());
    }

    @Test
    void isAttacking() {
        when(mockAnim.isAttacking()).thenReturn(true);
        assertTrue(player.isAttacking());

        when(mockAnim.isAttacking()).thenReturn(false);
        assertFalse(player.isAttacking());
    }

    @Test
    void setLeft() {
        player.setLeft(true);
        verify(mockMove).setLeft(true);
    }

    @Test
    void setRight() {
        player.setRight(true);
        verify(mockMove).setRight(true);
    }

    @Test
    void changeHealth() {
        int initialHealth = player.getCurrentHealth();
        player.changeHealth(10);
        assertEquals(initialHealth + 10, player.getCurrentHealth());
        verify(mockAnim).changeHealth(10);
    }

    @Test
    void checkAttack() {
        when(mockAnim.isAttacking()).thenReturn(true);
        when(mockAnim.getAniIndex()).thenReturn(1);
        player.update();
        verify(mockPlaying).checkEnemyHit(any());
        verify(mockPlaying).checkObhectHit(any());
    }

    @Test
    void testUpdateWhenNotAttacking() {
        when(mockAnim.isAttacking()).thenReturn(false);
        player.update();
        verify(mockPlaying).checkPotionTouched(any());
        verify(mockPlaying).checkTrapTouched(player);
    }

    @Test
    void setJump() {
        player.setJump(true);
        verify(mockPhys).setJump(true);
    }

    @Test
    void getPlayerAction() {
        when(mockAnim.getCurrentAction()).thenReturn(1);
        assertEquals(1, player.getPlayerAction());
    }

    @Test
    void setAttacking() {
        player.setAttacking(true);
        verify(mockAnim).setAttacking(true);
    }

    @Test
    void resetDirBooleans() {
        player.resetDirBooleans();
        verify(mockMove).resetDirBooleans();
    }

    @Test
    void resetAll() {
        player.resetAll();
        verify(mockAnim).resetHealth();
        verify(mockMove).resetDirBooleans();
        verify(mockPhys).setJump(false);
    }

    @Test
    void updateAttackBox() {
        when(mockMove.getFlipW()).thenReturn(1);
        player.update();
        when(mockMove.getFlipW()).thenReturn(-1);
        player.update();
    }

    @Test
    void loadLvlData() {
        int[][] mockLvlData = new int[10][10];
        player.loadLvlData(mockLvlData);
        verify(mockPhys).loadLvlData(mockLvlData);
        verify(mockMove).loadLvlData(mockLvlData);
    }
}