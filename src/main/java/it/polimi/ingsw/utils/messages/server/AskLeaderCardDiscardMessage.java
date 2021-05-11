package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.client.cli.PacketHandler;
import it.polimi.ingsw.client.structures.PlayerBoardView;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.utils.messages.client.LeaderCardDiscardMessage;

import java.util.ArrayList;
import java.util.List;

public class AskLeaderCardDiscardMessage implements ServerActionMessage {

    @Override
    public void doAction(CLI client) {                      //TODO: print nel messaggio e azioni da delegare alla GraphicalCLI
        String nickname = client.getNickname();
        PacketHandler packetHandler = client.getPacketHandler();
        GraphicalCLI graphicalCLI = client.getGraphicalCLI();
        List<LeaderCard> leaderCards = new ArrayList<>();
        int firstOne, secondOne;

        try {
            PlayerBoardView playerBoard = client.playerBoardFromNickname(nickname);
            int size = playerBoard.getLeaderBoard().getHand().size();

            System.out.println("You have to discard 2 leader cards from your hand:");
            graphicalCLI.printLeaderCardList(playerBoard.getLeaderBoard().getHand());

            System.out.print("Choose the first one by selecting the corresponding number: ");
            firstOne = client.getNextInt() - 1;
            while(firstOne < 0 || firstOne >= size){
                System.out.print("The chosen number is invalid, please choose another one: ");
                firstOne = client.getNextInt() - 1;
            }
            System.out.print("Choose the second one by selecting the corresponding number: ");
            secondOne = client.getNextInt() - 1;
            while(secondOne < 0 || secondOne >= size || secondOne == firstOne){
                System.out.print("The chosen number is invalid, please choose another one: ");
                secondOne = client.getNextInt() - 1;
            }

            leaderCards.add((LeaderCard) playerBoard.getLeaderBoard().getHand().get(firstOne));
            leaderCards.add((LeaderCard) playerBoard.getLeaderBoard().getHand().get(secondOne));
            packetHandler.sendMessage(new LeaderCardDiscardMessage(leaderCards));
        }catch (NotExistingNickname e){
            e.printStackTrace();
        }
    }
}
