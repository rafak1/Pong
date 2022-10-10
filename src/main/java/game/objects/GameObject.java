package game.objects;

import javafx.scene.image.ImageView;

public class GameObject {
    double x;
    double y;
    ImageView imageView;



    public GameObject(double x, double y, ImageView imageView){
        this.x = x;
        this.y = y;
        this.imageView = imageView;
    }

    public void resize(){

    }

    public void move(double x, double y){
     this.x +=x;
     this.y +=y;
    }

    public double getX(){
        return x;
    }

    public void setY(double a){
        y=a;
    }

    public double getY(){
        return y;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
