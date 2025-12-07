package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel implements ActionListener {

    MainClass mainClass;
    Image bgImage;
    JButton btnPlay;
    JButton btnGuide;

    public MenuPanel(MainClass mainClass) {
        this.mainClass = mainClass;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));

        // bg img
        try {
            bgImage = new ImageIcon(getClass().getResource("/res/menuBg.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // play btn
        btnPlay = new JButton();
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/res/btnPlay.png")));
        btnPlay.setBounds(220, 200, 150, 50);
        btnPlay.setBorderPainted(false);
        btnPlay.setContentAreaFilled(false);
        btnPlay.setFocusPainted(false);
        btnPlay.addActionListener(this);
        this.add(btnPlay);

        // guide btn
        btnGuide = new JButton();
        btnGuide.setIcon(new ImageIcon(getClass().getResource("/res/btnGuide.png")));
        btnGuide.setBounds(220, 600, 150, 50);
        btnGuide.setBorderPainted(false);
        btnGuide.setContentAreaFilled(false);
        btnGuide.setFocusPainted(false);
        btnGuide.addActionListener(this);
        this.add(btnGuide);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPlay) {
            mainClass.showGame();
        } else if (e.getSource() == btnGuide) {
            mainClass.showGuide();
        }
    }
}