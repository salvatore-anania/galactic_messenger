import java.io.*;
import java.net.*;
import java.util.List;

import java.sql.Connection;

public class ClientHandler implements Runnable {
    private List<Channels> channels;
    private Connection connection;
    private Users user;

    public ClientHandler(Socket clientSocket, List<Channels> channels, Connection connection) {
        this.user = new Users(clientSocket, "", channels.get(0));
        this.channels = channels;
        this.connection = connection;
    }
    public String getSocketname(){
        return this.user.getUsename();
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
                if (commandeCode[0] == 0 && user.getUsename().length() > 0){
                    // Envoyer le message à tous les autres channels
                    for (Users otherClient : user.getCurrentChannel().getChannelUsers()) {
                        if (otherClient.getSocket() != user.getSocket()) {
                            PrintWriter otherOut = new PrintWriter(otherClient.getSocket().getOutputStream(), true);
                            otherOut.println(user.getUsename() + ": " + message);
                        }
                    }
                }else if(commandeCode[0] == 3 && commandeCode[1] == 1){
                    user.setUsername(message.split(" ")[1]);
                    channels.get(0).removeBySocket(user.getSocket());
                    channels.get(1).addUser(new Users(user.getSocket(), user.getUsename(), channels.get(1)));
                    user.setCurrentChannel(channels.get(1));                    
                    out.println("Vous êtes connecté en tant que " + user.getUsename());
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
}