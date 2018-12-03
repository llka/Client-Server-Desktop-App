package ru.sportequipment.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ManageUsersController {

    @FXML
    private TextField seachByIdTextField;

    @FXML
    private Button searchBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button saveChangesBtn;

    @FXML
    private TextField searchByEmailTextField;

    @FXML
    private TextField myFirstNameTextField;

    @FXML
    private TextField myLastNameTextField;

    @FXML
    private TextField myEmailTextField;

    @FXML
    private TextField myPasswordTextField;

    @FXML
    private TableView<?> employeeTable;

    @FXML
    private TableColumn<?, ?> userIdColumn;

    @FXML
    private TableColumn<?, ?> userFirstNameColumn;

    @FXML
    private TableColumn<?, ?> userLastNameColumn;

    @FXML
    private TableColumn<?, ?> userEmailColumn;

    @FXML
    private TableColumn<?, ?> userRoleColumn;

    @FXML
    private Button getAllUsersBtn;

    @FXML
    void saveChanges(ActionEvent event){

    }

    @FXML
    void delete(ActionEvent event) {

    }

    @FXML
    void getAllUsers(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {

    }

    @FXML
    void update(ActionEvent event) {

    }


}
