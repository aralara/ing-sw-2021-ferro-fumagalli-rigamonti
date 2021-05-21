package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.messages.AckMessage;
import it.polimi.ingsw.utils.messages.client.ClientMessage;
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

                message = input.readObject();
                NewLobbyMessage lobbyMessage = (NewLobbyMessage) message;
                waitingGame = new GameHandler(((NewLobbyMessage) message).doSetup());  //TODO: controllare la size ricevuta
                gameList.add(waitingGame);
                writeAck(output, lobbyMessage, true);
            }

            waitingGame.add(client, output, input, nickname);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected during setup");
        }

        if (waitingGame != null && waitingGame.isFull()) {
            new Thread(waitingGame).start();
            waitingGame = null;
        }
    }

    public boolean checkValidNickname(String nickname) {
        for (GameHandler gameHandler : gameList) {
            if(gameHandler.isActive()) {
                for (String s : gameHandler.getAllNicknames())
                    if (s.equals(nickname))
                        return false;
            }
        }
        return true;
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
