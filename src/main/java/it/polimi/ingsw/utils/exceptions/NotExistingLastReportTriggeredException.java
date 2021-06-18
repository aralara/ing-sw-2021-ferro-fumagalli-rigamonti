package it.polimi.ingsw.utils.exceptions;

/**
 * Exception thrown when the triggered report has an impossible value
 */
public class NotExistingLastReportTriggeredException extends Exception {

    @Override
    public String getMessage() {
        return "Error! There are no triggered reports to check";
    }
}
