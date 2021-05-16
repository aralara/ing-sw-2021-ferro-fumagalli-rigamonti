package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.concurrent.LinkedBlockingQueue;


public class AckMessageReader implements Runnable{

    private final ClientController client;
    private final LinkedBlockingQueue<ServerAckMessage> updateQueue;
    private boolean active;


    public AckMessageReader(ClientController client, LinkedBlockingQueue<ServerAckMessage> updateQueue) {
        this.client = client;
        this.updateQueue = updateQueue;
        this.active = false;
    }


    @Override
    public void run() {
        active = true;
        try {
            while(active) {
                ServerAckMessage message = updateQueue.take();
                message.doACK(client);
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        active = false;
    }
}