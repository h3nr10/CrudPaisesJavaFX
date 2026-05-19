package com.template;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/template/main.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setScene(scene);

        stage.setTitle("CRUD Países");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}