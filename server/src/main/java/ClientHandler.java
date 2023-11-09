import java.io.*;
import java.net.*;
import java.util.List;

import java.sql.Connection;

public class ClientHandler implements Runnable {
    private List<Channels> channels;
    private Connection connection;
    private Users user;

    public ClientHandler(Socket clientSocket, List<Channels> channels, Connection connection) {
        this.user = new Users(clientSocket, "", channels.get(0).getChannelName());
        this.channels = channels;
        this.connection = connection;
    }
    
    @Override
    public void run() {
        try (
            PrintWriter out = new PrintWriter(user.getSocket().getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(user.getSocket().getInputStream()))
        ) {
            String clientAddress = user.getSocket().getInetAddress().getHostAddress();
            System.out.println("Connecté au client : " + clientAddress);
            CommandHandler commandHandler = new CommandHandler();
            CommandHandler.doHelp(user.getSocket());

            while (true) {
                // Lire le message du client
                String message = in.readLine();
                int[] commandeCode = new int[2];
                if (message == null || message.isEmpty()) {
                    break;
                }
                if (message.charAt(0) == '/'){
                    commandeCode = commandHandler.getCommand(message, user, connection, channels);
                }
                if (commandeCode[0] == 0 && user.getUsername().length() > 0){
                    // Envoyer le message à tous les autres channels
                    String channelName = user.getCurrentChannel();
                    Channels channel =getChannelByName(channels, channelName);
                    for (Users otherClient : channel.getChannelUsers()) {
                        if ((otherClient.getSocket() != user.getSocket()) && otherClient.getCurrentChannel().equals(channelName)){
                            PrintWriter otherOut = new PrintWriter(otherClient.getSocket().getOutputStream(), true);
                            otherOut.println(user.getUsername() + ": " + message);
                        }
                    }
                }
            }
            System.out.println("Client déconnecté : " + clientAddress);
            for(Channels channel : channels){
                channel.removeBySocket(user.getSocket());
            }       
        }catch (IOException e) {
            System.err.println("Erreur lors de la communication avec le client : " + e.getMessage());
        }
        
    }
    
    public static Channels getChannelByName( List<Channels> channels ,String name){
        for(Channels channel : channels){
            if(channel.getChannelName().equals(name)){
                return channel;
            }
        }
        return null;
    }

}