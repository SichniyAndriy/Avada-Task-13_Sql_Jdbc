package app.dao;

import app.model.Product;
import app.model.User;
import app.model.UserDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoUtil {
    static User getUserById(long id, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = getUserInstance(resultSet);
        }
        resultSet.close();
        return user;
    }


    static String getCityName(long id, Connection connection) throws SQLException {
        PreparedStatement getCityName =
                connection.prepareStatement("SELECT name FROM cities WHERE id = ?");
        getCityName.setLong(1, id);
        ResultSet executed = getCityName.executeQuery();
        executed.next();
        String city = executed.getString(1);
        executed.close();
        return  city;
    }

    static long getCityId(String city, Connection connection) throws SQLException {
        PreparedStatement getCityId =
                connection.prepareStatement("SELECT id FROM cities WHERE name = ?");
        getCityId.setString(1, city);
        ResultSet resultSet = getCityId.executeQuery();
        long cityId;
        if (resultSet.next()) {
            cityId = resultSet.getLong(1);
        } else {
            String query = "INSERT INTO cities(name) VALUES(?)";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, city);
            preparedStatement.execute();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            cityId = keys.getLong(1);
            keys.close();
        }
        resultSet.close();
        return cityId;
    }

    static User getUserInstance(ResultSet resultSet) throws SQLException {
        User user = User.EMPTY();
        user.setId(resultSet.getLong(1));
        user.setFirstName(resultSet.getString(2));
        user.setLastName(resultSet.getString(3));
        user.setEmail(resultSet.getString(4));
        user.setPhone(resultSet.getString(5));
        user.setTimeStamp(resultSet.getTimestamp(6).toLocalDateTime());
        return user;
    }

    static UserDetails getUserDetailsInstance(ResultSet resultSet, Connection connection) throws SQLException {
        UserDetails userDetails = UserDetails.EMPTY();
        userDetails.setId(resultSet.getLong(1));
        userDetails.setUser(getUserById(resultSet.getLong(2), connection));
        userDetails.setPostalCode(resultSet.getString(3));
        userDetails.setCity(getCityName(resultSet.getLong(4), connection));
        userDetails.setStreet(resultSet.getString(5));
        userDetails.setHouse(resultSet.getString(6));
        userDetails.setIpn(resultSet.getString(7));
        userDetails.setPassport(resultSet.getString(8));
        return userDetails;
    }

    static Product getProductInstance(ResultSet resultSet) throws SQLException {
        Product product = Product.EMPTY();
        product.setId(resultSet.getLong(1));
        product.setName(resultSet.getString(2));
        product.setPrice(resultSet.getBigDecimal(3));
        return  product;
    }

    static long checkProductName(String name, Connection connection) throws SQLException {
        String query = "SELECT id FROM products WHERE name = ?";
        PreparedStatement preparedStatement =
                connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        long id = 0;
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getLong(1);
        }
        resultSet.close();
        return id;
    }
}
