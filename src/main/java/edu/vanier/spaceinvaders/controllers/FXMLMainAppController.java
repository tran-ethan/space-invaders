package edu.vanier.spaceinvaders.controllers;

import edu.vanier.spaceinvaders.models.Sprite;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Controller class of the MainApp's UI.
 *
 * @author frostybee
 */
public class FXMLMainAppController {

    @FXML
    private Pane animationPanel;
    private double elapsedTime = 0;
    private Sprite spaceShip;
    private Scene scene;
    AnimationTimer animation;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    @FXML
    public void initialize() {
        spaceShip = new Sprite(300, 750, 40, 40, "player", Color.BLUE);
    }

    public void initGameComponents() {
        createContent();
        this.scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W -> upPressed = true;
                case A -> leftPressed = true;
                case S -> downPressed = true;
                case D -> rightPressed = true;
                case SPACE -> shoot(spaceShip);
            }
        });

        this.scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W -> upPressed = false;
                case A -> leftPressed = false;
                case S -> downPressed = false;
                case D -> rightPressed = false;
            }
        });
    }

    private void createContent() {
        animationPanel.setPrefSize(600, 800);
        animationPanel.getChildren().add(spaceShip);
        // Create the game loop.
        animation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        animation.start();
        nextLevel();
    }

    private void nextLevel() {
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 5; i++) {
                Sprite invader = new Sprite(90 + i * 100, 150+j*50, 30, 30, "enemy", Color.RED);

                animationPanel.getChildren().add(invader);
            }
        }
    }

    private List<Sprite> sprites() {
        return animationPanel.getChildren().stream().map(n -> (Sprite) n).collect(Collectors.toList());
    }

    private void update() {
        elapsedTime += 0.016;

        // Move player every time timer is updated
        if (leftPressed) {
            spaceShip.moveLeft();
        }
        if (rightPressed) {
            spaceShip.moveRight();
        }
        if (upPressed) {
            spaceShip.moveUp();
        }
        if (downPressed) {
            spaceShip.moveDown();
        }

        sprites().forEach(sprite -> {
            switch (sprite.getType()) {

                case "enemybullet":
                    sprite.moveDown();

                    if (sprite.getBoundsInParent().intersects(spaceShip.getBoundsInParent())) {
                        spaceShip.setDead(true);
                        sprite.setDead(true);
                    }
                    break;

                case "playerbullet":
                    sprite.moveUp();

                    sprites().stream().filter(e -> e.getType().equals("enemy")).forEach(enemy -> {
                        if (sprite.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.setDead(true);
                            sprite.setDead(true);
                        }
                    });

                    break;

                case "enemy":

                    if (elapsedTime > 2) {
                        // Random probability of shooting and only shoot if entity is alive
                        if (!sprite.isDead()) {
                            if (Math.random() < 0.3) {
                                shoot(sprite);
                            }

                        }
                    }

                    break;
            }
        });

        // Count enemies alive
        int enemiesAlive = 0;
        for (Sprite sprite: sprites()) {
            if (!sprite.isDead() && sprite.getType().equals("enemy")) {
                enemiesAlive++;
            }
        }

        if (enemiesAlive == 0) {
            System.out.println("GAME OVER");
            animation.stop();
        }

        animationPanel.getChildren().removeIf(n -> {
            Sprite sprite = (Sprite) n;
            return sprite.isDead();
        });

        if (elapsedTime > 2) {
            elapsedTime = 0;
        }
    }

    private void shoot(Sprite who) {
        Sprite s = new Sprite((int) who.getTranslateX() + 20, (int) who.getTranslateY(), 5, 20, who.getType() + "bullet", Color.BLACK);
        animationPanel.getChildren().add(s);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void stopAnimation() {
        if (animation != null) {
            animation.stop();
        }
    }
}
