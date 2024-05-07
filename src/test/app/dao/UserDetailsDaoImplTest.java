package app.dao;

import app.TestUtil;
import app.model.User;
import app.model.UserDetails;
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
@DisplayName("Testing UserDetailsDaoImpl.class")
class UserDetailsDaoImplTest {
    private UserDetailsDao userDetailsDao;
    private UsersDao usersDao;
    private UserDetails tmpUsersDetails;
    private long tmpUDId;
    private User testUser;

    @BeforeAll
    void setDao() {
        userDetailsDao = new UserDetailsDaoImpl();
        usersDao = new UsersDaoImpl();
        testUser = TestUtil.getTestUserInstance();
        long testUId = usersDao.add(testUser);
        testUser.setId(testUId);
    }

    @AfterAll
    void clearDao() {
        userDetailsDao = null;
        usersDao.delete(testUser);
        usersDao = null;
    }

    @BeforeEach
    void setUp() {
        tmpUsersDetails = UserDetails.EMPTY();
        tmpUsersDetails.setUser(testUser);
        tmpUsersDetails.setPostalCode("postal");
        tmpUsersDetails.setCity("test_city");
        tmpUsersDetails.setStreet("test_street");
        tmpUsersDetails.setHouse("house");
        tmpUsersDetails.setIpn("0123456789");
        tmpUsersDetails.setPassport("testpspr");
        tmpUDId = userDetailsDao.add(tmpUsersDetails);
        tmpUsersDetails.setId(tmpUDId);
    }

    @AfterEach
    void tearDown() {
        userDetailsDao.delete(tmpUsersDetails);
        tmpUsersDetails = null;
        tmpUDId = 0;
    }

    @Test
    void add() {
        assertTrue(tmpUDId > 0);
    }

    @Test
    void getById() {
        Optional<UserDetails> byId = userDetailsDao.getById(tmpUDId);
        assertTrue(byId.isPresent());
        UserDetails local = byId.get();
        assertEquals(tmpUsersDetails.getPassport(), local.getPassport());
        assertEquals(tmpUsersDetails.getIpn(), local.getIpn());
        assertEquals(tmpUsersDetails.getStreet(), local.getStreet());

    }

    @Test
    void getAll() {
        List<UserDetails> detailsList = userDetailsDao.getAll();
        assertEquals(16, detailsList.size());
    }

    @Test
    void update() {
        String line = "newpassp";
        tmpUsersDetails.setPassport(line);
        assertTrue(userDetailsDao.update(tmpUsersDetails));
    }

    @Test
    void delete() {
        assertTrue(userDetailsDao.delete(tmpUsersDetails));
    }
}
