package game;

import com.example.pong.Menu;
import game.objects.Ball;
import game.objects.ObjectAnimator;
import game.objects.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import net.SocketClass;
import net.packets.BallSyncPacket;
import net.packets.MovePacket;
import net.packets.PointSyncPacket;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.pong.MainVariables.*;
import static net.SocketClass.confirmationAlert;

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
    public AtomicBoolean isRunning;
    private SocketClass socketClass;
    private Label score;
    private AtomicInteger score1;
    private AtomicInteger score2;
    private Thread animationThread;

    public Game(Menu menu, Stage stage){
        isRunning = new AtomicBoolean(true);
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

        //text
        score = new Label();
        score1 = new AtomicInteger(0);
        score2 = new AtomicInteger(0);
        score.setText(score1.get() + " : " + score2.get());
        score.setFont(Font.font("Verdana", FontWeight.BOLD, sizeX * 0.037));
        score.setLayoutX(sizeX * 0.45);
        score.setLayoutY(sizeY * 0.05);
        root.getChildren().add(score);




        animator = new ObjectAnimator(ball);
        animator.addPlatform(platform);
        animator.addPlatform(enemyPlatform);
    }

    @Override
    public void run() {
        int counter = 0;
        int speedCounter=0;
        animationThread = new Thread(animator);
        animationThread.setDaemon(true);
        animationThread.start();
        boolean reflected= false;
        while(true){
            try {
                Thread.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            //ball synchronization
            if(isServer) {
                if (counter == 4) {
                    BallSyncPacket packet = new BallSyncPacket(ball.getX(), ball.getY(), ball.angle);
                    packet.sendData(socketClass);
                    counter = 0;
                } else counter++;
            }
            if(speedCounter == 2500){
                ball.setSpeed(ball.getSpeed() + 0.25);
                speedCounter=0;
            }else speedCounter++;
            //refelctions
            if(!reflected) {
                if (ball.getY() >= -ball.getSpeed() && ball.getY() <= 0) {
                    double a = ball.angle - 90;
                    ball.reflect(2 * (ball.angle - 2 * a));
                    reflected = true;
                }
                if (ball.getY() >= sizeY -50- ball.getSpeed() && ball.getY() <= sizeY) {
                    double a = ball.angle - 90;
                    ball.reflect(2 * (ball.angle - 2 * a));
                    reflected = true;
                }
                if(ball.getX() <= platform.getX() + ball.getSpeed() && ball.getX() >= platform.getX()){
                    ball.platformReflect(platform);
                    reflected = true;
                }
                if(ball.getX() <= enemyPlatform.getX() + ball.getSpeed() && ball.getX() >= enemyPlatform.getX()){
                    ball.platformReflect(enemyPlatform);
                    reflected = true;
                }
            }else{
                if(ball.getY()>= ball.getSpeed() && ball.getY() <= sizeY- 50 -ball.getSpeed()){
                    reflected = false;
                }
            }

            //game ended
            if(isServer) {
                if (ball.getX() <= 0) {
                    changeScore(score1.get(), score2.get() + 1);
                    reset();
                    if (score1.get() == 5) {
                        animationThread.interrupt();
                        stop();
                    }
                }
                if (ball.getX() >= sizeX) {
                    changeScore(score1.get() + 1, score2.get());
                    reset();
                    if (score2.get() == 5) {
                        animationThread.interrupt();
                        stop();
                    }
                }
            }
        }
    }

    public void stop(){
        if(gamethread.isAlive()) gamethread.interrupt();
        javafx.application.Platform.runLater(()->  menu = new Menu(stage));
        isRunning.set(false);
        scene.setRoot(menu.getMenuRoot());
    }

    public void reset(){
        ball.setSpeed(ballSpeed);
        ball.setX(sizeX/2);
        ball.setY(sizeY/2);
        ball.setAngle(0);
        platform.setY(sizeY/2 - 75);
        platform.atomicY.set(platform.getY());
        enemyPlatform.setY(sizeY/2 - 75);
        enemyPlatform.atomicY.set(enemyPlatform.getY());

        if(isServer) {
            System.out.println(score1.get()    + " game " + score2.get());
            PointSyncPacket packet = new PointSyncPacket(score1.get(), score2.get());
            packet.sendData(socketClass);
        }


        BallSyncPacket packet = new BallSyncPacket(ball.getX(), ball.getY(), ball.angle);
        packet.sendData(socketClass);

        MovePacket packet2 = new MovePacket(player.username, player.platform.atomicY.get());
        packet2.sendData(socketClass);
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

    public void changeScore(int score1, int score2){
        this.score1.set( score1);
        this.score2.set( score2);
        javafx.application.Platform.runLater(()-> this.score.setText(score1 + " : " + score2));
        if(this.score1.get() == 5) {
            javafx.application.Platform.runLater(()-> confirmationAlert(platform.getOwner().username + " has won", "Winner").showAndWait() );
            animationThread.interrupt();
            stop();
        }else if(this.score2.get() == 5) {
            javafx.application.Platform.runLater(()-> confirmationAlert(enemyPlatform.getOwner().username + " has won", "Winner").showAndWait() );
            animationThread.interrupt();
            stop();
        }
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
