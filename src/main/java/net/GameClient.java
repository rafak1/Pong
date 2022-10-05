package net;

import game.Game;

import java.net.DatagramSocket;
import java.net.Inet4Address;

public class GameClient implements Runnable{

    private Inet4Address ipAdress;
    private DatagramSocket socket;
    private Game game;

    public GameClient(){

    }

    @Override
    public void run() {

    }
}
