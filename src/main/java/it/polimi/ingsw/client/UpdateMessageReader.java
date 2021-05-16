package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.messages.server.update.ServerUpdateMessage;

import java.util.concurrent.LinkedBlockingQueue;

public class UpdateMessageReader implements Runnable{

    private final ClientController client;
    private final LinkedBlockingQueue<ServerUpdateMessage> updateQueue;
    private boolean active;


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
            e.printStackTrace();
        }
    }

    public void stop() {
        active = false;
    }
}
