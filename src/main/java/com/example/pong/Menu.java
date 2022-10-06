package com.example.pong;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Menu {
    private  Group menuRoot;
    private Scene scene;
    public Menu(Stage stage){

        menuRoot = new Group();
        scene = new Scene(menuRoot);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);

        Canvas canvas=new Canvas(MainVariables.sizeX, MainVariables.sizeY);
        menuRoot.getChildren().add(canvas);
    }

}
