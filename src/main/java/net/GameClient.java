package net;

import game.Game;

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

            // do something with the data

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
