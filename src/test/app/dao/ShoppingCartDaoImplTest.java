package app.dao;

import app.TestUtil;
import app.model.Product;
import app.model.User;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Testing ShoppingCartDaoImpl class")
class ShoppingCartDaoImplTest {
    private ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();
    private UsersDao usersDao = new UsersDaoImpl();
    private ProductsDao productsDao = new ProductsDaoImpl();
    private long testId;
    private User testUser;
    private Product testProduct;

    @BeforeAll
    void createUser() {
        usersDao = new UsersDaoImpl();
        testUser = TestUtil.getTestUserInstance();
        long testUId = usersDao.add(testUser);
        testUser.setId(testUId);

        productsDao = new ProductsDaoImpl();
        testProduct = TestUtil.getTestProductInstance();
        long testPId = productsDao.add(testProduct);
        testProduct.setId(testPId);
    }

    @AfterAll
    void clear() {
        usersDao.delete(testUser);
        usersDao = null;
        productsDao.delete(testProduct);
        productsDao = null;
        shoppingCartDao = null;
    }

    @BeforeEach
    void setUp() {
        testId = shoppingCartDao.addProduct(testUser.getId(), testProduct.getId(), 1);
    }

    @AfterEach
    void tearDown() {
        shoppingCartDao.deleteProduct(testId);
    }

    @Test
    void addProduct() {
        Assertions.assertTrue(testId > 0);
    }

    @Test
    void deleteProduct() {
        Assertions.assertTrue(shoppingCartDao.deleteProduct(testId));
    }

    @Test
    void getAllUserProducts() {
        Map<Product, Integer> userProducts = shoppingCartDao.getAllUserProducts(testUser.getId());
        Assertions.assertEquals(1, userProducts.size());
    }

    @Test
    void deleteAllUserProducts() {
        Assertions.assertTrue(shoppingCartDao.deleteAllUserProducts(testUser.getId()));
    }
}
