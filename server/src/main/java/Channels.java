import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Channels {
    private List<Users> users;
    private String password;
    private String name;

    public Channels(List<Users> users, String password, String name) {
        this.users = users;
        this.password = password;
        this.name = name;
    }

    public List<Users> getChannelUsers() {
        List<Users> clients = new ArrayList<Users>();
        for (Users user : users) {
            clients.add(user);
        }
        return clients;
    }

    public List<Socket> getChannelClients() {
        List<Socket> clients = new ArrayList<Socket>();
        for (Users user : users) {
            clients.add(user.getSocket());
        }
        return clients;
    }

    public void setChannelUsers(List<Users> users) {
        this.users = users;
    }

    public String getChannelName() {
        return this.name;
    }

    public List<String> getChannelUsernames() {
        List<String> usernames = new ArrayList<String>();
        for (Users user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    public Users getUserByUsername(String username) {
        for (Users user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public Users getSocketBySocket(Socket client) {
        for (Users user : users) {
            if (user.getSocket() == client) {
                return user;
            }
        }
        return null;
    }

    public void addUser(Users client){
        this.users.add(client);
    }

    public void addUserUnlog(Socket client,Channels channel){
        this.users.add(new Users(client, "", channel.getChannelName()));
    }

    public void removeBySocket(Socket client){
        this.users.removeIf(user -> user.getSocket() == client);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void updateOneUser(Users user){
        for(Users userChannel : users){
            if(userChannel.getSocket() == user.getSocket()){
                userChannel.setCurrentChannel(user.getCurrentChannel());
                userChannel.setUsername(user.getUsername());
            }
        }
    }

    public void updateUsers(List<Users> usersList){
        for(Users user : usersList){
            for(Users userChannel : users){
                if(userChannel.getSocket() == user.getSocket()){
                    userChannel.setCurrentChannel(user.getCurrentChannel());
                }
            }
        }
    }

}