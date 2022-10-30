package net;

import game.Game;
import game.Player;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import net.packets.*;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameServer extends SocketClass implements Runnable {
    private Player player;
    public Alert connectionAlert;
    public AtomicBoolean isConnected;

    public GameServer(String ip,  Game game){   //ip ?
        this.game = game;
        isConnected = new AtomicBoolean(false);
        try {
            ipAdress = InetAddress.getByName(ip);
            socket = new DatagramSocket(1331);
            player = new Player(this.ipAdress, 1331, this);
        }catch (Exception a){
            a.printStackTrace();
        }
        game.setPlayer(player);
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
                System.out.println("INVALID PACKET");
            }
            case LOGIN -> {
                packet = new LoginPacket(data);
                System.out.println(address.getHostAddress()+":" + port + " -> " + ((LoginPacket)packet).getUsername() + " connected");
                //TODO create a player

                enemy = new Player(address, port, this);
                enemy.setPlatform(game.getEnemyPlatform());

                LoginPacket loginPacket = new LoginPacket("server");
                loginPacket.sendData(this);
                isConnected.set(true);
                Platform.runLater( connectionAlert::close);

                //this.addConnection(player,(LoginPacket) packet);
            }
            case DISCONNECT ->{

            }
            case MOVE -> {
                packet = new MovePacket(data);
                moveEnemy((MovePacket) packet);
            }
        }
    }

    public void sendDataToAddress(byte[] data, InetAddress ipAddress, int port){
        sendPacket(data, ipAddress,socket,port);
    }

    public void sendDataToAllClients(byte[] data) {
        if(enemy!=null) sendDataToAddress(data, enemy.ipAddress, enemy.port);
    }
}
