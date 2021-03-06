package it.polimi.ingsw.server;

import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.server.controller.GameHandler;
import it.polimi.ingsw.server.saves.GameLibrary;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.messages.client.ClientSetupMessage;
import it.polimi.ingsw.utils.messages.client.ConnectionMessage;
import it.polimi.ingsw.utils.messages.client.NewLobbyMessage;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.EndGameMessage;
import it.polimi.ingsw.utils.messages.server.action.LobbyMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server class that handles lobbies and new client connections
 */
public class Server {

    public final static int SOCKET_PORT = 1919;

    private boolean running;

    private ServerSocket socket;

    private GameHandler waitingGame;
    private List<GameHandler> gameList;


    /**
     * Server constructor with no parameters
     */
    public Server() {
        reset();
    }

    /**
     * Main method
     * @param args Arguments
     */
    public static void main(String[] args) {

        GraphicalCLI.printlnString("Master of Renaissance: Server version");

        GraphicalCLI.printString("Select a port for the Server socket (0 for default port): ");
        int port = GraphicalCLI.getNextInt();
        if(port <= 0)
            port = SOCKET_PORT;

        Server server = new Server();

        try {
            server.socket = new ServerSocket(port);
            server.running = true;
            GraphicalCLI.printlnString("Server started on port " + port);
        } catch (IOException e) {
            GraphicalCLI.printlnString("Unable to start Server");
            System.exit(1);
            return;
        }

        try {
            while (server.running) {
                Socket client = server.socket.accept();
                server.handleNewConnection(client);
            }
        } catch (IOException e) {
            GraphicalCLI.printlnString("Error! Connection dropped");
        }
    }

    /**
     * Handles a new connection with a client
     * @param client Socket of the client
     */
    public void handleNewConnection(Socket client) {
        Object message;
        String nickname = "";
        int lobbySize = 1;
        ObjectOutputStream output;
        ObjectInputStream input;

        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());

            boolean success = false;
            while(!success) {
                message = input.readObject();
                ConnectionMessage connectionMessage = (ConnectionMessage) message;
                nickname = connectionMessage.doSetup();
                success = checkValidNickname(nickname);
                writeAck(output, connectionMessage, success);
            }

            if(waitingGame != null) {
                output.writeObject(new LobbyMessage(waitingGame.getSize(), waitingGame.getAllNicknames().size()));
                output.reset();
                output.flush();
            }
            else {
                output.writeObject(new LobbyMessage(0, 0));
                output.reset();
                output.flush();

                success = false;
                while(!success) {
                    message = input.readObject();
                    NewLobbyMessage lobbyMessage = (NewLobbyMessage) message;
                    lobbySize = lobbyMessage.doSetup();
                    success = checkValidSize(lobbySize);
                    writeAck(output, lobbyMessage, success);
                }
                waitingGame = new GameHandler(lobbySize);
                gameList.add(waitingGame);
            }

            if(waitingGame.isActive())
                waitingGame.add(new VirtualView(client, output, input, nickname));
            else {
                output.writeObject(new EndGameMessage(null, true));
                output.reset();
                output.flush();
                waitingGame = null;
            }
        } catch (IOException | ClassNotFoundException e) {
            GraphicalCLI.printlnString("Client disconnected during setup");
        }

        if (waitingGame != null && waitingGame.isFull()) {
            new Thread(waitingGame).start();
            waitingGame = null;
        }
    }

    /**
     * Checks if a given nickname is valid (isn't present in an active game and doesn't contain special characters)
     * @param nickname Nickname that needs to be checked
     * @return Returns true if the nickname is valid, false otherwise
     */
    public boolean checkValidNickname(String nickname) {
        if(nickname.matches(GameLibrary.NON_VALID_REGEX))
            return false;
        for (GameHandler gameHandler : gameList) {
            if(gameHandler.isActive()) {
                for (String s : gameHandler.getAllNicknames())
                    if (s.equals(nickname))
                        return false;
            }
        }
        return true;
    }

    /**
     * Checks if a given size is valid for a game (needs to be betweenMIN_LOBBY_SIZE and MAX_LOBBY_SIZE)
     * @param size Size that needs to be checked
     * @return Returns ture if the size is valid, false otherwise
     */
    public boolean checkValidSize(int size) {
        return size >= Constants.MIN_LOBBY_SIZE.value() && size <= Constants.MAX_LOBBY_SIZE.value();
    }

    /**
     * Sends an ACK message to the client
     * @param output Stream that will be used to send the message
     * @param message Referenced client message
     * @param success Determines if the message sent is an ACK or a NACK
     * @throws IOException Throws a IOException in case it's not possible to send the message
     */
    public void writeAck(ObjectOutputStream output, ClientSetupMessage message, boolean success) throws IOException {
        ServerAckMessage ack = new ServerAckMessage(message, success);
        output.writeObject(ack);
        output.reset();
        output.flush();
    }

    /**
     * Resets the server attributes to their default values
     */
    public void reset() {
        running = false;
        waitingGame = null;
        gameList = new ArrayList<>();
    }
}
