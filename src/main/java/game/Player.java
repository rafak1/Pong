package game;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.example.pong.MainVariables;
import game.objects.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import net.GameServer;
import net.SocketClass;
import net.packets.MovePacket;

public class Player {
    public InetAddress ipAddress;
    public int port;
    String username;
    SocketClass socketClass;
    Platform platform;

    public Player(InetAddress ipAddress, int port, SocketClass socketClass){
        username = "player";
        this.socketClass = socketClass;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    /**
     * sets controllers foor both players
     * @param scene scene where those controls will work
     * @param player1 player with a platform facing right
     */
    public static void setSceneControllers(Scene scene, Player player1){
        scene.setOnKeyPressed(e -> {
            try {
                if(player1.ipAddress.equals(InetAddress.getByName("localhost")) && player1.socketClass.getClass() == GameServer.class){
                    if (e.getCode() == KeyCode.W) {
                        if (player1.platform.atomicY.get() > 0)
                            player1.platform.atomicY.set(player1.platform.atomicY.get() - player1.platform.getSpeed());
                    }
                    if (e.getCode() == KeyCode.S) {
                        if (player1.platform.atomicY.get() + 150 < MainVariables.sizeY)
                            player1.platform.atomicY.set(player1.platform.atomicY.get() + player1.platform.getSpeed());
                    }
                }else {
                    if (e.getCode() == KeyCode.UP) {
                        if (player1.platform.atomicY.get() > 0)
                            player1.platform.atomicY.set(player1.platform.atomicY.get() - player1.platform.getSpeed());
                    }
                    if (e.getCode() == KeyCode.DOWN) {
                        if (player1.platform.atomicY.get() + 150 < MainVariables.sizeY)
                            player1.platform.atomicY.set(player1.platform.atomicY.get() + player1.platform.getSpeed());
                    }
                }
                MovePacket packet = new MovePacket(player1.username, player1.platform.atomicY.get());
                packet.sendData(player1.socketClass);
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            }
        });
    }

    public Platform getPlatform() {
        return platform;
    }
}
