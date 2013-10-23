import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;


public class Maze {

	private ArrayList<Cell> wallCells = new ArrayList<Cell>();
	private Cell startCell, endCell = null, lastCell;
	private Color startColor = new Color(36, 173, 222), endColor = new Color(138, 189, 0),
			lastColor = new Color(255, 0, 0);
	private Random rand = new Random();
	private int[][] maze;
	private int cellWidth, cellHeight;
	
	public Maze(int width, int height, int cellWidth, int cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		maze = new int[width][height];
		
		// Defaults all cell values to -1 to show they have not been visited
		for(int col = 0; col < maze.length; col++) {
			for(int row = 0; row < maze[0].length; row++) {
				maze[col][row] = -1;
			}
		}
		
		// Provides a single cell border which allows neater printing and prevents referencing out of bounds
		for(int col = 0; col < maze.length; col++) {
			for(int row = 0; row < maze[0].length; row += maze[0].length - 1) {
				maze[col][row] = -2;
			}
		}
		
		for(int col = 0; col < maze.length; col += maze.length - 1) {
			for(int row = 0; row < maze[0].length; row++) {
				maze[col][row] = -2;
			}
		}
		
		// Selects a random cell as the start of the maze
//		int x = rand.nextInt(width - 2) + 1;
//		int y = rand.nextInt(height - 2) + 1;
//		startCell = new Cell(x, y);
		startCell = new Cell(2, height - 2);
		lastCell = startCell;
		addPassage(startCell);
	}

	/**
	 * Generates a maze using Prim's Algorithm
	 * 
	 * @return true if maze is not fully expanded
	 */
	public boolean expand() {
		int randIndex = rand.nextInt(wallCells.size());
		Cell randCell = wallCells.get(randIndex);
		lastCell = randCell;
		
		addPassage(randCell);
		if(wallCells.size() == 1) endCell = randCell;
		wallCells.remove(randIndex);
		
		return !wallCells.isEmpty();	// Done expanding when there are no more unchecked wall cells
	}
	
	/**
	 * Converts wall to a passage if doing so does not create an intersection
	 */
	private void addPassage(Cell cell) {
		if(cell.setPassage()) {
			addWalls(cell);
		}
	}
	
	/**
	 * Adds walls adjacent to the given cell
	 */
	private void addWalls(Cell cell) {
		if(cell.setWallLeft()) wallCells.add(new Cell(cell.x - 1, cell.y));
		if(cell.setWallUp()) wallCells.add(new Cell(cell.x, cell.y - 1));
		if(cell.setWallRight()) wallCells.add(new Cell(cell.x + 1, cell.y));
		if(cell.setWallDown()) wallCells.add(new Cell(cell.x, cell.y + 1));
	}
	
	/**
	 * Paints the maze
	 */
	public void paint(Graphics g) {
		for(int col = 1; col < maze.length - 1; col++) {
			for(int row = 1; row < maze[0].length - 1; row++) {
				if(maze[col][row] == 0) {
					g.setColor(Color.WHITE);
				} else {
					g.setColor(Color.BLACK);
				}
				
				g.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
			}
		}
		
		g.setColor(startColor);
		g.fillRect(startCell.x * cellWidth, startCell.y * cellHeight, cellWidth, cellHeight);
		
		g.setColor(lastColor);
		g.fillRect(lastCell.x * cellWidth, lastCell.y * cellHeight, cellWidth, cellHeight);
		
		if(endCell != null) {
			g.setColor(endColor);
			g.fillRect(endCell.x * cellWidth, endCell.y * cellHeight, cellWidth, cellHeight);
		}
	}
	
	/**
	 * Holds a given location in the maze, allowing or easy manipulation of neighboring cells
	 */
	class Cell {
		final int x, y;
		
		public Cell(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		/**
		 * Sets the cell as a passage if it will not create an intersection
		 * @return true if the value was able to be set
		 */
		public boolean setPassage() {
			int adjacentPassages = 0;
			if(maze[x - 1][y] == 0) adjacentPassages++;
			if(maze[x][y - 1] == 0) adjacentPassages++;
			if(maze[x + 1][y] == 0) adjacentPassages++;
			if(maze[x][y + 1] == 0) adjacentPassages++;
			
			if(adjacentPassages < 2) {
				maze[x][y] = 0;
				return true;
			}
			return false;
		}
		
		/**
		 * Sets the cell to the left of this cell in the maze as a wall if it is unvisited
		 * @return true if the value was able to be set
		 */
		public boolean setWallLeft() {
			if(maze[x - 1][y] == -1) {
				maze[x - 1][y] = 1;
				return true;
			}
			return false;
		}
		
		/**
		 * Sets the cell above this cell in the maze as a wall if it is unvisited
		 * @return true if the value was able to be set
		 */
		public boolean setWallUp() {
			if(maze[x][y - 1] == -1) {
				maze[x][y - 1] = 1;
				return true;
			}
			return false;
		}
		
		/**
		 * Sets the cell to the right of this cell in the maze as a wall if it is unvisited
		 * @return true if the value was able to be set
		 */
		public boolean setWallRight() {
			if(maze[x + 1][y] == -1) {
				maze[x + 1][y] = 1;
				return true;
			}
			return false;
		}
		
		/**
		 * Sets the cell below this cell in the maze as a wall if it is unvisited
		 * @return true if the value was able to be set
		 */
		public boolean setWallDown() {
			if(maze[x][y + 1] == -1) {
				maze[x][y + 1] = 1;
				return true;
			}
			return false;
		}
	}

}
