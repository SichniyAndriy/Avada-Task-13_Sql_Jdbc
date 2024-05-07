package app.dao;

import app.model.Order;
import java.util.List;

public interface OrdersDao {
    long add(Order order);

    List<Order> getUserOrder(long userId);

    List<Order> getAllOrders();
}
