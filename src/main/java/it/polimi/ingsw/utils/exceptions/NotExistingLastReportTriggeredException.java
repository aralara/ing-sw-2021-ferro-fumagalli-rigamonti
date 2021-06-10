package it.polimi.ingsw.utils.exceptions;

public class NotExistingLastReportTriggeredException extends Exception {

    @Override
    public String getMessage() {
        return "Error! There are no triggered reports to check";
    }
}
