package com.tunabytes.maze;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MazePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int MaxWidth = 160, MaxHeight = 80, CellWidth = 8, CellHeight = CellWidth;
	private final Color backgroundColor = new Color(63, 68, 71);
	private boolean quickGenerate = false, quickSolve = false;
	private Maze maze;
	private Solver solver;
	
	public MazePanel() {
	}
	
	public void buildMaze() {
		maze = new Maze(MaxWidth + 2, MaxHeight + 2, CellWidth, CellHeight);
		if(quickGenerate) {
			long time = System.currentTimeMillis();
			while(!maze.expand());
			repaint();
			System.out.println("Generation time: " + (System.currentTimeMillis() - time) + "ms");
		} else {
			while(!maze.expand()) {
				repaint();
				try {
					Thread.sleep(0, 1);
				} catch(InterruptedException e) {
					System.exit(1);
				}
			}
		}
	}
	
	public void solveMaze() {
		solver = new Solver(maze);
		if(quickSolve) {
			long time = System.currentTimeMillis();
			while(!solver.solve());
			repaint();
			System.out.println("Solving time: " + (System.currentTimeMillis() - time) + "ms");
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				System.exit(1);
			}
		} else {
			while(!solver.solve()) {
				repaint();
				try {
					Thread.sleep(0, 1);
				} catch(InterruptedException e) {
					System.exit(1);
				}
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(maze != null) maze.paint(g);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		MazePanel panel = new MazePanel();
		frame.add(panel);
		frame.setSize(MaxWidth * CellWidth + 2 * CellWidth + 6, 
				MaxHeight * CellHeight + 2 * CellHeight + 29);
		frame.setTitle("Maze");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		loop(panel);
	}
	
	private static void loop(MazePanel panel) {
		while(true) {
			panel.buildMaze();
			panel.solveMaze();
		}
	}
	
}
