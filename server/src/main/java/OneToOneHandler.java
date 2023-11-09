import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class OneToOneHandler {
    public static int doConnexionRequest(List<Channels> channels, Users requestingUser, String message) {
        Channels channelgeneral=channels.get(1);
        List<String> usernames = channelgeneral.getChannelUsernames();
        try {
            PrintWriter out = new PrintWriter(requestingUser.getSocket().getOutputStream(), true);
            for (String username : usernames) {
                if (username.equals(message.split(" ")[1])) {
                    PrintWriter outReceiver = new PrintWriter(channelgeneral.getUserByUsername(username).getSocket().getOutputStream(), true);
                    out.println("Connexion request sent to "+username);
                    String channelName="private_"+requestingUser.getUsername()+"_"+username;
                    Channels privateChannel = new Channels(new ArrayList<Users>(), "", channelName);
                    requestingUser.setCurrentChannel(privateChannel.getChannelName());
                    privateChannel.addUser(requestingUser);
                    channels.add(privateChannel);
                    outReceiver.println(requestingUser.getUsername() + " voudrais vous parler en privé.");
                    outReceiver.println("Voulez vous accepter ? (/accept "+requestingUser.getUsername()+" ou /decline "+requestingUser.getUsername()+")");
                    return 1;
                }
            }
             
        }catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
    }

    public static int doConnexionResponse(List<Channels> channels,Users receivingUser, Users requestingUser, String message) {
        try {
            PrintWriter out = new PrintWriter(receivingUser.getSocket().getOutputStream(), true);
            PrintWriter outReceiver = new PrintWriter(requestingUser.getSocket().getOutputStream(), true);
            if(message.startsWith("/accept")){
                Channels previousChannel=ClientHandler.getChannelByName(channels,receivingUser.getCurrentChannel());          
                String channelName="private_"+requestingUser.getUsername()+"_"+receivingUser.getUsername();
                Channels privateChannel = ClientHandler.getChannelByName(channels, channelName);
                receivingUser.setCurrentChannel(privateChannel.getChannelName());
                privateChannel.addUser(receivingUser);
                out.println("Vous êtes connecté en privé avec "+requestingUser.getUsername());
                outReceiver.println("Vous êtes connecté en privé avec "+receivingUser.getUsername());
                previousChannel.updateUsers(privateChannel.getChannelUsers());
                return 1;
            }else{
                requestingUser.setCurrentChannel(channels.get(1).getChannelName());
                return 0;
            }
        }catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
        
    }

    public static int doExitPrivate(List<Channels> channels, Users user){
        try {
            PrintWriter out = new PrintWriter(user.getSocket().getOutputStream(), true);
            Channels privatChannel=ClientHandler.getChannelByName(channels, user.getCurrentChannel());
            privatChannel.removeBySocket(user.getSocket());
            user.setCurrentChannel(channels.get(1).getChannelName());
            channels.get(1).updateOneUser(user);
            for(Users otherClient : privatChannel.getChannelUsers()){
                PrintWriter otherOut = new PrintWriter(otherClient.getSocket().getOutputStream(), true);
                otherOut.println(user.getUsername() + " a quitté le private chat.");
                otherOut.println("Vous êtes de retour dans le channel general.");
                otherClient.setCurrentChannel(channels.get(1).getChannelName());
                channels.get(1).updateOneUser(otherClient);
                channels.remove(privatChannel);
            }
            out.println("Vous avez quitter le private chat.");
            return 1;
             
        }catch (IOException e) {
        System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
        
    }
}
