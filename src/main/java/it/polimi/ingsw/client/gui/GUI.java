package it.polimi.ingsw.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
            stage.setTitle("Master of Renaissance");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
