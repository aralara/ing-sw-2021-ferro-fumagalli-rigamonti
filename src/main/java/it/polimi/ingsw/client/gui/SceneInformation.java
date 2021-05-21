package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllers.GenericController;
import javafx.scene.Scene;

public class SceneInformation {
    private final Scene scene;
    private final SceneNames fileName;
    private final GenericController controller;

    public SceneInformation(Scene scene, SceneNames fileName, GenericController controller){
        this.scene = scene;
        this.fileName = fileName;
        this.controller = controller;
    }

    public Scene getScene() {
        return scene;
    }

    public SceneNames getFileName() {
        return fileName;
    }

    public GenericController getController() {
        return controller;
    }
}
