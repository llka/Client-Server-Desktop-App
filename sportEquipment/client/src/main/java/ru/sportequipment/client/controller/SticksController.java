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
import ru.sportequipment.entity.Equipment;
import ru.sportequipment.entity.Stick;
import ru.sportequipment.entity.enums.StickType;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.sportequipment.client.util.AlertUtil.alert;

public class SticksController {
    private static final Logger logger = LogManager.getLogger(SticksController.class);

    private static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static boolean firstOpened = true;

    @FXML
    private TableView sticksTable;
    @FXML
    private TableColumn<Stick, String> typeColumn;
    @FXML
    private TableColumn<Stick, String> costPerHourColumn;
    @FXML
    private TableColumn<Stick, String> bookedFromColumn;
    @FXML
    private TableColumn<Stick, String> bookedToColumn;
    @FXML
    private TableColumn<Stick, Integer> idColumn;

    @FXML
    private Button getAllSticksBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button bookBtn;
    @FXML
    private Button refreshBtn;

    @FXML
    private ComboBox<String> searchByTypeComboBox;
    @FXML
    private TextField searchByIdTextField;
    @FXML
    private TextField bookForHoursTextField;

    @FXML
    private void initialize() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        logger.debug("initialize");
        searchByTypeComboBox.setItems(FXCollections.observableArrayList(
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
    void book(ActionEvent event) {
        String hoursText = bookForHoursTextField.getText();

        if (hoursText != null && !hoursText.isEmpty()) {
            try {
                int hours = Integer.parseInt(hoursText);
                Map<String, String> params = prepareFilterParams();
                params.put("hours", hoursText);
                try {
                    ContextHolder.getClient().sendRequest(new CommandRequest("BOOK_STICK", params));
                    CommandResponse response = Controller.getLastResponse();
                    logger.debug("Response " + response);
                    if (response.getStatus().is2xxSuccessful()) {
                        StickListDTO stickListDTO = JsonUtil.deserialize(response.getBody(), StickListDTO.class);
                        logger.debug(stickListDTO);
                        populateSticksList(FXCollections.observableArrayList(stickListDTO.getStickList()));

                        List<Equipment> equipment = ContextHolder.getSession().getVisitor().getContact().getBookedEquipment();
                        equipment.add(stickListDTO.getStickList().get(0));
                        ContextHolder.getSession().getVisitor().getContact().setBookedEquipment(equipment);
                        alert("Successfully booked stick for " + hours + " hours!");
                    } else {
                        alert(Alert.AlertType.ERROR, "Cannot fill in Sticks table!", response.getBody());
                    }
                } catch (ClientException e) {
                    logger.error(e);
                    alert(Alert.AlertType.ERROR, "Cannot fill in Sticks table!", e.getMessage());
                }
            } catch (IllegalArgumentException e) {
                alert(Alert.AlertType.ERROR, "Invalid hours value!", "Use integers to define hours!");
            }

        }
    }

    @FXML
    void getAllSticks(ActionEvent event) {
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
        SticksController.firstOpened = firstOpened;
    }
}
