package entities;

import static org.mockito.Mockito.*;
import components.MovementComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.geom.Rectangle2D;

class EntityTest {

    private Entity entity;
    private MovementComponent mockMovementComponent;

    @BeforeEach
    void setUp() {
        mockMovementComponent = mock(MovementComponent.class);
        entity = new Entity(0, 0, 100, 100) {
            @Override
            public void update() {
                super.update();
            }
        };
        entity.addComponent(mockMovementComponent);
    }

    @Test
    void testResetDirBooleans() {
        entity.setLeft(true);
        entity.setRight(true);
        entity.resetDirBooleans();
        verify(mockMovementComponent).setLeft(false);
        verify(mockMovementComponent).setRight(false);
    }

    @Test
    void testSetLeft() {
        entity.setLeft(true);
        verify(mockMovementComponent).setLeft(true);
    }

    @Test
    void testSetRight() {
        entity.setRight(true);
        verify(mockMovementComponent).setRight(true);
    }

    @Test
    void testUpdateHitbox() {
        entity.initHitbox(10, 20, 50, 50);
        entity.x = 30;
        entity.y = 40;
        entity.updateHitbox();
        assertEquals(30, entity.getHitbox().x);
        assertEquals(40, entity.getHitbox().y);
    }

    @Test
    void testResetAll() {
        entity.resetAll();
    }
}