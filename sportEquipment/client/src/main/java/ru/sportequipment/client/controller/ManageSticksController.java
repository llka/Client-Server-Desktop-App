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
import ru.sportequipment.dto.StickListDTO;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Stick;
import ru.sportequipment.entity.enums.StickType;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static ru.sportequipment.client.util.AlertUtil.alert;

public class ManageSticksController {
    private static final Logger logger = LogManager.getLogger(ManageSticksController.class);

    private static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static boolean firstOpened = true;

    @FXML
    private Button searchBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button getAllBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button updateBtn;

    @FXML
    private TextField searchByIdTextField;
    @FXML
    private ComboBox<String> searchByTypeComboBox;

    @FXML
    private TextField idForUpdateTextField;
    @FXML
    private ComboBox<String> typeForUpdateComboBox;
    @FXML
    private TextField costForUpdateTextField;

    @FXML
    private TableView sticksTable;
    @FXML
    private TableColumn<Stick, Integer> idColumn;
    @FXML
    private TableColumn<Stick, String> typeColumn;
    @FXML
    private TableColumn<Stick, String> costPerHourColumn;
    @FXML
    private TableColumn<Stick, String> bookedFromColumn;
    @FXML
    private TableColumn<Stick, String> bookedToColumn;

    @FXML
    private void initialize() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        logger.debug("initialize");
        searchByTypeComboBox.setItems(FXCollections.observableArrayList(
                StickType.ICE_HOCKEY.toString(),
                StickType.FIELD_HOCKEY.toString(),
                StickType.ROLLER_HOCKEY.toString()));
        typeForUpdateComboBox.setItems(FXCollections.observableArrayList(
                StickType.ICE_HOCKEY.toString(),
                StickType.FIELD_HOCKEY.toString(),
                StickType.ROLLER_HOCKEY.toString()));

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStickType().toString()));
        costPerHourColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCostPerHour().toString()));

        bookedFromColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getBookedFrom() != null) {
                return new SimpleStringProperty(dateFormat.format(cellData.getValue().getBookedFrom()));
            } else {
                return new SimpleStringProperty("");
            }
        });
        bookedToColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getBookedTo() != null) {
                return new SimpleStringProperty(dateFormat.format(cellData.getValue().getBookedTo()));
            } else {
                return new SimpleStringProperty("");
            }
        });

        if (firstOpened) {
            fillTable();
            firstOpened = false;
        }
    }

    private void fillTable() {
        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("GET_STICKS"));
            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                StickListDTO stickListDTO = JsonUtil.deserialize(response.getBody(), StickListDTO.class);
                logger.debug(stickListDTO);
                sticksTable.setItems(FXCollections.observableArrayList(stickListDTO.getStickList()));
            } else {
                alert(Alert.AlertType.ERROR, "Cannot fill in Sticks table!", response.getBody());
            }
        } catch (ClientException e) {
            logger.error(e);
            alert(Alert.AlertType.ERROR, "Cannot fill in Sticks table!", e.getMessage());
        }
    }

    @FXML
    private void populateStick(Stick stick) {
        ObservableList<Stick> stickObservableList = FXCollections.observableArrayList();
        stickObservableList.add(stick);
        sticksTable.setItems(stickObservableList);
    }

    @FXML
    private void populateSticksList(ObservableList<Stick> stickObservableList) {
        sticksTable.setItems(stickObservableList);
    }

    @FXML
    void search(ActionEvent event) {
        Map<String, String> params = prepareFilterParams();
        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("GET_STICKS", params));
            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                StickListDTO stickListDTO = JsonUtil.deserialize(response.getBody(), StickListDTO.class);
                logger.debug(stickListDTO);
                populateSticksList(FXCollections.observableArrayList(stickListDTO.getStickList()));
            } else {
                alert(Alert.AlertType.ERROR, "Cannot fill in Sticks table!", response.getBody());
            }
        } catch (ClientException e) {
            logger.error(e);
            alert(Alert.AlertType.ERROR, "Cannot fill in Sticks table!", e.getMessage());
        }
    }

    @FXML
    void getAllSkates(ActionEvent event) {
        fillTable();
    }

    @FXML
    void refresh(ActionEvent event) {
        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("REFRESH_STICKS"));
            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                StickListDTO stickListDTO = JsonUtil.deserialize(response.getBody(), StickListDTO.class);
                logger.debug(stickListDTO);
                populateSticksList(FXCollections.observableArrayList(stickListDTO.getStickList()));
                alert("Refreshed!");
            } else {
                alert(Alert.AlertType.ERROR, "Cannot refresh Sticks info!", response.getBody());
            }
        } catch (ClientException e) {
            logger.error(e);
            alert(Alert.AlertType.ERROR, "Cannot refresh Sticks info!", e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        String id = idForUpdateTextField.getText();
        String type = typeForUpdateComboBox.getValue();
        String cost = costForUpdateTextField.getText();
        Stick stick = new Stick();
        try {
            stick.setId(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Invalid id!", "Invalid id!");
            return;
        }
        try {
            stick.setCostPerHour(new BigDecimal(cost));
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Invalid cost!", "Invalid cost!");
            return;
        }
        if (type != null && !type.isEmpty()) {
            stick.setStickType(StickType.valueOf(type));
            try {
                ContextHolder.getClient().sendRequest(new CommandRequest("UPDATE_STICK", JsonUtil.serialize(stick)));
                CommandResponse response = Controller.getLastResponse();
                logger.debug("Response " + response);
                if (response.getStatus().is2xxSuccessful()) {
                    StickListDTO stickListDTO = JsonUtil.deserialize(response.getBody(), StickListDTO.class);
                    populateSticksList(FXCollections.observableArrayList(stickListDTO.getStickList()));
                    alert("Successfully updated stick!");
                } else {
                    alert(Alert.AlertType.ERROR, "Cannot update Stick!", response.getBody());
                }
            } catch (ClientException e) {
                logger.error(e);
                alert(Alert.AlertType.ERROR, "Cannot update Stick!", e.getMessage());
            }

        } else {
            alert(Alert.AlertType.ERROR, "Stick Type not set!", "Stick Type not set!");
        }
    }

    @FXML
    void add(ActionEvent event) {
        String type = typeForUpdateComboBox.getValue();
        String cost = costForUpdateTextField.getText();
        Stick stick = new Stick();
        try {
            stick.setCostPerHour(new BigDecimal(cost));
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Invalid cost!", "Invalid cost!");
            return;
        }
        if (type != null && !type.isEmpty()) {
            stick.setStickType(StickType.valueOf(type));
            try {
                ContextHolder.getClient().sendRequest(new CommandRequest("CREATE_STICK", JsonUtil.serialize(stick)));
                CommandResponse response = Controller.getLastResponse();
                logger.debug("Response " + response);
                if (response.getStatus().is2xxSuccessful()) {
                    StickListDTO stickListDTO = JsonUtil.deserialize(response.getBody(), StickListDTO.class);
                    populateSticksList(FXCollections.observableArrayList(stickListDTO.getStickList()));
                    alert("Successfully added new stick!");
                } else {
                    alert(Alert.AlertType.ERROR, "Cannot create Stick!", response.getBody());
                }
            } catch (ClientException e) {
                logger.error(e);
                alert(Alert.AlertType.ERROR, "Cannot create Stick!", e.getMessage());
            }

        } else {
            alert(Alert.AlertType.ERROR, "Stick Type not set!", "Stick Type not set!");
        }
    }

    @FXML
    void delete(ActionEvent event) {
        Map<String, String> params = prepareFilterParams();
        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("DELETE_STICK", params));
            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                fillTable();
                alert("Successfully deleted!");
            } else {
                alert(Alert.AlertType.ERROR, "Cannot delete Stick!", response.getBody());
            }
        } catch (ClientException e) {
            logger.error(e);
            alert(Alert.AlertType.ERROR, "Cannot delete Stick!", e.getMessage());
        }
    }

    private Map<String, String> prepareFilterParams() {
        String id = searchByIdTextField.getText();
        String type = searchByTypeComboBox.getValue();
        Map<String, String> params = new HashMap<>();
        if (id != null && !id.isEmpty()) {
            params.put("id", id);
        }
        if (type != null && !type.isEmpty()) {
            params.put("type", type);
        }
        return params;
    }

    public static boolean isFirstOpened() {
        return firstOpened;
    }

    public static void setFirstOpened(boolean firstOpened) {
        ManageSticksController.firstOpened = firstOpened;
    }
}
