package game.objects;

public class BallAnimator implements Runnable{

    Ball ball;

    @Override
    public void run() {
        while(true){
            try {
                wait(5);
            } catch (InterruptedException e) {
                break;
            }

        }
    }

    public BallAnimator(Ball ball){
        this.ball = ball;
    }
}
