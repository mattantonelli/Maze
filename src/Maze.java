import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;


public class Maze {

	private ArrayList<Cell> wallCells = new ArrayList<Cell>();
	private Cell startCell, endCell = null, lastCell;
	private Color startColor = new Color(36, 173, 222), endColor = new Color(138, 189, 0), 
			lastColor = new Color(204, 0, 0), backgroundColor = new Color(63, 68, 71), 
			borderColor = new Color(37, 40, 41);
	private Random rand = new Random();
	private Cell[][] maze;
	private int cellWidth, cellHeight;
	
	private enum Dir {
		Left, Up, Right, Down
	}
	
	public Maze(int width, int height, int cellWidth, int cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		maze = new Cell[width][height];
		
		// Defaults all cell values to -1 to show they have not been visited
		for(int col = 0; col < maze.length; col++) {
			for(int row = 0; row < maze[0].length; row++) {
				maze[col][row] = new Cell(col, row, -1);
			}
		}
		
		// Provides a single cell border which allows neater printing and prevents referencing out of bounds
		for(int col = 0; col < maze.length; col++) {
			for(int row = 0; row < maze[0].length; row += maze[0].length - 1) {
				maze[col][row] = new Cell(col, row, -2);
			}
		}
		
		for(int col = 0; col < maze.length; col += maze.length - 1) {
			for(int row = 0; row < maze[0].length; row++) {
				maze[col][row] = new Cell(col, row, -2);
			}
		}
		
		// Selects a random cell as the start of the maze
		int x = rand.nextInt(width - 2) + 1;
		int y = rand.nextInt(height - 2) + 1;
		maze[x][y].setVal(1);
		startCell = maze[x][y];
		addPassage(startCell);
	}

	/**
	 * Generates a maze using Prim's Algorithm
	 * 
	 * @return true if maze is not fully expanded
	 */
	public boolean expand() {
		int randIndex = rand.nextInt(wallCells.size());
		Cell randCell = wallCells.remove(randIndex);
		
		addPassage(randCell);
		
		if(wallCells.isEmpty()) {
			endCell = lastCell;
			lastCell = null;
		}
		
		return !wallCells.isEmpty();	// Done expanding when there are no more unchecked wall cells
	}
	
	/**
	 * Converts wall to a passage if doing so does not create an intersection
	 */
	private void addPassage(Cell cell) {
		cell.setPassage();
		addWalls(cell);
	}
	
	/**
	 * Adds walls adjacent to the given cell
	 */
	private void addWalls(Cell cell) {
		cell.setWallLeft();
		cell.setWallUp();
		cell.setWallRight();
		cell.setWallDown();
	}
	
	/**
	 * Paints the maze
	 */
	public void paint(Graphics g) {
		for(Cell[] row : maze) {
			for(Cell cell : row) {
				cell.paintCell(g);
			}
		}

		for(Cell[] row : maze) {
			for(Cell cell : row) {
				cell.paintWalls(g);
			}
		}
	}
	
	/**
	 * Holds a given location in the maze, allowing or easy manipulation of neighboring cells
	 */
	class Cell {
		private final int x, y;
		private final boolean[] passable = new boolean[]{false, false, false, false};	// Left, Up, Right, Down
		
		private Dir prev;
		private int val;
		
		public Cell(int x, int y, int val) {
			this.x = x;
			this.y = y;
			this.val = val;
		}
		
		/**
		 * Sets the cell as a passage if it will not create an intersection
		 */
		public boolean setPassage() {
			maze[x][y].setVal(0);
			lastCell = maze[x][y];
			if(prev != null) openPrevious();
			return true;
		}
		
		private void openPrevious() {
			openPassage(prev);
			switch(prev) {
			case Left:
				maze[x - 1][y].openPassage(Dir.Right);
				break;
			case Up:
				maze[x][y - 1].openPassage(Dir.Down);
				break;
			case Right:
				maze[x + 1][y].openPassage(Dir.Left);
				break;
			case Down:
				maze[x][y + 1].openPassage(Dir.Up);
			}
		}
		
		public void openPassage(Dir dir) {
			passable[dir.ordinal()] = true;
		}
		
		/**
		 * Sets the cell to the left of this cell in the maze as a wall if it is unvisited
		 */
		public void setWallLeft() {
			Cell cell = maze[x - 1][y];
			if(cell.getVal() == -1) {
				cell.setVal(1);
				cell.setPrev(Dir.Right);
				wallCells.add(cell);
			}
		}
		
		/**
		 * Sets the cell above this cell in the maze as a wall if it is unvisited
		 */
		public void setWallUp() {
			Cell cell = maze[x][y - 1];
			if(cell.getVal() == -1) {
				cell.setVal(1);
				cell.setPrev(Dir.Down);
				wallCells.add(cell);
			}
		}
		
		/**
		 * Sets the cell to the right of this cell in the maze as a wall if it is unvisited
		 */
		public void setWallRight() {
			Cell cell = maze[x + 1][y];
			if(cell.getVal() == -1) {
				cell.setVal(1);
				cell.setPrev(Dir.Left);
				wallCells.add(cell);
			}
		}
		
		/**
		 * Sets the cell below this cell in the maze as a wall if it is unvisited
		 */
		public void setWallDown() {
			Cell cell = maze[x][y + 1];
			if(cell.getVal() == -1) {
				cell.setVal(1);
				cell.setPrev(Dir.Up);
				wallCells.add(cell);
			}
		}
		
		public int getVal() {
			return val;
		}
		
		public void setVal(int val) {
			this.val = val;
		}
		
		public void setPrev(Dir prev) {
			this.prev = prev;
		}
		
		public void paintCell(Graphics g) {
			if(this == startCell) {
				g.setColor(startColor);
			} else if(this == lastCell) {
				g.setColor(lastColor);
			} else if(endCell != null && this == endCell) {
				g.setColor(endColor);
			} else if(val == 0) {
				g.setColor(Color.WHITE);
			} else if(val == -2) {
				g.setColor(borderColor);
			} else {
				g.setColor(backgroundColor);
			}
			
			g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
		}
		
		public void paintWalls(Graphics g) {
			if(val == 0) {
				g.setColor(Color.BLACK);
				if(!passable[Dir.Left.ordinal()]) {
					g.drawLine(x * cellWidth, y * cellHeight, x * cellWidth, (y + 1) * cellHeight);
				}
				
				if(!passable[Dir.Up.ordinal()]) {
					g.drawLine(x * cellWidth, y * cellHeight, (x + 1) * cellWidth, y * cellHeight);			
				}
				
				if(!passable[Dir.Right.ordinal()]) {
					g.drawLine((x + 1) * cellWidth, y * cellHeight, (x + 1) * cellWidth, (y + 1) * cellHeight);
				}
				
				if(!passable[Dir.Down.ordinal()]) {
					g.drawLine(x * cellWidth, (y + 1) * cellHeight, (x + 1) * cellWidth, (y + 1) * cellHeight);
				}
			}
		}		
		
	}

}
