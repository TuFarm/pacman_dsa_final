package main;

import javax.swing.*;
import java.awt.*;

public class MainClass {

    private JFrame frame;
    private JPanel mainContainer;
    private CardLayout cardLayout;

    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private GuidePanel guidePanel;

    public static void main(String[] args) {
        new MainClass();
    }

    public MainClass() {
        frame = new JFrame("Pacman Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        gamePanel = new GamePanel();
        menuPanel = new MenuPanel(this);
        guidePanel = new GuidePanel(this);

        mainContainer.add(menuPanel, "MENU");
        mainContainer.add(gamePanel, "GAME");
        mainContainer.add(guidePanel, "GUIDE");

        frame.add(mainContainer);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Show menu as default
        showMenu();
    }

    public void showMenu() {
        cardLayout.show(mainContainer, "MENU");
    }

    public void showGame() {
        cardLayout.show(mainContainer, "GAME");
        gamePanel.startGame(); // Start game loop
    }

    public void showGuide() {
        cardLayout.show(mainContainer, "GUIDE");
    }
}