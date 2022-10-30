package net.packets;

import net.GameClient;
import net.GameServer;

public class MovePacket extends Packet{

    public String username;
    public double y;

    public MovePacket(byte[] data) {
        super(12);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.y = Double.parseDouble(dataArray[1]);
    }


    public MovePacket(String username, double y){
        super(12);
        this.username = username;
        this.y = y;
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
    public byte[] getData() {
        return (12 + this.username + "," + this.y).getBytes();
    }
}