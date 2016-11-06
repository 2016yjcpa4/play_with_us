package com.github.yjcpaj4.play_with_us.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import com.github.yjcpaj4.play_with_us.util.FileUtil;

public class ImageResource implements Resource {
 
    private BufferedImage mImage;

    @Override
    public void load(File f, String k) throws IOException {
        mImage = ImageIO.read(f);
    }

    @Override
    public void release() {
        mImage.flush();
    }
    
    public BufferedImage getImageData() {
        return mImage;
    }

    public static boolean canLoad(File f) {
        return Arrays.asList("jpg", "jpeg", "bmp", "gif", "png", "tiff", "tif").contains(FileUtil.getExtension(f));
    }
}
