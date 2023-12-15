package edu.vanier.spaceinvaders.controllers;

import edu.vanier.spaceinvaders.models.Sprite;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.image.ImageView;

/**
 * Controller class of the MainApp's UI.
 *
 * @author frostybee
 */
public class FXMLMainAppController {

    @FXML
    private Pane animationPanel;
    @FXML
    private Button gameOverButton;
    @FXML
    private Text gameOverText;
    @FXML
    private Label livesLabel;
    @FXML
    private Text scoreLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private Text congratulationsText;

    private double elapsedTime = 0;
    private Sprite spaceShip;
    private Scene scene;
    AnimationTimer animation;

    private final double WIDTH = 800;
    private final double HEIGHT = 1000;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean isRocketsOn = false;

    private boolean movingRight = true;
    private boolean gameOver = false;
    private static int lives = 3;
    private static int level = 1;
    private static int score = 0;

    private MediaPlayer shootAudio;
    private MediaPlayer explosionAudio;

    /**
     * Cool-down between shots that user can fire
     */
    private final long COOL_DOWN = 400;

    /**
     * Timestamp of the previous spaceship shot
     */
    private long lastShotTime = 0;

    /**
     * Represents whether the spaceship is currently shooting or not
     */
    private boolean isShooting = false;

    @FXML
    public void initialize() {
        gameOverButton.setOnAction(e -> nextLevel());
    }

    public void initGameComponents() {
        createContent();
        // Define keybindings for spaceship movements
        this.scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W ->
                    upPressed = true;
                case A ->
                    leftPressed = true;
                case S ->
                    downPressed = true;
                case D ->
                    rightPressed = true;
                case R ->
                    isRocketsOn = !isRocketsOn;
                case SPACE -> {
                    long currentTime = System.currentTimeMillis();
                    // Only shoot if elapsed time since last shoot is greater or equal to cooldown
                    if (currentTime - lastShotTime >= COOL_DOWN) {
                        isShooting = true;
                        shoot(spaceShip);
                        lastShotTime = currentTime;
                    }
                }
            }
        });

        // Release movement for spaceship
        this.scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W ->
                    upPressed = false;
                case A ->
                    leftPressed = false;
                case S ->
                    downPressed = false;
                case D ->
                    rightPressed = false;
                case SPACE ->
                    isShooting = false;
            }
        });
    }

    private void createContent() {

        // Create the game loop
        animation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        nextLevel();
    }

    private void nextLevel() {
        // Reset counters
        gameOver = false;

        // Reset labels
        levelLabel.setText(Integer.toString(level));
        scoreLabel.setText(Integer.toString(score));
        livesLabel.setText(Integer.toString(lives));

        // Remove overlay text and button
        gameOverText.setVisible(false);
        gameOverButton.setVisible(false);
        congratulationsText.setVisible(false);

        animationPanel.getChildren().removeIf(n -> n instanceof Sprite);

        ImagePattern image = new ImagePattern(new Image(String.format("/images/ship%d.png", level)));

        spaceShip = new Sprite(WIDTH / 2, 750, 40, 40, "player", image, 5);
        animationPanel.getChildren().add(spaceShip);

        // Spawn enemies according to level
        for (int j = 0; j < level + 2; j++) {
            for (int i = 0; i < 5; i++) {
                ImagePattern enemy = new ImagePattern(new Image(String.format("/images/intruder%d.png", 1 + (int) (Math.random() * 5))));

                Sprite invader = new Sprite(90 + i * 100, 150 + j * 50, 30, 30, "enemy", enemy, level);
                animationPanel.getChildren().add(invader);
            }
        }

        // Prevent continual shooting after level reset
        isShooting = false;

        animation.start();

        // Set media for explosion sounds
        Media explosionSound = new Media(getClass().getResource("/sounds/explosion" + level + ".wav").toExternalForm());
        explosionAudio = new MediaPlayer(explosionSound);
        explosionAudio.setVolume(0.1);

        // Set text to overlay sprites
        gameOverText.toFront();
        gameOverButton.toFront();
    }

    private List<Sprite> sprites() {
        return animationPanel.getChildren().stream().filter(n -> n instanceof Sprite).map(n -> (Sprite) n).collect(Collectors.toList());
    }

    private void update() {
        elapsedTime += 0.016;

        // Handle spaceship movement and shooting every frame
        updateSpaceShip();

        // Determine movement direction of all enemies
        boolean movingDown = false;
        for (Sprite sprite : sprites().stream().filter(sprite -> sprite.getType().equals("enemy")).toList()) {
            if (sprite.getTranslateX() > WIDTH - 100) {
                movingRight = false;
                movingDown = true;
                break;
            }
            if (sprite.getTranslateX() < 70) {
                movingRight = true;
                movingDown = true;
                break;
            }
        }

        for (Sprite sprite : sprites()) {
            switch (sprite.getType()) {
                case "enemyBullet" -> {
                    sprite.moveDown();
                    if (sprite.getBoundsInParent().intersects(spaceShip.getBoundsInParent())) {
                        livesLabel.setText(Integer.toString(--lives));
                        if (lives == 0) {
                            spaceShip.setDead(true);
                            gameOver = true;
                        }
                        sprite.setDead(true);
                        explosionAudio.play();
                        explosionAudio.seek(explosionAudio.getStartTime());
                    }
                }
                case "playerBullet" -> {
                    sprite.moveUp();
                    sprites().stream().filter(e -> e.getType().equals("enemy")).forEach(enemy -> {
                        if (sprite.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            // Remove bullet and enemy
                            enemy.setDead(true);
                            sprite.setDead(true);

                            // Increment score
                            score += 10;
                            scoreLabel.setText(Integer.toString(score));

                            // Play audio
                            explosionAudio.play();
                            explosionAudio.seek(explosionAudio.getStartTime());

                            Image explosion = new Image("/images/explosion.png");
                            ImageView image = new ImageView(explosion);
                            image.setLayoutX(sprite.getLayoutX());
                            image.setLayoutY(sprite.getLayoutY());
                            animationPanel.getChildren().add(image);
                        }
                    });
                }
                case "enemy" -> {
                    if (elapsedTime > 2) {
                        // Random probability of shooting and only shoot if entity is alive
                        if (Math.random() < 0.3 && !sprite.isDead()) {
                            shoot(sprite);
                        }
                    }

                    if (sprite.getBoundsInParent().intersects(spaceShip.getBoundsInParent())) {
                        lives = 0;
                        livesLabel.setText(Integer.toString(lives));
                        explosionAudio.play();
                        explosionAudio.seek(explosionAudio.getStartTime());
                        gameOver = true;
                    }

                    // Move all enemies right or left depending on direction
                    if (movingRight) {
                        sprite.moveRight();
                    } else {
                        sprite.moveLeft();
                    }

                    // Move down if there is change in direction (wall is hit)
                    if (movingDown) {
                        for (int i = 0; i < 20; i++) {
                            sprite.moveDown();
                        }
                    }

                    // Game is over if enemies reach bottom of the screen
                    if (sprite.getTranslateY() + 30 > HEIGHT) {
                        gameOver = true;
                    }
                }
            }
        }

        // Remove bullets if they are off-screen
        sprites().forEach(sprite -> {
            double y = sprite.getTranslateY();
            if (y < 0 || y > HEIGHT) {
                sprite.setDead(true);
            }
        });

        // Remove dead entities
        animationPanel.getChildren().removeIf(n -> {
            try {
                Sprite sprite = (Sprite) n;
                return sprite.isDead();
            } catch (Exception e) {
                // Do not remove non-sprite entity
                return false;
            }
        });

        long invaderCount = sprites().stream().filter(s -> s.getType().equals("enemy")).count();

        // Set game over if all enemies are gone
        if (invaderCount == 0) {
            gameOver = true;
            if (level < 3) {
                level++;
                levelLabel.setText(Integer.toString(level));
                nextLevel();
            }
        }

        // Check if game is over
        if (gameOver) {
            // Display congratulations if no invaders level 3
            if (level == 3 && invaderCount == 0) {
                congratulationsText.setVisible(true);
            } else {
                gameOverText.setVisible(true);
            }

            // Display game over text
            gameOverButton.setVisible(true);
            stopAnimation();
            // Reset counters
            level = 1;
            score = 0;
            lives = 3;
        }

        // Reset timer for enemies shooting
        if (elapsedTime > 2) {
            elapsedTime = 0;
        }
    }

    private void shoot(Sprite who) {
        if (who == spaceShip) {
            ImagePattern image;
            if (isRocketsOn) {
                image = new ImagePattern(new Image(String.format("/images/rocket%d.png", level)));
            } else {
                image = new ImagePattern(new Image(String.format("/images/laser%d.png", level)));
            }
            double spacing = 15; // Spacing between bullets
            double width = (level - 1) * spacing; // Distance between furthest left and right bullet
            double x = 18 + who.getTranslateX() - width / 2; // x position of left bullet
            for (int i = 0; i < level; i++) {
                Sprite s = new Sprite(x + i * spacing, who.getTranslateY(), 5, 20, who.getType() + "Bullet", image, 5);
                animationPanel.getChildren().add(s);
            }
            // Set media for shooting sounds
            Media shootSound;
            if (isRocketsOn) {
                shootSound = new Media(getClass().getResource("/sounds/laser" + level + ".wav").toExternalForm());
            } else {
                shootSound = new Media(getClass().getResource("/sounds/laser" + level + ".wav").toExternalForm());
            }
            shootAudio = new MediaPlayer(shootSound);
            shootAudio.setVolume(0.2);
            shootAudio.play();
            shootAudio.seek(shootAudio.getStartTime());
        } else {
            Sprite s = new Sprite(who.getTranslateX() + 20, who.getTranslateY(), 5, 20, who.getType() + "Bullet", Color.RED, 5);
            animationPanel.getChildren().add(s);
        }
    }

    private void updateSpaceShip() {
        // Moves spaceship depending on which keys are pressed
        if (leftPressed && spaceShip.getTranslateX() > 0) {
            spaceShip.moveLeft();
        }
        if (rightPressed && spaceShip.getTranslateX() <= WIDTH - 40) {
            spaceShip.moveRight();
        }
        if (upPressed && spaceShip.getTranslateY() > 96) {
            spaceShip.moveUp();
        }
        if (downPressed && spaceShip.getTranslateY() < HEIGHT - 40) {
            spaceShip.moveDown();
        }

        // Shoot if SPACE held down and enough time passed since last shot
        if (isShooting && System.currentTimeMillis() - lastShotTime >= COOL_DOWN) {
            shoot(spaceShip);
            lastShotTime = System.currentTimeMillis();
        }
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
