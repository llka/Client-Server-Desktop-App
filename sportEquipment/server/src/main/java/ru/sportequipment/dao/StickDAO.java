package ru.sportequipment.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.datebase.ConnectionPool;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Stick;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.entity.enums.StickType;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.exception.DataBaseException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StickDAO extends EquipmentDAO {
    private static final Logger logger = LogManager.getLogger(StickDAO.class);
    private static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String SAVE = "INSERT INTO `stick` (`type`, `cost_per_hour`, `booked_from`, `booked_to`) " +
            " VALUES (?, ?, ?, ?);";
    private static final String UPDATE = "UPDATE `stick` SET `type` = ?, `cost_per_hour` = ?, `booked_from` = ?, `booked_to` = ? " +
            " WHERE `stick_id`= ?;";
    private static final String BOOK_STICK = "INSERT INTO `contact_has_stick` (`contact_contact_id`, `stick_stick_id`) " +
            " VALUES (?, ?)";

    private static final String GET_BY_ID = "SELECT `stick_id`, `type`, `cost_per_hour`, `booked_from`, `booked_to` " +
            " FROM `stick` WHERE `stick_id` = ?";
    private static final String GET_ALL = "SELECT `stick_id`, `type`, `cost_per_hour`, `booked_from`, `booked_to` " +
            "     FROM `stick`";
    private static final String FIND_STICKS = "SELECT `stick_id`, `type`, `cost_per_hour`, `booked_from`, `booked_to` " +
            " FROM `stick` WHERE ";

    private static final String GET_CONTACTS_STICKS = "SELECT `stick_id`, `type`, `cost_per_hour`, `booked_from`, `booked_to` " +
            " FROM `contact` " +
            " JOIN `contact_has_stick` ON `contact_has_stick`.`contact_contact_id` = ? " +
            " JOIN `stick` ON `contact_has_stick`.`stick_stick_id` = `stick`.`stick_id` " +
            " WHERE `contact_id` = ? ";

    private static final String DELETE = "DELETE FROM `stick` WHERE `stick_id`= ?";

    private static final String COLUMN_STICK_ID = "stick_id";
    private static final String COLUMN_TYPE = "type";


    public void save(@Valid Stick stick) throws DataBaseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
            preparedStatement.setString(1, stick.getStickType().toString());
            preparedStatement.setBigDecimal(2, stick.getCostPerHour());
            if (stick.getBookedFrom() != null) {
                preparedStatement.setString(3, dateFormat.format(stick.getBookedFrom()));
            } else {
                preparedStatement.setString(3, null);
            }
            if (stick.getBookedTo() != null) {
                preparedStatement.setString(4, dateFormat.format(stick.getBookedTo()));
            } else {
                preparedStatement.setString(4, null);
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException("Cannot save stick in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public Stick update(@Valid Stick stick) throws DataBaseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, stick.getStickType().toString());
            preparedStatement.setBigDecimal(2, stick.getCostPerHour());
            if (stick.getBookedFrom() != null) {
                preparedStatement.setString(3, dateFormat.format(stick.getBookedFrom()));
            } else {
                preparedStatement.setString(3, null);
            }
            if (stick.getBookedTo() != null) {
                preparedStatement.setString(4, dateFormat.format(stick.getBookedTo()));
            } else {
                preparedStatement.setString(4, null);
            }
            preparedStatement.setInt(5, stick.getId());
            preparedStatement.execute();
            return stick;
        } catch (SQLException e) {
            throw new DataBaseException("Cannot update stick in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public Stick getById(@Positive int id) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return buildStick(resultSet);
            } else {
                throw new DataBaseException("No stick with such id " + id + " found in database.", ResponseStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find stick in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public List<Stick> getAll() throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Stick> sticks = new ArrayList<>();
            while (resultSet.next()) {
                sticks.add(buildStick(resultSet));
            }
            return sticks;
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find stick in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public List<Stick> findSticks(Map<String, String> filterParams) throws ApplicationException {
        StringBuilder query = new StringBuilder(FIND_STICKS);
        final String ID_PARAM = "id";
        final String TYPE_PARAM = "type";
        boolean and = false;
        boolean findable = false;

        if (filterParams.get(ID_PARAM) != null) {
            query.append(" `stick_id` = ? ");
            and = true;
            findable = true;
        }
        if (filterParams.get(TYPE_PARAM) != null) {
            if (and) {
                query.append(" AND ");
            }
            query.append(" `type` = ? ");
            and = true;
            findable = true;
        }

        if (!findable) {
            query = new StringBuilder(GET_ALL);
        }

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {

            int index = 1;
            if (filterParams.get(ID_PARAM) != null) {
                try {
                    preparedStatement.setInt(index++, Integer.parseInt(filterParams.get(ID_PARAM)));
                } catch (NumberFormatException e) {
                    throw new ApplicationException("Invalid id!", ResponseStatus.BAD_REQUEST);
                }
            }
            if (filterParams.get(TYPE_PARAM) != null) {
                preparedStatement.setString(index++, filterParams.get(TYPE_PARAM));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Stick> sticks = new ArrayList<>();
            while (resultSet.next()) {
                sticks.add(buildStick(resultSet));
            }
            return sticks;
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find sticks in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }


    public List<Stick> getContactsSticks(@Positive int contactId) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CONTACTS_STICKS)) {
            preparedStatement.setInt(1, contactId);
            preparedStatement.setInt(2, contactId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Stick> sticks = new ArrayList<>();
            while (resultSet.next()) {
                sticks.add(buildStick(resultSet));
            }
            return sticks;
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find contact's with id = " + contactId + " sticks in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public void delete(@Valid Stick stick) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, stick.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException("Cannot delete stick  " + stick + " " + e, ResponseStatus.BAD_REQUEST);
        }
    }

    public void bookStick(Contact contact, Stick stick) throws DataBaseException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BOOK_STICK)) {
            preparedStatement.setInt(1, contact.getId());
            preparedStatement.setInt(2, stick.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException("Cannot find stick in database." + e, ResponseStatus.BAD_REQUEST);
        }
    }

    private Stick buildStick(ResultSet resultSet) throws DataBaseException {
        Stick stick = new Stick();
        try {
            stick.setId(resultSet.getInt(COLUMN_STICK_ID));
            stick.setStickType(StickType.valueOf(resultSet.getString(COLUMN_TYPE)));
            buildEquipment(stick, resultSet);
            return stick;
        } catch (SQLException e) {
            throw new DataBaseException("Error while building stick!" + e, ResponseStatus.BAD_REQUEST);
        }
    }


}
