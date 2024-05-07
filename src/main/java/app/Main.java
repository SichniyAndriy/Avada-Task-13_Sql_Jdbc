package app;

import app.dao.ProductsDao;
import app.dao.ProductsDaoImpl;
import app.dao.ShoppingCartDao;
import app.dao.ShoppingCartDaoImpl;
import app.dao.UserDetailsDao;
import app.dao.UserDetailsDaoImpl;
import app.dao.UsersDao;
import app.dao.UsersDaoImpl;
import app.model.Product;
import app.model.User;
import app.model.UserDetails;
import app.service.OrderService;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.datafaker.Faker;

public class Main {
    private final static Faker FAKER = new Faker();

    public static void main(String[] args) throws SQLException {
        UsersDao usersDao = new UsersDaoImpl();
//        fillUsers(usersDao);
        List<User> users = usersDao.getAll();
//        for (User user: users) {
//            System.out.println(user);
//        }
        System.out.println();

        UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();
//        fillUserDetails(userDetailsDao);
        List<UserDetails> userDetails = userDetailsDao.getAll();
//        for (UserDetails userDetails : details) {
//            System.out.println(userDetails);
//        }
        System.out.println();

        ProductsDao productsDao = new ProductsDaoImpl();
//        fillProducts(productsDao);
        List<Product> products = productsDao.getAll();
//        for (Product product: products) {
//            System.out.println(product);
//        }

        ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();
        OrderService orderService = new OrderService();
        fillShoppingCart(shoppingCartDao, users, products);
        for (User user: users) {
            Map<Product, Integer> userProducts = shoppingCartDao.getAllUserProducts(user.getId());
            if (!userProducts.isEmpty()) {
                orderService.formOrderByUser(user, userProducts);
            }
        }
    }

    private static void fillUsers(UsersDao usersDao) {
        for (int i = 0; i < 25; i++) {
            User user = User.EMPTY();
            user.setFirstName(FAKER.name().firstName());
            user.setLastName(FAKER.name().lastName());
            user.setEmail(FAKER.internet().emailAddress());
            user.setPhone(FAKER.phoneNumber().cellPhone());
            user.setTimeStamp(FAKER.date().past(50000, 50, TimeUnit.HOURS).toLocalDateTime());
            System.out.println(usersDao.add(user));
        }
    }

    private static void fillUserDetails(UserDetailsDao userDetailsDao) {
        UsersDao usersDao = new UsersDaoImpl();
        List<User> users = usersDao.getAll();

        for (int i = 0; i < 15; ++i) {
            UserDetails userDetails = UserDetails.EMPTY();
            userDetails.setPostalCode(FAKER.address().postcode());
            User user = users.get(FAKER.random().nextInt(users.size()));
            users.remove(user);
            userDetails.setUser(user);
            userDetails.setCity(FAKER.address().cityName());
            userDetails.setStreet(FAKER.address().streetName());
            userDetails.setHouse(FAKER.address().buildingNumber());
            userDetails.setIpn(FAKER.numerify("##########"));
            userDetails.setPassport(FAKER.bothify("??######", true));
            userDetailsDao.add(userDetails);
        }
    }

    private static void fillProducts(ProductsDao productDao) {
        for (int i = 0; i < 10; ++i) {
            Product product = Product.EMPTY();
            product.setName(FAKER.camera().brandWithModel());
            product.setPrice(BigDecimal.valueOf(Double.parseDouble(FAKER.commerce().price(500., 2500.))));
            productDao.add(product);
        }
    }

    private static void fillShoppingCart(ShoppingCartDao shoppingCartDao, List<User> users, List<Product> products) {
        ArrayList<User> users1 = new ArrayList<>(users);
        for (int i = 0; i < 7; i++) {
            int i1 = FAKER.random().nextInt(users1.size());
            User user = users1.get(i1);
            ArrayList<Product> products1 = new ArrayList<>(products);
            Integer len = FAKER.random().nextInt(1, 4);
            for (int j = 0; j < len; j++) {
                int i2 = FAKER.random().nextInt(products1.size());
                Product product = products1.get(i2);
                products1.remove(i2);
                shoppingCartDao.addProduct(user.getId(), product.getId(), FAKER.random().nextInt(1, 3));
            }
            users1.remove(i1);
        }
    }
}
