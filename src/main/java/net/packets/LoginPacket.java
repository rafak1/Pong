package net.packets;

import net.GameClient;
import net.GameServer;

public class LoginPacket extends Packet {

    private String username;


    public LoginPacket(byte[] username) {
        super(10);
        this.username = readData(username);
    }
    public LoginPacket(String username) {
        super(10);
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public void sendData(GameClient client) {
        client.sendDataToServer(getData().getBytes());
    }

    @Override
    public void sendData(GameServer server) {
        server.sendDataToAllClients(getData().getBytes());
    }

    @Override
    public String getData() {   //send data
        return ("10" + this.username);
    }
}
