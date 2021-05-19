package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllers.ControllerInterface;
import it.polimi.ingsw.server.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUI extends Application {

    private List<SceneInformation> scenesInformation;

    private Scene activeScene;
    private Stage stage;
    private Alert alert;

    public GUI() {
        super();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        setup(stage);
        setActiveScene(SceneNames.CONNECTION_MENU);
    }

    //TODO: mettere suppress
    private void setup(Stage stage){
        Image image = new Image(getClass().getResourceAsStream("/imgs/logo.png"));
        alert = new Alert(Alert.AlertType.NONE);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(image);

        stage.setTitle("Master of Renaissance");
        stage.setResizable(false);
        stage.getIcons().add(image);
        this.stage = stage;

        try{
            FXMLLoader loader;
            Parent root;
            scenesInformation = new ArrayList<>();
            for(int i=0; i<SceneNames.values().length; i++) {
                loader = new FXMLLoader(getClass().getResource(SceneNames.values()[i].value()));
                root = loader.load(); //TODO: farci qualcosa
                ControllerInterface controller = loader.getController();
                controller.setGui(this);
                scenesInformation.add(new SceneInformation(new Scene(root),
                        SceneNames.values()[i], controller));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setActiveScene(SceneNames sceneName){
        activeScene = scenesInformation.get(getSceneIndex(sceneName)).getScene();
        stage.setScene(activeScene);
        stage.show();
    }

    public Alert getAlert(){
        return alert;
    }

    private int getSceneIndex(SceneNames sceneName){ //TODO: brutto? fare classe List a parte e mettere lì il metodo?
        for(int i=0; i<scenesInformation.size(); i++){
            if(scenesInformation.get(i).getFileName().equals(sceneName))
                return i;
        }
        return -1; //TODO: potrebbe dare eccezione?
    }
}

