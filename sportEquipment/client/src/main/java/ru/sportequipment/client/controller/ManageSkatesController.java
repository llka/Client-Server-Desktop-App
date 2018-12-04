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
import ru.sportequipment.entity.Skates;
import ru.sportequipment.entity.enums.SkatesType;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static ru.sportequipment.client.util.AlertUtil.alert;

public class ManageSkatesController {
    private static final Logger logger = LogManager.getLogger(ManageSkatesController.class);

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
    private TextField seachByIdTextField;
    @FXML
    private ComboBox<String> searchByTypeComboBox;
    @FXML
    private TextField searchBySizeTextField;

    @FXML
    private Button searchBtn;
    @FXML
    private Button getAllSkatesBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button updateSkatesBtn;
    @FXML
    private Button addSkatesBtn;

    @FXML
    private TextField idForUpdateTextField;
    @FXML
    private TextField sizeForUpdateTextField;
    @FXML
    private ComboBox<String> typeForUpdateComboBox;
    @FXML
    private TextField costForUpdateTextField;

    @FXML
    private void initialize() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);

        logger.debug("initialize");

        searchByTypeComboBox.setItems(FXCollections.observableArrayList(
                SkatesType.ICE_HOCKEY.toString(),
                SkatesType.FIGURE.toString(),
                SkatesType.ROLLER.toString()));

        typeForUpdateComboBox.setItems(FXCollections.observableArrayList(
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

    @FXML
    void addSkates(ActionEvent event) {
        String type = typeForUpdateComboBox.getValue();
        String size = sizeForUpdateTextField.getText();
        String cost = costForUpdateTextField.getText();
        Skates skates = new Skates();
        try {
            skates.setSize(Integer.parseInt(size));
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Invalid size!", "Invalid size!");
            return;
        }
        try {
            skates.setCostPerHour(new BigDecimal(cost));
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Invalid cost!", "Invalid cost!");
            return;
        }
        if (type != null && !type.isEmpty()) {
            skates.setType(SkatesType.valueOf(type));
            try {
                ContextHolder.getClient().sendRequest(new CommandRequest("CREATE_SKATES", JsonUtil.serialize(skates)));
                CommandResponse response = Controller.getLastResponse();
                logger.debug("Response " + response);
                if (response.getStatus().is2xxSuccessful()) {
                    alert("Successfully added new skates!");
                } else {
                    alert(Alert.AlertType.ERROR, "Cannot add new Skates!", response.getBody());
                }
            } catch (ClientException e) {
                logger.error(e);
                alert(Alert.AlertType.ERROR, "Cannot add new Skates!", e.getMessage());
            }

        } else {
            alert(Alert.AlertType.ERROR, "Skates Type not set!", "Skates Type not set!");
        }
    }

    @FXML
    void delete(ActionEvent event) {
        Map<String, String> params = prepareFilterParams();
        try {
            ContextHolder.getClient().sendRequest(new CommandRequest("DELETE_SKATES", params));
            CommandResponse response = Controller.getLastResponse();
            logger.debug("Response " + response);
            if (response.getStatus().is2xxSuccessful()) {
                fillTable();
                alert("Successfully deleted!");
            } else {
                alert(Alert.AlertType.ERROR, "Cannot fill in Skates table!", response.getBody());
            }
        } catch (ClientException e) {
            logger.error(e);
            alert(Alert.AlertType.ERROR, "Cannot fill in Skates table!", e.getMessage());
        }
    }

    @FXML
    void updateSkates(ActionEvent event) {
        String id = idForUpdateTextField.getText();
        String type = typeForUpdateComboBox.getValue();
        String size = sizeForUpdateTextField.getText();
        String cost = costForUpdateTextField.getText();
        Skates skates = new Skates();
        try {
            skates.setId(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Invalid id!", "Invalid id!");
            return;
        }
        try {
            skates.setSize(Integer.parseInt(size));
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Invalid size!", "Invalid size!");
            return;
        }
        try {
            skates.setCostPerHour(new BigDecimal(cost));
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Invalid cost!", "Invalid cost!");
            return;
        }
        if (type != null && !type.isEmpty()) {
            skates.setType(SkatesType.valueOf(type));
            try {
                ContextHolder.getClient().sendRequest(new CommandRequest("UPDATE_SKATES", JsonUtil.serialize(skates)));
                CommandResponse response = Controller.getLastResponse();
                logger.debug("Response " + response);
                if (response.getStatus().is2xxSuccessful()) {
                    alert("Successfully updated skates!");
                } else {
                    alert(Alert.AlertType.ERROR, "Cannot update Skates!", response.getBody());
                }
            } catch (ClientException e) {
                logger.error(e);
                alert(Alert.AlertType.ERROR, "Cannot update Skates!", e.getMessage());
            }

        } else {
            alert(Alert.AlertType.ERROR, "Skates Type not set!", "Skates Type not set!");
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
        ManageSkatesController.firstOpened = firstOpened;
    }
}
