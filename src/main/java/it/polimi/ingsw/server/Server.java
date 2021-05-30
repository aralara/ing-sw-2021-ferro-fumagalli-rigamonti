package it.polimi.ingsw.server;

import it.polimi.ingsw.server.saves.GameLibrary;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.messages.client.ClientSetupMessage;
import it.polimi.ingsw.utils.messages.client.ConnectionMessage;
import it.polimi.ingsw.utils.messages.client.NewLobbyMessage;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.LobbyMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public final static int SOCKET_PORT = 1919;

    private boolean running;

    private ServerSocket socket;

    private GameHandler waitingGame;
    private List<GameHandler> gameList;


    public Server() {
        reset();
    }


    public static void main(String[] args) {
        Server server = new Server();

        try {
            server.socket = new ServerSocket(SOCKET_PORT);
            server.running = true;
            System.out.println("Server started");
        } catch (IOException e) {
            System.out.println("Error! Cannot open server socket");
            System.exit(1);
            return;
        }

        try {
            while (server.running) {
                Socket client = server.socket.accept();
                server.handleNewConnection(client);
            }
        } catch (IOException e) {
            System.out.println("Error! Connection dropped");
        }
    }

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
            }
            else {
                output.writeObject(new LobbyMessage(0, 0));
                output.reset();

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

            waitingGame.add(new VirtualView(client, output, input, nickname));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected during setup");
        }

        if (waitingGame != null && waitingGame.isFull()) {
            new Thread(waitingGame).start();
            waitingGame = null;
        }
    }

    public boolean checkValidNickname(String nickname) {
        if(nickname.matches(GameLibrary.NONVALID_REGEX))
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

    public boolean checkValidSize(int size) {
        return size >= Constants.MIN_LOBBY_SIZE.value() && size <= Constants.MAX_LOBBY_SIZE.value();
    }

    public void writeAck(ObjectOutputStream output, ClientSetupMessage message, boolean success) throws IOException {
        ServerAckMessage ack = new ServerAckMessage(message, success);
        output.writeObject(ack);
        output.reset();
    }

    public void reset() {
        running = false;
        waitingGame = null;
        gameList = new ArrayList<>();
    }
}
