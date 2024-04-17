package com.example.renzone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Renzone extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXML/login.fxml"));
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setTitle("Renzone");
        stage.setScene(scene);
        StageManager.setPrimaryStage(stage);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}