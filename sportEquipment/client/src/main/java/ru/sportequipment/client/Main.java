package ru.sportequipment.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.client.controller.RootController;

import java.io.IOException;


public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    private static final String WINDOW_TITLE = "Sport Equipment";

    private Stage window;
    private Scene startPage;

    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.window = primaryStage;
        window.setTitle(WINDOW_TITLE);

        initRootLayout();
    }


    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/layout/root.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout, 600, 400);
            window.setScene(scene);

            //Give the controller access to the main.
            RootController controller = loader.getController();
            controller.setMain(this);

            window.show();
        } catch (IOException e) {
            logger.error("Cannot init root layout" + e);
        }
    }

    public void showMyProfileView() {
        logger.debug("show my profile from main");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/layout/myProfileView.fxml"));
            AnchorPane myProfileView = (AnchorPane) loader.load();

            rootLayout.setCenter(myProfileView);
        } catch (IOException e) {
            logger.error("Cannot show myProfileView" + e);
        }
    }

    public void showGuestMainView() {
        logger.debug("show guest view from main");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/layout/guestMainView.fxml"));
            AnchorPane guestMainView = (AnchorPane) loader.load();

            rootLayout.setCenter(guestMainView);
        } catch (IOException e) {
            logger.error("Cannot show guestMainView" + e);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
