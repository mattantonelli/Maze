Maze Generator and Solver
========
<i>By: Matt Antonelli</i>

This Java application was designed to procedurally generate a maze using a randomized implemenation of Prim's Algorithm and solve the maze using the left-hand rule.

The GUI highlights the starting cell in <i>blue</i> and the ending cell in <i>green</i>. While the maze is being calculated, the
current cell being checked is highlighted in <i>red</i>.

Quick generating and quick solving can both be enabled manually. Otherwise, each step takes 1ms to calculate and render.

	private boolean isGenerating, isSolving, quickGenerate = false, quickSolve = false;


Sample runs:

![Sample Run 1](http://tunabytes.com/imgdump/maze1.png)

![Sample Run 2](http://tunabytes.com/imgdump/maze2.png)

![Sample Run 3](http://tunabytes.com/imgdump/maze3.png)

![Sample Run 4](http://tunabytes.com/imgdump/maze4.png)