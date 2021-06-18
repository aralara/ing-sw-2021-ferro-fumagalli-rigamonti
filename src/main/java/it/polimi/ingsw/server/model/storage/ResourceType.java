package it.polimi.ingsw.server.model.storage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Types that a resource could have
 */
public enum ResourceType {
    COIN,
    SHIELD,
    SERVANT,
    STONE,
    FAITH,
    WILDCARD;

    /**
     * Gets a collection of all the enum values
     * @return Returns a list containing all specified the values
     */
    public static List<ResourceType> getValues() {
        return Arrays.asList(values());
    }

    /**
     * Gets a collection of all the selectable ResourceType values
     * @return Returns a list containing all specified the values
     */
    public static List<ResourceType> getRealValues() {
        return Arrays.stream(values()).filter(rt -> rt != ResourceType.FAITH && rt != ResourceType.WILDCARD)
                .collect(Collectors.toList());
    }
}
