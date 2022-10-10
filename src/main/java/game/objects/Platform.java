package game.objects;

import com.example.pong.App;
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
    public AtomicReference<Double> atomicY;

    public Platform(double x, double y, ImageView imageView,int speed, Scene scene) {
        super(x, y, imageView);
        this.speed = speed;
        atomicY =new AtomicReference<Double>(y);
        active = false;
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP ) {
                atomicY.set(atomicY.get() + speed);
            }
            if(e.getCode() == KeyCode.DOWN){
                atomicY.set(atomicY.get() - speed);
            }
        });
    }

    public void setActive(){
        active = true;
    }

}
