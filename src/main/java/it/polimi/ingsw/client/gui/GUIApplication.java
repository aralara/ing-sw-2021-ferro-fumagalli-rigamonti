package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllers.GenericController;
import it.polimi.ingsw.client.gui.controllers.SetupController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIApplication extends Application {

    private GUI gui;

    private List<SceneInformation> scenesInformation;

    private Stage stage, secondStage, cardStage, popUpStage;
    private Alert alert;

    //TODO: deve avere javadoc?
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes attributes and starts a new GUI thread
     */
    private void setup() {
        this.gui = new GUI(this);
        gui.setup();
        new Thread(() -> gui.run()).start();
    }

    @Override
    public void start(Stage stage) {
        setupStage(stage);
        setup();
        setActiveScene(SceneNames.LOADING);
    }

    /**
     * Initializes the stage by setting parameters and loading fxml files
     * @param stage Stage to set up
     */
    private void setupStage(Stage stage) {
        Image image = new Image(getClass().getResourceAsStream("/imgs/icon_inkwell.png"));
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

    /**
     * Gets the fileName attribute of the active scene
     * @return Returns fileName
     */
    public SceneNames getActiveSceneName(){
        for (SceneInformation sceneInformation : scenesInformation) {
            if (sceneInformation.getScene().equals(stage.getScene()))
                return sceneInformation.getFileName();
        }
        return null;  //TODO: potrebbe dare eccezione?
    }

    /**
     * Sets the active scene to show
     * @param sceneName Scene's name to active
     */
    public void setActiveScene(SceneNames sceneName) {
        if(sceneName.equals(SceneNames.LEADER_CHOICE_MENU) || sceneName.equals(SceneNames.RESOURCE_CHOICE_MENU))
            openPopUpStage(scenesInformation.get(getSceneIndex(sceneName)).getScene());
        else if(sceneName.equals(SceneNames.MARKET_BOARD) || sceneName.equals(SceneNames.DECKS_BOARD) ||
                sceneName.equals(SceneNames.DEPOTS))
            openSecondStage(scenesInformation.get(getSceneIndex(sceneName)).getScene());
        else if(sceneName.equals(SceneNames.CARD))
            openCardStage(scenesInformation.get(getSceneIndex(sceneName)).getScene());
        else {
            stage.setScene(scenesInformation.get(getSceneIndex(sceneName)).getScene());
            if (sceneName.equals(SceneNames.PLAYER_BOARD) || sceneName.equals(SceneNames.LOADING) ||
                    sceneName.equals(SceneNames.CONNECTION_MENU) || sceneName.equals(SceneNames.RANKING))
                stage.centerOnScreen();
            stage.show();
        }
    }

    /**
     * Creates and shows a new popUp stage over the main one
     * @param scene Scene to load and show
     */
    private void openPopUpStage(Scene scene){
        double x;
        double y;
        if(popUpStage==null){
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.25;
            y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.25;
        }
        else{
            x = popUpStage.getX();
            y = popUpStage.getY();
        }
        popUpStage = new Stage();
        Image image = new Image(getClass().getResourceAsStream("/imgs/icon_inkwell.png"));
        popUpStage.setTitle("Master of Renaissance");
        popUpStage.setResizable(false);
        popUpStage.getIcons().add(image);
        popUpStage.setScene(scene);

        popUpStage.setX(x);
        popUpStage.setY(y);

        popUpStage.initStyle(StageStyle.UNDECORATED);
        popUpStage.show();
    }

    /**
     * Closes the popUp stage
     */
    public void closePopUpStage(){
        popUpStage.hide();
    }

    /**
     * Creates and shows a new second stage over the main one
     * @param scene Scene to load and show
     */
    private void openSecondStage(Scene scene){
        secondStage = new Stage();
        Image image = new Image(getClass().getResourceAsStream("/imgs/icon_inkwell.png"));
        secondStage.setTitle("Master of Renaissance");
        secondStage.setResizable(false);
        secondStage.getIcons().add(image);
        secondStage.setScene(scene);

        secondStage.initModality(Modality.WINDOW_MODAL);
        secondStage.initOwner(this.stage);
        secondStage.centerOnScreen();

        secondStage.initStyle(StageStyle.UNDECORATED);
        secondStage.show();
    }

    /**
     * Closes the second stage
     */
    public void closeSecondStage(){
        secondStage.hide();
    }

    /**
     * Creates and shows a new card stage over the main one
     * @param scene Scene to load and show
     */
    private void openCardStage(Scene scene){
        double x;
        double y;
        if(cardStage==null){
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.705;
            y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.25;
        }
        else{
            x = cardStage.getX();
            y = cardStage.getY();
        }
        cardStage = new Stage();
        Image image = new Image(getClass().getResourceAsStream("/imgs/icon_inkwell.png"));
        cardStage.setTitle("Master of Renaissance");
        cardStage.setResizable(false);
        cardStage.getIcons().add(image);
        cardStage.setScene(scene);

        cardStage.initModality(Modality.WINDOW_MODAL);
        cardStage.initOwner(this.stage);

        cardStage.setX(x);
        cardStage.setY(y);

        cardStage.initStyle(StageStyle.UNDECORATED);
        cardStage.show();
    }

    /**
     * Closes the card stage
     */
    public void closeCardStage(){
        cardStage.hide();
    }

    /**
     * Gets the alert attribute
     * @return Returns alert
     */
    public Alert getAlert() {
        return alert;
    }

    /**
     * Gets the index of the scene given by parameter
     * @param sceneName The scene's name
     * @return Returns the index of the scene
     */
    private int getSceneIndex(SceneNames sceneName) {
        for(int i = 0; i < scenesInformation.size(); i++){
            if(scenesInformation.get(i).getFileName().equals(sceneName))
                return i;
        }
        return -1;
    }

    /**
     * Gets the controller of the scene given by parameter
     * @param sceneName The scene's name
     * @return Returns the controller of the scene
     */
    public GenericController getController(SceneNames sceneName) {
        return scenesInformation.get(getSceneIndex(sceneName)).getController();
    }

    /**
     * Gets the gui attribute
     * @return Returns gui
     */
    public GUI getGUI() {
        return gui;
    }

    /**
     * Disable buttons after the first click until a message (ack/nack) is received
     */
    public void changeConnectionMenuStatus(){
        SetupController controller = (SetupController) getController(SceneNames.CONNECTION_MENU);
        boolean newDisableValue = !controller.getIpAddress_field().isDisable();
        controller.getIpAddress_field().setDisable(newDisableValue);
        controller.getPortNumber_field().setDisable(newDisableValue);
        controller.getConnect_button().setDisable(newDisableValue);
        controller.getConnecting_progressIndicator().setVisible(newDisableValue);
    }

    /**
     * Disable buttons after the first click until a message (ack/nack) is received
     */
    public void changeNicknameMenuStatus(){
        SetupController controller = (SetupController) getController(SceneNames.NICKNAME_MENU);
        boolean newDisableValue = !controller.getNickname_field().isDisable();
        controller.getNickname_label().setDisable(newDisableValue);
        controller.getNickname_field().setDisable(newDisableValue);
        controller.getConfirm_button().setDisable(newDisableValue);
        controller.getWaitingNickname_progressIndicator().setVisible(newDisableValue);
    }
}
