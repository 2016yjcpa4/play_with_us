package com.github.yjcpaj4.play_with_us.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageResource implements Resource {
 
    private BufferedImage mImage;

    public ImageResource(File f) {
        try {
            mImage = ImageIO.read(f);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public BufferedImage getData() {
        return mImage;
    }
}
