package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MasterOfRenaissance {
    private static int getNextInt(Scanner scanner) {
        int value = -1;
        boolean valid;
        do {
            valid = true;
            try {
                value = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input! Try again: ");
                scanner.nextLine();
                valid = false;
            }
        } while(!valid);
        return value;
    }

    public static void main(String[] args){
        System.out.println("This is Master of Renaissance!");
        System.out.println("0: Server\n1: Client - CLI version\n2: Client - GUI version");
        System.out.print("What do you want to launch? ");
        Scanner scanner = new Scanner(System.in);
        int input = getNextInt(scanner);
        while(input<0 || input>2){
            System.out.print("Invalid choice, please try again: ");
            input = getNextInt(scanner);
        }
        if (input==0) {
            System.out.println("Server starting...");
            Server.main(null);
        } else {
            String[] arguments = new String[]{Integer.toString(input)};
            Client.main(arguments);
        }
    }
}
