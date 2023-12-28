package com.mycopmany.myproject.machineapi.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycopmany.myproject.machineapi.AbstractIntegrationTest;
import com.mycopmany.myproject.machineapi.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIntTest extends AbstractIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void getUsers() throws Exception {
        UserToCreate userToCreate = new UserToCreate(
                "firstname",
                "lastname",
                "username",
                "password");
        authenticationService.register(userToCreate);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users")
                        .header("Authorization", "Bearer " + "token"))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserToGet[] usersToGet = objectMapper.readValue(jsonResponse, UserToGet[].class);

        assertEquals(2, usersToGet.length);
        assertEquals("admin", usersToGet[0].getFirstName());
        assertEquals("admin", usersToGet[0].getLastName());
        assertEquals("admin", usersToGet[0].getUsername());

    }

    @Test
    void deleteUserWhenExists() throws Exception {
        UserToCreate userToCreate1 = new UserToCreate(
                "firstname",
                "lastname",
                "username",
                "password");
        authenticationService.register(userToCreate1);
        Long idToDelete = 1L;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/" + idToDelete)
                        .header("Authorization", "Bearer " + "token"))
                .andExpect(status().isNoContent());

        boolean userExists = userRepository.existsById(idToDelete);
        assertFalse(userExists);
        assertEquals(1, userRepository.count());
    }


}