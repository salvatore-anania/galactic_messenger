package galactic_messenger.server;

import java.io.*;
import java.net.*;
import java.util.List;
import java.sql.Connection;


public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<Socket> clients;
    private Connection connection;

    public ClientHandler(Socket clientSocket, List<Socket> clients, Connection connection) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.connection = connection;
    }
    @Override
    public void run() {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String clientAddress = clientSocket.getInetAddress().getHostAddress();
            System.out.println("Connecté au client : " + clientAddress);
            CommandHandler commandHandler = new CommandHandler();

            while (true) {
                // Lire le message du client
                String message = in.readLine();
                int commandeCode = 0;
                if (message == null || message.isEmpty()) {
                    break;
                }
                if (message.charAt(0) == '/'){
                    commandeCode = commandHandler.getCommand(message, clientSocket, connection);
                }
                if (commandeCode == 0){
                    // Envoyer le message à tous les autres clients
                    for (Socket otherClient : clients) {
                        if (otherClient != clientSocket) {
                            PrintWriter otherOut = new PrintWriter(otherClient.getOutputStream(), true);
                            otherOut.println(clientAddress + ": " + message);
                        }
                    }
                }
            }
            System.out.println("Client déconnecté : " + clientAddress);
            clients.remove(clientSocket);        
        }catch (IOException e) {
            System.err.println("Erreur lors de la communication avec le client : " + e.getMessage());
        }
    }
}