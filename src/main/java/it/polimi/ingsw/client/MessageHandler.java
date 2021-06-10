package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.exceptions.UnknownMessageException;
import it.polimi.ingsw.utils.PipedPair;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.client.ClientMessage;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;
import it.polimi.ingsw.utils.messages.server.update.PlayerDisconnectedMessage;
import it.polimi.ingsw.utils.messages.server.update.ServerUpdateMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageHandler implements Runnable {

    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private final LinkedBlockingQueue<ServerActionMessage> actionQueue;
    private final LinkedBlockingQueue<ServerUpdateMessage> updateQueue;
    private final LinkedBlockingQueue<ServerAckMessage> responseQueue;

    private final List<ClientMessage> confirmationList;

    private final AtomicBoolean active;


    public MessageHandler() {
        this.actionQueue = new LinkedBlockingQueue<>();
        this.updateQueue = new LinkedBlockingQueue<>();
        this.responseQueue = new LinkedBlockingQueue<>();
        this.confirmationList = new ArrayList<>();
        active = new AtomicBoolean(false);
    }


    @Override
    public void run() {
        while(active.get())
            try {
                managePackets();
            } catch(IOException | ClassNotFoundException | InterruptedException e) {
                stop();
            } catch(UnknownMessageException e) {
                e.printStackTrace();    //TODO: errore?
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
            server = null;
            return false;
        }
        active.set(true);
        return true;
    }

    public boolean connect(PipedPair pipedPair) {
        try {
            output = new ObjectOutputStream(pipedPair.getPipeOut());
            input = new ObjectInputStream(pipedPair.getPipeIn());
        } catch (IOException | ClassCastException e) {
            return false;
        }
        active.set(true);
        return true;
    }

    private void managePackets()
            throws IOException, ClassNotFoundException, InterruptedException, UnknownMessageException {

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
    }

    public void sendMessage(Message message) {
        try{
            output.writeObject(message);
            output.reset();
            output.flush();
        }
        catch(IOException e) {
            stop();
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
        message.setRelativeMessage(clientMessage);
        responseQueue.put(message);
    }

    public void stop() {
        if(active.getAndSet(false)) {
            try {
                updateQueue.put(new PlayerDisconnectedMessage());
                if (server != null && !server.isClosed())
                    server.close();
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
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
}