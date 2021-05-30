package it.polimi.ingsw.server.view;

import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.update.PlayerDisconnectedMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class VirtualView implements Runnable {

    private final Socket client;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    private boolean active;
    private String nickname;
    private GameHandler gameHandler;


    public VirtualView(ObjectOutputStream out, ObjectInputStream in, String nickname) {
        this.nickname = nickname;
        this.client = null;
        this.output = out;
        this.input = in;
        this.active = false;
    }

    public VirtualView(Socket client, ObjectOutputStream out, ObjectInputStream in, String nickname) {
        this.nickname = nickname;
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
            if(client != null)
                System.out.println("client " + client.getInetAddress() + " connection dropped");
            stop(true);
        }
    }

    private void handleClientConnection() throws IOException {
        Object message;
        try {
            while (active) {
                message = input.readObject();
                onMessageReceived((Message) message);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try{
            output.writeObject(message);
            output.reset();
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void onMessageReceived(Message message) {
        getGameHandler().handleMessage(this, message);
    }

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
        if(propagate)
            getGameHandler().stop();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public GameHandler getGameHandler() {
        return  gameHandler;
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
}
