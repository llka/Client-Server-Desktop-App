package ru.sportequipment.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.client.client.Client;

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

    //Initializes the root layout.
    public void initRootLayout() {
        try {
            //First, load root layout from RootLayout.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/layout/root.fxml"));
            rootLayout = (AnchorPane) loader.load();

            //Second, show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 400, 300);
            stage.setScene(scene); //Set the scene in primary stage.

            /*//Give the controller access to the main.
            RootLayoutController controller = loader.getController();
            controller.setMain(this);*/

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
