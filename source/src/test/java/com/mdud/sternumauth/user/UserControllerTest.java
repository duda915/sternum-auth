package com.mdud.sternumauth.user;

import com.mdud.sternumauth.cdn.CDNEntity;
import com.mdud.sternumauth.cdn.CDNService;
import com.mdud.sternumauth.user.form.ChangeImageForm;
import com.mdud.sternumauth.user.form.ChangePasswordForm;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private CDNService cdnService;

    private User mockUser;

    private MockMvc mockMvc;

    @Value("classpath:static/images/sternum-logo.png")
    private Resource resourceImage;

    @Before
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


    @Test
    public void userInfo() throws Exception {
        Principal principal = () -> mockUser.getUsername();

        mockMvc.perform(get("/")
                .principal(principal))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", CoreMatchers.notNullValue()));
    }

    @Test
    public void changePassword_TestValidScenario_ShouldChangePassword() throws Exception {
        Principal principal = () -> mockUser.getUsername();
        ChangePasswordForm changePasswordForm = new ChangePasswordForm("newpassx", "newpassx");

        mockMvc.perform(post("/password")
                .flashAttr("passwordForm", changePasswordForm)
                .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("info", CoreMatchers.notNullValue()));

        verify(userService, times(1)).changeUserPassword(mockUser.getUsername(), changePasswordForm.getPassword());
    }

    @Test
    public void changePassword_ConfirmPasswordNotMatch_ShouldNotChangePassword() throws Exception {
        Principal principal = () -> mockUser.getUsername();
        ChangePasswordForm changePasswordForm = new ChangePasswordForm("newpassx", "newpassxss");

        mockMvc.perform(post("/password")
                .flashAttr("passwordForm", changePasswordForm)
                .principal(principal))
                .andExpect(flash().attribute("error", CoreMatchers.notNullValue()))
                .andExpect(status().is3xxRedirection());

        verify(userService, never()).changeUserPassword(mockUser.getUsername(), changePasswordForm.getPassword());
    }

    @Test
    public void changePassword_PasswordNotMatchSixCharactersConstraint_ShouldNotChangePassword() throws Exception {
        Principal principal = () -> mockUser.getUsername();
        ChangePasswordForm changePasswordForm = new ChangePasswordForm("asd", "asd");

        mockMvc.perform(post("/password")
                .flashAttr("passwordForm", changePasswordForm)
                .principal(principal))
                .andExpect(flash().attribute("error", CoreMatchers.notNullValue()))
                .andExpect(status().is3xxRedirection());

        verify(userService, never()).changeUserPassword(mockUser.getUsername(), changePasswordForm.getPassword());
    }

    @Test
    public void changeUserImage_ChangeImage_ShouldChangeImage() throws Exception {
        when(cdnService.addImage(any())).thenReturn(new CDNEntity());

        Principal principal = () -> mockUser.getUsername();
        ChangeImageForm changeImageForm = new ChangeImageForm(new MockMultipartFile("image", resourceImage.getInputStream()));

        mockMvc.perform(post("/image")
                .flashAttr("imageForm", changeImageForm)
                .principal(principal))
                .andExpect(status().is3xxRedirection());


        verify(cdnService, times(1)).addImage(any());
        verify(userService, times(1)).changeUserImage(eq(principal.getName()), any());
    }

    @Test
    public void changeUserImage_DeleteImage_ShouldChangeToNull() throws Exception {
        byte[] empty = null;
        Principal principal = () -> mockUser.getUsername();
        ChangeImageForm changeImageForm = new ChangeImageForm(new MockMultipartFile("image", empty));

        mockMvc.perform(post("/image")
                .flashAttr("imageForm", changeImageForm)
                .principal(principal))
                .andExpect(status().is3xxRedirection());


        verify(cdnService, never()).addImage(any());
        verify(userService, times(1)).changeUserImage(principal.getName(), null);
    }
}