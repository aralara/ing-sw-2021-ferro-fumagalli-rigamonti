package it.polimi.ingsw.client.cli;

/**
 * Option for the CLI menu
 */
public class MenuOption {

    private final String title;
    private final Runnable action;

    /**
     * Constructor for a CLI option
     * @param title Name of the option
     * @param action Code called by the option, if selected
     */
    public MenuOption(String title, Runnable action) {
        this.title = title;
        this.action = action;
    }

    /**
     * Gets the title attribute
     * @return Returns title value
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the action attribute
     * @return Returns action value
     */
    public Runnable getAction() {
        return action;
    }
}
