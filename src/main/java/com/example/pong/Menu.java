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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import net.GameClient;
import net.GameServer;
import net.packets.LoginPacket;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Optional;

import static com.example.pong.MainVariables.sizeX;
import static com.example.pong.MainVariables.sizeY;

public class Menu {
    private final Group menuRoot;
    public Scene newScene;
    private final Game game;
    private GameServer socketServer;
    private GameClient socketClient;
    public Menu(Stage stage){




         game = new Game(this,stage);

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
                if(res != null ){
                    game.setIsServer(false);
                    socketClient = new GameClient(res.getValue(), game, res.getKey());
                    game.setSocketClass(socketClient);
                    Thread clientThread = new Thread(socketClient);
                    clientThread.setDaemon(true);
                    Alert alert = waitForConnection("Connecting", "Connecting...");
                    socketClient.connectionAlert = alert;
                    clientThread.start();
                    Thread connectingThread = new Thread(()-> {
                        LoginPacket loginPacket = new LoginPacket(res.getKey());
                        while (!socketClient.isConnected.get()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ignored) {
                            }
                            loginPacket.sendData(socketClient);
                        }
                    });
                    connectingThread.setDaemon(true);
                    connectingThread.start();
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.CANCEL && !socketClient.isConnected.get()) {
                        socketClient.closeSocket();
                        clientThread.interrupt();
                    }else{
                        connectingThread.interrupt();
                        game.show(newScene);
                    }
                }
            }
        });

        ImageButton serverButton = new ImageButton("/graphics/server_start.png",MainVariables.sizeX/2, MainVariables.sizeY*2/3 , 200*MainVariables.ratioXY, 50 / MainVariables.ratioXY,  new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog input = new TextInputDialog("username");
                input.setHeaderText("Enter your username");
                Optional<String> res =  input.showAndWait();
                if(res.isPresent() ) {
                    game.setIsServer(true);
                    socketServer = new GameServer( game, res.get());
                    game.setSocketClass(socketServer);
                    Thread serverThread = new Thread(socketServer);
                    serverThread.setDaemon(true);
                    Alert alert = null;
                    try {
                        alert = waitForConnection("Waiting for the other player", "Waiting...    \n Server ip : " + InetAddress.getLocalHost().getHostAddress());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    socketServer.connectionAlert = alert;
                    serverThread.start();
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.CANCEL && !socketServer.isConnected.get()) {
                        System.out.println("Canceled");
                        socketServer.closeSocket();
                        serverThread.interrupt();
                    }else{
                        game.show(newScene);
                    }
                }
            }
        });

        //title
        ImageView title = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/graphics/title.png")).toString(),300*MainVariables.ratioXY, 100 / MainVariables.ratioXY,true,true));


        buttonsBox.getChildren().addAll(title,startButton.getButton(), serverButton.getButton());
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

    private static Alert waitForConnection(String title, String context){
        Alert alert = new Alert(Alert.AlertType.NONE,"", ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        return alert;
    }

    public Group getMenuRoot() {
        return menuRoot;
    }
}
