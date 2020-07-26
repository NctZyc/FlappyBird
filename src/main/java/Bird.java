import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author: zyc
 * @date: 2020/7/21 12:43
 */
public class Bird {
    public boolean isJumping = false;        // 是否要跳
    int x, y;
    int ySpeed = 0;
    static int g = 1;
    BufferedImage[] birdImages;
    BufferedImage birdImage;
    int index = 0;

    public Bird() throws IOException {
        birdImages = Utility.getRandomBirdImages();
        birdImage = birdImages[0];
        x = 482;
        y = 270;
    }

    public void move() {

        if (this.isJumping) {
            if (y + ySpeed < 0) y = 0;
            else y += ySpeed;
            ySpeed += g;
            this.isJumping = false;
        } else {
            ySpeed += g;
            y = y + ySpeed;
        }
        index++;
        birdImage = birdImages[(index / 4) % 3];

    }

    //是否通过管子
    public boolean pass(Column column) {
        return x > column.x + column.pipe_up.getWidth();

    }

    //碰撞
    public boolean hit(Column column) {
        //撞地
        if (this.y + this.birdImage.getHeight() >= 560) {
            return true;
        } else {
            //撞到管子
            if (x + birdImage.getWidth() >= column.x && x <= column.x + column.pipe_up.getWidth()) {
                return !(y > column.pipe_up.getHeight() + column.y && y + birdImage.getHeight() < column.y + column.pipe_up.getHeight() + column.ySpace);
            }
        }
        return false;
    }

    public synchronized void paint(Graphics g) {
        g.drawImage(birdImage, x, y, null);
    }
}
