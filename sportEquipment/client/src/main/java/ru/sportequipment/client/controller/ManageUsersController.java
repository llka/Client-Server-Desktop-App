package ru.sportequipment.client.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.client.client.ContextHolder;
import ru.sportequipment.client.exception.ClientException;
import ru.sportequipment.client.util.JsonUtil;
import ru.sportequipment.dto.ContactsDTO;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Contact;

import static ru.sportequipment.client.util.AlertUtil.alert;

public class ManageUsersController {

    private static final Logger logger = LogManager.getLogger(ManageUsersController.class);

    private static boolean firstOpened = true;

    @FXML
    private TableView contactsTable;

    @FXML
    private TableColumn<Contact, Integer> userIdColumn;
    @FXML
    private TableColumn<Contact, String> userFirstNameColumn;
    @FXML
    private TableColumn<Contact, String> userLastNameColumn;
    @FXML
    private TableColumn<Contact, String> userEmailColumn;
    @FXML
    private TableColumn<Contact, String> userRoleColumn;

    @FXML
    private Button getAllUsersBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private TextField seachByIdTextField;

    @FXML
    private Button addUserBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchByEmailTextField;

    @FXML
    private Button deleteBtn;

    @FXML
    private TextField newLastNameTextField;

    @FXML
    private TextField newFirstNameTextField;

    @FXML
    private TextField newEmailTextField;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private void initialize() {
        logger.debug("initialize");
        userIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        userFirstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getFirstName())));
        userLastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        userEmailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        userRoleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().toString()));
    }

    private void fillContactsTable(ActionEvent event) {
        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("GET_CONTACTS"));
            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                ContactsDTO contactsDTO = JsonUtil.deserialize(response.getBody(), ContactsDTO.class);
                logger.debug(contactsDTO.getContacts());
                contactsTable.setItems(FXCollections.observableArrayList(contactsDTO.getContacts()));
            } else {
                alert(Alert.AlertType.ERROR, "Cannot fill in Contacts table!", response.getBody());
            }
        } catch (ClientException e) {
            logger.error(e);
            alert(Alert.AlertType.ERROR, "Cannot fill in Contacts table!", e.getMessage());
        }

    }

    @FXML
    private void populateContact(Contact contact) {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        contactList.add(contact);
        contactsTable.setItems(contactList);
    }

    @FXML
    private void populateContacts(ObservableList<Contact> contactObservableList) {
        contactsTable.setItems(contactObservableList);
    }


    @FXML
    void search(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {

    }

    @FXML
    void update(ActionEvent event) {

    }

    @FXML
    void addUser(ActionEvent event) {

    }

    @FXML
    void getAllUsers(ActionEvent event) {
        fillContactsTable(event);
    }


    public static boolean isFirstOpened() {
        return firstOpened;
    }

    public static void setFirstOpened(boolean firstOpened) {
        ManageUsersController.firstOpened = firstOpened;
    }
}
