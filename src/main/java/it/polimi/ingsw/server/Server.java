package it.polimi.ingsw.server;

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
                Object packet;
                String nick = null;
                do {
                    packet = server.input.readObject();
                    nick = ((ConnectionMessage) packet).getNickname();

                } while (!server.checkValidNickname(nick));
                server.nicknameUsed.add(nick);
                System.out.println("Benvenuto " + nick);


                server.output.writeObject(new LobbyMessage(server.size, server.connectedPlayers));

                if (!server.lobbyAlreadyExist) {
                    packet = server.input.readObject();
                    server.size = ((NewLobbyMessage) packet).getLobbySize();

                    server.gameHandler = new GameHandler(server.size);
                    server.lobbyAlreadyExist = true;

                }

                ClientHandler clientHandler = new ClientHandler(client, server.output, server.input, nick);

                server.gameHandler.add(clientHandler);
                server.connectedPlayers++;

                if (server.connectedPlayers == server.size) {
                    Thread thread = new Thread(server.gameHandler);
                    thread.start();
                    server.reset();
                }

                /*Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                thread.start();*/
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        try{
            output.writeObject(new ConnectionAckMessage(true));
        }catch(IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
