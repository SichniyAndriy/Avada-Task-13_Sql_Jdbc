package app.dao;

import app.TestUtil;
import app.model.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Testing ProductsDaoImpl.class")
class ProductsDaoImplTest {
    ProductsDao productsDao;
    Product testProduct;

    @BeforeAll
    void init() {
        productsDao = new ProductsDaoImpl();
    }

    @AfterAll
    void del() {
        productsDao = null;
    }

    @BeforeEach
    void setUp() {
        testProduct = TestUtil.getTestProductInstance();
        testProduct.setId(productsDao.add(testProduct));
    }

    @AfterEach
    void tearDown() {
        productsDao.delete(testProduct);
        testProduct = null;
    }

    @Test
    void add() {
        assertTrue(testProduct.getId() > 0);
    }

    @Test
    void getById() {
        Optional<Product> byId = productsDao.getById(testProduct.getId());
        assertTrue(byId.isPresent());
        Product localProduct = byId.get();
        assertEquals(testProduct.getName(), localProduct.getName());
        assertEquals(testProduct.getPrice(), localProduct.getPrice());
    }

    @Test
    void getAll() {
        List<Product> products = productsDao.getAll();
        assertEquals(21, products.size());
    }

    @Test
    void update() {
        String newName = "new name";
        testProduct.setName(newName);
        assertTrue(productsDao.update(testProduct));
    }

    @Test
    void delete() {
        assertTrue(productsDao.delete(testProduct));
    }
}
