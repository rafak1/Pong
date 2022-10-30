package net;

import game.Game;
import game.Player;
import game.objects.Ball;
import net.packets.BallSyncPacket;
import net.packets.MovePacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClass {
    DatagramSocket socket;
    InetAddress ipAdress;
    Game game;
    Player enemy;

    public void moveEnemy(MovePacket packet){
        enemy.getPlatform().atomicY.set ( packet.y);
        enemy.getPlatform().setY(packet.y);
    }

    public void syncBall(BallSyncPacket packet, Ball ball){
        ball.setX(packet.x);
        ball.setY(packet.y);
        ball.setAngle(packet.angle);
    }

    public void sendPacket(byte[] data, InetAddress ipAdress, DatagramSocket socket, int port){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAdress, port); //TODO change later
        try {
            System.out.println("sending packet ... ");
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getEnemy() {
        return enemy;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}