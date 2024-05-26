package com.nighthawk.spring_portfolio.mvc.datalogin;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class DataFrame {
    public static void main(String[] args) {
        String videoFilePath = "src/main/java/com/nighthawk/spring_portfolio/mvc/datalogin/AnthonyBazSignLogin.mp4"; // Replace this with your video file path
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFilePath);
        
        try {
            grabber.start();
            
            Java2DFrameConverter converter = new Java2DFrameConverter();
            Frame frame;
            int frameNumber = 0;
            
            while ((frame = grabber.grabFrame()) != null) {
                BufferedImage bi = converter.convert(frame);
                if (bi != null) {
                    // Ensure directory exists or created
                    File outputDir = new File("src/main/java/com/nighthawk/spring_portfolio/mvc/dataloginframes");
                    if (!outputDir.exists()) outputDir.mkdirs();
                    
                    // Construct file name and write image
                    String fileName = String.format("src/main/java/com/nighthawk/spring_portfolio/mvc/datalogin/frames/frame_%05d.png", frameNumber++);
                    ImageIO.write(bi, "png", new File(fileName));
                    System.out.println("Extracted frame: " + fileName);
                }
            }
            
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
