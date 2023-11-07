import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class OneToOneHandler {
    public static int doConnexionRequest(Users sendingUser, String message,Channels channel) {
        List<String> usernames = channel.getChannelUsernames();
        try {
            PrintWriter out = new PrintWriter(sendingUser.getSocket().getOutputStream(), true);
            for (String username : usernames) {
                if (username.equals(message.split(" ")[1])) {
                    PrintWriter outReceiver = new PrintWriter(channel.getUserByUsername(username).getSocket().getOutputStream(), true);
                    out.println("Connexion request sent to "+username);
                    outReceiver.println(sendingUser.getUsename() + " voudrais vous parler en privé.");
                    outReceiver.println("Voulez vous accepter ? (/accept "+sendingUser.getUsename()+" ou /decline "+sendingUser.getUsename()+")");
                    return 1;
                }
            }
             
        }catch (IOException e) {
        System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
    }

    public static int doConnexionResponse(List<Channels> channels,Users receivingUser, Users requestingUser, String message) {
        if(message.startsWith("/accept")){
            List<Users> users = new ArrayList<>();
            users.add(receivingUser);
            users.add(requestingUser);
            Channels privateChannel = new Channels(users, "");
            receivingUser.setCurrentChannel(privateChannel);
            requestingUser.setCurrentChannel(privateChannel);
            channels.add(privateChannel);

            return 1;
        }else{
            return 0;
        }
    }

    public static int doExitPrivate(Users user,List<Channels> channels){
        try {
            PrintWriter out = new PrintWriter(user.getSocket().getOutputStream(), true);
            channels.get(1).addUser(user);
            channels.get(2).removeBySocket(user.getSocket());
            user.setCurrentChannel(channels.get(1));
            out.println("Vous avez quitter le private chat.");
            return 1;
             
        }catch (IOException e) {
        System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
        
    }
}
