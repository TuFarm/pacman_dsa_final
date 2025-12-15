package entity;

import java.awt.*;
import java.util.Random;
import main.GamePanel;

/**
 * Ghost - Enemy AI with random movement
 * 
 * PRESENTATION POINTS:
 * 1. INHERITANCE - Extends Entity base class
 * 2. RANDOM ALGORITHM - Uses Random class for unpredictable movement
 * 3. COLLISION DETECTION - Checks walls before moving
 * 4. VELOCITY-BASED MOVEMENT - Uses speed and direction vectors
 * 
 * POTENTIAL IMPROVEMENTS (For Discussion):
 * - Could implement BFS/DFS for pathfinding
 * - Could add different ghost behaviors (chase, scatter, ambush)
 * - Could implement AI difficulty levels
 */
public class Ghost extends Entity {

    // Current direction the ghost is moving ('U'=Up, 'D'=Down, 'L'=Left, 'R'=Right)
    public char direction = 'U';
    
    // Velocity in pixels per frame
    public int xVelocity = 0;  // Horizontal speed
    public int yVelocity = 0;  // Vertical speed
    
    // Movement speed (pixels per frame)
    private int speed = 8;
    
    // Array of possible directions for random selection
    private char[] directions = {'U', 'D', 'L', 'R'};
    
    // Random number generator for AI decision-making
    private Random random = new Random();

    /**
     * Constructor - Create a new ghost
     * 
     * PRESENTATION POINT:
     * - Calls parent constructor using super()
     * - Speed calculated as tileSize/4 for smooth grid movement
     * - Initializes with random direction for variety
     * 
     * @param gp - Game panel reference
     * @param x - Starting X position
     * @param y - Starting Y position
     * @param width - Ghost width
     * @param height - Ghost height
     * @param img - Ghost image (different colors for each ghost)
     */
    public Ghost(GamePanel gp, int x, int y, int width, int height, Image img) {
        super(gp, x, y, width, height);  // Call parent constructor
        this.img = img;
        this.speed = gp.tileSize / 4;  // Speed = 8 pixels (32/4) per frame
        randomDir();  // Start moving in random direction
    }

    /**
     * Update ghost position each frame
     * 
     * PRESENTATION POINTS:
     * 1. VELOCITY-BASED MOVEMENT - position += velocity
     * 2. COLLISION DETECTION - Iterate through all walls
     * 3. BACKTRACKING - Undo movement if collision occurs
     * 4. RANDOM DECISION-MAKING - Choose new direction on collision
     * 
     * ALGORITHM FLOW:
     * Step 1: Move ghost by current velocity
     * Step 2: Check special game rules (e.g., ghost spawn area)
     * Step 3: Check for collisions with walls or boundaries
     * Step 4: If collision, step back and choose new direction
     */
    public void update() {
        // STEP 1: Apply velocity to position (basic physics)
        this.x += xVelocity;
        this.y += yVelocity;

        // STEP 2: Special rule - Force downward movement at spawn area
        // This ensures ghosts leave their starting area
        if (direction != 'U' && direction != 'D' && y == 32 * 9) {
            updateDir('D');
        }
        
        // STEP 3: Collision detection loop
        // Iterate through all wall entities in the game
        for (Entity wall : gp.walls) {
            // Check if ghost hit a wall OR went out of bounds
            if (gp.collision(wall, this) || this.x <= 0 || this.x + this.width >= gp.WIDTH) {
                // STEP 4a: Undo the movement (backtrack)
                this.x -= xVelocity;
                this.y -= yVelocity;
                
                // STEP 4b: Choose new random direction
                randomDir();
                break;  // Exit loop once collision is handled
            }
        }
    }

    /**
     * Choose a random direction
     * 
     * PRESENTATION POINTS:
     * 1. RANDOM ALGORITHM - random.nextInt(4) generates 0-3
     * 2. ARRAY INDEXING - Use random number to pick from array
     * 3. Creates unpredictable ghost behavior
     * 
     * DATA STRUCTURE: Array {'U', 'D', 'L', 'R'}
     * - Index 0 = 'U' (Up)
     * - Index 1 = 'D' (Down)
     * - Index 2 = 'L' (Left)
     * - Index 3 = 'R' (Right)
     */
    public void randomDir() {
        char newDir = directions[random.nextInt(4)];  // Random index: 0, 1, 2, or 3
        updateDir(newDir);  // Apply the new direction
    }

    /**
     * Update ghost's direction and velocity
     * 
     * PRESENTATION POINTS:
     * 1. SWITCH STATEMENT - Clean way to handle multiple cases
     * 2. VECTOR MATHEMATICS - Velocity as (x, y) components
     * 3. Direction mapping:
     *    - Up: (0, -speed) - negative Y moves upward
     *    - Down: (0, +speed) - positive Y moves downward
     *    - Left: (-speed, 0) - negative X moves left
     *    - Right: (+speed, 0) - positive X moves right
     * 
     * @param newDir - The new direction character
     */
    public void updateDir(char newDir) {
        this.direction = newDir;
        switch (direction) {
            case 'U': xVelocity = 0; yVelocity = -speed; break;  // Move up
            case 'D': xVelocity = 0; yVelocity = speed; break;   // Move down
            case 'L': xVelocity = -speed; yVelocity = 0; break;  // Move left
            case 'R': xVelocity = speed; yVelocity = 0; break;   // Move right
        }
    }

    /**
     * Reset ghost to starting position and random direction
     * 
     * PRESENTATION POINT:
     * - METHOD OVERRIDING - Extends parent's reset() method
     * - Calls super.reset() first, then adds ghost-specific behavior
     * - Demonstrates polymorphism in OOP
     */
    @Override
    public void reset() {
        super.reset();  // Reset position (from Entity class)
        randomDir();    // Choose new random direction
    }
}