package components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovementComponentTest {

    private MovementComponent movementComponent;

    @BeforeEach
    void setUp() {
        movementComponent = new MovementComponent();
    }

    @Test
    void testIsLeft() {
        assertFalse(movementComponent.isLeft());

        movementComponent.setLeft(true);
        assertTrue(movementComponent.isLeft());

        movementComponent.setLeft(false);
        assertFalse(movementComponent.isLeft());
    }

    @Test
    void testIsRight() {
        assertFalse(movementComponent.isRight());

        movementComponent.setRight(true);
        assertTrue(movementComponent.isRight());

        movementComponent.setRight(false);
        assertFalse(movementComponent.isRight());
    }

    @Test
    void testUpdate() {
        movementComponent.setRight(true);
        movementComponent.update();
        assertEquals(0, movementComponent.getFlipX());
        assertEquals(1, movementComponent.getFlipW());
        assertTrue(movementComponent.isMoving());

        movementComponent.setLeft(true);
        movementComponent.setRight(false);
        movementComponent.update();
        assertEquals(1, movementComponent.getFlipX());
        assertEquals(-1, movementComponent.getFlipW());
        assertTrue(movementComponent.isMoving());

        movementComponent.setLeft(false);
        movementComponent.setRight(false);
        movementComponent.update();
        assertEquals(1, movementComponent.getFlipX());
        assertEquals(-1, movementComponent.getFlipW());
        assertFalse(movementComponent.isMoving());
    }
}
