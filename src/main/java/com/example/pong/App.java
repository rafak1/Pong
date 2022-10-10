package com.example.pong;


import game.Game;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.example.pong.MainVariables.sizeX;
import static com.example.pong.MainVariables.sizeY;

/**
 * JavaFX App
 */
public class App extends Application {
    private Stage mainStage;
    public void start(Stage stage)
    {
        mainStage = stage;
        Rectangle2D screenBounds=Screen.getPrimary().getBounds();
        MainVariables.ratioX =  screenBounds.getWidth() /  1920d;
        MainVariables.ratioY =  screenBounds.getHeight() / 1080d;
        sizeX=900 * MainVariables.ratioX;
        sizeY=600 * MainVariables.ratioY;
        MainVariables.ratioXY = sizeX / sizeY;
        stage.setTitle("Pong");
        stage.setResizable(true);
        stage.setWidth(sizeX);
        stage.setHeight(sizeY);
        Menu menu=new Menu(stage);
        stage.show();
        bindWidthAndHeightTogether();
    }

    public void bindWidthAndHeightTogether() {
        ChangeListener<Number> listener =
                new ChangeListener<>() {

                    private boolean changing;

                    @Override
                    public void changed(ObservableValue<? extends Number> obs, Number ov, Number nv) {
                        if (!changing) {
                            changing = true;
                            try {
                                if (obs == mainStage.widthProperty()) {
                                    mainStage.setHeight(nv.doubleValue() / MainVariables.ratioXY);
                                } else {
                                    mainStage.setWidth(nv.doubleValue() * MainVariables.ratioXY);
                                }
                            } finally {
                                changing = false;
                            }
                        }
                    }
                };
        mainStage.widthProperty().addListener(listener);
        mainStage.heightProperty().addListener(listener);
    }


    public static void main(String[] args) {
        launch();
    }

}