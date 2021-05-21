package it.polimi.ingsw.server.view;

import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.action.PlayerDisconnectedMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public abstract class ClientHandler implements Runnable {

    private final Socket client;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private boolean active;

    public ClientHandler(Socket client, ObjectOutputStream out, ObjectInputStream in) {
        this.client = client;
        this.output = out;
        this.input = in;
        this.active = false;
    }

    @Override
    public void run() {
        active = true;
        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
            stop(true);
        }
    }

    private void handleClientConnection() throws IOException {
        System.out.println("Connected to " + client.getInetAddress());

        Object message;

        try {
            while (active){
                message = input.readObject();
                onMessageReceived((Message) message);
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try{
            output.writeObject(message);
            output.reset();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public abstract void onMessageReceived(Message message);

    public void stop(boolean propagate) {
        if(active) {
            if(!propagate)
                sendMessage(new PlayerDisconnectedMessage());
            active = false;
            try {
                if (client != null && !client.isClosed()) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    output.close();
                    input.close();
                    client.close();
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Fatal Error! Unable to close socket");
            }
        }
    }
}
