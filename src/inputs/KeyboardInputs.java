package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;

/**
 * KeyboardInputs - Handles all keyboard input from the player
 * 
 * PRESENTATION POINTS:
 * 1. INTERFACE IMPLEMENTATION - Implements KeyListener interface
 * 2. EVENT-DRIVEN PROGRAMMING - Responds to user actions
 * 3. WASD CONTROLS - Common gaming control scheme
 * 4. STATE-BASED INPUT - Different behavior based on game state
 * 
 * DESIGN PATTERN: Observer Pattern
 * - Java's event listener system
 * - This class "observes" keyboard events
 * - GamePanel is notified when keys are pressed
 */
public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    /**
     * Constructor - Link keyboard input to game panel
     * 
     * @param gamePanel - The game panel to control
     */
    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Called when a key is typed (pressed and released)
     * Not used in this game
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Called when a key is pressed down
     * 
     * PRESENTATION POINTS:
     * 1. CONDITIONAL LOGIC - Different behavior for game vs game-over state
     * 2. SWITCH STATEMENT - Clean mapping of keys to actions
     * 3. KEY CODES - VK_W, VK_A, VK_S, VK_D constants
     * 
     * WASD CONTROLS:
     * - W = Up
     * - A = Left  
     * - S = Down
     * - D = Right
     * - SPACE = Restart (when game over)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // During gameplay - control Pacman
        if (!gamePanel.getGameOver()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W: gamePanel.pacman.setDirection('U'); break;  // W = Up
                case KeyEvent.VK_S: gamePanel.pacman.setDirection('D'); break;  // S = Down
                case KeyEvent.VK_A: gamePanel.pacman.setDirection('L'); break;  // A = Left
                case KeyEvent.VK_D: gamePanel.pacman.setDirection('R'); break;  // D = Right
            }
        }
        // During game over - restart game
        else {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                gamePanel.resetGame();  // SPACE = Restart
            }
        }
    }

    /**
     * Called when a key is released
     * Not used in this game (Pacman keeps moving)
     */
    @Override
    public void keyReleased(KeyEvent e) {}
}