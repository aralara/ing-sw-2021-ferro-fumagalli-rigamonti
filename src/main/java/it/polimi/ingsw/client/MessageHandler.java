package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.UnknownMessageException;
import it.polimi.ingsw.utils.messages.AckMessage;
import it.polimi.ingsw.utils.messages.IdentifiedMessage;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.client.ClientActionMessage;
import it.polimi.ingsw.utils.messages.server.ServerActionAckMessage;
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

    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private final LinkedBlockingQueue<ServerActionMessage> actionQueue;
    private final LinkedBlockingQueue<ServerUpdateMessage> updateQueue;
    private final LinkedBlockingQueue<ServerActionAckMessage> responseQueue;

    private final List<ClientActionMessage> confirmationList;

    private boolean active;


    public MessageHandler() {
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

    public LinkedBlockingQueue<ServerActionAckMessage> getResponseQueue() {
        return responseQueue;
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
            else if(message instanceof ServerActionAckMessage)
                handleACKMessage((ServerActionAckMessage) message);
            else if(message instanceof AckMessage)
                ;//TODO: temp andrà tolto
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

    public void sendActionMessage(ClientActionMessage message) {
        confirmationList.add(message);
        sendMessage(message);
    }

    private void handleACKMessage(ServerActionAckMessage message) throws UnknownMessageException {
        ClientActionMessage clientMessage = confirmationList.stream()
                .filter(m -> message.compareTo((IdentifiedMessage) m))
                .findFirst().orElseThrow(UnknownMessageException::new);
        confirmationList.remove(clientMessage);
        message.setRelativeMessage(clientMessage);
        responseQueue.add(message);
    }

    public void stop() {
        active = false;
    }
}