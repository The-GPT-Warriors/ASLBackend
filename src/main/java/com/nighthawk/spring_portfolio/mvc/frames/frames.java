package com.nighthawk.spring_portfolio.mvc.frames;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class frames {

    @PostMapping("/upload")
    public ResponseEntity<String> handleImageUpload(@RequestParam("image") MultipartFile image) {
        try {
            // Convert MultipartFile to byte array
            byte[] imageBytes = image.getBytes();

            // Save the imageBytes to your database or perform any other necessary operations

            // You may want to return a response to the frontend
            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
