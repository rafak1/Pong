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

    /**
     * Sets keyboard controls for platform
     * @param scene scene where EventHandler will be placed
     */
    public static void setSceneControllers(Scene scene, Platform platform1, Platform platform2){
        scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.UP) {
                    if (platform1.atomicY.get() > 0) platform1.atomicY.set(platform1.atomicY.get() - platform1.speed);
                }
                if (e.getCode() == KeyCode.DOWN) {
                    if (platform1.atomicY.get() + 150 < MainVariables.sizeY) platform1.atomicY.set(platform1.atomicY.get() + platform1.speed);
                }
                if (e.getCode() == KeyCode.W) {
                    if (platform2.atomicY.get() > 0) platform2.atomicY.set(platform2.atomicY.get() - platform2.speed);
                }
                if (e.getCode() == KeyCode.S) {
                    if (platform2.atomicY.get() + 150 < MainVariables.sizeY) platform2.atomicY.set(platform2.atomicY.get() + platform2.speed);
                }
        });
    }

    public double calculateReflectionAngle(double ballY){
        double up = y;
        double down = up+150;
        double a = 180/(up-down);
        double b = -90*(down+up)/(up-down);
        if(isFacingRight){
            return (a*ballY + b)%360;
        }else{
            a*=2;
            b= b*4+180;
            return (a*ballY + b)%360;
        }
    }



    public boolean isFacingRight() {
        return isFacingRight;
    }
}
