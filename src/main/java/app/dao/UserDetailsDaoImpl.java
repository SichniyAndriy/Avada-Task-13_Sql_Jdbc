package app.dao;

import app.ConnectionProvider;
import app.model.UserDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDetailsDaoImpl implements UserDetailsDao {
    private static final Logger logger = LogManager.getLogger(UserDetailsDaoImpl.class);

    @Override
    public long add(UserDetails userDetails) {
        String query =
                "INSERT INTO user_details(user_id, postal_code, city_id, street, house, ipn, passport) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement =
                    connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setLong(1, userDetails.getUser().getId());
            preparedStatement.setString(2, userDetails.getPostalCode());
            preparedStatement.setLong(3, DaoUtil.getCityId(userDetails.getCity(), connection));
            preparedStatement.setString(4, userDetails.getStreet());
            preparedStatement.setString(5, userDetails.getHouse());
            preparedStatement.setString(6, userDetails.getIpn());
            preparedStatement.setString(7, userDetails.getPassport());
            long key = 0;
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet keys = preparedStatement.getGeneratedKeys();
                keys.next();
                key = keys.getLong(1);
                keys.close();
            }
            logger.printf(Level.INFO,"Adding userDetails by id %d", key);
            return key;
        } catch (SQLException e) {
            logger.error("Error adding user details");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserDetails> getById(long id) {
        String query = "SELECT * FROM user_details WHERE id = ?";
        try(
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserDetails userDetails = null;
            if (resultSet.next()) {
                userDetails = DaoUtil.getUserDetailsInstance(resultSet, connection);
            }
            resultSet.close();
            logger.printf(Level.INFO, "Getting user detail by id %d", id);
            return Optional.ofNullable(userDetails);
        } catch (SQLException e) {
            logger.printf(Level.ERROR,"Error getting user detail by id %d", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserDetails> getAll() {
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM user_details")
        ) {
            List<UserDetails> userDetailsList = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                userDetailsList.add(DaoUtil.getUserDetailsInstance(resultSet, connection));
            }
            resultSet.close();
            logger.info("Getting all user details");
            return userDetailsList;
        } catch (SQLException e) {
            logger.error("Error of getting all user details");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(UserDetails userDetails) {
        String query =
                "UPDATE user_details " +
                        "SET postal_code = ?, city_id = ?, street = ?, house = ?, ipn = ?, passport = ? " +
                        "WHERE id = ?";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, userDetails.getPostalCode());
            preparedStatement.setLong(2, DaoUtil.getCityId(userDetails.getCity(), connection));
            preparedStatement.setString(3, userDetails.getStreet());
            preparedStatement.setString(4, userDetails.getHouse());
            preparedStatement.setString(5, userDetails.getIpn());
            preparedStatement.setString(6, userDetails.getPassport());
            preparedStatement.setLong(7, userDetails.getId());
            logger.printf(Level.INFO, "Updating user details by id %d", userDetails.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error updating user details by id %d", userDetails.getId());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(UserDetails userDetails) {
        String query = "DELETE FROM user_details WHERE id = ?";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, userDetails.getId());
            logger.printf(Level.INFO,"Deleted user details by id %d", userDetails.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.printf(Level.ERROR,"Error of deleting user details by id %d", userDetails.getId());
            throw new RuntimeException(e);
        }
    }
}
