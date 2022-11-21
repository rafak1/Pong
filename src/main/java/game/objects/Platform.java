package game.objects;

import com.example.pong.App;
import com.example.pong.MainVariables;
import com.example.pong.Menu;
import game.Player;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.nio.DoubleBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Platform extends GameObject{

    boolean active;
    final int speed;
    final boolean isFacingRight;
    public AtomicReference<Double> atomicY;
    private Player owner;


    /**
     *  creates a platform
     * @param x x position at start
     * @param y y position at start
     * @param imageView imageView of the platform
     * @param speed speed of the platform
     * @param isFacingRight is the "reflective surface" of the platform facing right
     */
    public Platform(double x, double y, ImageView imageView,int speed, boolean isFacingRight) {
        super(x, y, imageView);
        this.isFacingRight = isFacingRight;
        this.speed = speed;
        atomicY =new AtomicReference<Double>(y);
    }

    public double calculateReflectionAngle(double ballY){
        double linearA;
        double linearB;
        double up = y;
        double down = up+150;
        if(isFacingRight){
            linearA = 180/(up-down);
            linearB = -90*(down+up)/(up-down);
        }else{
            linearA = 180/(down-up);
            linearB = 90 - (linearA*up);
        }
        double newAngle = (linearA*ballY + linearB)%360;
        if(isFacingRight) {
            if (newAngle > 80 && newAngle <= 90) return 80;
            else if (newAngle >= 270 && newAngle < 280) return 280;
        }else {
             if (newAngle >= 90 && newAngle < 100) return 100;
            else if(newAngle <280 && newAngle >=270) return 280;
        }
        return newAngle;
    }


    public int getSpeed() {
        return speed;
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
