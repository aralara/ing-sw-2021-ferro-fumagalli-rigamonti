package it.polimi.ingsw.server.saves;

import it.polimi.ingsw.server.model.games.Game;
import it.polimi.ingsw.server.model.games.MultiGame;
import it.polimi.ingsw.server.model.games.SingleGame;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class GameSave {

    private final List<String> players;
    private final int id;
    private final String fileName;
    private boolean loaded;
    private Game game;


    public GameSave(String fileName) {
        this.players = new ArrayList<>();
        int id = 0;
        StringTokenizer tokenizer = new StringTokenizer(
                fileName.substring(0, fileName.lastIndexOf(".")),
                GameLibrary.NAME_SEPARATOR + ".");
        while(tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if(tokenizer.hasMoreElements())
                players.add(token);
            else
                id = Integer.parseInt(token);
        }
        this.id = id;
        this.fileName = fileName;
        this.loaded = false;
        this.game = null;
    }

    public GameSave(List<String> players, int id, Game game) {
        this.players = players;
        this.id = id;
        this.fileName = GameLibrary.LIBRARY_PATH
                .concat(String.join(GameLibrary.NAME_SEPARATOR, players))
                .concat(GameLibrary.NAME_SEPARATOR + id)
                .concat(GameLibrary.FILE_EXTENSION);
        this.loaded = true;
        this.game = game;
    }


    /**
     * Saves the currently loaded game to the referenced file if a game is loaded
     * @throws IOException Throws an IOException if an I/O error occurs
     */
    public void save() throws IOException {
        if(loaded) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName));
            output.writeObject(game);
            output.flush();
            output.close();
        }
    }

    /**
     * Loads the game contained in the referenced file
     * @throws IOException Throws an IOException if an I/O error occurs
     * @throws ClassNotFoundException Throws a ClassNotFoundException if the game can't be serialized
     */
    public void load() throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));
        if(players.size() == 1)
            game = (SingleGame) input.readObject();
        else
            game = (MultiGame) input.readObject();
        input.close();
        loaded = true;
    }

    /**
     * Deletes the referenced file
     * @return Returns true if the file is successfully deleted, false otherwise
     */
    public boolean delete() {
        File file = new File(fileName);
        return file.delete();
    }

    /**
     * Compares a String list to the contained list of players
     * @param players List to compare
     * @return Returns true if the lists are identical, false otherwise
     */
    public boolean samePlayers(List<String> players) {
        return this.players.equals(players);
    }

    /**
     * Compares an id to the contained id
     * @param id Id to compare
     * @return Returns true if the ids are identical, false otherwise
     */
    public boolean sameId(int id) {
        return this.id == id;
    }

    /**
     * Gets the loaded attribute
     * @return Returns loaded value
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Gets the id attribute
     * @return Returns id value
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the game attribute
     * @return Returns game value
     */
    public Game getGame() {
        return game;
    }
}
