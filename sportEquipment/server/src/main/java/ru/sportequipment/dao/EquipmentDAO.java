package ru.sportequipment.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.datebase.ConnectionPool;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Equipment;
import ru.sportequipment.entity.Skates;
import ru.sportequipment.entity.Stick;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EquipmentDAO {
    private static final Logger logger = LogManager.getLogger(EquipmentDAO.class);
    private static final String DATE_PATTERN = "yyyy-MM-dd-HH:mm:ss";

    private static final String BOOK_SKATES = "INSERT INTO `contact_has_skates` (`contact_contact_id`, `skates_skates_id`) " +
            " VALUES (?, ?)";
    private static final String BOOK_STICK = "INSERT INTO `contact_has_stick` (`contact_contact_id`, `stick_stick_id`) " +
            " VALUES (?, ?)";


    private static final String COLUMN_COST_PER_HOUR = "cost_per_hour";
    private static final String COLUMN_BOOKED_FROM = "booked_from";
    private static final String COLUMN_BOOKED_TO = "booked_to";

    public void bookEquipment(Contact contact, Skates skates) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BOOK_SKATES)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find skates in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public void bookEquipment(Contact contact, Stick stick) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BOOK_STICK)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find stick in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }


    protected Equipment buildEquipment(Equipment equipment, ResultSet resultSet) throws DataBaseException {
        try {
            equipment.setCostPerHour(resultSet.getBigDecimal(COLUMN_COST_PER_HOUR));
            equipment.setBookedFrom(resultSet.getDate(COLUMN_BOOKED_FROM));
            equipment.setBookedTo(resultSet.getDate(COLUMN_BOOKED_TO));
            return equipment;
        } catch (SQLException e) {
            throw new DataBaseException("Error while building equipment!" + e, ResponseStatus.BAD_REQUEST);
        }
    }


}
