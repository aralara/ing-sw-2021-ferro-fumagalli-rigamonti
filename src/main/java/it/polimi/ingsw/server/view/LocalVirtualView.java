package it.polimi.ingsw.server.view;

import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.client.ClientActionMessage;

public class LocalVirtualView extends VirtualView {

    public LocalVirtualView(String nickname) {
        setNickname(nickname);
    }

    @Override
    public void sendMessage(Message message) {

    }

    public void onMessageReceived(Message message) {
        if (message instanceof ClientActionMessage) {
            ((ClientActionMessage) message).doAction(this);
        } else {
            System.out.println("Can't handle message");
        }
    }
}
