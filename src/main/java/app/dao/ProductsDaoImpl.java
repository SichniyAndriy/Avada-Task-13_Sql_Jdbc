package app.dao;

import app.ConnectionProvider;
import app.model.Product;
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

public class ProductsDaoImpl implements ProductsDao {
    private static final Logger logger = LogManager.getLogger(ProductsDaoImpl.class);

    @Override
    public long add(Product product) {
        String query = "INSERT INTO products (name, price) VALUES (?, ?)";
        try(
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement =
                    connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            long key = 0;

            if (
                    DaoUtil.checkProductName(product.getName(), connection) == 0 &&
                    preparedStatement.executeUpdate() > 0
            ) {
                ResultSet keys = preparedStatement.getGeneratedKeys();
                keys.next();
                key = keys.getLong(1);
                keys.close();
            }
            logger.printf(Level.INFO,"Added product with id %d", key);
            return key;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of adding product %s", product.getName());
            throw new RuntimeException("Помилка додавання продукту", e);
        }
    }

    @Override
    public Optional<Product> getById(long id) {
        String query = "SELECT * FROM products WHERE id = ?";
        try(
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, id);
            Product product = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product = DaoUtil.getProductInstance(resultSet);
            }
            resultSet.close();
            logger.printf(Level.INFO,"Getting product by id %d", id);
            return Optional.of(product);
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of getting product by id %d", id);
            throw new RuntimeException("Помилка отримання продукту по Id", e);
        }
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM products";
        try (
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(query)
        ) {
            List<Product> productList = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                productList.add(DaoUtil.getProductInstance(resultSet));
            }
            logger.info("Get all products");
            resultSet.close();
            return productList;
        } catch (SQLException e) {
            logger.error("Error of get all products");
            throw new RuntimeException("Помилка отримання продуктів", e);
        }
    }

    @Override
    public boolean update(Product product) {
        String query =
                "UPDATE products " +
                "SET name = ?, price = ? " +
                "WHERE id = ?";
        try(
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setLong(3, product.getId());
            logger.printf(Level.INFO, "Updating product by id %d", product.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error updating product by id %d", product.getId());
            throw new RuntimeException("Помилка оновлення продукту", e);
        }
    }

    @Override
    public boolean delete(Product product) {
        String query = "DELETE FROM products WHERE id = ?";
        try(
                Connection connection = ConnectionProvider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, product.getId());
            logger.printf(Level.INFO, "Deleted product by id %d", product.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.printf(Level.ERROR, "Error of deleting product by id %d", product.getId());
            throw new RuntimeException("Помилка видалення продукту", e);
        }
    }
}
