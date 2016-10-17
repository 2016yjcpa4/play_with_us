package org.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteManager {

    private BufferedImage spriteSheet;

    public BufferedImage loadSprite(String file) {

        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File("res/" + file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }

    public BufferedImage getSprite(String name, int x, int y, int w, int h) {

        if (spriteSheet == null) {
            spriteSheet = loadSprite(name);
        }

        return spriteSheet.getSubimage(x * w, y * h, w, h);
    }
}
