package game;

import java.net.InetAddress;

import com.example.pong.MainVariables;
import game.objects.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class Player {
    public InetAddress ipAddress;
    public int port;
    Platform paltform;

    public Player(InetAddress ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void setPaltform(Platform paltform) {
        this.paltform = paltform;
    }

    /**
     * sets controllers foor both players
     * @param scene scene where those controls will work
     * @param player1 player with a platform facing right
     * @param player2 the other player
     */
    public static void setSceneControllers(Scene scene, Player player1, Player player2){
        if(!player1.paltform.isFacingRight()){
            throw new IllegalArgumentException();
        }
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                if (player1.paltform.atomicY.get() > 0) player1.paltform.atomicY.set(player1.paltform.atomicY.get() - player1.paltform.getSpeed());
            }
            if (e.getCode() == KeyCode.DOWN) {
                if (player1.paltform.atomicY.get() + 150 < MainVariables.sizeY) player1.paltform.atomicY.set(player1.paltform.atomicY.get() + player1.paltform.getSpeed());
            }
            if (e.getCode() == KeyCode.W) {
                if (player2.paltform.atomicY.get() > 0) player2.paltform.atomicY.set(player2.paltform.atomicY.get() - player2.paltform.getSpeed());
            }
            if (e.getCode() == KeyCode.S) {
                if (player2.paltform.atomicY.get() + 150 < MainVariables.sizeY) player2.paltform.atomicY.set(player2.paltform.atomicY.get() + player2.paltform.getSpeed());
            }
        });
    }
}
