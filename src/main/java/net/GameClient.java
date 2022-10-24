package net;

import game.Game;
import game.Player;
import net.packets.LoginPacket;
import net.packets.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GameClient implements Runnable{

    private InetAddress ipAdress;
    private DatagramSocket socket;
    private Game game;

    public GameClient(String ip, Game game){
        this.game = game;
        try {
            socket = new DatagramSocket();
            ipAdress = InetAddress.getByName(ip);
        }catch (Exception a){
            a.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            byte[] data = new byte[1024];
            DatagramPacket newPacket = new DatagramPacket(data, data.length);

            try {
                socket.receive(newPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            parsePacket(newPacket.getData(), newPacket.getAddress(), newPacket.getPort());
            // do something with the data

        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        Packet.PacketTypes type = Packet.lookupPacket(new String(data).trim().substring(0,2));
        Packet packet;
        switch (type){
            default -> {
            }
            case INVALID -> {
            }
            case LOGIN -> {
                packet = new LoginPacket(data);
                System.out.println(address.getHostAddress()+":" + port + " -> " + ((LoginPacket)packet).getUsername() + " has joined");
                //TODO create a player
                Player player = new Player(address, port);
                //TODO add to the game
            }
            case DISCONNECT ->{

            }
        }
    }


    public void sendDataToServer(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAdress, 1331); //TODO change later
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
