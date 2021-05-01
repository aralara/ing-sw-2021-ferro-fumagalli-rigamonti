package it.polimi.ingsw.client;

import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("IP address of server?");
        String ip = scanner.nextLine();

        Socket server;
        try {
            server = new Socket(ip, Server.SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected");

        try {
            ObjectOutputStream output = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(server.getInputStream());


        } catch (IOException e) {
            System.out.println("server has died");
        } catch (ClassCastException e) {
            System.out.println("protocol violation");
        }
        while(true); //TODO: inserire nel while i controlli che facciamo con i pacchetti
    }
}