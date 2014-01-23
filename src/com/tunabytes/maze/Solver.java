package com.tunabytes.maze;
import java.util.HashMap;
import java.util.LinkedList;

import com.tunabytes.maze.Maze.Cell;
import com.tunabytes.maze.Maze.Dir;


public class Solver {

	private Cell startCell, endCell;
	private LinkedList<Cell> path = new LinkedList<Cell>();
	private HashMap<Cell, HashMap<Dir, Boolean>> checked = 
			new HashMap<Cell, HashMap<Dir, Boolean>>(); 
	
	private boolean isBacktracking = false;
	
	public Solver(Maze maze) {
		this.startCell = maze.getStartCell();
		this.endCell = maze.getEndCell();
		path.push(startCell);
	}
	
	/**
	 * Solves the maze using the left hand rule
	 * 
	 * @return true if maze is solved
	 */
	public boolean solve() {
		if(isBacktracking) {
			if(getNextDir(path.peek()) != null) {
				isBacktracking = false;
			} else {
				path.pop().setInPath(false);
			}
			return false;
		}
		
		Cell current = path.peek();
		if(!checked.containsKey(current)) {
			expandChecked(current);
		}
		
		Dir nextDir = getNextDir(current);
		if(nextDir == null) {
			isBacktracking = true;
			path.pop().setInPath(false);
			return false;
		} else {
			Cell nextCell = current.getNeighbor(nextDir);
			nextCell.setInPath(true);
			path.push(nextCell);
			checked.get(current).put(nextDir, true);
			expandChecked(nextCell);
			checked.get(nextCell).put(getOppositeDir(nextDir), true);
		}

		if(path.peek() == endCell) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds the specified cell to the checked cells to memoize taken paths
	 */
	private void expandChecked(Cell cell) {
		HashMap<Dir, Boolean> passable = new HashMap<Dir, Boolean>(cell.getPassable());
		for(Dir dir : passable.keySet()) {
			passable.put(dir, !passable.get(dir));
		}
		checked.put(cell, passable);
	}
	
	/**
	 * @return the next direction to travel in solving the maze
	 */
	private Dir getNextDir(Cell cell) {
		HashMap<Dir, Boolean> passed = checked.get(cell);
		if(!passed.get(Dir.Left)) {
			return Dir.Left;
		} else if(!passed.get(Dir.Up)){
			return Dir.Up;
		} else if(!passed.get(Dir.Right)) {
			return Dir.Right;
		} else if(!passed.get(Dir.Down)){
			return Dir.Down;
		} else {
			return null;
		}
	}
	
	private Dir getOppositeDir(Dir dir) {
		switch(dir) {
		case Left:
			return Dir.Right;
		case Up:
			return Dir.Down;
		case Right:
			return Dir.Left;
		default:
			return Dir.Up;
		}
	}
	
	/**
	 * Clears the solved path from the maze
	 */
	public void clearPath() {
		for(Cell cell : path) {
			cell.setInPath(false);
		}
	}
	
}
