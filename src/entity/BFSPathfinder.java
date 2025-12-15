package entity;

import main.GamePanel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;

/**
 * BFSPathfinder - Breadth-First Search algorithm for ghost pathfinding
 * 
 * PRESENTATION POINTS:
 * 
 * 1. BREADTH-FIRST SEARCH (BFS) ALGORITHM
 *    - Graph traversal algorithm
 *    - Explores nodes level by level (breadth-first)
 *    - GUARANTEES shortest path in unweighted graphs
 *    - Uses QUEUE data structure (FIFO)
 * 
 * 2. TIME COMPLEXITY: O(V + E)
 *    - V = number of vertices (grid cells)
 *    - E = number of edges (connections between cells)
 *    - In grid: V = rows × cols, E ≈ 4V
 *    - Final: O(rows × cols)
 * 
 * 3. SPACE COMPLEXITY: O(V)
 *    - Queue stores at most all vertices
 *    - HashSet stores visited vertices
 *    - Total: O(rows × cols)
 * 
 * WHY BFS FOR PACMAN?
 * - Finds shortest path (optimal ghost movement)
 * - Handles obstacles (walls) automatically
 * - Efficient for grid-based games
 * - Realistic AI behavior
 */
public class BFSPathfinder {
    
    private GamePanel gp;
    
    /**
     * Constructor - Initialize pathfinder with game reference
     * 
     * @param gp - Game panel containing walls and grid info
     */
    public BFSPathfinder(GamePanel gp) {
        this.gp = gp;
    }
    
    /**
     * Find shortest path from start to target using BFS
     * 
     * PRESENTATION - ALGORITHM OVERVIEW:
     * 
     * INPUT: Start position (ghost), Target position (Pacman)
     * OUTPUT: PathLinkedList of directions, or null if unreachable
     * 
     * STEPS:
     * 1. Convert pixel coordinates to grid coordinates
     * 2. Initialize queue with starting node
     * 3. Initialize visited set (prevents cycles)
     * 4. While queue not empty:
     *    a. Dequeue current node
     *    b. Check if reached target
     *    c. Explore all 4 neighbors (Up, Down, Left, Right)
     *    d. Add valid unvisited neighbors to queue
     * 5. Reconstruct path by following parent links
     * 6. Convert path to direction commands
     * 
     * @param startX - Ghost's current X position (pixels)
     * @param startY - Ghost's current Y position (pixels)
     * @param targetX - Pacman's X position (pixels)
     * @param targetY - Pacman's Y position (pixels)
     * @return PathLinkedList with directions, or null if no path
     */
    public PathLinkedList findPath(int startX, int startY, int targetX, int targetY) {
        
        // === STEP 1: Convert pixel coordinates to grid coordinates ===
        // PRESENTATION POINT: Coordinate system transformation
        // Pixels → Tiles: divide by tileSize
        int startRow = startY / gp.tileSize;
        int startCol = startX / gp.tileSize;
        int targetRow = targetY / gp.tileSize;
        int targetCol = targetX / gp.tileSize;
        
        // Edge case: Already at target
        if (startRow == targetRow && startCol == targetCol) {
            return new PathLinkedList();  // Return empty path
        }
        
        // === STEP 2: Initialize BFS data structures ===
        
        // QUEUE: Stores nodes to explore (FIFO - First In First Out)
        // PRESENTATION POINT: Queue is KEY to BFS algorithm
        // - Ensures level-by-level exploration
        // - Guarantees shortest path found first
        Queue<PathfindingNode> queue = new LinkedList<>();
        
        // VISITED SET: Tracks explored positions (prevents infinite loops)
        // PRESENTATION POINT: HashSet for O(1) lookup
        // - Prevents revisiting same position
        // - Critical for algorithm termination
        HashSet<PathfindingNode> visited = new HashSet<>();
        
        // Create starting node (no parent)
        PathfindingNode startNode = new PathfindingNode(startRow, startCol, null);
        
        // Add start to queue and mark as visited
        queue.offer(startNode);
        visited.add(startNode);
        
        // === STEP 3: BFS MAIN LOOP ===
        // PRESENTATION POINT: This is the heart of BFS
        // Continue until queue is empty (all reachable nodes explored)
        
        while (!queue.isEmpty()) {
            
            // DEQUEUE: Remove and get first node from queue
            // PRESENTATION POINT: FIFO order ensures breadth-first
            PathfindingNode current = queue.poll();
            
            // === STEP 4: Check if we reached the target ===
            if (current.row == targetRow && current.col == targetCol) {
                // SUCCESS! We found Pacman
                // Now reconstruct the path from target back to start
                return reconstructPath(current);
            }
            
            // === STEP 5: Explore all 4 neighbors ===
            // PRESENTATION POINT: Grid has 4-connectivity (Up, Down, Left, Right)
            // We explore in all directions to find shortest path
            
            // Try moving UP (row - 1)
            exploreNeighbor(current, current.row - 1, current.col, queue, visited);
            
            // Try moving DOWN (row + 1)
            exploreNeighbor(current, current.row + 1, current.col, queue, visited);
            
            // Try moving LEFT (col - 1)
            exploreNeighbor(current, current.row, current.col - 1, queue, visited);
            
            // Try moving RIGHT (col + 1)
            exploreNeighbor(current, current.row, current.col + 1, queue, visited);
        }
        
        // Queue is empty and target not found
        // This means Pacman is unreachable from ghost's position
        return null;
    }
    
    /**
     * Explore a neighboring grid cell
     * 
     * PRESENTATION POINTS:
     * 1. VALIDATION - Check bounds, walls, and visited status
     * 2. NODE CREATION - Create new node with parent link
     * 3. ENQUEUE - Add to queue for future exploration
     * 4. MARK VISITED - Prevent revisiting
     * 
     * ALGORITHM STEPS:
     * Step 1: Check if position is within grid bounds
     * Step 2: Check if position is a wall (obstacle)
     * Step 3: Create new node for this position
     * Step 4: Check if already visited (prevents cycles)
     * Step 5: If valid, add to queue and mark visited
     * 
     * @param current - Current node we're expanding from
     * @param newRow - Row of neighbor to explore
     * @param newCol - Column of neighbor to explore
     * @param queue - The BFS queue
     * @param visited - Set of already visited nodes
     */
    private void exploreNeighbor(PathfindingNode current, int newRow, int newCol,
                                  Queue<PathfindingNode> queue, HashSet<PathfindingNode> visited) {
        
        // STEP 1: Bounds checking
        // PRESENTATION POINT: Prevent array out of bounds errors
        if (newRow < 0 || newRow >= GamePanel.rowCount || 
            newCol < 0 || newCol >= GamePanel.columnCount) {
            return;  // Out of bounds, skip this neighbor
        }
        
        // STEP 2: Wall checking
        // PRESENTATION POINT: Obstacle avoidance
        if (isWall(newRow, newCol)) {
            return;  // Can't move through walls, skip
        }
        
        // STEP 3: Create new node
        // PRESENTATION POINT: Parent link enables path reconstruction
        PathfindingNode neighbor = new PathfindingNode(newRow, newCol, current);
        
        // STEP 4: Check if already visited
        // PRESENTATION POINT: HashSet.contains() is O(1)
        if (visited.contains(neighbor)) {
            return;  // Already explored, skip
        }
        
        // STEP 5: Valid neighbor found! Add to queue and mark visited
        // PRESENTATION POINT: This expands the search frontier
        queue.offer(neighbor);      // Add to end of queue
        visited.add(neighbor);      // Mark as visited
    }
    
    /**
     * Check if a grid position contains a wall
     * 
     * PRESENTATION POINT:
     * - Converts grid coordinates back to pixel coordinates
     * - Checks all walls in game
     * - Linear search: O(number of walls)
     * 
     * POTENTIAL OPTIMIZATION:
     * - Could use 2D boolean array for O(1) lookup
     * - Trade memory for speed
     * 
     * @param row - Grid row to check
     * @param col - Grid column to check
     * @return true if wall exists at position
     */
    private boolean isWall(int row, int col) {
        // Convert grid coordinates to pixel coordinates
        int pixelX = col * gp.tileSize;
        int pixelY = row * gp.tileSize;
        
        // Check all walls in the game
        for (Entity wall : gp.walls) {
            if (wall.x == pixelX && wall.y == pixelY) {
                return true;  // Wall found
            }
        }
        return false;  // No wall at this position
    }
    
    /**
     * Reconstruct path by following parent links backward
     * 
     * PRESENTATION POINTS:
     * 1. BACKTRACKING - Follow parent links from target to start
     * 2. PATH REVERSAL - Build path backward, then reverse
     * 3. DIRECTION CALCULATION - Convert position changes to directions
     * 
     * ALGORITHM STEPS:
     * Step 1: Start at target node
     * Step 2: Follow parent link to previous node
     * Step 3: Calculate direction moved (current - parent)
     * Step 4: Add direction to front of list (builds in reverse)
     * Step 5: Repeat until reaching start node (parent = null)
     * 
     * WHY THIS WORKS:
     * - Each node stores parent (where we came from)
     * - Following parents traces path backward
     * - Adding to front reverses the path automatically
     * 
     * @param targetNode - The node representing Pacman's position
     * @return PathLinkedList with directions from ghost to Pacman
     */
    private PathLinkedList reconstructPath(PathfindingNode targetNode) {
        PathLinkedList path = new PathLinkedList();
        PathfindingNode current = targetNode;
        
        // Traverse backward through parent links
        while (current.parent != null) {
            PathfindingNode parent = current.parent;
            
            // Calculate which direction we moved from parent to current
            char direction = getDirection(parent, current);
            
            // Add to FRONT of list (since we're going backward)
            // PRESENTATION POINT: This reverses the path automatically
            path.addFirst(direction);
            
            // Move to parent
            current = parent;
        }
        
        return path;
    }
    
    /**
     * Calculate direction moved between two adjacent nodes
     * 
     * PRESENTATION POINT:
     * - Vector subtraction: to - from = direction
     * - Row/Col differences map to U/D/L/R
     * 
     * MAPPING:
     * - Row decreased (-1): Moved Up
     * - Row increased (+1): Moved Down
     * - Col decreased (-1): Moved Left
     * - Col increased (+1): Moved Right
     * 
     * @param from - Starting node
     * @param to - Destination node
     * @return Direction character ('U', 'D', 'L', or 'R')
     */
    private char getDirection(PathfindingNode from, PathfindingNode to) {
        int rowDiff = to.row - from.row;
        int colDiff = to.col - from.col;
        
        if (rowDiff == -1) return 'U';  // Moved up
        if (rowDiff == 1) return 'D';   // Moved down
        if (colDiff == -1) return 'L';  // Moved left
        if (colDiff == 1) return 'R';   // Moved right
        
        return 'R';  // Default (should never reach here)
    }
}
