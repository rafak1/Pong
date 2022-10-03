package com.example.pong;


import game.Game;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage)
    {
        Rectangle2D screenBounds= Screen.getPrimary().getBounds();
        stage.setTitle("Pong");
        stage.setResizable(true);
        Game game = new Game();
        stage.show();

        //stage.setFullScreen(true);
    }

    public static void main(String[] args) {
        launch();
    }

}