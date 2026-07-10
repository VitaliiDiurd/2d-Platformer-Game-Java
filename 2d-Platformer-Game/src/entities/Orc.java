package entities;

import components.*;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;

/**
 * Represents an Orc enemy in the game.
 */
public class Orc extends Enemy {

    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;



    public Orc(float x, float y) {
        super(x, y, ORC_WIDTH, ORC_HEIGHT, ORC);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
        initAttackBox();

    }

    /**
     * Initializes the attack box dimensions and offset.
     */
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (60 * Game.SCALE), (int) (19 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 20);
    }

    /**
     * Updates the orc's movement, animation, and attack box.
     * @param lvlData
     * @param player
     */
    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
        updateAttackBox();

    }

    /**
     * Updates the position of the attack box to match the hitbox.
     */
    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    /**
     * Updates the orc's movement and behavior based on its current state.
     * @param lvlData
     * @param player
     */
    protected void updateMove(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }

                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;

                    if (aniIndex == 1 && !attackChecked)
                        checkPlayerHit(attackBox, player);

                    break;
                case HIT:
                    break;
            }
        }

    }

    /**
     * Returns the horizontal flip offset for rendering the orc sprite.
     * @return
     */
    public int flipX() {
        if (walkDir == LEFT)
            return ORC_WIDTH;
        else
            return 0;
    }

    /**
     * Returns the horizontal flip direction for rendering the orc sprite.
     * @return
     */
    public int flipW() {
        if (walkDir == LEFT)
            return -1;
        else
            return 1;

    }

}