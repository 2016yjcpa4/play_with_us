package org.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.game.util.FileUtil;

public class ImageResource implements ResourceManager.Resource {

    private String mAlias;
    private BufferedImage mImage;

    @Override
    public void load(File f, String k) throws IOException {
        mAlias = k;
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
        return Arrays.asList(new String[] {
                                "jpg",
                                "jpeg",
                                "bmp",
                                "gif",
                                "png",
                                "tiff",
                                "tif"
                            }).contains(FileUtil.getExtension(f));
    }
}
