package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gamePanel.getGameOver()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W: gamePanel.pacman.setDirection('U'); break;
                case KeyEvent.VK_S: gamePanel.pacman.setDirection('D'); break;
                case KeyEvent.VK_A: gamePanel.pacman.setDirection('L'); break;
                case KeyEvent.VK_D: gamePanel.pacman.setDirection('R'); break;
            }
        }
        else {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                gamePanel.resetGame(); // press space --> reset game
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}