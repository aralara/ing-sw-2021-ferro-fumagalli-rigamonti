package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.messages.server.update.ServerUpdateMessage;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles the asynchronous reception of update messages associated with a client
 */
public class UpdateMessageReader implements Runnable {

    private final ClientController client;
    private final LinkedBlockingQueue<ServerUpdateMessage> updateQueue;
    private boolean active;


    /**
     * Constructor for an UpdateMessageReader given a client and its update queue
     * @param client Client that needs updates
     * @param updateQueue Queue of messages sent to the specific client
     */
    public UpdateMessageReader(ClientController client, LinkedBlockingQueue<ServerUpdateMessage> updateQueue) {
        this.client = client;
        this.updateQueue = updateQueue;
        this.active = false;
    }


    @Override
    public void run() {
        active = true;
        try {
            while(active) {
                ServerUpdateMessage message = updateQueue.take();
                message.doUpdate(client);
            }
        } catch(InterruptedException e) {
            if(active)
                e.printStackTrace();
        }
    }

    /**
     * Stops the update loop and interrupts the current thread
     */
    public void stop() {
        active = false;
        Thread.currentThread().interrupt();
    }
}
