package it.polimi.ingsw.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {

    public GUI() {
        super();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try { //TODO: ancora da fare
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConnectionMenu.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/logo.png")));
            stage.setTitle("Master of Renaissance");
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
