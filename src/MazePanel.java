import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class MazePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int MaxWidth = 150, MaxHeight = 80, CellWidth = 8, CellHeight = CellWidth;
	private boolean isGenerating, isSolving, quickGenerate = true;
	private Timer timer;
	private Maze maze;
	
	public MazePanel() {
		timer = new Timer(0, new Update());
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
				if(quickGenerate) {
					long time = System.currentTimeMillis();
					while(maze.expand());
					isGenerating = false;
					System.out.println((System.currentTimeMillis() - time) + "ms");
					isSolving = true;
					
				} else {
					if(!maze.expand()) {
						isGenerating = false;
						repaint();
						isSolving = true;
					}
				}
			} else if(isSolving) {
				solveMaze();
			} else {
				maze = new Maze(MaxWidth + 2, MaxHeight + 2, CellWidth, CellHeight);
				isGenerating = true;
			}
			repaint();
		}
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
