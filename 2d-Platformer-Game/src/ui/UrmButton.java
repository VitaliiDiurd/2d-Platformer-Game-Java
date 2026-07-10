package ui;

import exeptions.ResourceLoadException;
import exeptions.ResourceNotFoundException;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.*;

/**
 * The UrmButton class represents a button with different states (normal, hovered, pressed)
 */
public class UrmButton extends PauseButton {

    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    /**
     * Loads the images for the button (normal, hovered, and pressed states).
     */
    private void loadImgs() {
        try {
            BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
            imgs = new BufferedImage[3];
            for(int i = 0; i < imgs.length; i++) {
                imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
            }
        } catch (ResourceNotFoundException e) {
            System.err.println("Failed to load URM button images: " + e.getMessage());
        } catch (ResourceLoadException e) {
            System.err.println("Error loading URM button images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the button's state based on mouse interaction (hovered or pressed).
     */
    public void update(){
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }

    /**
     * Draws the button with the appropriate image for its current state.
     * @param g
     */
    public void draw(Graphics g){
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE,null);
    }

    /**
     * Resets the mouse interaction states (hovered and pressed).
     */

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}