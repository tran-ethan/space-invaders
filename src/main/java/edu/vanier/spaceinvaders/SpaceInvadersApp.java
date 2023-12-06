package edu.vanier.spaceinvaders;

import edu.vanier.spaceinvaders.controllers.FXMLMainAppController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a JavaFX project template to be used for creating GUI applications.
 * The JavaFX GUI framework (version: 20.0.2) is linked to this project in the
 * build.gradle file.
 *
 * @author Ethan Tran
 * @author Zachary Tremblay
 */
public class SpaceInvadersApp extends Application {

    FXMLMainAppController controller;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load scene graph from the specified FXML file and associate it with controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainApp_layout.fxml"));
            controller = new FXMLMainAppController();
            loader.setController(controller);
            Pane root = loader.load();

            // Create and set the scene to the stage.
            Scene scene = new Scene(root, 1000, 1000);
            controller.setScene(scene);
            controller.initGameComponents();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Space Invaders!");
            primaryStage.sizeToScene();
            primaryStage.setAlwaysOnTop(true);
            primaryStage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        // Stop the animation timer upon closing the main stage.
        controller.stopAnimation();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
