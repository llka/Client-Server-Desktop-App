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
import ru.sportequipment.dto.SkatesListDTO;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Equipment;
import ru.sportequipment.entity.Skates;
import ru.sportequipment.entity.enums.SkatesType;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.sportequipment.client.util.AlertUtil.alert;

public class SkatesController {
    private static final Logger logger = LogManager.getLogger(SkatesController.class);

    private static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static boolean firstOpened = true;

    @FXML
    private TableView skatesTable;
    @FXML
    private TableColumn<Skates, Integer> idColumn;
    @FXML
    private TableColumn<Skates, Integer> sizeColumn;
    @FXML
    private TableColumn<Skates, String> typeColumn;
    @FXML
    private TableColumn<Skates, String> costPerHourColumn;
    @FXML
    private TableColumn<Skates, String> bookedFromColumn;
    @FXML
    private TableColumn<Skates, String> bookedToColumn;

    @FXML
    private Button getAllSkatesBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button bookBtn;
    @FXML
    private Button refreshBtn;

    @FXML
    private TextField seachByIdTextField;
    @FXML
    private ComboBox<String> searchByTypeComboBox;
    @FXML
    private TextField searchBySizeTextField;
    @FXML
    private TextField bookForHoursTExtField;

    @FXML
    private void initialize() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        logger.debug("initialize");
        searchByTypeComboBox.setItems(FXCollections.observableArrayList(
                SkatesType.ICE_HOCKEY.toString(),
                SkatesType.FIGURE.toString(),
                SkatesType.ROLLER.toString()));

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        sizeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((cellData.getValue().getSize())).asObject());
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().toString()));
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
            ContextHolder.getClient().sendRequest(new CommandRequest("GET_SKATES"));
            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                SkatesListDTO skatesListDTO = JsonUtil.deserialize(response.getBody(), SkatesListDTO.class);
                logger.debug(skatesListDTO);
                skatesTable.setItems(FXCollections.observableArrayList(skatesListDTO.getSkatesList()));
            } else {
                alert(Alert.AlertType.ERROR, "Cannot fill in Skates table!", response.getBody());
            }
        } catch (ClientException e) {
            logger.error(e);
            alert(Alert.AlertType.ERROR, "Cannot fill in Skates table!", e.getMessage());
        }
    }

    @FXML
    private void populateSkates(Skates skates) {
        ObservableList<Skates> skatesObservableList = FXCollections.observableArrayList();
        skatesObservableList.add(skates);
        skatesTable.setItems(skatesObservableList);
    }

    @FXML
    private void populateSkatesList(ObservableList<Skates> skatesObservableList) {
        skatesTable.setItems(skatesObservableList);
    }


    @FXML
    void search(ActionEvent event) {
        Map<String, String> params = prepareFilterParams();
        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("GET_SKATES", params));
            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                SkatesListDTO skatesListDTO = JsonUtil.deserialize(response.getBody(), SkatesListDTO.class);
                logger.debug(skatesListDTO);
                populateSkatesList(FXCollections.observableArrayList(skatesListDTO.getSkatesList()));
            } else {
                alert(Alert.AlertType.ERROR, "Cannot fill in Skates table!", response.getBody());
            }
        } catch (ClientException e) {
            logger.error(e);
            alert(Alert.AlertType.ERROR, "Cannot fill in Skates table!", e.getMessage());
        }
    }

    @FXML
    void getAllSkates(ActionEvent event) {
        fillTable();
    }

    @FXML
    void book(ActionEvent event) {
        String hoursText = bookForHoursTExtField.getText();

        if (hoursText != null && !hoursText.isEmpty()) {
            try {
                int hours = Integer.parseInt(hoursText);
                Map<String, String> params = prepareFilterParams();
                params.put("hours", hoursText);
                try {
                    ContextHolder.getClient().sendRequest(new CommandRequest("BOOK_SKATES", params));
                    CommandResponse response = Controller.getLastResponse();
                    logger.debug("Response " + response);
                    if (response.getStatus().is2xxSuccessful()) {
                        SkatesListDTO skatesListDTO = JsonUtil.deserialize(response.getBody(), SkatesListDTO.class);
                        logger.debug(skatesListDTO);
                        populateSkatesList(FXCollections.observableArrayList(skatesListDTO.getSkatesList()));

                        List<Equipment> equipment = ContextHolder.getSession().getVisitor().getContact().getBookedEquipment();
                        equipment.add(skatesListDTO.getSkatesList().get(0));
                        ContextHolder.getSession().getVisitor().getContact().setBookedEquipment(equipment);
                        alert("Successfully booked skates for " + hours + " hours!");
                    } else {
                        alert(Alert.AlertType.ERROR, "Cannot fill in Skates table!", response.getBody());
                    }
                } catch (ClientException e) {
                    logger.error(e);
                    alert(Alert.AlertType.ERROR, "Cannot fill in Skates table!", e.getMessage());
                }
            } catch (IllegalArgumentException e) {
                alert(Alert.AlertType.ERROR, "Invalid hours value!", "Use integers to define hours!");
            }

        }
    }

    @FXML
    void refresh(ActionEvent event) {
        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("REFRESH_SKATES"));
            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                SkatesListDTO skatesListDTO = JsonUtil.deserialize(response.getBody(), SkatesListDTO.class);
                logger.debug(skatesListDTO);
                populateSkatesList(FXCollections.observableArrayList(skatesListDTO.getSkatesList()));
                alert("Refreshed!");
            } else {
                alert(Alert.AlertType.ERROR, "Cannot refresh Skates info!", response.getBody());
            }
        } catch (ClientException e) {
            logger.error(e);
            alert(Alert.AlertType.ERROR, "Cannot refresh Skates info!", e.getMessage());
        }
    }

    private Map<String, String> prepareFilterParams() {
        String id = seachByIdTextField.getText();
        String type = searchByTypeComboBox.getValue();
        String size = searchBySizeTextField.getText();
        Map<String, String> params = new HashMap<>();
        if (id != null && !id.isEmpty()) {
            params.put("id", id);
        }
        if (type != null && !type.isEmpty()) {
            params.put("type", type);
        }
        if (size != null && !size.isEmpty()) {
            params.put("size", size);
        }
        return params;
    }

    public static boolean isFirstOpened() {
        return firstOpened;
    }

    public static void setFirstOpened(boolean firstOpened) {
        SkatesController.firstOpened = firstOpened;
    }
}
