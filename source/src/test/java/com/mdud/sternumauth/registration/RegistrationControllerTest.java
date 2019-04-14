package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.cdn.CDNEntity;
import com.mdud.sternumauth.cdn.CDNService;
import com.mdud.sternumauth.user.CredentialUserDTO;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private CDNService cdnService;

    @Mock
    private RegistrationService registrationService;

    private MockMvc mockMvc;

    @Value("classpath:static/images/sternum-logo.png")
    private Resource resourceImage;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void registration_Get_ShouldReturnRegisterTemplate() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(view().name("registerView"));
    }

    @Test
    public void register_TestValidation_ShouldHaveBindingErrors() throws Exception {
        RegistrationForm registrationForm = new RegistrationForm();

        mockMvc.perform(multipart("/register")
        .flashAttr("form", registrationForm))
                .andExpect(status().isOk())
                .andExpect(view().name("registerView"))
                .andExpect(model().attribute("error", CoreMatchers.notNullValue()));
    }

    @Test
    public void register_TestConfirmPassword_ShouldHaveError() throws Exception {
        RegistrationForm registrationForm = new RegistrationForm("test", "test123", "testss123", "test@email.com", null);

        mockMvc.perform(multipart("/register")
                .flashAttr("form", registrationForm))
                .andExpect(status().isOk())
                .andExpect(view().name("registerView"))
                .andExpect(model().attribute("error", CoreMatchers.notNullValue()));
    }

    @Test
    public void register_WithoutImage_ShouldRegister() throws Exception {
        byte[] empty = null;
        RegistrationForm registrationForm = new RegistrationForm("test", "test123", "test123", "test@email.com",
                new MockMultipartFile("form", empty));

        mockMvc.perform(multipart("/register")
        .flashAttr("form", registrationForm))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("error", CoreMatchers.nullValue()))
                .andExpect(view().name("redirect:login"));

        CredentialUserDTO expectedRegisteredUser = new CredentialUserDTO("test", "test123", "test@email.com", null);

        verify(registrationService, times(1)).register(expectedRegisteredUser);
        verify(cdnService, never()).addImage(any());
    }

    @Test
    public void register_WithImage_ShouldRegister() throws Exception {
        MockMultipartFile image = new MockMultipartFile("file", resourceImage.getInputStream());
        RegistrationForm registrationForm = new RegistrationForm("test", "test123", "test123", "test@email.com", image);

        CDNEntity imagePath = new CDNEntity();
        imagePath.setResourceURL("/test");

        when(cdnService.addImage(any())).thenReturn(imagePath);

        mockMvc.perform(multipart("/register")
                .flashAttr("form", registrationForm))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("error", CoreMatchers.nullValue()))
                .andExpect(view().name("redirect:login"));

        CredentialUserDTO expectedRegisteredUser = new CredentialUserDTO("test", "test123", "test@email.com", "/test");

        verify(registrationService, times(1)).register(expectedRegisteredUser);
        verify(cdnService, times(1)).addImage(image.getBytes());
    }
}