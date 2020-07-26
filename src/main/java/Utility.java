import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
/**
 * @author: zyc
 * @date: 2020/7/21 16:16
 */
public class Utility {
    //难度
    public static final int EASY =10;
    public static final int HARD =20;
    private static Random random = new Random();
    //管子随机y坐标
    public static int getRandomY() {
        return random.nextInt(101)-200;
    }

    //上下管子随机间隔
    public static int getRandomYSpace(int level) {
        if(level == EASY)
            return random.nextInt(51)+150;
        else return random.nextInt(51)+100;
    }

    //前后管子水平间隔
    public static int getRandomXSpace(int level) {
        if(level == EASY)
            return random.nextInt(51)+200;
        else return random.nextInt(51)+150;
    }
    //随机背景图
    public static BufferedImage getBackGroundImage() throws IOException {
        if(random.nextInt(2)<1)
            return ImageIO.read(Utility.class.getResource("img/bg_day.png"));
        else
            return ImageIO.read(Utility.class.getResource("img/bg_night.png"));

    }
    //随机管子图
    public static ColumnImage getRandomColumnImage() throws IOException {
        if(random.nextInt(6)<5)
            return new ColumnImage(ImageIO.read(Utility.class.getResource("img/pipe_up.png"))
                    ,ImageIO.read(Utility.class.getResource("img/pipe_down.png")));
        else
            return new ColumnImage(ImageIO.read(Utility.class.getResource("img/pipe2_up.png"))
                    ,ImageIO.read(Utility.class.getResource("img/pipe2_down.png")));
    }
    //随机鸟
    public static BufferedImage[] getRandomBirdImages() throws IOException {
          BufferedImage[]  birdImages = new BufferedImage[3];
          int x = random.nextInt(3);
           switch(x){
               case 0:
               default:{
                   birdImages[0] =  ImageIO.read(Utility.class.getResource("img/bird0_0.png"));
                   birdImages[1] =  ImageIO.read(Utility.class.getResource("img/bird0_1.png"));
                   birdImages[2] =  ImageIO.read(Utility.class.getResource("img/bird0_2.png"));
                   break;
               }
               case 1: {
                   birdImages[0] =  ImageIO.read(Utility.class.getResource("img/bird1_0.png"));
                   birdImages[1] =  ImageIO.read(Utility.class.getResource("img/bird1_1.png"));
                   birdImages[2] =  ImageIO.read(Utility.class.getResource("img/bird1_2.png"));
                   break;
               }
               case 2: {
                   birdImages[0] =  ImageIO.read(Utility.class.getResource("img/bird2_0.png"));
                   birdImages[1] =  ImageIO.read(Utility.class.getResource("img/bird2_1.png"));
                   birdImages[2] =  ImageIO.read(Utility.class.getResource("img/bird2_2.png"));
                   break;
               }
           }
           return birdImages;
       }
    //奖牌判定
    public static ImageIcon getMedal(int score){
        if(score<3){ return new ImageIcon(Utility.class.getResource("img/medals_0.png"));}
        else if(score<7){ return new ImageIcon(Utility.class.getResource("img/medals_2.png"));}
        else if(score<12){ return new ImageIcon(Utility.class.getResource("img/medals_3.png"));}
        else { return new ImageIcon(Utility.class.getResource("img/medals_1.png"));}
       }
}
