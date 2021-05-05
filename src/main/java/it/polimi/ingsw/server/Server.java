package it.polimi.ingsw.server;

import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.client.ConnectionMessage;
import it.polimi.ingsw.utils.messages.client.NewLobbyMessage;
import it.polimi.ingsw.utils.messages.server.LobbyMessage;
import it.polimi.ingsw.utils.messages.server.ack.ConnectionAckMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public final static int SOCKET_PORT = 1919;

    private boolean lobbyAlreadyExist;
    private int size;
    private int connectedPlayers;
    private List<String> nicknameUsed;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private GameHandler gameHandler;

    public void reset(){
        lobbyAlreadyExist = false;
        connectedPlayers = 0;
        size = 0;
    }

    public static void main(String[] args) {

        Server server = new Server();
        server.nicknameUsed = new ArrayList<>();
        server.reset();

        ServerSocket socket;


        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("Error! Cannot open server socket");
            System.exit(1);
            return;
        }

        try {
            while (true) {
                Socket client = socket.accept();

                server.output = new ObjectOutputStream(client.getOutputStream());
                server.input = new ObjectInputStream(client.getInputStream());
                Object message;
                String nickname;
                do {
                    message = server.input.readObject();
                    nickname = ((ConnectionMessage) message).getNickname();

                } while (!server.checkValidNickname(nickname));
                server.nicknameUsed.add(nickname);
                System.out.println("Benvenuto " + nickname);


                server.output.writeObject(new LobbyMessage(server.size, server.connectedPlayers));
                server.output.reset();

                if (!server.lobbyAlreadyExist) {
                    message = server.input.readObject();
                    server.size = ((NewLobbyMessage) message).getLobbySize();

                    server.gameHandler = new GameHandler(server.size);
                    server.lobbyAlreadyExist = true;

                }

                server.gameHandler.add(client, server.output, server.input, nickname);
                server.connectedPlayers++;

                if (server.connectedPlayers == server.size) {
                    Thread thread = new Thread(server.gameHandler);
                    thread.start();
                    server.reset();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error! Connection dropped");
        }
    }

    public boolean checkValidNickname(String nickname) {
        for (String s : nicknameUsed) {
            if (s.equals(nickname)) {
                try {
                    output.writeObject(new ConnectionAckMessage(false));
                    output.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        try{
            output.writeObject(new ConnectionAckMessage(true));
            output.reset();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
