package game;

import game.objects.Ball;
import game.objects.BallAnimator;
import game.objects.Platform;
import javafx.scene.image.ImageView;
import net.GameClient;
import net.GameServer;

import static com.example.pong.MainVariables.sizeX;
import static com.example.pong.MainVariables.sizeY;

public class Game implements Runnable{
    private GameClient gameClient;
    private GameServer gameServer;

    Ball ball;
    BallAnimator ballAnimator;
    Platform platform1;
    Platform platform2;

    public Game(){
        //ImageView ball = TODO
        ball = new Ball(sizeX/2, sizeY/2, null, 10);
        platform1 = new Platform(sizeX/5,sizeY/2);
        platform2 = new Platform((sizeX - sizeX/5),sizeY/2);
        ballAnimator = new BallAnimator(ball);
    }

    @Override
    public void run() {
        Thread ballAnimation = new Thread(ballAnimator);
        ballAnimation.start();
        while(true){

            //refelctions
            if(ball.getY() <= 0 ){
                ball.reflect(90);
            }
            if(ball.getX() >= sizeY){
                ball.reflect(-90);
            }

            //game ended
            if(ball.getX() == 0){
                ballAnimation.interrupt();
                stop();
            }
            if(ball.getX() == sizeX){
                ballAnimation.interrupt();
                stop();
            }



        }

    }

    public void stop(){

    }


    public void show(){

    }
}
