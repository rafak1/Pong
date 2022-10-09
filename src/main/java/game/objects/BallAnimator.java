package game.objects;

import javafx.application.Platform;

public class BallAnimator implements Runnable{

    Ball ball;



    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            ball.moveBall();
            System.out.println(ball.angle + " " + ball.getY());
            Platform.runLater(() -> {
                ball.getImageView().setY(ball.getY());
                ball.getImageView().setX(ball.getX());
            });
        }
    }

    public BallAnimator(Ball ball){
        this.ball = ball;
    }
}
