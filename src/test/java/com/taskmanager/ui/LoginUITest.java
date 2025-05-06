package com.taskmanager.ui;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class LoginUITest {

    @Test
    public void testBackgroundImageLoadsSuccessfully() throws Exception {
        // Create the LoginUI object
        LoginUI loginUI = new LoginUI();

        // Use reflection to access the private field
        Field field = LoginUI.class.getDeclaredField("backgroundImage");
        field.setAccessible(true);
        BufferedImage image = (BufferedImage) field.get(loginUI);

        // Assert that the image is not null
        assertNotNull(image, "Background image should be loaded successfully");
    }
}
