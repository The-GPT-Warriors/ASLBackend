package com.nighthawk.spring_portfolio.mvc.frames;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Base64;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import java.io.IOException;

@RestController
public class FrameApiController {

    @Autowired
    private FrameJpaRepository frameJpaRepository;

    @PostMapping("/image")
    public String processImage(@RequestBody ImageData imageData) {
        // convert image to MNIST format
        String mnistData = convertToMNIST(imageData.getImage(), imageData.getLabel());

        // store MNIST data in database
        Frame frame = new Frame();
        frame.setMnistData(mnistData);
        frameJpaRepository.save(frame);

        return "{\"message\": \"Image uploaded successfully!\"}";
    }

    @GetMapping("/mnist")
    public List<Frame> getMnistData() {
        return frameJpaRepository.findAll();
    }

    // method for MNIST format
    private String convertToMNIST(String imageData, int label) {
        try {
            // decode Base64 image string
            byte[] imageBytes = Base64.getDecoder().decode(imageData);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bis);

            // resize image to 28 x 28
            BufferedImage resizedImage = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(image, 0, 0, 28, 28, null);
            g.dispose();

            // convert BufferedImage to MNIST data format
            StringBuilder mnistData = new StringBuilder();
            for (int y = 0; y < 28; y++) {
                for (int x = 0; x < 28; x++) {
                    int pixel = resizedImage.getRGB(x, y) & 0xFF; // grayscale value
                    mnistData.append(pixel).append(",");
                }
            }
            mnistData.deleteCharAt(mnistData.length() - 1);
            return mnistData.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class ImageData {
        //store base64 encoded string of the image
        private String image;
        // store the label associated with image
        private int label;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getLabel() {
            return label;
        }

        public void setLabel(int label) {
            this.label = label;

        }
    }
}
