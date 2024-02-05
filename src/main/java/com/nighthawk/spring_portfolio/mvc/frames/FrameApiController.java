package com.nighthawk.spring_portfolio.mvc.frames;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class FrameApiController {
    @Autowired
    private FrameJpaRepository uploadFileRepository;

    @Autowired
    FrameApiAppl imageService;

    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping("/")
    public ResponseEntity<List<Frame>> getFrame() {
        return new ResponseEntity<>(uploadFileRepository.findAll(), HttpStatus.OK);
    }
    // handle http post request for saving an image
    @PostMapping
    public ResponseEntity<String> save(MultipartFile image, @RequestParam("fileName") String fileName) throws IOException {
        String encodedString = Base64.getEncoder().encodeToString(image.getBytes());
        Frame file = new Frame(fileName, encodedString);
        uploadFileRepository.save(file);

        // Create a response object
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(Map.of("message", "Image has been uploaded"));

        return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
    }
    // handle http get request for downloading an image by its filename
    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String fileName) {
        // check if the image with the given filename exists in the repository
        Optional<Frame> optional = uploadFileRepository.findByfileName(fileName);
        if (optional.isPresent()) {
            Frame file = optional.get();
            String data = file.getImageEncoder();
            byte[] imageBytes = Base64.getDecoder().decode(data);

            // determine the MediaType based on the file extension
            MediaType mediaType = MediaType.IMAGE_PNG; // default set to PNG
            if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (fileName.toLowerCase().endsWith(".gif")) {
                mediaType = MediaType.IMAGE_GIF;
            } 
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}