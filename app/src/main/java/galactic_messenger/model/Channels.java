package galactic_messenger.model;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Channels {
    private List<Users> users;
    private String password;

    public Channels(List<Users> users, String password) {
        this.users = users;
        this.password = password;
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

    public List<String> getChannelUsernames() {
        List<String> usernames = new ArrayList<String>();
        for (Users user : users) {
            usernames.add(user.getUsename());
        }
        return usernames;
    }

    public Users getUserByUsername(String username) {
        for (Users user : users) {
            if (user.getUsename().equals(username)) {
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
        this.users.add(new Users(client, "", channel));
    }

    public void removeUser(Users user){
        this.users.remove(user);
    }

    public void removeBySocket(Socket client){
        this.users.removeIf(user -> user.getSocket() == client);
    }

    public String getPassword() {
        return password;
    }

}