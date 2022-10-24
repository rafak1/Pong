package net.packets;

import net.GameClient;
import net.GameServer;

public abstract class Packet {

    public static enum PacketTypes{
        INVALID(-1), LOGIN(10), DISCONNECT(11);

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

    public abstract String getData();

    public String readData(byte[] data){
        String dataMessage = new String(data).trim();
        return dataMessage.substring(2);
    }
}
