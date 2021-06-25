package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.exceptions.UnknownMessageException;
import it.polimi.ingsw.utils.PipedPair;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.client.ClientMessage;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.EndGameMessage;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;
import it.polimi.ingsw.utils.messages.server.update.ServerUpdateMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles the messages received and sent by a client
 */
public class MessageHandler implements Runnable {

    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private final LinkedBlockingQueue<ServerActionMessage> actionQueue;
    private final LinkedBlockingQueue<ServerUpdateMessage> updateQueue;
    private final LinkedBlockingQueue<ServerAckMessage> responseQueue;

    private final List<ClientMessage> confirmationList;

    private final AtomicBoolean active;


    /**
     * Constructor for the message handler
     */
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
                actionQueue.offer(new EndGameMessage(null, true));
                stop();
            } catch(UnknownMessageException e) {
                e.printStackTrace();
            }
    }

    /**
     * Connects remotely to a server
     * @param address IP address of the server
     * @param port Port of the server
     * @return Returns true if the connection was successful, false otherwise
     */
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

    /**
     * Connects locally to a simulated server
     * @param pipedPair Pair of piped streams used to establish a local connection
     * @return Returns true if the connection was successful, false otherwise
     */
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

    /**
     * Receives a packet from the server and handles it accordingly to its contents
     * @throws IOException Throws an IOException if there was a problem reading the stream
     * @throws ClassNotFoundException Throws an ClassNotFoundException if there was a problem
     *                                instantiating the packet object
     * @throws InterruptedException Throws an InterruptedException if there was a problem in placing
     *                              the packet in a queue
     * @throws UnknownMessageException Throws an UnknownMessageException if the packet wasn't recognized
     */
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

    /**
     * Sends a message to the server
     * @param message Message to send
     */
    private void sendMessage(Message message) {
        try{
            output.writeObject(message);
            output.reset();
            output.flush();
        }
        catch(IOException e) {
            actionQueue.offer(new EndGameMessage(null, true));
            stop();
        }
    }

    /**
     * Sends a message to the server and puts it in a confirmation list while waiting for an ACK
     * @param message Message to send
     */
    public void sendClientMessage(ClientMessage message) {
        confirmationList.add(message);
        sendMessage(message);
    }

    /**
     * Matches an ACK with its corresponding original message and puts in its dedicated queue
     * @param message ACK message received
     * @throws UnknownMessageException  Throws an UnknownMessageException if the packet wasn't recognized
     * @throws InterruptedException Throws an InterruptedException if there was a problem in placing
     *                              the packet in a queue
     */
    private void handleACKMessage(ServerAckMessage message) throws UnknownMessageException, InterruptedException {
        ClientMessage clientMessage = confirmationList.stream()
                .filter(message::compareTo)
                .findFirst().orElseThrow(UnknownMessageException::new);
        message.setRelativeMessage(clientMessage);
        responseQueue.put(message);
    }

    /**
     * Stops the packet reception loop and closes the connection to the server
     */
    public void stop() {
        if(active.getAndSet(false)) {
            try {
                if (server != null && !server.isClosed())
                    server.close();
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the actionQueue attribute
     * @return Returns actionQueue value
     */
    public LinkedBlockingQueue<ServerActionMessage> getActionQueue() {
        return actionQueue;
    }

    /**
     * Gets the updateQueue attribute
     * @return Returns updateQueue value
     */
    public LinkedBlockingQueue<ServerUpdateMessage> getUpdateQueue() {
        return updateQueue;
    }

    /**
     * Gets the responseQueue attribute
     * @return Returns responseQueue value
     */
    public LinkedBlockingQueue<ServerAckMessage> getResponseQueue() {
        return responseQueue;
    }

    /**
     * Gets the confirmationList attribute
     * @return Returns confirmationList value
     */
    public List<ClientMessage> getConfirmationList() {
        return confirmationList;
    }
}