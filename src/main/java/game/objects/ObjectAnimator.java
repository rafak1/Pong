package game.objects;

import javafx.application.Platform;

import java.util.*;

public class ObjectAnimator implements Runnable{

    Ball ball;
    List<game.objects.Platform> list = Collections.synchronizedList(new ArrayList<game.objects.Platform> ());


    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(12);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            ball.moveBall();
            Platform.runLater(() -> {
                ball.getImageView().setY(ball.getY());
                ball.getImageView().setX(ball.getX());
                synchronized (list){
                    for (game.objects.Platform a : list) {
                        a.setY(a.atomicY.get());
                        a.getImageView().setY(a.getY());
                    }
                }
            });
        }
    }

    public ObjectAnimator(Ball ball){
        this.ball = ball;
    }

    public void addPlatform(game.objects.Platform object){
        list.add(object);
    }
}
