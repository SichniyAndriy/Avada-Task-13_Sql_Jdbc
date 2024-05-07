package app;

import app.model.Product;
import app.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestUtil {
    public static User getTestUserInstance() {
        User testUser = User.EMPTY();
        testUser.setFirstName("test_first_name");
        testUser.setLastName("test_last_name");
        testUser.setEmail("test@email.com");
        testUser.setPhone("test_number");
        testUser.setTimeStamp(LocalDateTime.now());
        return testUser;
    }

    public static Product getTestProductInstance() {
        Product testProduct = Product.EMPTY();
        testProduct.setName("test_product");
        testProduct.setPrice(BigDecimal.valueOf(99.99));
        return testProduct;
    }
}
