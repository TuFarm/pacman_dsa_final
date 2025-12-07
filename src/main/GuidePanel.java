package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GuidePanel extends JPanel {

    private MainClass mainClass;
    private JButton backButton;
    private Image guideBgImage;

    public GuidePanel(MainClass mainClass) {
        this.mainClass = mainClass;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));

        guideBgImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/guideBg.png"))).getImage();

        // back btn
        backButton = new JButton();
        backButton.setIcon(new ImageIcon(getClass().getResource("/res/btnBack.png")));
        backButton.setBounds(220, 400, 150, 50);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainClass.showMenu();
            }
        });
        this.add(backButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (guideBgImage != null) {
            g.drawImage(guideBgImage, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("HOW TO PLAY", 200, 50);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("- USE W, A, S, D TO MOVE YOUR PACMAN", 50, 140);
        g.drawString("- EAT ALL FOODS TO FINISH THE LEVEL", 50, 190);
        g.drawString("- AT DEFAULT YOU WILL HAVE 3 LIVES", 50, 240);
        g.drawString("- AVOID GHOSTS ", 50, 290);
        g.drawString("- THERE ARE TELEPORTATION PORTALS AT THE EDGE", 50, 340);
    }
}