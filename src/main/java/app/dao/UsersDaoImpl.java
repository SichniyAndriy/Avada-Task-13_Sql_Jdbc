package app.dao;

import app.ConnectionProvider;
import app.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsersDaoImpl implements UsersDao {
    private final Logger logger = LogManager.getLogger(UsersDaoImpl.class);

    @Override
    public long add(User user) {
        String query =
                "INSERT INTO users (first_name, last_name, email, phone, registration) " +
                "VALUES (?, ?, ?, ?, ?);";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement =
                    connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPhone());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(user.getTimeStamp()));
            long key = 0;
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet keys = preparedStatement.getGeneratedKeys();
                keys.next();
                key = keys.getLong(1);
                keys.close();
            }
            logger.printf(Level.INFO, "Added user by id %d", key);
            return key;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of adding user %s %s", user.getFirstName(), user.getLastName());
            throw new RuntimeException("Помилка додавання користувача", e);
        }
    }

    @Override
    public Optional<User> getById(long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
               user = DaoUtil.getUserInstance(resultSet);
            }
            resultSet.close();
            logger.printf(Level.INFO,"Getting user by id %d", id);
            return Optional.of(user);
        } catch (SQLException e) {
            logger.printf(Level.ERROR,"Error getting user by id %d", id);
            throw new RuntimeException("Помилка отриманя користувача " + id, e);
        }
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM users";
        try (
                Connection connection = ConnectionProvider.getConnection();
                Statement statement = connection.createStatement();
        ) {
            List<User> users = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                users.add(DaoUtil.getUserInstance(resultSet));
            }
            resultSet.close();
            logger.info("Get all user from table");
            return users;
        } catch (SQLException e) {
            logger.error("Error of getting all user from table");
            throw new RuntimeException("Помилка отримання списку користувачів", e);
        }
    }

    @Override
    public boolean update(User user) {
        String query =
                "UPDATE users " +
                "SET first_name = ?, last_name = ?, email = ?, phone = ? " +
                "WHERE id = ?";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPhone());
            preparedStatement.setLong(5, user.getId());
            logger.printf(Level.INFO,"Update user by id %d", user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of updating user by id %d", user.getId());
            throw new RuntimeException("Помилка оновлення користувача " + user.getId() , e);
        }
    }

    @Override
    public boolean delete(User user) {
        String query = "DELETE FROM users WHERE id = ?";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setLong(1, user.getId());
            logger.printf(Level.INFO, "Delete user by id %d", user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.printf(Level.ERROR,"Error of deleting user by id %d", user.getId());
            throw new RuntimeException("Помилка видалення користувача " + user.getId(), e);
        }
    }
}
