package entity;

/**
 * PathfindingNode - Represents a single position in the BFS search tree
 * 
 * PRESENTATION POINTS:
 * 1. NODE DATA STRUCTURE - Building block for BFS algorithm
 * 2. PARENT REFERENCE - Enables path reconstruction via backtracking
 * 3. EQUALS & HASHCODE - Required for HashSet operations (prevents revisiting nodes)
 * 
 * WHY THIS MATTERS:
 * - Each node represents one grid position explored during BFS
 * - Parent links form a tree that we traverse backward to find the path
 * - HashSet uses equals() and hashCode() to track visited positions
 */
public class PathfindingNode {
    
    // Grid position (in tile coordinates, not pixels)
    public int row;      // Row index in the game grid
    public int col;      // Column index in the game grid
    
    // Reference to the previous node in the search path
    // This allows us to reconstruct the path from target back to start
    public PathfindingNode parent;
    
    /**
     * Constructor - Create a new search node
     * 
     * PRESENTATION POINT:
     * - Constructor pattern in OOP
     * - Initializes position and links to previous node in search
     * 
     * @param row - Grid row position
     * @param col - Grid column position  
     * @param parent - Previous node in search (null for starting node)
     */
    public PathfindingNode(int row, int col, PathfindingNode parent) {
        this.row = row;
        this.col = col;
        this.parent = parent;
    }
    
    /**
     * Check if two nodes represent the same grid position
     * 
     * PRESENTATION POINT:
     * - Required for HashSet.contains() operation
     * - Two nodes are equal if they have same row and col
     * - Used to detect when we've reached the target position
     * 
     * @param obj - Object to compare with
     * @return true if same position, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PathfindingNode)) return false;
        PathfindingNode other = (PathfindingNode) obj;
        return this.row == other.row && this.col == other.col;
    }
    
    /**
     * Generate hash code for this node
     * 
     * PRESENTATION POINT:
     * - Required for HashSet performance
     * - Must be consistent with equals() method
     * - Creates unique integer for each (row, col) pair
     * 
     * FORMULA: row * 1000 + col
     * - Assumes grid won't exceed 1000 columns
     * - Creates unique hash for each position
     * 
     * @return Hash code as integer
     */
    @Override
    public int hashCode() {
        return row * 1000 + col;
    }
}
