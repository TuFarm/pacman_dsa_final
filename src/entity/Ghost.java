package entity;

import java.awt.*;
import java.util.Random;
import main.GamePanel;

/**
 * Ghost - Enemy AI with PROXIMITY-BASED BFS pathfinding
 * 
 * PRESENTATION POINTS:
 * 1. INHERITANCE - Extends Entity base class
 * 2. HYBRID AI - Combines random movement and intelligent pathfinding
 * 3. PROXIMITY DETECTION - Switches behavior based on distance to Pacman
 * 4. BFS ALGORITHM - Finds shortest path when chasing
 * 5. STATE-BASED BEHAVIOR - Two modes: RANDOM and CHASE
 * 
 * AI BEHAVIOR:
 * - RANDOM MODE: When Pacman is far (> 3 tiles away)
 *   - Moves randomly for unpredictability
 *   - Less threatening, gives player breathing room
 * 
 * - CHASE MODE: When Pacman is near (≤ 2 tiles away)
 *   - Uses BFS to find shortest path
 *   - Actively hunts player
 *   - Creates tension and challenge
 * 
 * GAME DESIGN BENEFIT:
 * - Dynamic difficulty adjustment
 * - Balanced gameplay (not too easy or hard)
 * - Rewards player for keeping distance
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
    
    // === BFS PATHFINDING COMPONENTS ===
    
    // PRESENTATION POINT: These enable intelligent ghost behavior
    
    // The BFS algorithm instance
    private BFSPathfinder pathfinder;
    
    // Current path being followed (from BFS)
    private PathLinkedList currentPath;
    
    // Counter for when to recalculate path
    private int pathRecalculateCounter = 0;
    
    // Recalculate path every N frames (prevents constant recalculation)
    private static final int RECALCULATE_INTERVAL = 20;
    
    // === PROXIMITY DETECTION THRESHOLDS ===
    
    // PRESENTATION POINT: These control when ghost switches modes
    
    // Distance to START chasing (2 tiles = 64 pixels)
    // When Pacman gets this close, ghost starts BFS
    private static final int CHASE_DISTANCE = 2 * 32;  // 2 tiles
    
    // Distance to STOP chasing (3 tiles = 96 pixels)
    // When Pacman gets this far, ghost returns to random movement
    private static final int STOP_CHASE_DISTANCE = 3 * 32;  // 3 tiles
    
    // Current AI mode
    private boolean isChasing = false;

    /**
     * Constructor - Create a new ghost with BFS pathfinding
     * 
     * PRESENTATION POINTS:
     * - Calls parent constructor using super()
     * - Speed calculated as tileSize/4 for smooth grid movement
     * - Initializes BFS pathfinder
     * - Starts in RANDOM mode (not chasing)
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
        
        // Initialize BFS pathfinding system
        this.pathfinder = new BFSPathfinder(gp);
        this.currentPath = new PathLinkedList();
        this.isChasing = false;  // Start in random mode
        
        randomDir();  // Start moving in random direction
    }

    /**
     * Update ghost position with proximity-based AI
     * 
     * PRESENTATION POINTS:
     * 1. PROXIMITY DETECTION - Calculate distance to Pacman
     * 2. MODE SWITCHING - Change between RANDOM and CHASE modes
     * 3. BFS PATHFINDING - When chasing, use shortest path
     * 4. COLLISION DETECTION - Handle walls and boundaries
     * 
     * ALGORITHM FLOW:
     * Step 1: Calculate distance to Pacman
     * Step 2: Update AI mode based on distance (chase or random)
     * Step 3: If chasing, use BFS path; otherwise move randomly
     * Step 4: Apply velocity to position
     * Step 5: Handle collisions and boundaries
     */
    public void update() {
        
        // === STEP 1: PROXIMITY DETECTION ===
        // PRESENTATION POINT: Distance calculation using Pythagorean theorem
        
        if (gp.pacman != null) {
            double distanceToPacman = calculateDistance(gp.pacman);
            
            // === STEP 2: MODE SWITCHING ===
            // PRESENTATION POINT: Hysteresis prevents rapid mode switching
            
            if (!isChasing && distanceToPacman <= CHASE_DISTANCE) {
                // Pacman got close! Start chasing
                isChasing = true;
                currentPath.clear();  // Clear old path
                pathRecalculateCounter = RECALCULATE_INTERVAL;  // Force immediate recalculation
            }
            else if (isChasing && distanceToPacman > STOP_CHASE_DISTANCE) {
                // Pacman escaped! Stop chasing
                isChasing = false;
                currentPath.clear();  // Clear BFS path
            }
        }
        
        // === STEP 3: AI DECISION MAKING ===
        
        if (isChasing) {
            // CHASE MODE: Use BFS pathfinding
            // PRESENTATION POINT: This is where BFS algorithm is used
            updateChaseMode();
        }
        // else: RANDOM MODE uses default random movement (no changes needed)
        
        // === STEP 4: APPLY MOVEMENT ===
        this.x += xVelocity;
        this.y += yVelocity;

        // === STEP 5: SPECIAL RULES ===
        // Force downward movement at spawn area
        if (direction != 'U' && direction != 'D' && y == 32 * 9) {
            updateDir('D');
        }
        
        // === STEP 6: COLLISION DETECTION ===
        for (Entity wall : gp.walls) {
            if (gp.collision(wall, this) || this.x <= 0 || this.x + this.width >= gp.WIDTH) {
                // Backtrack on collision
                this.x -= xVelocity;
                this.y -= yVelocity;
                
                if (isChasing) {
                    // Path blocked! Recalculate immediately
                    currentPath.clear();
                    pathRecalculateCounter = RECALCULATE_INTERVAL;
                } else {
                    // Random mode: just pick new direction
                    randomDir();
                }
                break;
            }
        }
    }
    
    /**
     * Update ghost behavior when in CHASE mode (using BFS)
     * 
     * PRESENTATION POINTS:
     * 1. PERIODIC RECALCULATION - Don't recalculate every frame (expensive)
     * 2. PATH FOLLOWING - Use directions from BFS result
     * 3. GRID ALIGNMENT - Only change direction at grid intersections
     * 
     * ALGORITHM FLOW:
     * Step 1: Check if need to recalculate path (timer or empty path)
     * Step 2: If yes, run BFS to find new path
     * Step 3: Follow current path one step at a time
     * Step 4: Change direction only when aligned to grid
     */
    private void updateChaseMode() {
        pathRecalculateCounter++;
        
        // STEP 1: Decide when to recalculate path
        // PRESENTATION POINT: Trade-off between accuracy and performance
        
        if (pathRecalculateCounter >= RECALCULATE_INTERVAL || currentPath.isEmpty()) {
            
            // STEP 2: Run BFS algorithm
            // PRESENTATION POINT: This is the BFS function call
            
            if (gp.pacman != null) {
                PathLinkedList newPath = pathfinder.findPath(
                    this.x, this.y,           // Ghost position (start)
                    gp.pacman.x, gp.pacman.y  // Pacman position (target)
                );
                
                if (newPath != null && !newPath.isEmpty()) {
                    currentPath = newPath;
                } else {
                    // No path found (Pacman unreachable)
                    // Fall back to random movement
                    isChasing = false;
                }
            }
            
            pathRecalculateCounter = 0;  // Reset counter
        }
        
        // STEP 3: Follow the path
        // PRESENTATION POINT: Path is a sequence of directions
        
        if (!currentPath.isEmpty()) {
            char nextDir = currentPath.peek();  // Look at next direction
            
            // STEP 4: Change direction only when aligned to grid
            // PRESENTATION POINT: Prevents jagged movement
            
            if (isAlignedToGrid()) {
                currentPath.removeFirst();  // Remove current waypoint
                if (!currentPath.isEmpty()) {
                    nextDir = currentPath.peek();
                    updateDir(nextDir);
                }
            }
        }
    }
    
    /**
     * Calculate Euclidean distance to Pacman
     * 
     * PRESENTATION POINT:
     * - PYTHAGOREAN THEOREM: distance = √(dx² + dy²)
     * - Used for proximity detection
     * - Returns distance in pixels
     * 
     * FORMULA:
     * distance = √[(x₂ - x₁)² + (y₂ - y₁)²]
     * 
     * @param pacman - Pacman entity
     * @return Distance in pixels
     */
    private double calculateDistance(Pacman pacman) {
        int dx = pacman.x - this.x;  // Horizontal distance
        int dy = pacman.y - this.y;  // Vertical distance
        return Math.sqrt(dx * dx + dy * dy);  // Pythagorean theorem
    }
    
    /**
     * Check if ghost is aligned to grid
     * 
     * PRESENTATION POINT:
     * - Grid alignment means position is exactly on tile boundary
     * - Ensures smooth turns at intersections
     * - Both X and Y must be multiples of tileSize
     * 
     * @return true if at exact grid position
     */
    private boolean isAlignedToGrid() {
        return (this.x % gp.tileSize == 0) && (this.y % gp.tileSize == 0);
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
     * Reset ghost to starting position and state
     * 
     * PRESENTATION POINT:
     * - METHOD OVERRIDING - Extends parent's reset() method
     * - Calls super.reset() first, then adds ghost-specific behavior
     * - Resets AI state (back to random mode)
     * - Clears any existing BFS path
     */
    @Override
    public void reset() {
        super.reset();           // Reset position (from Entity class)
        this.isChasing = false;  // Return to random mode
        this.currentPath.clear(); // Clear any BFS path
        this.pathRecalculateCounter = 0;  // Reset counter
        randomDir();             // Choose new random direction
    }
}
