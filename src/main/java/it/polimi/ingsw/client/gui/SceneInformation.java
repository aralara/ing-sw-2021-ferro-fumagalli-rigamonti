package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllers.ControllerInterface;
import javafx.scene.Scene;

public class SceneInformation {
    private final Scene scene;
    private final SceneNames fileName;
    private final ControllerInterface controller;

    public SceneInformation(Scene scene, SceneNames fileName, ControllerInterface controller){
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

    public ControllerInterface getController() {
        return controller;
    }
}
