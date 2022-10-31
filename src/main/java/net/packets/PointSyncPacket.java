package net.packets;

import com.example.pong.MainVariables;
import net.GameClient;
import net.GameServer;

public class PointSyncPacket extends Packet{
    public int score1;
    public int score2;
    public PointSyncPacket(byte[] data) {
        super(14);
        String[] dataArray = readData(data).split(",");
        this.score1 = Integer.parseInt(dataArray[0]);
        this.score2 = Integer.parseInt(dataArray[1]);
    }

    public PointSyncPacket( int x, int y){
        super(14);
        this.score1 = x;
        this.score2 = y;
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
        return (14 + "" + this.score1 + "," + this.score2).getBytes();
    }
}
