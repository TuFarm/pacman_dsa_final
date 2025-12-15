package entity;

import java.awt.*;
import main.GamePanel;

/**
 * Entity - Base class for all game objects (Pacman, Ghosts, Walls, Food)
 * 
 * PRESENTATION POINTS:
 * 1. This is an example of OBJECT-ORIENTED PROGRAMMING (OOP)
 * 2. Uses INHERITANCE - other classes extend this base class
 * 3. Encapsulates common properties and behaviors
 * 4. Follows DRY principle (Don't Repeat Yourself)
 */
public class Entity {
    // Reference to the game panel (needed for game rules and collision detection)
    public GamePanel gp;
    
    // Current position in pixels
    public int x, y;
    
    // Size of the entity in pixels
    public int width, height;
    
    // The image to display for this entity
    public Image img;

    // Starting position (used for reset when game restarts)
    public int startX, startY;

    /**
     * Constructor - Initialize a new entity
     * 
     * PRESENTATION POINT:
     * - Constructor pattern in OOP
     * - Initializes all required fields
     * - Saves starting position for reset functionality
     * 
     * @param gp - Reference to the game panel
     * @param x - Initial X position (pixels)
     * @param y - Initial Y position (pixels)
     * @param width - Entity width (pixels)
     * @param height - Entity height (pixels)
     */
    public Entity(GamePanel gp, int x, int y, int width, int height) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;  // Remember starting X for reset
        this.startY = y;  // Remember starting Y for reset
    }

    /**
     * Draw the entity on the screen
     * 
     * PRESENTATION POINT:
     * - Graphics rendering in Java
     * - Null-check prevents errors if image not loaded
     * - Called every frame (60 times per second)
     * 
     * @param g - Graphics object for drawing
     */
    public void draw(Graphics g) {
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        }
    }

    /**
     * Reset entity to starting position
     * 
     * PRESENTATION POINT:
     * - Used when game restarts or player loses a life
     * - Demonstrates state management
     */
    public void reset() {
        this.x = startX;
        this.y = startY;
    }

    /**
     * Get collision boundary rectangle
     * 
     * PRESENTATION POINT:
     * - Rectangle-based collision detection
     * - Returns a Rectangle object representing entity's bounds
     * - Used for detecting overlaps between entities
     * 
     * @return Rectangle representing this entity's position and size
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
