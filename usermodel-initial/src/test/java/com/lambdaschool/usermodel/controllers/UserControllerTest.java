package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception {
        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin",
                "password",
                "adminTEST@lambdaschool.local");
        u1.setUserid(10);

        u1.getRoles()
                .add(new UserRoles(u1, r1));
        u1.getRoles()
                .add(new UserRoles(u1, r2));
        u1.getRoles()
                .add(new UserRoles(u1, r3));

        u1.getUseremails()
                .add(new Useremail(u1,
                        "adminTEST@email.local"));
        u1.getUseremails().get(0).setUseremailid(11);

        u1.getUseremails()
                .add(new Useremail(u1,
                        "adminTEST@mymail.local"));
        u1.getUseremails().get(1).setUseremailid(12);

        userList.add(u1);

        // data, user
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamonTEST@lambdaschool.local");
        u2.setUserid(20);

        u2.getRoles()
                .add(new UserRoles(u2, r2));
        u2.getRoles()
                .add(new UserRoles(u2, r3));

        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamonTEST@mymail.local"));
        u2.getUseremails().get(0).setUseremailid(21);

        u2.getUseremails()
                .add(new Useremail(u2,
                        "hopsTEST@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(22);

        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunnyTEST@email.local"));
        u2.getUseremails().get(2).setUseremailid(23);

        userList.add(u2);

        // user
        User u3 = new User("barnbarn",
                "ILuvM4th!",
                "barnbarnTEST@lambdaschool.local");
        u3.setUserid(30);

        u3.getRoles()
                .add(new UserRoles(u3, r2));

        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarnTEST@email.local"));
        u3.getUseremails().get(0).setUseremailid(31);

        userList.add(u3);

        User u4 = new User("puttat",
                "password",
                "puttatTEST@school.lambda");
        u4.setUserid(40);

        u4.getRoles()
                .add(new UserRoles(u4, r2));

        userList.add(u4);

        User u5 = new User("misskitty",
                "password",
                "misskittyTEST@school.lambda");
        u5.setUserid(50);

        u5.getRoles()
                .add(new UserRoles(u5, r2));

        userList.add(u5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_listAllUsers() throws Exception{
        var apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);

        var builder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(builder).andReturn();
        var jsonResult = result.getResponse().getContentAsString();

        var usersAsJson = new ObjectMapper().writeValueAsString(userList);
        assertEquals(jsonResult, usersAsJson);
    }

    private void assertEquals(String jsonResult, String usersAsJson) {
    }

    @Test
    public void b_getUserById() throws Exception{
        var apiUrl = "/users/user/10";
        Mockito.when(userService.findUserById(10)).thenReturn(userList.get(0));

        var builder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(builder).andReturn();
        var jsonResult = result.getResponse().getContentAsString();

        var usersAsJson = new ObjectMapper().writeValueAsString(userList.get(0));
        assertEquals(jsonResult, usersAsJson);
    }

    @Test
    public void c_getUserByName() throws Exception{
        var apiUrl = "/users/user/name/namedperson";
        Mockito.when(userService.findByName("namedperson")).thenReturn(userList.get(0));

        var builder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(builder).andReturn();
        var jsonResult = result.getResponse().getContentAsString();

        var userAsJson = new ObjectMapper().writeValueAsString(userList.get(0));
        assertEquals(jsonResult, userAsJson);
    }

    @Test
    public void d_getUserLikeName() throws Exception{
        var apiUrl = "/users/user/name/like/name";
        Mockito.when(userService.findByNameContaining("name")).thenReturn(userList);
        var builder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(builder).andReturn();
        var jsonResult = result.getResponse().getContentAsString();

        var usersAsJson = new ObjectMapper().writeValueAsString(userList);
        assertEquals(jsonResult, usersAsJson);
    }

    @Test
    public void e_addNewUser() throws Exception{
        var apiUrl = "/users/user";

        User newUser = new User();
        newUser.setUserid(0);
        newUser.setUsername("test");
        newUser.setPassword("test1234");
        newUser.setPrimaryemail("test@test.com");
        Role r2 = new Role("test");
        newUser.getRoles().add(new UserRoles(newUser,r2));
        newUser.getUseremails().add(new Useremail(newUser, "test2@test.com"));
        var newUserAsJson = new ObjectMapper().writeValueAsString(newUser);
        Mockito.when(userService.save(any(User.class))).thenReturn(newUser);
        var builder = MockMvcRequestBuilders.post(apiUrl).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserAsJson);
        mockMvc.perform(builder).andExpect(status().isCreated());
    }

    @Test
    public void f_updateFullUser() throws Exception{
        String apiUrl = "/users/user/99";
        User newUser = new User("Kevin", "goodpasssord", "test@test.com");
        newUser.setUserid(35);
        Role r2 = new Role("test");
        newUser.getRoles().add(new UserRoles(newUser,r2));
        newUser.getUseremails().add(new Useremail(newUser, "test@test.com"));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(newUser);

        Mockito.when(userService.save(any(User.class))).thenReturn(newUser);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void g_updateUser() throws Exception{
        String apiUrl = "/users/user/99";
        User newUser = new User("Kevin", "goodpasssord", "test@test.com");
        newUser.setUserid(35);
        Role r2 = new Role("test");
        newUser.getRoles().add(new UserRoles(newUser,r2));
        newUser.getUseremails().add(new Useremail(newUser, "test@test.com"));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(newUser);

        Mockito.when(userService.save(any(User.class))).thenReturn(newUser);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void h_deleteUserById() throws Exception{
        var apiUrl = "/users/user/1";
        var builder = MockMvcRequestBuilders.delete(apiUrl).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder).andExpect(status().isOk());
    }
}