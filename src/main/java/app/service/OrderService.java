package app.service;

import app.dao.OrdersDao;
import app.dao.OrdersDaoImpl;
import app.dao.ShoppingCartDao;
import app.dao.ShoppingCartDaoImpl;
import app.model.Order;
import app.model.Product;
import app.model.User;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderService {
    private static final Logger logger = LogManager.getLogger(OrderService.class);
    ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();
    OrdersDao ordersDao = new OrdersDaoImpl();


    public boolean formOrderByUser(User user, Map<Product, Integer> userProducts) {
        if (!userProducts.isEmpty()) {
            Faker faker = new Faker();
            StringBuilder stringBuilder = new StringBuilder();
            BigDecimal summ = BigDecimal.ZERO;
            logger.info("Start form order data");
            for (var entry: userProducts.entrySet()) {
                Product product = entry.getKey();
                int amount = entry.getValue();
                stringBuilder.append(product.getName()).append(": ").append(amount).append("\n");
                summ = summ.add(product.getPrice().multiply(BigDecimal.valueOf(amount)));
            }
            logger.info("Save order data to db");
            Order order = Order.EMPTY();
            order.setUser(user);
            order.setTotalPrice(summ);
            order.setDescription(stringBuilder.toString());
            order.setTimeStamp(faker.date().past(45000, 10, TimeUnit.HOURS).toLocalDateTime());
            logger.info("Clear shopping cart");
            shoppingCartDao.deleteAllUserProducts(user.getId());
            return ordersDao.add(order) > 0;
        }
        return false;
    }
}
