package game;

import game.objects.Ball;
import game.objects.BallAnimator;
import game.objects.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.GameClient;
import net.GameServer;

import java.util.Objects;

import static com.example.pong.MainVariables.*;

public class Game implements Runnable{
    private GameClient gameClient;
    private GameServer gameServer;
    private Group root;
    private Thread gamethread;

    private Ball ball;
    private BallAnimator ballAnimator;
    private Platform platform1;
    private Platform platform2;
    private int ballSpeed;

    public Game(){
        root = new Group();
        Canvas canvas = new Canvas(sizeX, sizeY);
        root.getChildren().add(canvas);

        //ball
        ballSpeed = 10;
        ImageView ballView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/graphics/ball.png")).toString(),20,20,true,true));
        ballView.setY(sizeY/2);
        ballView.setX(sizeX/2);
        ball = new Ball(sizeX/2, sizeY/2,ballView, ballSpeed);
        root.getChildren().add(ballView);
        ballAnimator = new BallAnimator(ball);


        //platforms
        ImageView platformView1 = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/graphics/table.png")).toString(),20,150,true,true));
        platformView1.setX(sizeX/10);
        platformView1.setY(sizeY/2 - 75);
        ImageView platformView2 = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/graphics/table.png")).toString(),20,150,true,true));
        platformView2.setX((sizeX - sizeX/10));
        platformView2.setY(sizeY/2 - 75);
        platform1 = new Platform(sizeX/5,sizeY/2, platformView1);
        platform2 = new Platform((sizeX - sizeX/5),sizeY/2, platformView2);
        root.getChildren().add(platformView1);
        root.getChildren().add(platformView2);
    }

    @Override
    public void run() {
        Thread ballAnimation = new Thread(ballAnimator);
        ballAnimation.setDaemon(true);
        ballAnimation.start();
        boolean reflected= false;
        while(true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            //refelctions
            if(!reflected) {
                if (ball.getY() >= -ballSpeed && ball.getY() <= 0) {
                    double a = ball.angle - 90;
                    ball.reflect(2 * (ball.angle - 2 * a));
                    reflected = true;
                }
                if (ball.getY() >= sizeY - 50 - ballSpeed && ball.getY() <= sizeY) {
                    double a = ball.angle - 90;
                    ball.reflect(2 * (ball.angle - 2 * a));
                    reflected = true;
                }
            }else{
                if(ball.getY()>= 0 && ball.getY() <= sizeY-50-ballSpeed){
                    reflected = false;
                }
            }

            //game ended
            if(ball.getX() <= 0){
                ballAnimation.interrupt();
                stop();
            }
            if(ball.getX() >= sizeX){
                ballAnimation.interrupt();
                stop();
            }
        }

    }

    public void stop(){

    }


    public void show(Scene scene){
        scene.setRoot(root);
        scene.setFill(Color.BLACK);
        gamethread = new Thread(this);
        gamethread.setDaemon(true);
        gamethread.start();
    }
}
