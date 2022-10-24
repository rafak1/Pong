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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

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

        VBox buttonsBox = new VBox();

        ImageButton startButton = new ImageButton("/graphics/start.png",MainVariables.sizeX/2, MainVariables.sizeY/2 , 200*MainVariables.ratioXY, 50 / MainVariables.ratioXY,  new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pair<String, String> res = optionPane(new Pair<>("Username: ","Username"), new Pair<>("Server ip: ","00:00:00:00"), "Join a Server", "Join a Server");
                if(res != null ) game.show(newScene);
            }
        });

        ImageButton serverButton = new ImageButton("/graphics/server_start.png",MainVariables.sizeX/2, MainVariables.sizeY*2/3 , 200*MainVariables.ratioXY, 50 / MainVariables.ratioXY,  new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pair<String, String> res = optionPane(new Pair<>("Username: ","Username"), new Pair<>("Server ip: ","00:00:00:00"), "Create a Server", "Start Server");
                if(res != null ) game.show(newScene);
            }
        });

        buttonsBox.getChildren().addAll(startButton.getButton(), serverButton.getButton());
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(30);


        stackPane.getChildren().add(buttonsBox);
        stackPane.setBackground(new Background(new BackgroundFill(Color.BLACK ,CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setFillHeight(startButton.getButton(), true);
        GridPane.setFillWidth(startButton.getButton(), true);

        NumberBinding maxScale = Bindings.min(stackPane.widthProperty().divide(MainVariables.sizeX), stackPane.heightProperty().divide(MainVariables.sizeY));
        gridPane.scaleXProperty().bind(maxScale);
        gridPane.scaleYProperty().bind(maxScale);

        Canvas canvas=new Canvas(MainVariables.sizeX, MainVariables.sizeY);
        menuRoot.getChildren().add(canvas);
    }

    private static Pair<String, String> optionPane(Pair<String,String> arg1, Pair<String,String> arg2 , String header, String title) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(header);
        dialog.setHeaderText(title);

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField field1 = new TextField();
        field1.setPromptText(arg1.getValue());
        TextField field2 = new TextField();
        field2.setPromptText(arg2.getValue());

        grid.add(new Label(arg1.getKey()), 0, 0);
        grid.add(field1, 1, 0);
        grid.add(new Label(arg2.getKey()), 0, 1);
        grid.add(field2, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(field1.getText(), field2.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        return result.orElse(null);
    }

}
