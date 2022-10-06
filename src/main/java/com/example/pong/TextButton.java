package com.example.pong;

import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.util.Duration;


public class TextButton {
    private Button startButton;
    /**
     * Creates a button
     * @param X - x coordinate
     * @param Y - y coordinate
     * @param Texts - text to display
     * @param XSize - size X
     * @param YSize - size Y
     * @param eventHandler - event on click
     */
    public TextButton(double X, double Y, String Texts, double XSize, double YSize, javafx.event.EventHandler<javafx.event.ActionEvent> eventHandler){
        startButton=new Button();
        startButton.setText(Texts);
        startButton.setFont(new Font("Verdana", (XSize / YSize)*0.5));
        startButton.setLayoutX(X);
        startButton.setLayoutY(Y);
        startButton.setPrefSize(XSize, YSize);
        startButton.setOnAction(eventHandler);
        /*EventHandler<MouseEvent> bigger= e->{
            ScaleTransition makeBigger=new ScaleTransition(Duration.millis(75), startButton);
            makeBigger.setToY(1.1);
            makeBigger.setToX(1.1);
            makeBigger.play();
        };
        EventHandler <MouseEvent> smaller=e->{
            ScaleTransition reset=new ScaleTransition(Duration.millis(75), startButton);
            reset.setToY(1);
            reset.setToX(1);
            reset.play();
        };
        startButton.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, bigger);
        startButton.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, smaller);*/
    }
    public Button getButton(){
        return startButton;
    }
}
