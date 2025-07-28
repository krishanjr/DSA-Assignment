// A Maze Solver
// Functionality:
//  Graph (Adjacency List/Matrix): Use a graph to represent the maze where each cell is a node
// connected to its adjacent cells.
//  Stack (DFS) / Queue (BFS): Use a stack for Depth-First Search (DFS) or a queue for Breadth-First
// Search (BFS) to find a path from start to finish.
// GUI:
//  A grid-based maze where each cell is a node.
//  A start and end point for the player or algorithm.
//  Buttons to solve the maze using DFS or BFS.
//  A "Generate New Maze" button to create a randomized maze.
// Implementation:
// Initialization:
// 1. Generate a random maze using a grid where walls block movement.
// 2. Represent the maze as a graph (adjacency list or matrix).
// 3. Allow the user to choose a start and end point.
// 4. Display the maze in the GUI.
// Solving the Maze:
// While the path is not found:
// 1. Choose an algorithm:
// o If DFS is selected, use a stack.
// o If BFS is selected, use a queue.
// 2. Explore adjacent nodes:
// o If a path is found, mark it.
// o If a dead-end is reached, backtrack.
// 3. Highlight the solution path on the GUI.
// Game Over:
//  If the end point is reached, display a success message.
//  If no path exists, display a failure message.
// Data Structures:
//  Graph: Represent the maze where each cell is a node connected to adjacent walkable cells.
//  Queue: Used for BFS to find the shortest path.
//  Stack: Used for DFS to explore paths.
//  2D Array: Represents the grid-based maze.
// Additional Considerations:
//  Random Maze Generation: Use algorithms like Prim’s or Recursive Backtracking to generate
// new mazes dynamically.
//  User Input: Allow the player to manually navigate through the maze.
//  Path Animation: Visually show the algorithm exploring paths.
//  Scoring System: Award points based on efficiency (steps taken, time, etc.).



package Question5;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;


class Cell {
    int row, col;
    boolean isWall, visited;
    Cell parent;

    public Cell(int row, int col, boolean isWall) {
        this.row = row;
        this.col = col;
        this.isWall = isWall;
        this.visited = false;
        this.parent = null;
    }
}

class MazePanel extends JPanel {
    Cell[][] maze;
    List<Cell> path = new ArrayList<>();
    int rows = 20, cols = 20, cellSize = 25;

    public MazePanel() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        generateMaze();
    }

    public void generateMaze() {
        maze = new Cell[rows][cols];
        Random rand = new Random();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                boolean isWall = rand.nextDouble() < 0.3;
                maze[r][c] = new Cell(r, c, isWall);
            }
        }
        maze[0][0].isWall = false;
        maze[rows - 1][cols - 1].isWall = false;
        path.clear();
        repaint();
    }

    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            int nr = cell.row + dr[i];
            int nc = cell.col + dc[i];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols)
                neighbors.add(maze[nr][nc]);
        }
        return neighbors;
    }

    public void solveDFS() {
        for (Cell[] row : maze)
            for (Cell cell : row) {
                cell.visited = false;
                cell.parent = null;
            }

        Stack<Cell> stack = new Stack<>();
        Cell start = maze[0][0], end = maze[rows - 1][cols - 1];
        stack.push(start);

        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            if (current.visited || current.isWall) continue;
            current.visited = true;
            if (current == end) break;

            for (Cell neighbor : getNeighbors(current)) {
                if (!neighbor.visited && !neighbor.isWall) {
                    neighbor.parent = current;
                    stack.push(neighbor);
                }
            }
        }

        path.clear();
        for (Cell c = end; c != null; c = c.parent)
            path.add(c);
        repaint();
    }

    public void solveBFS() {
        for (Cell[] row : maze)
            for (Cell cell : row) {
                cell.visited = false;
                cell.parent = null;
            }

        Queue<Cell> queue = new LinkedList<>();
        Cell start = maze[0][0], end = maze[rows - 1][cols - 1];
        queue.offer(start);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            if (current.visited || current.isWall) continue;
            current.visited = true;
            if (current == end) break;

            for (Cell neighbor : getNeighbors(current)) {
                if (!neighbor.visited && !neighbor.isWall) {
                    neighbor.parent = current;
                    queue.offer(neighbor);
                }
            }
        }

        path.clear();
        for (Cell c = end; c != null; c = c.parent)
            path.add(c);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                Cell cell = maze[r][c];
                if (cell.isWall) g.setColor(Color.BLACK);
                else g.setColor(Color.WHITE);
                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }

        g.setColor(Color.GREEN);
        for (Cell cell : path)
            g.fillRect(cell.col * cellSize, cell.row * cellSize, cellSize, cellSize);

        g.setColor(Color.BLUE);
        g.fillRect(0, 0, cellSize, cellSize);
        g.setColor(Color.RED);
        g.fillRect((cols - 1) * cellSize, (rows - 1) * cellSize, cellSize, cellSize);
    }
}

public class Question5a extends JFrame {
    MazePanel mazePanel = new MazePanel();

    public Question5a() {
        super("Maze Solver");

        JButton dfsBtn = new JButton("Solve with DFS");
        JButton bfsBtn = new JButton("Solve with BFS");
        JButton resetBtn = new JButton("Generate New Maze");

        dfsBtn.addActionListener(_ -> mazePanel.solveDFS());
        bfsBtn.addActionListener(_ -> mazePanel.solveBFS());
        resetBtn.addActionListener(_ -> mazePanel.generateMaze());

        JPanel controls = new JPanel();
        controls.add(dfsBtn);
        controls.add(bfsBtn);
        controls.add(resetBtn);

        add(mazePanel, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Question5a::new);
    }
}
