package game;

import com.example.pong.Menu;
import game.objects.Ball;
import game.objects.ObjectAnimator;
import game.objects.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
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
    private ObjectAnimator animator;
    private Platform platform1;
    private Platform platform2;
    private int ballSpeed;

    public Game(){
        root = new Group();
        Canvas canvas = new Canvas(sizeX, sizeY);
        root.getChildren().add(canvas);

        //ball
        ballSpeed = 4;
        ImageView ballView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/graphics/ball.png")).toString(),20,20,true,true));
        ballView.setY(sizeY/2);
        ballView.setX(sizeX/2);
        ball = new Ball(sizeX/2, sizeY/2,ballView, ballSpeed);
        root.getChildren().add(ballView);



        //platforms
        ImageView platformView1 = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/graphics/table.png")).toString(),20,150,true,true));
        platformView1.setX(sizeX/10);
        platformView1.setY(sizeY/2 - 75);
        ImageView platformView2 = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/graphics/table.png")).toString(),20,150,true,true));
        platformView2.setX((sizeX - sizeX/10));
        platformView2.setY(sizeY/2 - 75);
        Player player1 = null;
        Player player2 = null;
        platform1 = new Platform((sizeX/10),sizeY/2 - 75, platformView1, 10, true);
        platform2 = new Platform((sizeX - sizeX/10),sizeY/2 - 75, platformView2, 10, false);
       //player1.setPaltform(platform1);
        //player2.setPaltform(platform2); todo
        root.getChildren().add(platformView1);
        root.getChildren().add(platformView2);

        animator = new ObjectAnimator(ball);
        animator.addPlatform(platform1);
        animator.addPlatform(platform2);
    }

    @Override
    public void run() {
        Thread animationThread = new Thread(animator);
        animationThread.setDaemon(true);
        animationThread.start();
        boolean reflected= false;
        while(true){
            try {
                Thread.sleep(6);
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
                if(ball.getX() <=platform1.getX() + ballSpeed && ball.getX() >= platform1.getX()){
                    ball.platformReflect(platform1);
                    reflected = true;
                }
                if(ball.getX() <=platform2.getX() + ballSpeed && ball.getX() >= platform2.getX()){
                    ball.platformReflect(platform2);
                    reflected = true;
                }
            }else{
                if(ball.getY()>= 0 && ball.getY() <= sizeY-50-ballSpeed){
                    reflected = false;
                }
            }

            //game ended
            if(ball.getX() <= 0){
                animationThread.interrupt();
                stop();
            }
            if(ball.getX() >= sizeX){
                animationThread.interrupt();
                stop();
            }
        }
    }

    public void stop(){
        //todo
    }


    public void show(Scene scene){
        scene.setRoot(root);
        scene.setFill(Color.BLACK);
        Platform.setSceneControllers(scene, platform1, platform2);
        gamethread = new Thread(this);
        gamethread.setDaemon(true);
        gamethread.start();
    }
}
