package edu.vanier.spaceinvaders.models;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Represents a sprite in the Space Invaders game. Extends JavaFX Rectangle and includes additional functionality
 * for movement and status.
 *
 * @author Ethan Tran
 * @author Zachary Tremblay
 */
public class Sprite extends Rectangle {
    
    /**
     * Flag indicating whether the sprite is dead or alive
     */
    private boolean dead = false;
    
    /**
     * The type of the sprite
     */
    private final String type;
    
    /**
     * The speed of the sprite
     */
    private final double speed;
    
    /**
     * Constructs a sprite with the specified position, dimensions, type, image, and speed.
     *
     * @param x      The initial x-coordinate of the sprite.
     * @param y      The initial y-coordinate of the sprite.
     * @param w      The width of the sprite.
     * @param h      The height of the sprite.
     * @param type   The type of the sprite, used for identification.
     * @param image  The paint/image to be applied to the sprite.
     * @param speed  The speed at which the sprite moves.
     */
    public Sprite(double x, double y, int w, int h, String type, Paint image, double speed) {
        super(w, h, image);

        this.type = type;
        this.speed = speed;
        setTranslateX(x);
        setTranslateY(y);
    }
    
    /**
     * Moves the sprite to the left based on its speed.
     */
    public void moveLeft() {
        setTranslateX(getTranslateX() - speed);
    }
    
    /**
     * Moves the sprite to the right based on its speed.
     */
    public void moveRight() {
        setTranslateX(getTranslateX() + speed);
    }
    
     /**
     * Moves the sprite upwards based on its speed.
     */
    public void moveUp() {
        setTranslateY(getTranslateY() - speed);
    }
    
     /**
     * Moves the sprite downwards based on its speed.
     */
    public void moveDown() {
        setTranslateY(getTranslateY() + speed);
    }
    
    /**
     * Checks if the sprite is marked as dead.
     *
     * @return True if the sprite is dead, false otherwise.
     */
    public boolean isDead() {
        return dead;
    }
    
    /**
     * Gets the type of the sprite.
     *
     * @return The type of the sprite.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Sets the dead status of the sprite.
     *
     * @param dead True to mark the sprite as dead, false otherwise.
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
