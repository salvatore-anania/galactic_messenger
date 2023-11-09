import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {
    public static void main(String[] args) {
        int serverPort = Integer.parseInt(args[0]); // Port du serveur
        
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Définissez l'URL de la base de données H2 (en mémoire) et les informations de connexion
        String dbUrl = "jdbc:h2:mem:demo"; // Changez l'URL si nécessaire
        String dbUser = "sa"; // Utilisateur par défaut pour H2
        String dbPassword = ""; // Mot de passe par défaut pour H2

        try {
            // Établissez une connexion à la base de données H2
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            DatabaseInitializer.initializeDatabase(connection); // Vous pouvez appeler cette méthode si nécessaire

            List<Users> clients = new ArrayList<>();
            List<Users> connectedClients = new ArrayList<>();
            List<Channels> channels = new ArrayList<>();
            channels.add(new Channels(clients, "","entry"));
            channels.add(new Channels(connectedClients, "", "general"));

            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
                InetAddress serverAddress = InetAddress.getLocalHost(); // Obtient l'adresse IP de la machine locale
                System.out.println("Server available at " + serverAddress.getHostAddress() + ':' + serverPort);

                while (true) {
                    // Accepter une nouvelle connexion de client
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nouvelle connexion : " + clientSocket.getInetAddress().getHostAddress());

                    clients.add(new Users(clientSocket, "", channels.get(0).getChannelName()));

                    // Créez un thread pour gérer la communication avec le client
                    Thread clientThread = new Thread(new ClientHandler(clientSocket, channels, connection));
                    clientThread.start();
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la création du serveur : " + e.getMessage());
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
        
    }

}
