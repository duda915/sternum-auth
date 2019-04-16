package com.mdud.sternumauth.cdn;

import com.mdud.sternumauth.cdn.exception.ImageException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CDNServiceImplIT {

    @Value("classpath:static/images/sternum-logo.png")
    private Resource image;

    @Autowired
    private CDNServiceImpl cdnService;

    @Test
    public void addImage_AddNotImage_ShouldThrowException() {
        byte[] stringBytes = "test".getBytes(StandardCharsets.UTF_8);

        assertThrows(ImageException.class, () -> cdnService.addImage(stringBytes));
    }

    @Test
    public void addImage_AddImage_ShouldAddImage() throws IOException {
        byte[] images = IOUtils.toByteArray(image.getInputStream());
        assertDoesNotThrow(() -> cdnService.addImage(images), "adding image to cdn should not throw exception");
    }
}