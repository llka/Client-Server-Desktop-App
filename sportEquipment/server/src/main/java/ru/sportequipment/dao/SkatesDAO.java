package ru.sportequipment.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.datebase.ConnectionPool;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Skates;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.entity.enums.SkatesType;
import ru.sportequipment.exception.DataBaseException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SkatesDAO extends EquipmentDAO {
    private static final Logger logger = LogManager.getLogger(SkatesDAO.class);

    private static final String SAVE = "INSERT INTO `skates` (`type`, `size`, `cost_per_hour`, `booked_from`, `booked_to`) " +
            " VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE = "UPDATE `skates` SET `type` = ?, `size` = ?, `cost_per_hour` = ?, `booked_from` = ?, `booked_to` = ? " +
            " WHERE `skates_id`= ?;";

    private static final String GET_BY_ID = "SELECT `skates_id`, `type`, `size`, `cost_per_hour`, `booked_from`, `booked_to` " +
            " FROM `skates` WHERE `skates_id` = ?)";
    private static final String GET_ALL = "SELECT `skates_id`, `type`, `size`, `cost_per_hour`, `booked_from`, `booked_to` " +
            " FROM `skates`";
    private static final String GET_CONTACTS_SKATES = "SELECT `skates_id`, `type`, `size`, `cost_per_hour`, `booked_from`, `booked_to` " +
            " FROM `contact` " +
            " JOIN `contact_has_skates` ON `contact_has_skates`.`contact_contact_id` = ? " +
            " JOIN `skates` ON `contact_has_skates`.`skates_skates_id` = `skates`.`skates_id` " +
            " WHERE `contact_id` = ?";

    private static final String DELETE = "DELETE FROM `skates` WHERE `skates_id`= ?";

    private static final String BOOK_SKATES = "INSERT INTO `contact_has_skates` (`contact_contact_id`, `skates_skates_id`)" +
            " VALUES (?, ?)";

    private static final String COLUMN_SKATES_ID = "skates_id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_SIZE = "size";


    public void save(@Valid Skates skates) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
            preparedStatement.setString(1, skates.getType().toString());
            preparedStatement.setInt(2, skates.getSize());
            preparedStatement.setBigDecimal(3, skates.getCostPerHour());
            preparedStatement.setString(4, skates.getBookedFrom().toString());
            preparedStatement.setString(5, skates.getBookedTo().toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException("Cannot save skates in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public Skates update(@Valid Skates skates) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, skates.getType().toString());
            preparedStatement.setInt(2, skates.getSize());
            preparedStatement.setBigDecimal(3, skates.getCostPerHour());
            preparedStatement.setString(4, skates.getBookedFrom().toString());
            preparedStatement.setString(5, skates.getBookedTo().toString());
            preparedStatement.setInt(6, skates.getId());
            preparedStatement.execute();
            return skates;
        } catch (SQLException e) {
            throw new DataBaseException("Cannot update skates in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public Skates getById(@Positive int id) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return buildSkates(resultSet);
            } else {
                throw new DataBaseException("No skates with such id " + id + " found in database.", ResponseStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find skates in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public List<Skates> getAll() throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Skates> skates = new ArrayList<>();
            while (resultSet.next()) {
                skates.add(buildSkates(resultSet));
            }
            return skates;
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find skates in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public List<Skates> getContactsSkates(@Positive int contactId) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CONTACTS_SKATES)) {
            preparedStatement.setInt(1, contactId);
            preparedStatement.setInt(2, contactId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Skates> skates = new ArrayList<>();
            while (resultSet.next()) {
                skates.add(buildSkates(resultSet));
            }
            return skates;
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find contact's with id = " + contactId + " skates in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public void delete(@Valid Skates skates) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, skates.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException("Cannot delete skates  " + skates + " " + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public void bookSkates(Contact contact, Skates skates) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BOOK_SKATES)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find skates in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    private Skates buildSkates(ResultSet resultSet) throws DataBaseException {
        Skates skates = new Skates();
        try {
            skates.setId(resultSet.getInt(COLUMN_SKATES_ID));
            skates.setType(SkatesType.valueOf(resultSet.getString(COLUMN_TYPE)));
            skates.setSize(resultSet.getInt(COLUMN_SIZE));
            buildEquipment(skates, resultSet);
            return skates;
        } catch (SQLException e) {
            throw new DataBaseException("Error while building skates!" + e, ResponseStatus.BAD_REQUEST);
        }
    }


}
