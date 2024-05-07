package app.dao;

import app.model.Product;
import java.util.Map;

public interface ShoppingCartDao {
    long addProduct(long userId, long productId, int amount);

    boolean deleteProduct(long id);

    Map<Product, Integer> getAllUserProducts(long userId);

    boolean deleteAllUserProducts(long userId);
}
