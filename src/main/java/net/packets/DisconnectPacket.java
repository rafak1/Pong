package net.packets;

import net.GameClient;
import net.GameServer;

public class DisconnectPacket extends Packet{
    private String username;


    public DisconnectPacket(byte[] username) {
        super(11);
        this.username = readData(username);
    }
    public DisconnectPacket(String username) {
        super(11);
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public void sendData(GameClient client) {
        client.sendDataToServer(getData());
    }

    @Override
    public void sendData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {   //send data
        return ("11" + this.username).getBytes();
    }
}
