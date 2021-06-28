package it.polimi.ingsw.server.saves;

import it.polimi.ingsw.utils.exceptions.LibraryNotLoadedException;
import it.polimi.ingsw.server.model.games.Game;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Library that handles loading saves, updating saves and accessing saves
 */
public class GameLibrary {

    public static final String LIBRARY_PATH = "./saves/";
    public static final String NAME_SEPARATOR = "-";
    public static final String FILE_EXTENSION = ".mor";
    public static final String NON_VALID_REGEX = "(.*[<>:/\\\\|?*-]+.*)|(^$)";

    private static GameLibrary instance = null;

    private Set<GameSave> saves;
    private boolean loaded;


    private GameLibrary() {
        loadSaves();
    }


    /**
     * Gets singleton instance
     * @return Returns a unique GameLibrary object
     */
    public static GameLibrary getInstance() {
        if (instance == null)
            instance = new GameLibrary();
        return instance;
    }


    /**
     * Loads all the saves from the specified library path and adds them to the "saves" set
     */
    private void loadSaves() {
        if(!loaded) {
            saves = new HashSet<>();
            File directory = new File(LIBRARY_PATH);
            boolean directoryExists = directory.exists();
            if(!directoryExists)
                directoryExists = directory.mkdir();
            if(directoryExists) {
                String[] files = directory.list();
                if (files != null) {
                    for (String fileName : files)
                        saves.add(new GameSave(fileName));
                }
                loaded = true;
            }
        }
    }

    /**
     * Creates a save from a game and adds it to the "saves" set
     * @param game Game to create a save of
     * @return Returns the created save object
     * @throws LibraryNotLoadedException Throws a LibraryNotLoadedException if the library is not loaded
     */
    public GameSave createSave(Game game) throws LibraryNotLoadedException {
        checkLoad();
        List<String> players = game.getPlayerBoards().stream()
                .map(pb -> pb.getPlayer().getNickname()).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        int id = getNextId(players);
        GameSave save = new GameSave(players, id, game);
        saves.add(save);
        return save;
    }

    /**
     * Deletes a save from the "saves" set and deletes its source file
     * @param save Save object to delete
     * @return Returns true if the save source file is successfully deleted
     * @throws LibraryNotLoadedException Throws a LibraryNotLoadedException if the library is not loaded
     */
    public boolean deleteSave(GameSave save) throws LibraryNotLoadedException {
        checkLoad();
        if(save != null) {
            saves.remove(save);
            return save.delete();
        }
        return false;
    }

    /**
     * Returns a list of saves given a list of player names
     * @param players List of player names
     * @return Returns the saves list
     * @throws LibraryNotLoadedException Throws a LibraryNotLoadedException if the library is not loaded
     */
    public List<GameSave> getSaves(List<String> players) throws LibraryNotLoadedException {
        checkLoad();
        List<String> orderedPlayers = players.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        return saves.stream().filter(s -> s.samePlayers(orderedPlayers)).collect(Collectors.toList());
    }

    /**
     * Checks if at least one save is present given a list of player names
     * @param players List of player names
     * @return True if at least one save is present, false otherwise
     * @throws LibraryNotLoadedException Throws a LibraryNotLoadedException if the library is not loaded
     */
    public boolean checkSave(List<String> players) throws LibraryNotLoadedException {
        checkLoad();
        List<String> orderedPlayers = players.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        return saves.stream().anyMatch(s -> s.samePlayers(orderedPlayers));
    }

    /**
     * Returns all the ids of the saves given a list of player names
     * @param players List of player names
     * @return Returns a list of save ids
     * @throws LibraryNotLoadedException Throws a LibraryNotLoadedException if the library is not loaded
     */
    public List<Integer> getIds(List<String> players) throws LibraryNotLoadedException {
        checkLoad();
        List<String> orderedPlayers = players.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        return saves.stream().filter(s -> s.samePlayers(orderedPlayers))
                .map(GameSave::getId).collect(Collectors.toList());
    }

    /**
     * Returns the next id following numerical order given a list of player names
     * @param players List of player names
     * @return Returns an int containing the next possible id
     * @throws LibraryNotLoadedException Throws a LibraryNotLoadedException if the library is not loaded
     */
    public int getNextId(List<String> players) throws LibraryNotLoadedException {
        checkLoad();
        List<String> orderedPlayers = players.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        return saves.stream().filter(s -> s.samePlayers(orderedPlayers))
                .mapToInt(GameSave::getId).max().orElse(-1) + 1;
    }

    /**
     * Method that throws a LibraryNotLoadedException if the library is not loaded
     * @throws LibraryNotLoadedException LibraryNotLoadedException to be thrown
     */
    private void checkLoad() throws LibraryNotLoadedException {
        if(!loaded)
            throw new LibraryNotLoadedException();
    }
}
