import java.awt.image.BufferedImage;

/**
 * @author: zyc
 * @date: 2020/7/24 10:53
 */
public class ColumnImage {
    public BufferedImage up;
    public BufferedImage down;

    public ColumnImage(BufferedImage up, BufferedImage down) {
        this.up = up;
        this.down = down;
    }
}
