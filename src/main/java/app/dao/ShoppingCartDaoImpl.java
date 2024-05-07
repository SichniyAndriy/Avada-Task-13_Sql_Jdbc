package app.dao;

import app.ConnectionProvider;
import app.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShoppingCartDaoImpl implements ShoppingCartDao {
    private static final Logger logger = LogManager.getLogger(ShoppingCartDaoImpl.class);

    @Override
    public long addProduct(long userId, long productId, int amount) {
        String query = "INSERT INTO shopping_cart(user_id, product_id, amount) VALUES (?,?,?)";
        try(
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, productId);
            preparedStatement.setInt(3, amount);
            long key = 0;
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet keys = preparedStatement.getGeneratedKeys();
                keys.next();
                key = keys.getLong(1);
                keys.close();
            }
            logger.printf(Level.INFO, "Add record in shopping_cart by id %d ", key);
            return key;
        } catch (SQLException e) {
            logger.info("Error of adding record in shopping_cart");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteProduct(long id) {
        String query = "DELETE FROM shopping_cart WHERE id = ?";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, id);
            logger.printf(Level.INFO, "Delete product by id %d", id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of deleting product by id %d", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Product, Integer> getAllUserProducts(long userId) {
        String query =
                "SELECT p.id, p.name, p.price, shc.amount " +
                "FROM shopping_cart AS shc " +
                "INNER JOIN products AS p ON shc.product_id = p.id " +
                "WHERE shc.user_id = ?";
        try(
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<Product, Integer> userProducts = new HashMap<>();
            while (resultSet.next()) {
                Product product = Product.EMPTY();
                product.setId(resultSet.getLong(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getBigDecimal(3));
                userProducts.put(product, resultSet.getInt(4));
            }
            logger.printf(Level.INFO, "Get list all products by user id %d", userId);
            resultSet.close();
            return userProducts;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of getting list all products by user id %d", userId);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteAllUserProducts(long userId) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ?";
        try(
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){
            preparedStatement.setLong(1, userId);
            logger.printf(Level.INFO, "Delete list all products by user id %d", userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of deleting list all products by user id %d", userId);
            throw new RuntimeException(e);
        }
    }
}
