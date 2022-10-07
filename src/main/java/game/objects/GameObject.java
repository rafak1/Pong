package game.objects;

public class GameObject {
    private double x;
    private double y;



    public GameObject(double x, double y){
        this.x = x;
        this.y = y;
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

    public double getY(){
        return y;
    }
}
