package ru.sportequipment.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.client.Main;
import ru.sportequipment.client.client.Client;
import ru.sportequipment.client.client.ContextHolder;
import ru.sportequipment.client.exception.ClientException;
import ru.sportequipment.client.listner.ServerResponseListener;
import ru.sportequipment.client.util.JsonUtil;
import ru.sportequipment.entity.*;
import ru.sportequipment.entity.enums.RoleEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.sportequipment.client.util.AlertUtil.alert;

public class RootController {
    private static final Logger logger = LogManager.getLogger(RootController.class);

    private static boolean connected = false;

    //Reference to the main application
    private Main main;

    //Is called by the main application to give a reference back to itself.
    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private MenuItem menuMyProfile;

    @FXML
    private MenuItem menuServerConnect;

    @FXML
    private MenuItem menuLogOut;

    @FXML
    private MenuItem menuServerDisconnect;

    @FXML
    private MenuItem menuLogIn;

    @FXML
    private MenuItem menuSkates;

    @FXML
    private MenuItem menuStick;

    @FXML
    void connectToServer(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1996");
        dialog.setTitle("Connect Dialog");
        dialog.setHeaderText("Enter server's port to connect with.");
        dialog.setContentText("Port number:");

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
                Session session = new Session();
                session.setVisitor(new Visitor(RoleEnum.GUEST));
                ContextHolder.setSession(session);

                refreshDisabledMenu();
                alert("Successfully connected to the server!");
            } catch (ClientException e) {
                alert("Can not connect to the server!");
                logger.error("Can not connect to the server!" + e);
            }
        } else {
            logger.info("cancelled connection dialog");
        }
    }

    @FXML
    void disconnectFromServer(ActionEvent event) {
        try {
            ContextHolder.getClient().disconnect();
            ContextHolder.setSession(null);

            refreshDisabledMenu();
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

        grid.add(new Label("Email:"), 0, 0);
        grid.add(email, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        email.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> email.requestFocus());

        // Convert the result to a email-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(email.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(emailAndPasswordPair -> {
            Map<String, String> params = new HashMap<>();
            params.put("email", emailAndPasswordPair.getKey());
            params.put("password", emailAndPasswordPair.getValue());
            try {
                logger.debug("email=" + emailAndPasswordPair.getKey() + ", Password=" + emailAndPasswordPair.getValue());
                ContextHolder.getClient().sendRequest(new CommandRequest("LOGIN", null, params));

                logger.debug("Request sent");
                CommandResponse response = Controller.getLastResponse();
                logger.debug("Response " + response);
                if (response.getStatus().is2xxSuccessful()) {
                    alert("Successfully logged in!");
                    Contact contact = JsonUtil.deserialize(response.getBody(), Contact.class);
                    ContextHolder.getSession().getVisitor().setContact(contact);
                    ContextHolder.getSession().getVisitor().setRole(contact.getRole());
                    logger.debug("session " + ContextHolder.getSession());
                    refreshDisabledMenu();
                } else {
                    alert(Alert.AlertType.ERROR, "Cannot login!", response.getBody());
                }
            } catch (ClientException e) {
                alert(Alert.AlertType.ERROR, "Cannot login!", e.getMessage());
            }
        });


    }

    @FXML
    void logOut(ActionEvent event) {
        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("LOGOUT", null));
            logger.debug("Request sent");

            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                ContextHolder.getSession().getVisitor().setContact(null);
                ContextHolder.getSession().getVisitor().setRole(RoleEnum.GUEST);
                refreshDisabledMenu();
                logger.debug("session " + ContextHolder.getSession());
                alert("Successfully logged out!");
            } else {
                alert(Alert.AlertType.ERROR, "Cannot logout!", response.getBody());
            }

        } catch (ClientException e) {
            alert(Alert.AlertType.ERROR, "Cannot logout!", e.getMessage());
        }
    }

    @FXML
    void openMyProfileView(ActionEvent event) {
        if (ContextHolder.getSession() == null ||
                ContextHolder.getSession().getVisitor() == null) {
            alert(Alert.AlertType.ERROR, "You are not authorized!", "You are not authorized!");
        }
        first = true;
        main.showMyProfileView();
    }

    @FXML
    void openSkatesView(ActionEvent event) {
        logger.debug("open openSkatesView!");
    }

    @FXML
    void openSticksView(ActionEvent event) {
        logger.debug("open openStickView!");
    }

    private void refreshDisabledMenu() {
        Session session = ContextHolder.getSession();
        if (session == null) {
            menuServerConnect.setDisable(false);
            menuServerDisconnect.setDisable(true);

            menuLogIn.setDisable(true);
            menuLogOut.setDisable(true);

            menuMyProfile.setDisable(true);
            menuSkates.setDisable(true);
            menuStick.setDisable(true);
        } else {
            if (session.getVisitor() != null) {
                menuServerConnect.setDisable(true);
                menuServerDisconnect.setDisable(false);
                switch (session.getVisitor().getRole()) {
                    case GUEST:
                        menuLogIn.setDisable(false);
                        menuLogOut.setDisable(true);

                        menuMyProfile.setDisable(true);
                        menuSkates.setDisable(true);
                        menuStick.setDisable(true);
                        break;
                    case USER:
                        menuLogIn.setDisable(true);
                        menuLogOut.setDisable(false);

                        menuMyProfile.setDisable(false);
                        menuSkates.setDisable(false);
                        menuStick.setDisable(false);
                        break;
                    case ADMIN:
                        menuLogIn.setDisable(true);
                        menuLogOut.setDisable(false);

                        menuMyProfile.setDisable(false);
                        menuSkates.setDisable(false);
                        menuStick.setDisable(false);
                        break;
                    default:
                        logger.error("unknown role!");
                }
            }
        }
    }

    //    --------------------My Profile Controller -------------------------
    @FXML
    private AnchorPane myProfilePane;

    @FXML
    private Button saveChangesBtn;

    @FXML
    private TextField myFirstNameTextField;

    @FXML
    private TextField myLastNameTextField;

    @FXML
    private TextField myEmailTextField;

    @FXML
    private TextField myPasswordTextField;

    @FXML
    void saveChanges(ActionEvent event) {
        Contact contact = ContextHolder.getSession().getVisitor().getContact();
        contact.setFirstName(myFirstNameTextField.getText());
        contact.setLastName(myLastNameTextField.getText());
        contact.setPassword(myPasswordTextField.getText());

        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("UPDATE_CONTACT", JsonUtil.serialize(contact)));
            logger.debug("Request sent " + contact);


            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                Contact updatedContact = JsonUtil.deserialize(response.getBody(), Contact.class);
                ContextHolder.getSession().getVisitor().setContact(updatedContact);
                ContextHolder.getSession().getVisitor().setRole(updatedContact.getRole());
                refreshDisabledMenu();
                logger.debug("session " + ContextHolder.getSession());

                myFirstNameTextField.setText(updatedContact.getFirstName());
                myLastNameTextField.setText(updatedContact.getLastName());
                myEmailTextField.setText(updatedContact.getEmail());
                myPasswordTextField.setText(updatedContact.getPassword());

                alert("Successfully saved changes!");
            } else {
                alert(Alert.AlertType.ERROR, "Cannot logout!", response.getBody());
            }

        } catch (ClientException e) {
            alert(Alert.AlertType.ERROR, "Cannot save changes!", e.getMessage());
        }


    }

    private boolean first = true;

    @FXML
    void onMouseEntered(MouseEvent event) {
        if (first) {
            Contact contact = ContextHolder.getSession().getVisitor().getContact();

            myFirstNameTextField.setText(contact.getFirstName());
            myLastNameTextField.setText(contact.getLastName());
            myEmailTextField.setText(contact.getEmail());
            myPasswordTextField.setText(contact.getPassword());

            myFirstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                saveChangesBtn.setDisable(newValue.trim().isEmpty());
            });
            myLastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                saveChangesBtn.setDisable(newValue.trim().isEmpty());
            });
            myPasswordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                saveChangesBtn.setDisable(newValue.trim().isEmpty());
            });
            first = false;
        }
    }
}
