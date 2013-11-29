package Tests;

import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.trollapp.User;
import com.example.trollapp.UserDAO;

public class TestUserDAO extends TestCase {
	
	@Mock
    private UserDAO userDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //todo remove when implemented
        MockitoAnnotations.initMocks(this);

    }

    public void testAddUser() {
        System.out.println("testAddUser");

        //todo remove when implemented
        User mockUser = new User("George", "password");
        when(userDAO.getAllUsers()).thenReturn(Collections.singletonList(mockUser));

        userDAO.clearDB();

        User user = new User();
        	user.setName("George");
        	user.setPassword("Password");
        userDAO.addNewUser(user);

        List<User> users = userDAO.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());

        User realUser = users.iterator().next();
        assertNotNull(realUser);
        assertEquals(user.getName(), realUser.getName());
        assertEquals(user.getPassword(), realUser.getPassword());
    }

    public void testDeleteUser() {
        /**System.out.println("testDeleteUser");

        //todo remove when implemented
        User mockUser = new User("Taras");
        when(userDAO.getAllUsers()).thenReturn(Collections.singletonList(mockUser)).thenReturn(Collections.<User>emptyList());

        userDAO.clear();

        User user = new User();
        user.setName("Taras");
        userDAO.save(user);

        List<User> users = userDAO.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());

        userDAO.delete(user);
        users = userDAO.getAllUsers();
        assertNotNull(users);
        assertEquals(0, users.size()); */
    }

}
