package com.example.pong;

import game.Game;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.Stack;

public class Menu {
    private  Group menuRoot;
    public Scene newScene;
    private Game game = new Game();
    public Menu(Stage stage){


        GridPane gridPane = new GridPane();
        StackPane stackPane = new StackPane(gridPane);
        newScene = new Scene(stackPane, MainVariables.sizeX, MainVariables.sizeY);
        menuRoot = new Group();

        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        // keep gridPane at original size
        gridPane.setMinSize(MainVariables.sizeX, MainVariables.sizeY);
        gridPane.setMaxSize(MainVariables.sizeX, MainVariables.sizeY);


        stage.setScene(newScene);

        ImageButton startButton = new ImageButton("/graphics/start.png",MainVariables.sizeX/2, MainVariables.sizeY/2 , 200*MainVariables.ratioXY, 50 / MainVariables.ratioXY,  new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                game.show(newScene);
            }
        });


        stackPane.getChildren().add(startButton.getButton());
        stackPane.setBackground(new Background(new BackgroundFill(Color.BLACK ,CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setFillHeight(startButton.getButton(), true);
        GridPane.setFillWidth(startButton.getButton(), true);

        NumberBinding maxScale = Bindings.min(stackPane.widthProperty().divide(MainVariables.sizeX), stackPane.heightProperty().divide(MainVariables.sizeY));
        gridPane.scaleXProperty().bind(maxScale);
        gridPane.scaleYProperty().bind(maxScale);

        Canvas canvas=new Canvas(MainVariables.sizeX, MainVariables.sizeY);
        menuRoot.getChildren().add(canvas);
    }

}
