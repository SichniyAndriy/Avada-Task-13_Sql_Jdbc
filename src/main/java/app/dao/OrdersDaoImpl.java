package app.dao;

import app.ConnectionProvider;
import app.model.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrdersDaoImpl implements OrdersDao {
    private static final Logger logger = LogManager.getLogger(OrdersDaoImpl.class);

    @Override
    public long add(Order order) {
        String query = "INSERT INTO orders (user_id, total_price, description, order_date) VALUES (?, ?, ?, ?)";
        try(
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setLong(1 , order.getUser().getId());
            preparedStatement.setBigDecimal(2, order.getTotalPrice());
            preparedStatement.setString(3, order.getDescription());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getTimeStamp()));
            long id = 0;
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet keys = preparedStatement.getGeneratedKeys();
                keys.next();
                id = keys.getLong(1);
                keys.close();
            }
            logger.printf(Level.INFO, "Add order with id %d", id);
            return id;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of adding order with user id %d", order.getUser().getId());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getUserOrder(long userId) {
        String query = "SELECT * FROM orders WHERE user_id = ?";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = Order.EMPTY();
                order.setId(resultSet.getLong(1));
                order.setUser(DaoUtil.getUserById(resultSet.getLong(2), connection));
                order.setTotalPrice(resultSet.getBigDecimal(3));
                order.setDescription(resultSet.getString(4));
                order.setTimeStamp(resultSet.getTimestamp(5).toLocalDateTime());
                orders.add(order);
            }
            logger.printf(Level.INFO, "Get orders by user id %d", userId);
            resultSet.close();
            return orders;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of getting orders by user id %d", userId);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        String query = "SELECT * FROM orders";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = Order.EMPTY();
                order.setId(resultSet.getLong(1));
                order.setUser(DaoUtil.getUserById(resultSet.getLong(2), connection));
                order.setTotalPrice(resultSet.getBigDecimal(3));
                order.setDescription(resultSet.getString(4));
                order.setTimeStamp(resultSet.getTimestamp(5).toLocalDateTime());
                orders.add(order);
            }
            resultSet.close();
            logger.info("Get all order list");
            return orders;
        } catch (SQLException e) {
            logger.error("Error of getting all order list");
            throw new RuntimeException(e);
        }
    }
}
