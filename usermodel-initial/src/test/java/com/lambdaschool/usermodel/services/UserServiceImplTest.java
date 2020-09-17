package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        List<User> myList = userService.findAll();
        for (User u : myList) {
            System.out.println(u.getUserid() + " " + u.getUsername());
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_findUserById() {
        assertEquals("puttattest", userService.findUserById(13).getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void aa_findUserByIdNotFound() {
        assertEquals("", userService.findUserById(1234567890).getUsername());
    }

    @Test
    public void b_findByNameContaining() {
        assertEquals(1, userService.findByNameContaining("barn").size());
    }

    @Test
    public void bb_findByInvalidNameContaining() {
        assertEquals(0, userService.findByNameContaining("orangeManBad").size());
    }

    @Test
    public void c_findAll() {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void d_findByName() {
        assertEquals("barnbarntest", userService.findByName("barnbarntest").getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void d_findByInvalidName() {
        assertEquals("", userService.findByName("barnbarntesttesttest").getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void dd_findByNameNotFound() {
        assertEquals("", userService.findByName("barn").getUsername());
    }

    @Test
    public void e_save() {
        String u4Name = "newtest";
        User u4 = new User("newTEST",
                "password",
                "newTEST@school.lambda");
        Role role = new Role("turtle");
        role.setRoleid(3);
        u4.getRoles()
                .add(new UserRoles(u4, role));
        User addUser = userService.save(u4);
        assertNotNull(addUser);
        assertEquals(u4Name, addUser.getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void ga_saveInvalidId() {
        User newUser = new User();
        newUser.setPassword("test");
        newUser.setUsername("test user");
        newUser.setPrimaryemail("test@user.com");
        newUser.setUserid(98765);
        List<Role> roles = roleService.findAll();
        for(Role r : roles) {
            newUser.getRoles().add(new UserRoles(newUser, r));
        }
        var dataUser = userService.save(newUser);
        assertEquals("", dataUser.getUsername());
    }

    @Test
    public void f_update() {
        String updateName = "bugstest";
        User updateUser = new User();
        updateUser.setUsername("bugstest");
        updateUser.setUserid(14);
        updateUser.setPrimaryemail("bugstest@lambda.com");
        updateUser.setPassword("pwd1234$7&");
        updateUser.getUseremails().add(new Useremail(updateUser, "mrbunnytest@aol.com"));

        Role role = new Role("sandwich");
        role.setRoleid(2);
        updateUser.getRoles().add(new UserRoles(updateUser, role));

        User userDetails = userService.update(updateUser, 13);
        assertNotNull(userDetails);
        assertEquals(updateName, userService.findUserById(13).getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void ha_updateUserNotFound() {
        User newUser = new User();
        newUser.setPassword("test");
        newUser.setUsername("test user");
        newUser.setPrimaryemail("test@user.com");
        List<Role> roles = roleService.findAll();
        for(Role r : roles) {
            newUser.getRoles().add(new UserRoles(newUser, r));
        }
        var dataUser = userService.update(newUser, 1000);
    }

    @Test
    public void g_delete() {
        userService.delete(13);
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void h_deleteAll() {
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}