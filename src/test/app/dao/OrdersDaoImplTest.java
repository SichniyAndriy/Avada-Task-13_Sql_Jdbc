package app.dao;

import app.TestUtil;
import app.model.Order;
import app.model.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Testing OrdersDaoImpl class")
class OrdersDaoImplTest {
    private OrdersDao ordersDao;
    private UsersDao usersDao;
    private Order testOrder;
    private User testUser;

    @BeforeAll
    void init() {
        ordersDao = new OrdersDaoImpl();
        usersDao = new UsersDaoImpl();
        testUser = TestUtil.getTestUserInstance();
        testUser.setId(usersDao.add(testUser));
        testOrder = Order.EMPTY();
        testOrder.setUser(testUser);
        testOrder.setDescription("BLA BLA BLA - 1");
        testOrder.setTimeStamp(LocalDateTime.now());
        testOrder.setId(ordersDao.add(testOrder));
    }

    @AfterAll
    void clear() {
        usersDao.delete(testUser);
        usersDao = null;
        ordersDao = null;
    }

    @Test
    void add() {
        Assertions.assertTrue(testOrder.getId() > 0);
    }

    @Test
    void getUserOrder() {
        Assertions.assertTrue(ordersDao.getUserOrder(testUser.getId()).size() > 0);
    }

    @Test
    void getAllOrders() {
        Assertions.assertTrue(ordersDao.getAllOrders().size() > 0);
    }
}
