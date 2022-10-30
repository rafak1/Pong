package net.packets;

import net.GameClient;
import net.GameServer;
import net.SocketClass;

public abstract class Packet {

    public static enum PacketTypes{
        INVALID(-1), LOGIN(10), DISCONNECT(11), MOVE(12), BALLSYNC(13);

        private int packetId;
        private PacketTypes(int id){
            this.packetId = id;
        }
        public int getPacketId(){
            return  packetId;
        }
    }

    public static PacketTypes lookupPacket(String id){
        try {
            int packetId = Integer.parseInt(id);
            System.out.println(packetId + " -packetid");
            for(PacketTypes p : PacketTypes.values()){
                if(p.getPacketId() == packetId){
                    return p;
                }
            }
            return  PacketTypes.INVALID;
        }catch(NumberFormatException a){
            return PacketTypes.INVALID;
        }
    }

    public byte packetId;

    public Packet(int id){
        this.packetId = (byte) id;
    }

    public abstract void sendData(GameClient client);

    public abstract void sendData(GameServer server);   //send to all clients connected to the server

    public void sendData(SocketClass socketClass){
        socketClass.sendPacket(getData(), socketClass.getEnemy().ipAddress , socketClass.getSocket(), socketClass.getEnemy().port);
    }

    public abstract byte[] getData();

    public String readData(byte[] data){
        String dataMessage = new String(data).trim();
        return dataMessage.substring(2);
    }
}
