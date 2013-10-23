import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class MazePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int MaxWidth = 60, MaxHeight = 40, CellWidth = 16, CellHeight = CellWidth;
	private boolean isGenerating, isSolving;
	private Timer timer;
	private Maze maze;
	
	public MazePanel() {
		timer = new Timer(1, new Update());
		timer.start();
	}
	
	private void solveMaze() {
				
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(maze != null) maze.paint(g);
	}
	
	public class Update implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(isGenerating) {
				if(!maze.expand()) {
					isGenerating = false;
					isSolving = true;
				}
			} else if(isSolving) {
				solveMaze();
			} else {
				maze = new Maze(MaxWidth, MaxHeight, CellWidth, CellHeight);
				isGenerating = true;
			}
			repaint();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new MazePanel());
		frame.setSize(MaxWidth * CellWidth + 6, MaxHeight * CellWidth + 29);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
}
