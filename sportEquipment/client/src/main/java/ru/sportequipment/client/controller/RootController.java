package ru.sportequipment.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.client.client.Client;
import ru.sportequipment.client.client.ConnectionHolder;
import ru.sportequipment.client.exception.ClientException;
import ru.sportequipment.client.listner.ServerResponseListener;

import java.util.Optional;

public class RootController {
    private static final Logger logger = LogManager.getLogger(RootController.class);

    @FXML
    private MenuItem menuServerConnect;

    @FXML
    private MenuItem menuServerDisonnect;

    @FXML
    void connectToServer(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("8844");
        dialog.setTitle("Connect Dialog");
        dialog.setHeaderText("Enter server port to connect:");
        dialog.setContentText("Port number:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            int portNumber = 0;
            try {
                portNumber = Integer.parseInt(result.get());
                logger.info("port number: " + portNumber);
            } catch (NumberFormatException e) {
                logger.error("Wrnog input format!");
            }

            try {
                ConnectionHolder.setClient(new Client(portNumber));
                ConnectionHolder.getClient().connect();
                ConnectionHolder.setServer(new Thread(new ServerResponseListener(ConnectionHolder.getClient().getSocketInput())));
                ConnectionHolder.getServer().start();
            } catch (ClientException e) {
                logger.error("Can not connect to server o!" + e);
            }
        } else {
            logger.info("cancelled");
        }
    }

    @FXML
    void disconnectFromServer(ActionEvent event) {

    }

}
