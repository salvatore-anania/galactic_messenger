package galactic_messenger.server;

import java.io.*;
import java.net.*;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<Socket> clients;

    public ClientHandler(Socket clientSocket, List<Socket> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String clientAddress = clientSocket.getInetAddress().getHostAddress();
            System.out.println("Connecté au client : " + clientAddress);

            while (true) {
                // Lire le message du client
                String message = in.readLine();
                if (message == null) {
                    break;
                }

                // Envoyer le message à tous les autres clients
                for (Socket otherClient : clients) {
                    if (otherClient != clientSocket) {
                        PrintWriter otherOut = new PrintWriter(otherClient.getOutputStream(), true);
                        otherOut.println(clientAddress + ": " + message);
                    }
                }
            }

            System.out.println("Client déconnecté : " + clientAddress);
            clients.remove(clientSocket);
        } catch (IOException e) {
            System.err.println("Erreur lors de la communication avec le client : " + e.getMessage());
        }
    }
}
