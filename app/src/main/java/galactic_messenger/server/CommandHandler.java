package galactic_messenger.server;
import java.net.*;
import java.io.*;
import java.sql.*;


public class CommandHandler {

    public int getCommand(String message, Socket clientSocket, Connection connection) {
        int commandeCode = 0;
        if (message.equals("/help")) {
            commandeCode = 1;
        }
        if (message.startsWith("/register")) {
            commandeCode = 2;
        }
        if (message.startsWith("/login")) {
            commandeCode = 3;
        }
        doComand(message, commandeCode, clientSocket, connection);
        return commandeCode;
    }
    
    public void doComand(String message, int commandeCode, Socket clientSocket, Connection connection) {
        switch (commandeCode) {
            case 1:
                doHelp(clientSocket);
                break;
            case 2:
                this.doRegister(message, connection);
                break;
            case 3:
                break;
            default:
                break;
        }
    }
    
    static public void doHelp(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Commandes disponibles :");
            out.println("/help : Affiche la liste des commandes disponibles");
            out.println("/register : Permet de s'enregistrer");
            out.println("/login : Permet de se connecter");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message d'aide au client : " + e.getMessage());
        }
    }
    
    public void doRegister(String message, Connection connection) {
            try {
            // Utilisez une requête paramétrée pour insérer des données
            String sql = "INSERT INTO users (name, password) VALUES (?, ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message.split(" ")[1]);
            preparedStatement.setString(2, message.split(" ")[2]);

            // Exécutez la requête
            preparedStatement.executeUpdate();

            // Fermez la déclaration et la connexion
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


