package GameMain;

import static GameMain.Menu.loadFont;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ScoreBoard extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;

    private GameView theView;
    private int stage, totalTankNum;
    private int totalScore = 0;
    private final int SHIFT = 80;

    private JButton restartButton;
    private transient final ImageUtility imageInstance = ImageUtility.getInstance();

    private int[] tankScoreList = {0, 0, 0, 0};
    private int[] tankNumList = {0, 0, 0, 0};

    @SuppressWarnings("this-escape")
    public ScoreBoard(GameView theView) {
        this.theView = theView;

        setFocusable(true);
        setLayout(null);
        theView.setForeground(Color.BLACK);

        restartButton = new JButton("Restart");
        restartButton.setBounds(400, 400, 100, 30);
        restartButton.addActionListener(this);

        add(restartButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font font = loadFont();

        ArrayList<Image> tankList = new ArrayList<>(
            Arrays.asList(
                imageInstance.getTankBasic(),
                imageInstance.getTankFast(),
                imageInstance.getTankPower(),
                imageInstance.getTankArmor()
            )
        );

        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("STAGE   " + stage, 97 + SHIFT, 60);

        g.setColor(Color.RED);
        g.drawString("1-PLAYER", 37 + SHIFT, 95);

        g.setColor(Color.ORANGE);
        g.drawString(String.valueOf(totalScore), 121 + SHIFT, 130);

        for (int i = 0; i < 4; i++) {
            g.drawImage(tankList.get(i), 226 + SHIFT, 160 + (i * 45), this);
            g.drawImage(imageInstance.getArrow(), 206 + SHIFT, 168 + (i * 45), this);
        }

        for (int i = 0; i < 4; i++) {
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(tankScoreList[i]), 55 + SHIFT, 180 + (i * 45));
            g.drawString("PTS", 115 + SHIFT, 180 + (i * 45));
        }

        for (int i = 0; i < 4; i++) {
            g.drawString(String.valueOf(tankNumList[i]), 180 + SHIFT, 180 + (i * 45));
        }

        g.drawLine(170, 330, 307, 330);

        g.drawString("TOTAL", 85 + SHIFT, 355);
        g.drawString(String.valueOf(totalTankNum), 180 + SHIFT, 355);
    }

    public void loadScore() {
        this.stage = Board.getStage();
        int[] enemyTankNum = CollisionUtility.getEnemyTankNum();

        for (int i = 0; i < 4; i++) {
            tankNumList[i] = enemyTankNum[i];
            tankScoreList[i] = tankNumList[i] * 100 * (i + 1);
        }

        totalScore = 0;
        totalTankNum = 0;

        for (int score : tankScoreList) {
            totalScore += score;
        }

        for (int num : tankNumList) {
            totalTankNum += num;
        }
    }

    public void restart() {
        Board.gameOver = false;
        CollisionUtility.resetScore();
        loadMenu();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton)
            restart();
    }

    private void loadMenu() {
        theView.getGamePanel().removeAll();

        Menu menu = new Menu(theView);
        theView.getGamePanel().add(menu);

        menu.revalidate();
        menu.repaint();
        menu.requestFocusInWindow();

        theView.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            loadMenu();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}