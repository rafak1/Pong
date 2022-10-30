package game.objects;

import com.example.pong.App;
import com.example.pong.MainVariables;
import com.example.pong.Menu;
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
        return (linearA*ballY + linearB)%360;
    }


    public int getSpeed() {
        return speed;
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }
}
