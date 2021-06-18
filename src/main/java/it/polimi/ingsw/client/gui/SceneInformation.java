package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllers.GenericController;
import javafx.scene.Scene;

/**
 * Handles information of loaded scenes
 */
public class SceneInformation {
    private final Scene scene;
    private final SceneNames fileName;
    private final GenericController controller;

    /**
     * SceneInformation constructor with parameters
     * @param scene Scene which information is referred to
     * @param fileName Scene's name
     * @param controller Scene's controller
     */
    public SceneInformation(Scene scene, SceneNames fileName, GenericController controller){
        this.scene = scene;
        this.fileName = fileName;
        this.controller = controller;
    }

    /**
     * Gets the scene attribute
     * @return Returns scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Gets the fileName attribute
     * @return Returns fileName
     */
    public SceneNames getFileName() {
        return fileName;
    }

    /**
     * Gets the controller attribute
     * @return Returns controller
     */
    public GenericController getController() {
        return controller;
    }
}
