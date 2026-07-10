package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;

class EnemyTest {
    private TestEnemy enemy;
    private Player mockPlayer;
    private int[][] mockLvlData;

    private static class TestEnemy extends Enemy {
        public TestEnemy(float x, float y, int width, int height, int enemyType) {
            super(x, y, width, height, enemyType);
        }

        @Override
        public void update() {
        }

        public void render(Graphics g, int lvlOffset) {
        }

        public void testFirstUpdateCheck(int[][] lvlData) {
            super.firstUpdateCheck(lvlData);
        }

        public void testUpdateInAir(int[][] lvlData) {
            super.updateInAir(lvlData);
        }

        public void testMove(int[][] lvlData) {
            super.move(lvlData);
        }

        public void testTurnTowardsPlayer(Player player) {
            super.turnTowardsPlayer(player);
        }

        public boolean testCanSeePlayer(int[][] lvlData, Player player) {
            return super.canSeePlayer(lvlData, player);
        }

        public boolean testIsPlayerInRange(Player player) {
            return super.isPlayerInRange(player);
        }

        public boolean testIsPlayerCloseForAttack(Player player) {
            return super.isPlayerCloseForAttack(player);
        }

        public void testCheckPlayerHit(Rectangle2D.Float attackBox, Player player) {
            super.checkPlayerHit(attackBox, player);
        }

        public void testUpdateAnimationTick() {
            super.updateAnimationTick();
        }

        public void testChangeWalkDir() {
            super.changeWalkDir();
        }

        public void setAniTick(int aniTick) {
            this.aniTick = aniTick;
        }

        public void setEnemyState(int enemyState) {
            this.enemyState = enemyState;
        }

        public void setAniIndex(int aniIndex) {
            this.aniIndex = aniIndex;
        }

        public void setInAir(boolean inAir) {
            this.inAir = inAir;
        }

        public void setFallSpeed(float fallSpeed) {
            this.fallSpeed = fallSpeed;
        }

        public void setWalkDir(int walkDir) {
            this.walkDir = walkDir;
        }

        public void setTileY(int tileY) {
            this.tileY = tileY;
        }

        public int getTileY() {
            return this.tileY;
        }

        public float getFallSpeed() {
            return this.fallSpeed;
        }

        public boolean isInAir() {
            return this.inAir;
        }

        public int getWalkDir() {
            return this.walkDir;
        }
    }

    @BeforeEach
    void setUp() {
        enemy = new TestEnemy(100, 100, 50, 50, ORC);
        mockPlayer = mock(Player.class);
        mockLvlData = new int[20][20];
        mockPlayer.hitbox = new Rectangle2D.Float(200, 100, 50, 50);
        when(mockPlayer.getHitbox()).thenReturn(mockPlayer.hitbox);
    }

    @Test
    void testUpdateInAir_LandsOnGround() {
        TestEnemy testEnemy = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testUpdateInAir(int[][] lvlData) {
                hitbox.y = 150f;
                setInAir(false);
                setTileY((int)(150f / Game.TILES_SIZE));
            }
        };

        testEnemy.setInAir(true);
        testEnemy.setFallSpeed(2.0f);

        testEnemy.testUpdateInAir(mockLvlData);

        assertFalse(testEnemy.isInAir());
        assertEquals(150f, testEnemy.hitbox.y);
        assertEquals((int)(150f / Game.TILES_SIZE), testEnemy.getTileY());
    }

    @Test
    void testMove_AllBranchesWithoutStaticMocking() {
        class MoveExecutionTracker {
            boolean movedSuccessfully = false;
            boolean changedDirection = false;
        }

        final MoveExecutionTracker tracker1 = new MoveExecutionTracker();
        TestEnemy testEnemy1 = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testMove(int[][] lvlData) {
                float xSpeed = 0;

                if (walkDir == LEFT)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;

                hitbox.x += xSpeed;
                tracker1.movedSuccessfully = true;
            }
        };

        float initialX1 = testEnemy1.hitbox.x;
        testEnemy1.setWalkDir(LEFT);
        testEnemy1.testMove(mockLvlData);

        assertEquals(initialX1 - testEnemy1.walkSpeed, testEnemy1.hitbox.x);
        assertTrue(tracker1.movedSuccessfully);
        assertFalse(tracker1.changedDirection);

        final MoveExecutionTracker tracker2 = new MoveExecutionTracker();
        TestEnemy testEnemy2 = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testMove(int[][] lvlData) {
                float xSpeed = 0;

                if (walkDir == LEFT)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;


                hitbox.x += xSpeed;
                tracker2.movedSuccessfully = true;
            }
        };

        float initialX2 = testEnemy2.hitbox.x;
        testEnemy2.setWalkDir(RIGHT);
        testEnemy2.testMove(mockLvlData);

        assertEquals(initialX2 + testEnemy2.walkSpeed, testEnemy2.hitbox.x);
        assertTrue(tracker2.movedSuccessfully);
        assertFalse(tracker2.changedDirection);

        final MoveExecutionTracker tracker3 = new MoveExecutionTracker();
        TestEnemy testEnemy3 = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testMove(int[][] lvlData) {
                float xSpeed = 0;

                if (walkDir == LEFT)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;

                changeWalkDir();
                tracker3.changedDirection = true;
            }
        };

        testEnemy3.setWalkDir(LEFT);
        int initialDir3 = testEnemy3.getWalkDir();
        testEnemy3.testMove(mockLvlData);

        assertNotEquals(initialDir3, testEnemy3.getWalkDir());
        assertEquals(RIGHT, testEnemy3.getWalkDir());
        assertTrue(tracker3.changedDirection);
        assertFalse(tracker3.movedSuccessfully);

        final MoveExecutionTracker tracker4 = new MoveExecutionTracker();
        TestEnemy testEnemy4 = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testMove(int[][] lvlData) {
                float xSpeed = 0;

                if (walkDir == LEFT)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;

                changeWalkDir();
                tracker4.changedDirection = true;
            }
        };

        testEnemy4.setWalkDir(RIGHT);
        int initialDir4 = testEnemy4.getWalkDir();
        testEnemy4.testMove(mockLvlData);

        assertNotEquals(initialDir4, testEnemy4.getWalkDir());
        assertEquals(LEFT, testEnemy4.getWalkDir());
        assertTrue(tracker4.changedDirection);
        assertFalse(tracker4.movedSuccessfully);
    }

    @Test
    void testTurnTowardsPlayer() {
        mockPlayer.hitbox.x = enemy.hitbox.x + 100;
        enemy.testTurnTowardsPlayer(mockPlayer);
        assertEquals(RIGHT, enemy.getWalkDir());

        mockPlayer.hitbox.x = enemy.hitbox.x - 100;
        enemy.testTurnTowardsPlayer(mockPlayer);
        assertEquals(LEFT, enemy.getWalkDir());
    }


    @Test
    void testCanSeePlayer_ShouldReturnFalse_WhenPlayerIsNotInRange() {
        Player mockPlayer = mock(Player.class);
        int[][] mockLvlData = new int[10][10];

        Rectangle2D.Float playerHitbox = new Rectangle2D.Float(200, 200, 50, 50);

        when(mockPlayer.getHitbox()).thenReturn(playerHitbox);

        TestEnemy spyEnemy = spy(enemy);

        doReturn(false).when(spyEnemy).testIsPlayerInRange(mockPlayer);

        boolean result = spyEnemy.testCanSeePlayer(mockLvlData, mockPlayer);

        assertFalse(result);
    }

    @Test
    void testMove_AllCases() {
        TestEnemy testEnemy1 = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testMove(int[][] lvlData) {
                if (walkDir == LEFT) {
                    hitbox.x -= walkSpeed;
                } else {
                    hitbox.x += walkSpeed;
                }
            }
        };

        float initialX1 = testEnemy1.hitbox.x;
        testEnemy1.setWalkDir(LEFT);
        testEnemy1.testMove(mockLvlData);
        assertEquals(initialX1 - testEnemy1.walkSpeed, testEnemy1.hitbox.x);

        TestEnemy testEnemy2 = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testMove(int[][] lvlData) {
                if (walkDir == LEFT) {
                    hitbox.x -= walkSpeed;
                } else {
                    hitbox.x += walkSpeed;
                }
            }
        };

        float initialX2 = testEnemy2.hitbox.x;
        testEnemy2.setWalkDir(RIGHT);
        testEnemy2.testMove(mockLvlData);
        assertEquals(initialX2 + testEnemy2.walkSpeed, testEnemy2.hitbox.x);

        TestEnemy testEnemy3 = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testMove(int[][] lvlData) {
                changeWalkDir();
            }
        };

        testEnemy3.setWalkDir(LEFT);
        int initialDir3 = testEnemy3.getWalkDir();
        testEnemy3.testMove(mockLvlData);
        assertNotEquals(initialDir3, testEnemy3.getWalkDir());
        assertEquals(RIGHT, testEnemy3.getWalkDir());
    }



    @Test
    void testIsPlayerInRange() {
        mockPlayer.hitbox.x = enemy.hitbox.x + (int)(enemy.attackDistance * 3);
        assertTrue(enemy.testIsPlayerInRange(mockPlayer));

        mockPlayer.hitbox.x = enemy.hitbox.x + (int)(enemy.attackDistance * 6);
        assertFalse(enemy.testIsPlayerInRange(mockPlayer));
    }

    @Test
    void testIsPlayerCloseForAttack() {
        mockPlayer.hitbox.x = enemy.hitbox.x + (int)(enemy.attackDistance * 0.5);
        assertTrue(enemy.testIsPlayerCloseForAttack(mockPlayer));

        mockPlayer.hitbox.x = enemy.hitbox.x + (int)(enemy.attackDistance * 2);
        assertFalse(enemy.testIsPlayerCloseForAttack(mockPlayer));
    }

    @Test
    void testHurt_StillAlive() {
        int initialHealth = enemy.currentHealth;
        enemy.hurt(10);

        assertEquals(initialHealth - 10, enemy.currentHealth);
        assertEquals(4, enemy.getEnemyState());
    }

    @Test
    void testHurt_Dead() {
        enemy.hurt(enemy.currentHealth);
        assertEquals(0, enemy.currentHealth);
        assertEquals(DEAD, enemy.getEnemyState());
    }

    @Test
    void testCheckPlayerHit() {
        Rectangle2D.Float attackBox = new Rectangle2D.Float(200, 100, 60, 60);

        Player mockPlayer = mock(Player.class);
        mockPlayer.hitbox = new Rectangle2D.Float(200, 100, 50, 50);

        enemy.testCheckPlayerHit(attackBox, mockPlayer);

        verify(mockPlayer).changeHealth(anyInt());
        assertTrue(enemy.attackChecked);
    }

    @Test
    void testUpdateAnimationTick_Normal() {
        enemy.setAniTick(0);
        enemy.setAniIndex(0);
        enemy.testUpdateAnimationTick();

        assertEquals(1, enemy.aniTick);
    }

    @Test
    void testUpdateAnimationTick_ResetTick() {
        enemy.setAniTick(enemy.aniSpeed - 1);
        enemy.setAniIndex(0);

        enemy.testUpdateAnimationTick();

        assertEquals(0, enemy.aniTick);
        assertEquals(1, enemy.getAniIndex());
    }

    @Test
    void testUpdateAnimationTick_ResetAnimationIndex() {
        enemy.setAniTick(enemy.aniSpeed - 1);
        enemy.setAniIndex(3);
        enemy.setEnemyState(IDLE);

        enemy.testUpdateAnimationTick();

        assertEquals(0, enemy.getAniIndex());
        assertEquals(0, enemy.aniTick);
    }

    @Test
    void testUpdateAnimationTick_Attack() {
        TestEnemy testEnemy = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testUpdateAnimationTick() {
                if (enemyState == ATTACK && aniIndex >= 3) {
                    enemyState = IDLE;
                }
            }
        };

        testEnemy.setAniTick(testEnemy.aniSpeed - 1);
        testEnemy.setEnemyState(ATTACK);
        testEnemy.setAniIndex(3);

        testEnemy.testUpdateAnimationTick();

        assertEquals(IDLE, testEnemy.getEnemyState());
    }

    @Test
    void testUpdateAnimationTick_Hit() {
        TestEnemy testEnemy = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testUpdateAnimationTick() {
                if (enemyState == HIT && aniIndex >= 3) {
                    enemyState = IDLE;
                }
            }
        };

        testEnemy.setAniTick(testEnemy.aniSpeed - 1);
        testEnemy.setEnemyState(HIT);
        testEnemy.setAniIndex(3);

        testEnemy.testUpdateAnimationTick();

        assertEquals(IDLE, testEnemy.getEnemyState());
    }

    @Test
    void testUpdateAnimationTick_Dead() {
        TestEnemy testEnemy = new TestEnemy(100, 100, 50, 50, ORC) {
            @Override
            public void testUpdateAnimationTick() {
                if (enemyState == DEAD && aniIndex >= 3) {
                    active = false;
                }
            }
        };

        testEnemy.setAniTick(testEnemy.aniSpeed - 1);
        testEnemy.setEnemyState(DEAD);
        testEnemy.setAniIndex(3);

        testEnemy.testUpdateAnimationTick();

        assertFalse(testEnemy.isActive());
    }

    @Test
    void testChangeWalkDir() {
        enemy.setWalkDir(LEFT);
        enemy.testChangeWalkDir();
        assertEquals(RIGHT, enemy.getWalkDir());

        enemy.testChangeWalkDir();
        assertEquals(LEFT, enemy.getWalkDir());
    }
}