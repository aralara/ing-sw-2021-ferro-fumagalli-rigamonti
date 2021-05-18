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
        System.out.println("Welcome to Master of Renaissance!\nWhat do you want to launch?"); //TODO: da modificare
        System.out.println("0: SERVER\n1: CLIENT (CLI INTERFACE)\n2: CLIENT (GUI INTERFACE)");
        System.out.print("Type the number of the desired option: ");
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
