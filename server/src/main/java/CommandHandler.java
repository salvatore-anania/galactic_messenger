import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.List;

public class CommandHandler {

    public int[] getCommand(String message, Users user, Connection connection, List<Channels> channels) {
        int commandeCode = 0;
        message = message.trim().replaceAll("\\s+", " ");;
        if (message.equals("/help") || message.equals("/h")) {
            commandeCode = 1;
        }
        else if (message.startsWith("/register") || message.startsWith("/r")) {
            commandeCode = 2;
        }
        else if (message.startsWith("/login") || message.startsWith("/l")) {
            commandeCode = 3;
        }
        else if (message.startsWith("/private_chat") || message.startsWith("/p")) {
            commandeCode = 4;
        }
        else if (message.startsWith("/accept") || message.startsWith("/decline")) {
            commandeCode = 5;
        }
        else if (message.startsWith("/exit_private_chat")) {
            commandeCode = 6;
        }
        else if (message.startsWith("/create_group") || message.startsWith("/create_secure_group")) {
            commandeCode = 7;
        }
        else if (message.startsWith("/join_group") || message.startsWith("/join_secure_group")) {
            commandeCode = 8;
        }
        else if (message.startsWith("/msg_group")) {
            commandeCode = 9;
        }
        else if (message.startsWith("/exit_group")) {
            commandeCode = 10;
        }else if (message.startsWith("/online_users")) {
            commandeCode = 11;
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
                return ConnexionHandler.doLogin(channels, message, connection, user );
            case 4:
                return OneToOneHandler.doConnexionRequest(channels, user, message );
            case 5:
                return OneToOneHandler.doConnexionResponse(channels, user, channels.get(1).getUserByUsername(message.split(" ")[1]), message);
            case 6:
                return OneToOneHandler.doExitPrivate(channels, user);
            case 7:
                return GroupHandler.doCreationGroup(channels, message, user);
            case 8:
                return GroupHandler.doJoinGroup(channels,user, message);
            case 9:
                return GroupHandler.doMessagGroup(channels, user, message);
            case 10:
                return GroupHandler.doExitGroup(channels,user ,message);
            case 11:
                doOnlineUsers(user.getSocket(), channels.get(1));
                return 0;
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

    public void doOnlineUsers(Socket clientSocket, Channels channelGeneral) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Utilisateurs en ligne :");
            for (Users user : channelGeneral.getChannelUsers()) {
                out.println(user.getUsername());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
    }
}