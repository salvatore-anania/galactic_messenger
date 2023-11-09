import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GroupHandler {
    public static int doCreationGroup(List<Channels> channels, String message, Users creator) {
        try {
            String[] tableMessage = message.split(" ");
            PrintWriter out = new PrintWriter(creator.getSocket().getOutputStream(), true);
            String groupName = tableMessage[1];
            Channels groupChannel = new Channels(new ArrayList<Users>(), "", groupName);
            if(tableMessage[0].contains("secure")){
                groupChannel.setPassword(tableMessage[2]);
            }
            channels.add(groupChannel);
            out.println("Le groupe "+groupName+" a été créé.");
            out.println("Utiliser la commande '/join_group "+groupName+"' pour le rejoindre.");
            return 1;
                
        }catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
    }

    public static int doJoinGroup(List<Channels> channels, Users requestingUser, String message) {
        try {
            String[] tableMessage = message.split(" ");
            PrintWriter out = new PrintWriter(requestingUser.getSocket().getOutputStream(), true);        
            String groupName = tableMessage[1];
            Channels groupChannel = ClientHandler.getChannelByName(channels, groupName);
            if(tableMessage[0].contains("secure") && !(tableMessage[2].equals(groupChannel.getPassword()))){
                out.println("Le mot de passe est incorrect.");
                return 0;
            }
            groupChannel.addUser(requestingUser);
            out.println("Vous faites partie du groupe "+groupName);
            return 1;
        }catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
        
    }

    public static int doMessagGroup(List<Channels> channels, Users sendingUser, String message) {
        try {
            String[] tableMessage = message.split(" ");
            String groupName = tableMessage[1];
            tableMessage[0] = "";
            tableMessage[1] = "";
            Channels group = ClientHandler.getChannelByName(channels, groupName);
            if(group == null){
                PrintWriter out = new PrintWriter(sendingUser.getSocket().getOutputStream(), true);
                out.println("Le groupe "+groupName+" n'existe pas.");
                return 0;
            }
            if(!group.getChannelUsers().contains(sendingUser)){
                PrintWriter out = new PrintWriter(sendingUser.getSocket().getOutputStream(), true);
                out.println("Vous ne faite pas partie du groupe "+groupName+".");
                return 0;
            }
            for (Users otherClient : group.getChannelUsers()) {
                if ((otherClient.getSocket() != sendingUser.getSocket())) {
                    PrintWriter otherOut = new PrintWriter(otherClient.getSocket().getOutputStream(), true);
                    message= String.join(" ", tableMessage);
                    otherOut.println(groupName +" : " + sendingUser.getUsername() + " : " + message);
                }
            }
            return 1;
        }catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
        
    }

    public static int doExitGroup(List<Channels> channels, Users user, String message){
        try {
            PrintWriter out = new PrintWriter(user.getSocket().getOutputStream(), true);
            channels.get(1).updateOneUser(user);
            Channels Group=ClientHandler.getChannelByName(channels, message.split(" ")[1]);
            Group.removeBySocket(user.getSocket());
            if(Group.getChannelUsers().size() > 0){
                for(Users otherClient : Group.getChannelUsers()){
                PrintWriter otherOut = new PrintWriter(otherClient.getSocket().getOutputStream(), true);
                otherOut.println(user.getUsername() + " a quitté le groupe.");
                }
            }else{
                channels.remove(Group);
            }
            
            out.println("Vous avez quitter le groupe.");
            return 1;
                
        }catch (IOException e) {
        System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
        
    }
}
