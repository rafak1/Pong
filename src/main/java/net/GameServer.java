package net;

import game.Game;
import game.Player;
import net.packets.*;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class GameServer implements Runnable{

    private DatagramSocket socket;
    private Game game;
    private List<Player> connectedPlayers = new ArrayList<Player>();

    public GameServer(String ip, Game game){
        this.game = game;
        try {
            socket = new DatagramSocket(1331);
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

            // do something with the data
            parsePacket(newPacket.getData(), newPacket.getAddress(), newPacket.getPort());

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
                System.out.println(address.getHostAddress()+":" + port + " -> " + ((LoginPacket)packet).getUsername() + " connected");
                //TODO create a player
                Player player = new Player(address, port);
                this.addConnection(player,(LoginPacket) packet);
            }
            case DISCONNECT ->{

            }
        }
    }

    private void addConnection(Player player, LoginPacket packet) {
        //test
        this.connectedPlayers.add(player);
        packet.sendData(this);//?
        //TODO add player to the game
    }

    public void sendDataToAddress(byte[] data, InetAddress ipAddress, int port){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress , port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for(Player p : connectedPlayers){
            sendDataToAddress(data, p.ipAddress, p.port);
        }
    }
}
