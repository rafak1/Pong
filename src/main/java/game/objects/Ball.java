package game.objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.pong.MainVariables.ratioXY;
import static com.example.pong.MainVariables.sizeX;

public class Ball extends GameObject{

    public double angle;
    private double speed;

    public Ball(double x, double y, ImageView image, double speed) {
        super(x, y, image);
        angle = 89;
        this.speed = speed;
    }

    /**
     * changes angle
     * @param r angle in question
     */
    public void reflect(double r){
        angle = (angle + r) % 360;
    }

    /**
     * moves along the angle
     */
    public void moveBall(){
        double sin = Math.sin(Math.toRadians(angle));
        double cos = Math.cos(Math.toRadians(angle));
        double c = speed * sizeX/1000;
        double changeX = c * cos;
        double changeY = c * sin;
        move(changeX, -changeY);
    }
}