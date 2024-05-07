package app.service;

import app.TestUtil;
import app.dao.ProductsDao;
import app.dao.ProductsDaoImpl;
import app.dao.UsersDao;
import app.dao.UsersDaoImpl;
import app.model.Product;
import app.model.User;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testing OrderService class")
class OrderServiceTest {
    private OrderService orderService;
    private UsersDao usersDao;
    private ProductsDao productsDao;
    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
        usersDao = new UsersDaoImpl();
        productsDao = new ProductsDaoImpl();

        testUser = TestUtil.getTestUserInstance();
        testUser.setId(usersDao.add(testUser));

        testProduct = TestUtil.getTestProductInstance();
        testProduct.setId(productsDao.add(testProduct));
    }

    @AfterEach
    void tearDown() {
        orderService = null;
        usersDao.delete(testUser);
        usersDao = null;
        productsDao.delete(testProduct);
        productsDao = null;
    }

    @Test
    void test_formOrderByUser() {
        Map<Product, Integer> map = new HashMap<>();
        map.put(testProduct, 1);
        Assertions.assertTrue(orderService.formOrderByUser(testUser, map));
    }
}
