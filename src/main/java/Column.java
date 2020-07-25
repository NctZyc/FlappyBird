import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: zyc
 * @date: 2020/7/21 15:33
 */
public class Column {
    public int x; // 上方管子左上角x坐标
    public int y; // 上方管子左上角y坐标
    public int ySpace; // 上下管子间隔
    private int xSpace; // 前后管子间隔
    int speed;
    BufferedImage pipe_up;
    BufferedImage pipe_down;
    ColumnImage columnImage;
    boolean passed = false;

    public Column(CopyOnWriteArrayList<Column> pipeList,int score) throws IOException {
        speed=4;
        columnImage = Utility.getRandomColumnImage();
        pipe_up = columnImage.up;
        pipe_down = columnImage.down;
        int size = pipeList.size();
        if(pipeList.isEmpty()){this.x=1050;}
        else{this.x = pipeList.get(size-1).x+pipe_up.getWidth()+pipeList.get(size-1).xSpace;}
            this.y = Utility.getRandomY();
        if(score<=10) {
            this.ySpace = Utility.getRandomYSpace(Utility.EASY);
            this.xSpace = Utility.getRandomXSpace(Utility.EASY);
        }
        else {
            this.ySpace = Utility.getRandomYSpace(Utility.HARD);
            this.xSpace = Utility.getRandomXSpace(Utility.HARD);
        }

    }
    public void move(){
        x= this.x - speed;
    }

    public synchronized void paint(Graphics g){
        g.drawImage(pipe_up,x,y,null);
        g.drawImage(pipe_down,x,y+pipe_up.getHeight()+ySpace,null);
    }

    @Override
    public String toString() {
        return "Column{" +
                "x=" + x +
                ", y=" + y +
                ", ySpace=" + ySpace +
                ", xSpace=" + xSpace +
                ", speed=" + speed +
                ", pipe_up=" + pipe_up +
                ", pipe_down=" + pipe_down +
                ", passed=" + passed +
                '}';
    }
}
