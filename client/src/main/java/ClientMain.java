import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        String serverAddress = args[0]; // Adresse du serveur
        int serverPort = Integer.parseInt(args[1]); // Port du serveur

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connecté au serveur " + serverAddress + " sur le port " + serverPort);

            try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
            ) {
                Thread receiveThread = new Thread(() -> {
                    while (true) {
                        try {
                            String response = in.readLine();
                            if (response == null) {
                                break;
                            }
                            System.out.println(response);
                        } catch (IOException e) {
                            System.err.println("Erreur de réception : " + e.getMessage());
                            break;
                        }
                    }
                });
                receiveThread.start();

                while (true) {
                    String message = scanner.nextLine();
                    out.println(message);
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la communication avec le serveur : " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Impossible de se connecter au serveur : " + e.getMessage());
        }
    }
}
