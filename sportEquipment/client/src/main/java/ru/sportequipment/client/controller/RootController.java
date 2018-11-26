package ru.sportequipment.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.client.client.Client;
import ru.sportequipment.client.client.ContextHolder;
import ru.sportequipment.client.exception.ClientException;
import ru.sportequipment.client.listner.ServerResponseListener;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.sportequipment.client.util.AlertUtil.alert;

public class RootController {
    private static final Logger logger = LogManager.getLogger(RootController.class);

    private static boolean connected = false;
    private static boolean authenticated = false;

    @FXML
    private MenuItem menuServerConnect;

    @FXML
    private MenuItem menuLogOut;

    @FXML
    private MenuItem menuServerDisonnect;

    @FXML
    private MenuItem menuLogIn;

    @FXML
    void connectToServer(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1996");
        dialog.setTitle("Connect Dialog");
        dialog.setHeaderText("Enter server's port to connect with.");
        dialog.setContentText("Port number:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            int portNumber = 0;
            try {
                portNumber = Integer.parseInt(result.get());
                logger.info("port number: " + portNumber);
            } catch (NumberFormatException e) {
                logger.error("Wrong input format!");
                alert("Wrong input format!");
            }

            try {
                ContextHolder.setClient(new Client(portNumber));
                ContextHolder.getClient().connect();
                ContextHolder.setServer(new Thread(new ServerResponseListener(ContextHolder.getClient().getSocketInput())));
                ContextHolder.getServer().start();

                connected = true;
                menuServerConnect.setDisable(connected);
                menuServerDisonnect.setDisable(!connected);
                alert("Successfully connected to the server!");
            } catch (ClientException e) {
                alert("Can not connect to the server!");
                logger.error("Can not connect to the server!" + e);
            }

        } else {
            logger.info("cancelled");
        }
    }

    @FXML
    void disconnectFromServer(ActionEvent event) {
        try {
            ContextHolder.getClient().disconnect();
            connected = false;
            menuServerConnect.setDisable(connected);
            menuServerDisonnect.setDisable(!connected);
            alert("Successfully disconnected from the server!");
        } catch (ClientException e) {
            alert("Can not disconnect from the server!");
            logger.error("Can not disconnect from the server!" + e);
        }
    }

    @FXML
    void login(ActionEvent event) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> email.requestFocus());

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(emailAndPassword -> {
            Map<String, String> params = new HashMap<>();
            params.put("email", emailAndPassword.getKey());
            params.put("password", emailAndPassword.getValue());
            try {
                ContextHolder.getClient().sendRequest(new CommandRequest("LOGIN", null, params));

                CommandResponse response = ContextHolder.getResponseStack().pop();
                while (true) {
                    try {
                        CommandResponse response = ContextHolder.getResponseStack().pop();
                    }catch ()

                }
            } catch (ClientException e) {
                alert(Alert.AlertType.ERROR,"Cannot login", e.getMessage());
            }

            logger.debug("email=" + emailAndPassword.getKey() + ", Password=" + emailAndPassword.getValue());
        });


    }

    @FXML
    void logOut(ActionEvent event) {

    }

}
