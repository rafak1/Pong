package com.example.pong;

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
import javafx.stage.Stage;

public class Menu {
    private  Group menuRoot;
    private Scene scene;
    public Menu(Stage stage){

        menuRoot = new Group();
        scene = new Scene(menuRoot);
        scene.setFill(Color.BLACK);

        TextButton startButton = new TextButton(MainVariables.sizeX/2, MainVariables.sizeY/2 ,"START", 200*MainVariables.ratioXY, 50 / MainVariables.ratioXY,  new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("a");
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        // keep gridPane at original size
        gridPane.setMinSize(1500, 500);
        gridPane.setMaxSize(1500, 500);

        StackPane stackPane = new StackPane(gridPane);
        stackPane.getChildren().add(startButton.getButton());

        NumberBinding maxScale = Bindings.min(stackPane.widthProperty().divide(1500), stackPane.heightProperty().divide(500));
        gridPane.scaleXProperty().bind(maxScale);
        gridPane.scaleYProperty().bind(maxScale);

        stage.setScene(new Scene(stackPane, 1500, 500));

        Canvas canvas=new Canvas(MainVariables.sizeX, MainVariables.sizeY);
        menuRoot.getChildren().add(canvas);
    }

}
