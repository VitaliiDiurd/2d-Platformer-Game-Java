package components;

import exeptions.ResourceLoadException;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import utilz.SpriteAtlas;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static java.awt.Color.RED;
import static utilz.Constants.PlayerConstants.*;

/**
 * Manages the animations, health bar, and rendering for a game entity.
 */
public class AnimationComponent extends Component {
    private BufferedImage[][] animations;
    protected int aniTick = 0;
    protected int aniIndex = 0;
    private int aniSpeed = 25;
    private int currentAction = IDLE;
    private boolean attacking = false;

    private float xDrawOffset = 24 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    private int width = (int)(140 * Game.SCALE) / 2;
    private int height = (int)(80 * Game.SCALE) / 2;

    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    private boolean attackChecked;

    private Rectangle2D.Float attackBox;

    private SpriteAtlas atlas;

    public AnimationComponent(String atlasPath) throws ResourceLoadException {
        atlas = new SpriteAtlas(atlasPath);
        loadAnimations();
    }

    /**
     * Loads animation frames and the status bar image.
     * @throws ResourceLoadException
     */
    private void loadAnimations() throws ResourceLoadException {
        animations = new BufferedImage[7][6];

        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = atlas.getSprite(i * 64, j * 40, 64, 40);
                if (animations[j][i] == null) {
                    throw new ResourceLoadException("Failed to load animation frame at (" + i + ", " + j + ")");
                }
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
        if (statusBarImg == null) {
            throw new ResourceLoadException("Failed to load status bar image!");
        }
    }

    /**
     * Updates the health bar, animation state, and attack check.
     */
    @Override
    public void update() {
        updateHealthBar();
        if(attacking) {
            checkAttack();
        }
        updateAnimationTick();
    }

    /**
     * Checks if the attack animation frame has occurred and marks the attack as checked.
     */
    private void checkAttack() {
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
//        playing.checkEnemyHit(attackBox);
    }

    /**
     * Updates the visual width of the health bar based on current health.
     */
    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
    }

    /**
     * Updates animation tick.
     */
    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(currentAction)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    /**
     * Resets the health to maximum.
     */
    public void resetHealth() {
        this.currentHealth = this.maxHealth;
        updateHealthBar();
    }

    /**
     * Renders the entity's animation and UI elements on the screen.
     * @param g
     * @param lvlOffset
     */
    public void render(Graphics g, int lvlOffset) {
        MovementComponent moveComp = owner.getComponent(MovementComponent.class);
        int flipX = moveComp.getFlipX();
        int flipW = moveComp.getFlipW();

        g.drawImage(animations[currentAction][aniIndex],
                (int) (owner.getHitbox().x - xDrawOffset - lvlOffset + flipX * width),
                (int) (owner.getHitbox().y - yDrawOffset),
                width * flipW, height,
                null);

        drawUI(g);
    }


    /**
     * Draws the status bar and health bar.
     * @param g
     */
    private void drawUI(Graphics g) {
            g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        g.setColor(Color.RED);
        g.fillRect(
                healthBarXStart + statusBarX,
                healthBarYStart + statusBarY,
                healthWidth,
                healthBarHeight
        );

    }

    /**
     * Changes the current health by the given value.
     * @param value
     */
    public void changeHealth(int value){
        currentHealth += value;
        if(currentHealth <= 0){
            currentHealth = 0;
            //gameOver();
        }else if(currentHealth >= maxHealth){
            currentHealth = maxHealth;
        }
    }

    /**
     * Sets the current animation and its speed.
     * @param action
     * @param speed
     */
    public void setAnimation(int action, int speed) {
        if (currentAction != action) {
            resetAniTick();
        }
        this.currentAction = action;
        this.aniSpeed = speed;
    }

    /**
     * Resets the animation tick and index to start the animation from the beginning.
     */
    protected void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }


    public int getCurrentAction() {
        return currentAction;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
}