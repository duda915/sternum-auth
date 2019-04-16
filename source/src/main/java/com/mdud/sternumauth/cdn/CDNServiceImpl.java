package com.mdud.sternumauth.cdn;

import com.mdud.sternumauth.cdn.exception.ImageException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class CDNServiceImpl implements CDNService {

    private final RestTemplate restTemplate;

    @Autowired
    public CDNServiceImpl(@Qualifier("cdnTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private byte[] convertToPNG(byte[] image) {
        ByteArrayInputStream stream = new ByteArrayInputStream(image);

        try {
            BufferedImage bufferedImage = ImageIO.read(stream);

            if(bufferedImage == null) {
                throw new IOException();
            } else {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            throw new ImageException();
        }
    }

    @Override
    public CDNEntity addImage(byte[] image) {
        byte[] pngImage = convertToPNG(image);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        String filePath = "/tmp/" + UUID.randomUUID().toString() + ".png";

        try {
            FileUtils.writeByteArrayToFile(new File(filePath), pngImage);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("unknown image writing error");
        }

        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        params.add("resource", fileSystemResource);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<CDNEntity> response = restTemplate.exchange("/add", HttpMethod.POST, request, CDNEntity.class);

        return response.getBody();
    }
}
