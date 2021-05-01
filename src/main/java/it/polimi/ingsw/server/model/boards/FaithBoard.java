package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.faith.FaithTrack;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.util.List;
import java.util.function.Predicate;

public class FaithBoard extends Listened {
    private static final int POPE_PROGRESSION_SIZE = 3;

    private int faith;
    private final boolean[] popeProgression;


    public FaithBoard() {
        faith = 0;
        popeProgression = new boolean[POPE_PROGRESSION_SIZE];
    }


    /**
     * Adds a set amount of faith to the current player
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith) {
        int temp = this.faith;
        this.faith += faith;
        fireUpdate(Listeners.BOARD_FAITH_FAITH.value(), temp, this.faith);
    }

    /**
     * Gets the faith attribute
     * @return Returns faith
     */
    public int getFaith() {
        return this.faith;
    }

    /**
     * Gets the popeProgression attribute
     * @return Returns popeProgression
     */
    public boolean[] getPopeProgression() {
        return this.popeProgression;
    }

    /**
     * Calculates total VPs given by the faithBoard for a player
     * @param faithTrack FaithTrack utilized to calculate total VPs
     * @return Returns VP amount
     */
    public int calculateVP(FaithTrack faithTrack) {
        return faithTrack.calculateVP(faith, popeProgression);
    }

    /**
     * Handles the activation of a VaticanReport updating popeProgression values
     * @param faithTrack FaithTrack relative to the VaticanReport
     */
    public void handleReportActivation(FaithTrack faithTrack) {
        int lastReportTriggered = faithTrack.getLastReportTriggered();
        boolean temp = this.popeProgression[lastReportTriggered];
        this.popeProgression[lastReportTriggered] = faithTrack.checkPlayerReportPosition(faith);
        fireUpdate(Listeners.BOARD_FAITH_POPE.value(), temp, this.popeProgression);
    }

    /**
     * Adds faith to the current FaithBoard from a list of resources, removing the utilized resources from the list
     * @param resources List of resources
     */
    public void takeFaithFromResources(List<Resource> resources) {
        Predicate<Resource> isFaith = r -> r.getResourceType() == ResourceType.FAITH;
        addFaith(resources.stream().filter(isFaith).map(Resource::getQuantity).findFirst().orElse(0));
        resources.removeIf(isFaith);
    }

}