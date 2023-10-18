package galactic_messenger.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {
    public static void main(String[] args) {
        int serverPort = Integer.parseInt(args[0]); // Port du serveur
        
        List<Socket> clients = new ArrayList<>();
        
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Serveur en attente de connexions sur le port " + serverPort);

            while (true) {
                // Accepter une nouvelle connexion de client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion : " + clientSocket.getInetAddress().getHostAddress());

                clients.add(clientSocket);

                // Créer un thread pour gérer la communication avec le client
                Thread clientThread = new Thread(new ClientHandler(clientSocket, clients));
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du serveur : " + e.getMessage());
        }
    }
}
