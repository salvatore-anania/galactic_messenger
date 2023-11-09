import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class ConnexionHandler {

    public static int doRegister(String message, Connection connection,Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            // Utilisez une requête paramétrée pour insérer des données
                try{
                    String sql = "INSERT INTO users (name, password) VALUES (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    MessageDigest digest;
                    byte[] encodedhash=null;
                    try {
                        digest = MessageDigest.getInstance("SHA-256");
                        encodedhash= digest.digest( message.split(" ")[2].getBytes(StandardCharsets.UTF_8));
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        out.println("Register failed");
                        return 0;
                    }
                    
                    preparedStatement.setString(1, message.split(" ")[1]);
                    preparedStatement.setString(2, bytesToHex(encodedhash));
                    // Exécutez la requête
                    preparedStatement.execute();
                    // Fermez la déclaration et la connexion
                    preparedStatement.close();
                    out.println("Register success");
                    return 1;
                } catch (SQLException e) {
                    e.printStackTrace();
                    out.println("Register failed");
                } 
        }catch (IOException e) {
        System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
    }

    public static int doLogin(List<Channels> channels, String message, Connection connection,Users user) {
        try {
            PrintWriter out = new PrintWriter(user.getSocket().getOutputStream(), true);
            try{
                // Utilisez une requête paramétrée pour insérer des données
                String sql2 = "SELECT * FROM users";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql2);
                MessageDigest digest;
                byte[] encodedhash=null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                    encodedhash= digest.digest( message.split(" ")[2].getBytes(StandardCharsets.UTF_8));
                    while (resultSet.next()) {
        
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("name");
                    if(name.equals(message.split(" ")[1]) && password.equals(bytesToHex(encodedhash))){
                        user.setUsername(message.split(" ")[1]);
                        channels.get(0).removeBySocket(user.getSocket());
                        channels.get(1).addUser(new Users(user.getSocket(), user.getUsername(), channels.get(1).getChannelName()));
                        user.setCurrentChannel(channels.get(1).getChannelName());                    
                        out.println("Vous êtes connecté en tant que " + user.getUsername());
                        return 1;
                    }
                }
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    out.println("Register failed");
                    return 0;
                }
                // Fermez la déclaration et la connexion
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("Login failed");
            }
        }catch (IOException e) {
        System.err.println("Erreur lors de l'envoi du message au client : " + e.getMessage());
        }
        return 0;
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
