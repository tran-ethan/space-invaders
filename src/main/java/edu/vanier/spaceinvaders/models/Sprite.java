package edu.vanier.spaceinvaders.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author frostybee
 */
public class Sprite extends Rectangle {

    private boolean dead = false;
    private final String type;
    private double speed;

    public Sprite(int x, int y, int w, int h, String type, Color color, double speed) {
        super(w, h, color);

        this.type = type;
        this.speed = speed;
        setTranslateX(x);
        setTranslateY(y);
    }

    public void moveLeft() {
        setTranslateX(getTranslateX() - speed);
    }

    public void moveRight() {
        setTranslateX(getTranslateX() + speed);
    }

    public void moveUp() {
        setTranslateY(getTranslateY() - speed);
    }

    public void moveDown() {
        setTranslateY(getTranslateY() + speed);
    }

    public boolean isDead() {
        return dead;
    }

    public String getType() {
        return type;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
