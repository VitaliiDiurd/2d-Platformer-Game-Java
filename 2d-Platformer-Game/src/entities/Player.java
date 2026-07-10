package entities;

import components.*;
import gamestates.Playing;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Class representing the player entity in the game.
 */
public class Player extends Entity {
    private Rectangle2D.Float attackBox;
    private boolean attackChecked = false;
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private boolean moving = false;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        initHitbox(x, y, 20, 27);
        initAttackBox();
    }

    /**
     * Initializes the player's attack box size and position.
     */
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
    }

    /**
     * Loads level data for physics and movement components.
     * @param lvlData
     */
    public void loadLvlData(int[][] lvlData) {
        getComponent(PhysicsComponent.class).loadLvlData(lvlData);
        getComponent(MovementComponent.class).loadLvlData(lvlData);
    }

    /**
     * Updates the player's state, including health checks and attack box updates.
     */
    @Override
    public void update() {
        super.update();
        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        checkPotionTouched();
        checkTrapTouched();
        checkAttack();
    }

    /**
     * Checks if the player has touched a trap.
     */
    private void checkTrapTouched() {
        playing.checkTrapTouched(this);
    }

    /**
     * Killing player.
     */
    public void kill() {
        currentHealth = 0;
    }

    /**
     * Checks if the player has touched a potion.
     */
    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    /**
     * Updates the player's attack box position based on movement direction.
     */
    private void updateAttackBox() {
        float attackBoxWidth = attackBox.width;
        float xOffset = (int) (Game.SCALE * 10);
        MovementComponent move = getComponent(MovementComponent.class);
        if (move != null && move.getFlipW() == 1) {
            attackBox.x = hitbox.x + hitbox.width + xOffset;
        } else {
            attackBox.x = hitbox.x - attackBoxWidth - xOffset;
        }
        attackBox.y = hitbox.y;
    }

    /**
     * Checks if the player is attacking and performs the attack check.
     */
    private void checkAttack() {
        AnimationComponent anim = getComponent(AnimationComponent.class);
        if (anim != null && anim.isAttacking() && anim.getAniIndex() == 1 && !attackChecked) {
            attackChecked = true;
            playing.checkEnemyHit(attackBox);
            playing.checkObhectHit(attackBox);
        }

        if (anim != null && !anim.isAttacking()) {
            attackChecked = false;
        }
    }

    /**
     * Renders the player on the screen.
     * @param g
     * @param lvlOffset
     */
    public void render(Graphics g, int lvlOffset) {
        getComponent(RenderComponent.class).render(g, lvlOffset);
        drawHitbox(g, lvlOffset);
        drawAttackBox(g, lvlOffset);
    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
//        g.setColor(Color.BLUE);
//        g.drawRect((int) attackBox.x - lvlOffsetX, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    /**
     * Changes the player's health by a specified amount.
     * @param value
     */
    public void changeHealth(int value) {
        currentHealth += value;
        AnimationComponent animComp = getComponent(AnimationComponent.class);
        if (animComp != null) {
            animComp.changeHealth(value);
        }
    }

    /**
     * Resets the player's directional boolean values.
     */
    public void resetDirBooleans() {
        getComponent(MovementComponent.class).resetDirBooleans();
    }

    public void setAttacking(boolean attacking) {
        getComponent(AnimationComponent.class).setAttacking(attacking);
    }

    public boolean isAttacking() {
        return getComponent(AnimationComponent.class).isAttacking();
    }

    public void setLeft(boolean left) {
        getComponent(MovementComponent.class).setLeft(left);
    }

    public void setRight(boolean right) {
        getComponent(MovementComponent.class).setRight(right);
    }

    public Rectangle2D.Float getAttackBox() {
        return attackBox;
    }

    /**
     * Resets the player's state to its initial values (health, position, etc.).
     */
    @Override
    public void resetAll() {
        currentHealth = maxHealth;

        AnimationComponent anim = getComponent(AnimationComponent.class);
        if (anim != null) {
            anim.resetHealth();
        }

        MovementComponent movement = getComponent(MovementComponent.class);
        if (movement != null) {
            movement.resetDirBooleans();
        }

        PhysicsComponent physics = getComponent(PhysicsComponent.class);
        if (physics != null) {
            physics.setJump(false);
        }

        hitbox.x = x;
        hitbox.y = y;

        attackChecked = false;
        updateAttackBox();
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setJump(boolean jump) {
        getComponent(PhysicsComponent.class).setJump(jump);
    }

    public int getPlayerAction() {
        return getComponent(AnimationComponent.class).getCurrentAction();
    }


}
