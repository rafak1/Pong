package net;

import game.Game;
import game.Player;

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

        }
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
