import java.net.Socket;



public class Users {
    private Socket client;
    private String username;
    private String currentChannel;

    public Users(Socket client, String username, String currentChannel) {
        this.client = client;
        this.username = username;
        this.currentChannel = currentChannel;
    }

    public Socket getSocket() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public String getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
