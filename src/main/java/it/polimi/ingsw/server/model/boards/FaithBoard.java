package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.exceptions.NotExistingLastReportTriggeredException;
import it.polimi.ingsw.server.model.faith.FaithTrack;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.listeners.Listeners;
import it.polimi.ingsw.utils.listeners.server.PlayerListened;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

import static it.polimi.ingsw.utils.Constants.FAITH_TOTAL_VATICAN_REPORTS;

public class FaithBoard extends PlayerListened implements Serializable {

    private int faith;
    private final boolean[] popeProgression;


    /**
     * Default constructor for FaithBoard
     */
    public FaithBoard() {
        faith = 0;
        popeProgression = new boolean[FAITH_TOTAL_VATICAN_REPORTS.value()];
    }


    /**
     * Adds a set amount of faith to the current player
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith) {
        this.faith += faith;
        fireUpdate(Listeners.BOARD_FAITH_FAITH.value(), this.faith);
    }

    /**
     * Handles the activation of a VaticanReport updating popeProgression values
     * @param faithTrack FaithTrack relative to the VaticanReport
     */
    public void handleReportActivation(FaithTrack faithTrack) {
        int lastReportTriggered = faithTrack.getLastReportTriggered();
        try {
            this.popeProgression[lastReportTriggered] = faithTrack.checkPlayerReportPosition(faith);
        } catch (NotExistingLastReportTriggeredException e) {
            e.printStackTrace();
        }
        fireUpdate(Listeners.BOARD_FAITH_POPE.value(), this.popeProgression);
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

    /**
     * Calculates total VPs given by the faithBoard for a player
     * @param faithTrack FaithTrack utilized to calculate total VPs
     * @return Returns VP amount
     */
    public int calculateVP(FaithTrack faithTrack) {
        return faithTrack.calculateVP(faith, popeProgression);
    }

    /**
     * Gets the faith attribute
     * @return Returns faith value
     */
    public int getFaith() {
        return this.faith;
    }

    /**
     * Gets the popeProgression attribute
     * @return Returns popeProgression value
     */
    public boolean[] getPopeProgression() {
        return this.popeProgression;
    }
}
