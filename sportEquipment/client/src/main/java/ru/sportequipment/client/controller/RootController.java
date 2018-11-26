package ru.sportequipment.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.client.client.Client;
import ru.sportequipment.client.client.ContextHolder;
import ru.sportequipment.client.exception.ClientException;
import ru.sportequipment.client.listner.ServerResponseListener;

import java.util.Optional;

public class RootController {
    private static final Logger logger = LogManager.getLogger(RootController.class);

    private static boolean connected = false;
    private static boolean authenticated = false;

    @FXML
    private MenuItem menuServerConnect;

    @FXML
    private MenuItem menuServerDisonnect;

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

    private void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
