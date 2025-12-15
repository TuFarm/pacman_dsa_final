package entity;

/**
 * PathLinkedList - Custom Linked List implementation for storing ghost paths
 * 
 * PRESENTATION POINTS:
 * 1. LINKED LIST DATA STRUCTURE - Custom implementation (not using Java's built-in)
 * 2. NODE-BASED STORAGE - Each node contains data + reference to next
 * 3. DYNAMIC SIZE - Grows and shrinks as needed (no fixed capacity)
 * 4. O(1) OPERATIONS - Constant time for add/remove at head
 * 
 * WHY LINKED LIST?
 * - Efficient insertion/deletion at beginning (O(1) time)
 * - Perfect for queue-like behavior (FIFO - First In First Out)
 * - No need to resize like arrays
 * - Demonstrates fundamental data structure knowledge
 * 
 * USE CASE:
 * - Stores sequence of directions ('U', 'D', 'L', 'R') from BFS
 * - Ghost follows path by removing directions one at a time
 */
public class PathLinkedList {
    
    /**
     * Node - Inner class representing single element in linked list
     * 
     * PRESENTATION POINT:
     * - NESTED CLASS - Class defined inside another class
     * - Each node has two parts:
     *   1. Data: the direction character
     *   2. Link: reference to next node
     * 
     * This is the fundamental building block of linked lists
     */
    private class Node {
        char direction;    // The data (U, D, L, or R)
        Node next;         // Reference to next node in list
        
        /**
         * Create a new node with given direction
         * @param direction - The direction to store
         */
        Node(char direction) {
            this.direction = direction;
            this.next = null;  // Initially points to nothing
        }
    }
    
    // Head of the list (first node)
    private Node head;
    
    // Size of the list (number of nodes)
    private int size;
    
    /**
     * Constructor - Create empty linked list
     * 
     * PRESENTATION POINT:
     * - Initializes empty list
     * - head = null means list is empty
     * - size = 0 tracks number of elements
     */
    public PathLinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    /**
     * Add direction to the FRONT of the list
     * 
     * PRESENTATION POINTS:
     * 1. TIME COMPLEXITY: O(1) - Constant time
     * 2. ALGORITHM STEPS:
     *    Step 1: Create new node with direction
     *    Step 2: Point new node to current head
     *    Step 3: Update head to new node
     *    Step 4: Increment size
     * 
     * WHY ADD TO FRONT?
     * - Adding to front is O(1)
     * - Adding to back would be O(n) without tail pointer
     * - We build path in reverse, then reverse it
     * 
     * @param direction - Direction character to add
     */
    public void addFirst(char direction) {
        Node newNode = new Node(direction);  // Step 1: Create new node
        newNode.next = head;                 // Step 2: Link to current head
        head = newNode;                      // Step 3: New node becomes head
        size++;                              // Step 4: Increment size
    }
    
    /**
     * Remove and return first direction from list
     * 
     * PRESENTATION POINTS:
     * 1. TIME COMPLEXITY: O(1) - Constant time
     * 2. ALGORITHM STEPS:
     *    Step 1: Check if list is empty
     *    Step 2: Save the direction from head
     *    Step 3: Move head to next node
     *    Step 4: Decrement size
     *    Step 5: Return saved direction
     * 
     * USE CASE:
     * - Ghost removes first direction to follow path
     * - Like dequeuing from a queue
     * 
     * @return Direction character, or '\0' if list is empty
     */
    public char removeFirst() {
        if (isEmpty()) {
            return '\0';  // Return null character if empty
        }
        
        char direction = head.direction;  // Save the data
        head = head.next;                 // Move head forward
        size--;                           // Decrement size
        return direction;                 // Return saved data
    }
    
    /**
     * Look at first direction WITHOUT removing it
     * 
     * PRESENTATION POINT:
     * - "Peek" operation - non-destructive read
     * - Ghost can check next direction before committing
     * - TIME COMPLEXITY: O(1)
     * 
     * @return First direction, or '\0' if empty
     */
    public char peek() {
        if (isEmpty()) {
            return '\0';
        }
        return head.direction;
    }
    
    /**
     * Check if list is empty
     * 
     * PRESENTATION POINT:
     * - List is empty when head is null
     * - Used to prevent errors when removing
     * - TIME COMPLEXITY: O(1)
     * 
     * @return true if list has no elements
     */
    public boolean isEmpty() {
        return head == null;
    }
    
    /**
     * Get number of elements in list
     * 
     * PRESENTATION POINT:
     * - We maintain size counter for O(1) access
     * - Alternative: count nodes (would be O(n))
     * - Trade-off: extra memory for faster operation
     * 
     * @return Number of directions in list
     */
    public int size() {
        return size;
    }
    
    /**
     * Remove all elements from list
     * 
     * PRESENTATION POINT:
     * - Simply set head to null
     * - Java's garbage collector will clean up nodes
     * - Reset size to 0
     * - TIME COMPLEXITY: O(1)
     * 
     * USE CASE:
     * - Called when ghost switches from BFS to random mode
     * - Called when path becomes invalid
     */
    public void clear() {
        head = null;
        size = 0;
    }
}
