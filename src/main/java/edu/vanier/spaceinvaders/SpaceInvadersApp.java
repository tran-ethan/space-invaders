package edu.vanier.spaceinvaders;

import edu.vanier.spaceinvaders.controllers.FXMLMainAppController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The SpaceInvadersApp class is the main entry point for the Space Invaders application. It extends the JavaFX
 * Application class and sets up the graphical user interface (GUI) by loading the FXML layout file and associating
 * it with the FXMLMainAppController. The primary responsibilities include initializing the game components and starting
 * the main stage.
 *
 * @author Ethan Tran
 * @author Zachary Tremblay
 */
public class SpaceInvadersApp extends Application {
    
    /** The controller for the main application. */
    FXMLMainAppController controller;
    
    /**
     * The main entry point for the application. It loads the scene graph from the specified FXML file and associates
     * it with the FXMLMainAppController. The primary stage is then created and displayed, and the game components
     * are initialized.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load scene graph from the specified FXML file and associate it with controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainApp_layout.fxml"));
            controller = new FXMLMainAppController();
            loader.setController(controller);
            Pane root = loader.load();

            // Create and set the scene to the stage.
            Scene scene = new Scene(root);
            controller.setScene(scene);
            controller.initGameComponents();

            // Configure primary stage
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Space Invaders!");
            primaryStage.sizeToScene();
            primaryStage.setAlwaysOnTop(true);
            primaryStage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
     /**
     * This method is called when the application is stopped, providing an opportunity to clean up resources.
     * It stops the animation timer upon closing the main stage.
     *
     * @throws Exception If an exception occurs during the stopping process.
     */
    @Override
    public void stop() throws Exception {
        // Stop the animation timer upon closing the main stage
        controller.stopAnimation();
    }
    
    /**
     * The main method, launching the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
