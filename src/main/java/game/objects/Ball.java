package game.objects;

import javafx.scene.image.ImageView;

import static com.example.pong.MainVariables.ratioXY;
import static com.example.pong.MainVariables.sizeX;

public class Ball extends GameObject{

    private double angle;
    private ImageView image;
    private int speed;

    public Ball(double x, double y, ImageView image, int speed) {
        super(x, y);
        angle = 0;
        this.image = image;
        this.speed = speed;
    }

    /**
     * changes angle
     * @param r angle in question (can be multiplexed by (-1) depending on current angle)
     */
    public void reflect(double r){
        if(angle > 90 && angle <= 270){
            angle = (angle - r) % 360;
        }else{
            angle = (angle + r) % 360;
        }
    }

    public ImageView getImageView(){
        return image;
    }

    /**
     * moves along the angle
     */
    public void moveBall(){
        double tan = Math.tan(Math.toRadians(angle));
        double changeX = speed * sizeX/1000;
        double changeY = changeX * tan;
        move(changeX, changeY);
    }

}