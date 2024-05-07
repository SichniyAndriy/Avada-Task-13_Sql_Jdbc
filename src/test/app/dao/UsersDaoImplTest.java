package app.dao;

import app.TestUtil;
import app.model.User;
import java.sql.SQLException;
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
@DisplayName("Testing UsersDaoImpl.class")
class UsersDaoImplTest {
    UsersDao usersDao;
    private User testUser;
    private long testId;

    @BeforeAll
    void setUp() throws SQLException {
        usersDao = new UsersDaoImpl();
    }

    @AfterAll
    void tearDown() throws SQLException {
        usersDao = null;
    }

    @BeforeEach
    void addUser() {
        testUser = TestUtil.getTestUserInstance();
        testId = usersDao.add(testUser);
        testUser.setId(testId);
    }

    @AfterEach
    void deleteUser() {
        usersDao.delete(testUser);
        testUser = null;
    }

    @Test
    void add() {
        assertTrue(testId > 0);
    }

    @Test
    void getById() {
        Optional<User> user = usersDao.getById(testId);
        assertTrue(user.isPresent());
        assertEquals(testUser.getFirstName(), user.get().getFirstName());
        assertEquals(testUser.getLastName(), user.get().getLastName());
        assertEquals(testUser.getEmail(), user.get().getEmail());
    }

    @Test
    void getAll() {
        List<User> users = usersDao.getAll();
        assertTrue(users.size() > 25);
    }

    @Test
    void update() {
        String phoneNumber = "+38(067)987-65-43";
        testUser.setPhone(phoneNumber);
        usersDao.update(testUser);
        Optional<User> userById = usersDao.getById(testId);
        assertTrue(userById.isPresent());
        assertEquals(phoneNumber, userById.get().getPhone());
    }

    @Test
    void delete() {
        assertTrue(usersDao.delete(testUser));
    }
}
