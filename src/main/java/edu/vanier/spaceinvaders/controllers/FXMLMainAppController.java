package edu.vanier.spaceinvaders.controllers;

import edu.vanier.spaceinvaders.models.Sprite;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
    private Label scoreLabel;
    @FXML
    private Label levelLabel;

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

    private int invaderCount;
    private boolean gameOver = false;
    private static int lives = 3;
    private static int level = 1;
    private static int score = 0;
    

    @FXML
    public void initialize() {
        gameOverButton.setOnAction(e -> nextLevel());

        spaceShip = new Sprite(500, 750, 40, 40, "player", Color.BLUE);

    }

    public void initGameComponents() {
        createContent();
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
                case SPACE -> {
                    shoot(spaceShip);
                    Media sound = new Media(getClass().getResource("/sounds/laser" + Integer.toString(level) + ".wav").toExternalForm());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();

                }

            }
        });

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
        animation.start();
        nextLevel();
    }

    private void nextLevel() {
        // Remove overlay text and button
        gameOverText.setVisible(false);
        gameOverButton.setVisible(false);

        animationPanel.getChildren().removeIf(n -> n instanceof Sprite);

        animationPanel.getChildren().add(spaceShip);

        for (int j = 0; j < level + 2; j++) {
            for (int i = 0; i < 5; i++) {
                Sprite invader = new Sprite(90 + i * 100, 150 + j * 50, 30, 30, "enemy", Color.RED);
                animationPanel.getChildren().add(invader);

                invaderCount++;
            }
        }

        // Set text to overlay sprites
        gameOverText.toFront();
        gameOverButton.toFront();
    }

    private List<Sprite> sprites() {
        return animationPanel.getChildren().stream().filter(n -> n instanceof Sprite).map(n -> (Sprite) n).collect(Collectors.toList());
    }

    private void update() {
        elapsedTime += 0.016;

        // Move player every time timer is updated
        moveSpaceship();

        sprites().forEach(sprite -> {
            switch (sprite.getType()) {
                case "enemyBullet" -> {
                    sprite.moveDown();
                    if (sprite.getBoundsInParent().intersects(spaceShip.getBoundsInParent())) {
                        lives--;
                        livesLabel.setText(Integer.toString(lives));
                        if (lives == 0) {
                            spaceShip.setDead(true);
                            gameOver = true;
                        }
                        sprite.setDead(true);
                        Media sound = new Media(getClass().getResource("/sounds/explosion.wav").toExternalForm());
                        MediaPlayer mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();

                    }
                }
                case "playerBullet" -> {
                    sprite.moveUp();
                    sprites().stream().filter(e -> e.getType().equals("enemy")).forEach(enemy -> {
                        if (sprite.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.setDead(true);
                            sprite.setDead(true);
                            score += 10;
                            scoreLabel.setText(Integer.toString(score));
                            if (--invaderCount <= 0 && level == 3) {
                                gameOver = true;
                            } else if (invaderCount <= 0) {
                                level++;
                                levelLabel.setText(Integer.toString(level));
                                nextLevel();

                            }
                            Media sound = new Media(getClass().getResource("/sounds/explosion.wav").toExternalForm());
                            MediaPlayer mediaPlayer = new MediaPlayer(sound);
                            mediaPlayer.play();
                        }
                    });
                }
                case "enemy" -> {
                    if (elapsedTime > 2) {
                        // Random probability of shooting and only shoot if entity is alive
                        if (!sprite.isDead()) {
                            if (Math.random() < 0.3) {
                                shoot(sprite);
                            }
                        }
                    }
                }
            }
        });

        // Remove entities if they are off-screen
        sprites().forEach(sprite -> {
            double x = sprite.getTranslateX();
            double y = sprite.getTranslateY();
            if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
                sprite.setDead(true);
            }
        });

        // Remove dead entities
        animationPanel.getChildren().removeIf(n -> {
            try {
                Sprite sprite = (Sprite) n;
                return sprite.isDead();
            } catch (Exception e) {
                return false;
            }
        });

        // Check if game is over
        if (gameOver) {
            gameOverText.setVisible(true);
            gameOverButton.setVisible(true);
            stopAnimation();
        }

        if (elapsedTime > 2) {
            elapsedTime = 0;
        }
    }

    private void shoot(Sprite who) {
        Sprite s = new Sprite((int) who.getTranslateX() + 20, (int) who.getTranslateY(), 5, 20, who.getType() + "Bullet", Color.RED);
        animationPanel.getChildren().add(s);

    }

    private void moveSpaceship() {
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
