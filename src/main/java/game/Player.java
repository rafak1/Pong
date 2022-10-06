package game;

import java.net.InetAddress;

public class Player {
    public InetAddress ipAddress;
    public int port;

    public Player(InetAddress ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
    }
}
