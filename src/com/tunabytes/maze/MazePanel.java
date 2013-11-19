package com.tunabytes.maze;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class MazePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int MaxWidth = 40, MaxHeight = 40, CellWidth = 16, CellHeight = CellWidth;
	private final Color backgroundColor = new Color(63, 68, 71);
	private boolean isGenerating, isSolving, quickGenerate = false;
	private Timer timer;
	private Maze maze;
	private Solver solver;
	
	public MazePanel() {
		timer = new Timer(0, new Update());
		timer.start();
	}
	
	public class Update implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(isGenerating) {
				solver = null;
				if(quickGenerate) {
					long time = System.currentTimeMillis();
					while(!maze.expand());
					isGenerating = false;
					System.out.println((System.currentTimeMillis() - time) + "ms");
					solver = new Solver(maze);
					isSolving = true;
				} else {
					if(maze.expand()) {
						isGenerating = false;
						repaint();
						solver = new Solver(maze);
						isSolving = true;
					}
				}
			} else if(isSolving) {
				if(solver.solve()) {
					isSolving = false;
					repaint();
					try {
						Thread.sleep(5000);
					} catch(InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} else {
				maze = new Maze(MaxWidth + 2, MaxHeight + 2, CellWidth, CellHeight);
				isGenerating = true;
			}
			repaint();
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
		frame.add(new MazePanel());
		frame.setSize(MaxWidth * CellWidth + 2 * CellWidth + 6, 
				MaxHeight * CellHeight + 2 * CellHeight + 29);
		frame.setTitle("Maze");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
}
