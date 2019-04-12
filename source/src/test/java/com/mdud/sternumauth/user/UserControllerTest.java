package com.mdud.sternumauth.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User mockUser;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockUser = new User.UserBuilder()
                .username("user")
                .password("password")
                .email("email")
                .addAuthority(AuthorityType.ADMIN)
                .addAuthority(AuthorityType.MANAGER)
                .addAuthority(AuthorityType.USER)
                .createUser();

        when(userService.getUserByUsername(mockUser.getUsername())).thenReturn(mockUser.toDTO());

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void getUser() throws Exception {
        Principal principal = () -> mockUser.getUsername();

        mockMvc.perform(get("/me")
                .principal(principal))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(mockUser.getUsername()));
    }
}