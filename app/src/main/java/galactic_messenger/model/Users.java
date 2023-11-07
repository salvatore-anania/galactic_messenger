package galactic_messenger.model;

import java.net.Socket;



public class Users {
    private Socket client;
    private String username;
    private Channels currentChannel;

    public Users(Socket client, String username, Channels currentChannel) {
        this.client = client;
        this.username = username;
        this.currentChannel = currentChannel;
    }

    public Socket getSocket() {
        return client;
    }

    public String getUsename() {
        return username;
    }

    public Channels getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(Channels currentChannel) {
        this.currentChannel = currentChannel;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
