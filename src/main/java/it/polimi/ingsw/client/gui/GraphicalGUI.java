package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllers.ControllerInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphicalGUI extends Application {

    private static GUI gui;

    private List<SceneInformation> scenesInformation;

    private Scene activeScene;
    private Stage stage;
    private Alert alert;


    public static void main(String[] args) {
        launch(args);
    }


    public static void setup(GUI _gui) {
        gui = _gui;
    }

    @Override
    public void start(Stage stage) {
        setupStage(stage);
        setActiveScene(SceneNames.CONNECTION_MENU);
        /*SetupController setupController = (SetupController) getController(SceneNames.MULTI_PLAYER_WAITING);
        setupController.notifyNewPlayer("lara");*/
    }

    //TODO: mettere suppress
    private void setupStage(Stage stage) {
        Image image = new Image(getClass().getResourceAsStream("/imgs/inkwell.png"));
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
            for(int i = 0; i < SceneNames.values().length; i++) {
                loader = new FXMLLoader(getClass().getResource(SceneNames.values()[i].value()));
                root = loader.load(); //TODO: farci qualcosa
                ControllerInterface controller = loader.getController();
                controller.setGui(this);
                scenesInformation.add(new SceneInformation(new Scene(root), SceneNames.values()[i], controller));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setActiveScene(SceneNames sceneName) {
        activeScene = scenesInformation.get(getSceneIndex(sceneName)).getScene();
        stage.setScene(activeScene);
        stage.show();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - activeScene.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - activeScene.getHeight()) / 2);
    }

    public Alert getAlert() {
        return alert;
    }

    private int getSceneIndex(SceneNames sceneName) { //TODO: brutto? fare classe List a parte e mettere lì il metodo?
        for(int i = 0; i < scenesInformation.size(); i++){
            if(scenesInformation.get(i).getFileName().equals(sceneName))
                return i;
        }
        return -1; //TODO: potrebbe dare eccezione?
    }

    public ControllerInterface getController(SceneNames sceneName) {
        return scenesInformation.get(getSceneIndex(sceneName)).getController();
    }

    public static GUI getGUI() {
        return gui;
    }
}
