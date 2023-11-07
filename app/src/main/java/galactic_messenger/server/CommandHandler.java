package galactic_messenger.server;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.List;

import galactic_messenger.model.Channels;
import galactic_messenger.model.Users;


public class CommandHandler {

    public int[] getCommand(String message, Users user, Connection connection, List<Channels> channels) {
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
        if (message.startsWith("/private_chat")) {
            commandeCode = 4;
        }
        if (message.startsWith("/accept") || message.startsWith("/decline")) {
            commandeCode = 5;
        }
        if (message.startsWith("/exit_private_chat") || message.startsWith("/decline")) {
            commandeCode = 6;
        }
        int isRegistered = doComand(message, commandeCode, user, connection,channels);
        int[] returnValue = {commandeCode, isRegistered};
        return returnValue;
    }
    
    public int doComand(String message, int commandeCode, Users user, Connection connection, List<Channels> channels) {
        switch (commandeCode) {
            case 1:
                doHelp(user.getSocket());
                return 0;
            case 2:
                return ConnexionHandler.doRegister(message, connection, user.getSocket());
            case 3:
                return ConnexionHandler.doLogin(message, connection, user);
            case 4:
                return OneToOneHandler.doConnexionRequest(user, message, channels.get(1));
            case 5:
                return OneToOneHandler.doConnexionResponse(channels, user, channels.get(1).getUserByUsername(message.split(" ")[1]), message);
            case 6:
                return OneToOneHandler.doExitPrivate(user, channels);
            default:
                return 0;
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
    
    
}


