package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllers.GenericController;
import it.polimi.ingsw.client.gui.controllers.SetupController;
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

public class GUIApplication extends Application {

    private GUI gui;

    private List<SceneInformation> scenesInformation;

    private Stage stage;
    private Alert alert;

    public static void main(String[] args) {
        launch(args);
    }


    private void setup() {
        this.gui = new GUI(this);
        gui.setup();
        new Thread(() -> gui.run()).start();
    }

    @Override
    public void start(Stage stage) {
        setupStage(stage);
        setup();
        setActiveScene(SceneNames.CONNECTION_MENU);
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
                root = loader.load();
                GenericController controller = loader.getController();
                controller.setGUIApplication(this);
                scenesInformation.add(new SceneInformation(new Scene(root), SceneNames.values()[i], controller));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setActiveScene(SceneNames sceneName) {
        stage.setScene(scenesInformation.get(getSceneIndex(sceneName)).getScene());
        if(sceneName.equals(SceneNames.PLAYER_BOARD) || sceneName.equals(SceneNames.MARKET_BOARD) ||
                sceneName.equals(SceneNames.DECKS_BOARD))
            stage.centerOnScreen();
        stage.show();
    }

    public Alert getAlert() {
        return alert;
    }

    private int getSceneIndex(SceneNames sceneName) {
        for(int i = 0; i < scenesInformation.size(); i++){
            if(scenesInformation.get(i).getFileName().equals(sceneName))
                return i;
        }
        return -1; //TODO: potrebbe dare eccezione?
    }

    public GenericController getController(SceneNames sceneName) {
        return scenesInformation.get(getSceneIndex(sceneName)).getController();
    }

    public GUI getGUI() {
        return gui;
    }

    public void changeConnectionMenuStatus(){
        SetupController controller = (SetupController) getController(SceneNames.CONNECTION_MENU);
        boolean newDisableValue = !controller.getIpAddress_field().isDisable();
        controller.getIpAddress_field().setDisable(newDisableValue);
        controller.getPortNumber_field().setDisable(newDisableValue);
        controller.getConnect_button().setDisable(newDisableValue);
        controller.getQuit_button().setDisable(newDisableValue);
        controller.getConnecting_progressIndicator().setVisible(newDisableValue);
    }

    public void changeNicknameMenuStatus(){
        SetupController controller = (SetupController) getController(SceneNames.NICKNAME_MENU);
        boolean newDisableValue = !controller.getNickname_field().isDisable();
        controller.getNickname_label().setDisable(newDisableValue);
        controller.getNickname_field().setDisable(newDisableValue);
        controller.getConfirm_button().setDisable(newDisableValue);
        controller.getWaitingNickname_progressIndicator().setVisible(newDisableValue);
    }
}
