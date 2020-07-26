import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: zyc
 * @date: 2020/7/21 12:44
 */
public class Game extends JPanel {
    boolean start, gameOver;
    Bird bird;
    Column column;
    Ground ground;
    BufferedImage bgImage;
    BufferedImage gameOverImage;
    ImageIcon beginImage;
    JLabel beginButton;
    JLabel score_label;
    JLabel medal_label;
    JLabel currentScore_label;
    JLabel bestScore_label;
    JLabel home_label;
    JLabel restart_label;
    JFrame jFrame;
    JLayeredPane layeredPane;
    CopyOnWriteArrayList<Column> pipeList = new CopyOnWriteArrayList<>();// 管子容器
    private ScheduledExecutorService service;// 计时器
    int score;
    int best;
    boolean ifRestart = false;

    public Game() throws Exception {
        this.setSize(1000, 700);
        jFrame = new JFrame("FlyBird");
        jFrame.setLayout(null);
        jFrame.setBounds(0, 0, 1000, 700);
        jFrame.setResizable(false);
        jFrame.setLocation(450, 200);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layeredPane = jFrame.getLayeredPane();
        gameOverImage = ImageIO.read(getClass().getResource("img/text_game_over.png"));
        beginImage = new ImageIcon(getClass().getResource("img/button_play.png"));
        Home();
        layeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
        jFrame.setVisible(true);
    }

    //主界面
    public void Home() throws Exception {
        start = false;
        gameOver = false;
        bird = new Bird();
        ground = new Ground();
        column = new Column(pipeList,score);
        pipeList.add(column);
        bgImage = Utility.getBackGroundImage();
        beginButton = new JLabel(beginImage);
        beginButton.setBounds(442, 259, beginImage.getIconWidth(), beginImage.getIconHeight());
        beginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                beginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                beginButton.setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                start = true;
                try {
                    Start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                beginButton.setVisible(false);
            }
        });
        if (!ifRestart) {layeredPane.add(beginButton, JLayeredPane.POPUP_LAYER);}
        else {
            start = true;
            Start();
        }
    }

    //开始游戏
    public void Start() {
        score = 0;
        service = Executors.newScheduledThreadPool(3);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(gameOver) return;
                bird.isJumping = true;
                bird.ySpeed = -12;
            }
        });
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if(gameOver) return;
                    bird.isJumping = true;
                    bird.ySpeed = -12;
                }
            }
        });

        //提示
        ImageIcon ready = new ImageIcon(getClass().getResource("img/text_ready.png"));
        ImageIcon tutorial = new ImageIcon(getClass().getResource("img/tutorial.png"));
        JLabel ready_label = new JLabel(ready);
        JLabel tutorial_label = new JLabel(tutorial);
        ready_label.setBounds(402, 200, ready.getIconWidth(), ready.getIconHeight());
        tutorial_label.setBounds(443, 400, tutorial.getIconWidth(), tutorial.getIconHeight());
        layeredPane.add(ready_label,JLayeredPane.PALETTE_LAYER);
        layeredPane.add(tutorial_label,JLayeredPane.PALETTE_LAYER);

        java.util.Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                ready_label.setVisible(false);
                tutorial_label.setVisible(false);
                layeredPane.remove(ready_label);
                layeredPane.remove(tutorial_label);
            }
        }, 1500);

        //鸟飞行
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (start && !gameOver) {
                    bird.move();
                    repaint();
                }
            }
        }, 0, 35, TimeUnit.MILLISECONDS);

        //管子移动
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (start && !gameOver) {
                    if (pipeList.size() < 6) {
                        try {
                            column = new Column(pipeList,score);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        pipeList.add(column);
                    }
                    for (Column c : pipeList) {
                        if (c.x + c.pipe_up.getWidth() < 0) {
                            pipeList.remove(c);
                        }
                        if (bird.pass(c) && !c.passed) {
                            score++;
                            c.passed = true;
                        }
                        if (bird.hit(c)) {
                            start = false;
                            gameOver = true;
                            pipeList.clear();
                            break;
                        }
                        c.move();
                    }
                    if (gameOver) {
                        score();
                    }
                    repaint();
                }
            }
        }, 0, 35, TimeUnit.MILLISECONDS);

    }

    public void gc() {
        medal_label.setVisible(false);
        medal_label = null;
        currentScore_label.setVisible(false);
        currentScore_label = null;
        bestScore_label.setVisible(false);
        bestScore_label = null;
        score_label.setVisible(false);
        score_label = null;
        home_label.setVisible(false);
        restart_label.setVisible(false);
        service.shutdownNow();
        System.gc();
        repaint();

    }

    public void score() {
        //得分面板
        ImageIcon score_panel = new ImageIcon(getClass().getResource("img/score_panel.png"));
        score_label = new JLabel(score_panel);
        score_label.setOpaque(false);
        score_label.setBounds(381, 231, score_panel.getIconWidth(), score_panel.getIconHeight());
        layeredPane.add(score_label, JLayeredPane.MODAL_LAYER);
        //奖牌
        ImageIcon medal = Utility.getMedal(score);
        medal_label = new JLabel(medal);

        medal_label.setBounds(410, 274, medal.getIconWidth(), medal.getIconHeight());
        layeredPane.add(medal_label, JLayeredPane.DRAG_LAYER);

        //分数显示
        currentScore_label = new JLabel(String.valueOf(score));
        currentScore_label.setBounds(565, 265, 20, 20);
        layeredPane.add(currentScore_label, JLayeredPane.POPUP_LAYER);

        //最好分数
        if(score>best) best= score;
        bestScore_label = new JLabel(String.valueOf(best));
        bestScore_label.setBounds(565, 305, 20, 20);
        layeredPane.add(bestScore_label, JLayeredPane.POPUP_LAYER);

        //Home
        ImageIcon home = new ImageIcon(getClass().getResource("img/button_home.png"));
        home_label = new JLabel(home);
        //home_label.setOpaque(false);
        home_label.setBounds(625, 235, home.getIconWidth(), home.getIconHeight());
        layeredPane.add(home_label, JLayeredPane.MODAL_LAYER);

        //重新开始
        ImageIcon restart = new ImageIcon(getClass().getResource("img/button_restart.png"));
        restart_label = new JLabel(restart);
        restart_label.setOpaque(false);
        restart_label.setBounds(610, 284, restart.getIconWidth(), restart.getIconHeight());
        layeredPane.add(restart_label, JLayeredPane.MODAL_LAYER);
        //Home事件
        home_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                home_label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                home_label.setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                gc();
                try {
                    ifRestart = false;
                    Home();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //重新开始事件
        restart_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                restart_label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                restart_label.setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                gc();
                try {
                    ifRestart = true;
                    Home();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(bgImage, 0, 0, 1000, 700, null);
        for (Column c : pipeList) {
            c.paint(g);
        }
        bird.paint(g);
        ground.paint(g);
        if (gameOver) {
            g.drawImage(gameOverImage, 398, 160, null);
            bird.move();
            if (bird.y < 600) {
                repaint();
            }
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Game game = new Game();
    }
}
