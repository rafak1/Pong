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
import javafx.stage.Stage;
import net.SocketClass;
import net.packets.BallSyncPacket;

import java.util.Objects;

import static com.example.pong.MainVariables.*;

public class Game implements Runnable{
    private Group root;
    private Thread gamethread;

    private Ball ball;
    private ObjectAnimator animator;
    private Platform platform;
    private Platform enemyPlatform;
    private Player player;
    private int ballSpeed;
    private Menu menu;
    private Scene scene;
    private Stage stage;
    private boolean isServer;
    private SocketClass socketClass;

    public Game(Menu menu, Stage stage){
        this.stage = stage;
        this.menu = menu;
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
        platform = new Platform((sizeX/10),sizeY/2 - 75, platformView1, 10, true);
        enemyPlatform = new Platform((sizeX - sizeX/10),sizeY/2 - 75, platformView2, 10, false);
        root.getChildren().add(platformView1);
        root.getChildren().add(platformView2);

        animator = new ObjectAnimator(ball);
        animator.addPlatform(platform);
        animator.addPlatform(enemyPlatform);
    }

    @Override
    public void run() {
        int counter = 0;
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
            //ball synchronization
            if(isServer) {
                if (counter == 3) {
                    BallSyncPacket packet = new BallSyncPacket(ball.getX(), ball.getY(), ball.angle);
                    packet.sendData(socketClass);
                    counter = 0;
                } else counter++;
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
                if(ball.getX() <= platform.getX() + ballSpeed && ball.getX() >= platform.getX()){
                    ball.platformReflect(platform);
                    reflected = true;
                }
                if(ball.getX() <= enemyPlatform.getX() + ballSpeed && ball.getX() >= enemyPlatform.getX()){
                    ball.platformReflect(enemyPlatform);
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
        if(gamethread.isAlive()) gamethread.interrupt();
        javafx.application.Platform.runLater(()->  menu = new Menu(stage));
        scene.setRoot(menu.getMenuRoot());
    }


    public void show(Scene scene){
        this.scene = scene;
        scene.setRoot(root);
        scene.setFill(Color.BLACK);
        gamethread = new Thread(this);
        gamethread.setDaemon(true);
        Player.setSceneControllers(scene, player);
        gamethread.start();
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        if(isServer) player.setPlatform(enemyPlatform);
        else player.setPlatform(platform);
        this.player = player;
    }

    public Platform getEnemyPlatform() {
        if(isServer) return platform;
        else return enemyPlatform;
    }

    public void setIsServer(boolean server) {
        isServer = server;
    }

    public boolean isServer() {
        return isServer;
    }

    public void setSocketClass(SocketClass socketClass) {
        this.socketClass = socketClass;
    }

    public Ball getBall() {
        return ball;
    }
}
