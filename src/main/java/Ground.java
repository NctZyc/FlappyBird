import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author: zyc
 * @date: 2020/7/21 12:43
 */
public class Ground {
    BufferedImage image;

    int x;
    int y;
    int width;
    int height;

    public Ground() throws IOException {
        image = ImageIO.read(getClass().getResource("img/land.png"));
    }


    public void paint(Graphics g) {
        g.drawImage(image, 0, 560, null);
    }
}
