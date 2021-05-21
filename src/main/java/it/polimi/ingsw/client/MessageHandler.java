package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.UnknownMessageException;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.client.ClientActionMessage;
import it.polimi.ingsw.utils.messages.client.ClientMessage;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;
import it.polimi.ingsw.utils.messages.server.update.ServerUpdateMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageHandler implements Runnable{

    private ClientController client;

    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private final LinkedBlockingQueue<ServerActionMessage> actionQueue;
    private final LinkedBlockingQueue<ServerUpdateMessage> updateQueue;
    private final LinkedBlockingQueue<ServerAckMessage> responseQueue;

    private final List<ClientMessage> confirmationList;

    private boolean active;


    public MessageHandler(ClientController client) {
        this.client = client;
        this.actionQueue = new LinkedBlockingQueue<>();
        this.updateQueue = new LinkedBlockingQueue<>();
        this.responseQueue = new LinkedBlockingQueue<>();
        this.confirmationList = new ArrayList<>();
        active = false;
    }


    public LinkedBlockingQueue<ServerActionMessage> getActionQueue() {
        return actionQueue;
    }

    public LinkedBlockingQueue<ServerUpdateMessage> getUpdateQueue() {
        return updateQueue;
    }

    public LinkedBlockingQueue<ServerAckMessage> getResponseQueue() {
        return responseQueue;
    }

    public List<ClientMessage> getConfirmationList() {
        return confirmationList;
    }

    @Override
    public void run() {
        while(active)
            try {
                managePackets();
            } catch(UnknownMessageException e) {
                e.printStackTrace();
            }
    }

    public boolean connect(String address, int port) {
        try {
            server = new Socket(address, port);
        } catch (IOException e) {
            return false;
        }

        try {
            output = new ObjectOutputStream(server.getOutputStream());
            input = new ObjectInputStream(server.getInputStream());

        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
        }
        active = true;
        return true;
    }

    private void managePackets() throws UnknownMessageException {
        try {
            Object message;

            message = input.readObject();

            if(message instanceof ServerUpdateMessage)
                updateQueue.put((ServerUpdateMessage) message);
            else if(message instanceof ServerActionMessage)
                actionQueue.put((ServerActionMessage) message);
            else if(message instanceof ServerAckMessage)
                handleACKMessage((ServerAckMessage) message);
            else
                throw new UnknownMessageException();
        }catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try{
            output.writeObject(message);
            output.reset();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendClientMessage(ClientMessage message) {
        confirmationList.add(message);
        sendMessage(message);
    }

    private void handleACKMessage(ServerAckMessage message) throws UnknownMessageException, InterruptedException {
        ClientMessage clientMessage = confirmationList.stream()
                .filter(message::compareTo)
                .findFirst().orElseThrow(UnknownMessageException::new);
        confirmationList.remove(clientMessage);
        message.setRelativeMessage(clientMessage);
        responseQueue.put(message);
    }

    public void stop() {
        active = false;
    }
}