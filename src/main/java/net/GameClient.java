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

public class GameClient extends SocketClass implements Runnable{


    private Player player;
    public Alert connectionAlert;
    public AtomicBoolean isConnected;

    public GameClient(String ip, Game game, String username){
        this.game = game;
        isConnected = new AtomicBoolean(false);
        try {
            socket = new DatagramSocket();
            ipAdress = InetAddress.getByName(ip);
            player = new Player(username, ipAdress, 1133, this);
        }catch (Exception a){
            a.printStackTrace();
        }
        game.setPlayer(player);
    }

    @Override
    public void run() {
        while(game.isRunning.get()){
            byte[] data = new byte[1024];
            DatagramPacket newPacket = new DatagramPacket(data, data.length);

            try {
                socket.receive(newPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            parsePacket(newPacket.getData(), newPacket.getAddress(), newPacket.getPort());

        }
        socket.close();
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        Packet.PacketTypes type = Packet.lookupPacket(new String(data).trim().substring(0,2));
        Packet packet;
        switch (type){
            case INVALID -> {
                System.out.println("INVALID PACKET");
            }
            case LOGIN -> {
                packet = new LoginPacket(data);
                System.out.println(address.getHostAddress()+":" + port + " -> " + ((LoginPacket)packet).getUsername() + " has joined");

                isConnected.set(true);
                Platform.runLater( connectionAlert::close);


                enemy = new Player(((LoginPacket) packet).getUsername(), address, port, this);
                enemy.setPlatform(game.getEnemyPlatform());
            }
            case DISCONNECT ->{
                packet = new DisconnectPacket(data);
                game.stop();
                Platform.runLater(()-> confirmationAlert(((DisconnectPacket) packet).getUsername() + " has disconnected", "Player Disconnected").showAndWait() );
                Thread.currentThread().interrupt();
            }
            case MOVE -> {
                packet = new MovePacket(data);
                moveEnemy((MovePacket) packet);
            }
            case BALLSYNC -> {
                packet = new BallSyncPacket(data);
                syncBall((BallSyncPacket) packet, game.getBall());
            }
            case POINTSSYNC -> {
                packet = new PointSyncPacket(data);
                game.reset();
                game.changeScore(((PointSyncPacket) packet).score1, ((PointSyncPacket) packet).score2);
            }
        }
    }


    public void sendDataToServer(byte[] data){
        sendPacket(data, ipAdress,socket, 1331);
    }
}
