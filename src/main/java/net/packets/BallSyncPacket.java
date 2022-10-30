package net.packets;

import net.GameClient;
import net.GameServer;

public class BallSyncPacket extends Packet{
    public double y;
    public double x;
    public double angle;

    public BallSyncPacket(byte[] data) {
        super(13);
        String[] dataArray = readData(data).split(",");
        this.x = Double.parseDouble(dataArray[0]);
        this.y = Double.parseDouble(dataArray[1]);
        this.angle = Double.parseDouble(dataArray[2]);
    }

    public BallSyncPacket( double x, double y, double angle){
        super(13);
        this.x = x;
        this.y = y;
        this.angle = angle;
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
        return (13 + "" + this.x + "," + this.y + "," + this.angle).getBytes();
    }
}
