package ru.sportequipment.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;


public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    private Stage stage;

    private AnchorPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;
        stage.setTitle("SportEquipment");

        initRootLayout();
    }


    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/layout/root.fxml"));
            rootLayout = (AnchorPane) loader.load();

            Scene scene = new Scene(rootLayout, 400, 300);
            stage.setScene(scene);

            /*//Give the controller access to the main.
            RootLayoutController controller = loader.getController();
            controller.setMain(this);*/

            stage.show();
        } catch (IOException e) {
            logger.error("Cannot init rool layout" + e);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
