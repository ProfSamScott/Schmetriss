import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class TetrisScore implements Runnable {
	static final int INITIAL_ROWS_PER_LEVEL = 2;
	static final int MAX_ROWS_PER_LEVEL = 20;
	static final int ROWS_PER_LEVEL_INC = 2;
	int score, level, lines, rbTimeLeft;
	boolean clockwise = false;
	Piece nextPiece;
	int x, y;

	public TetrisScore(int x, int y) {
		this.x = x;
		this.y = y;
		resetScore();
		new Thread(this).start();
	}

	public void run()
	{
		while (true)
		{
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
			if (nextPiece != null)
				nextPiece.rotate(null);
		}
	}

	public void resetScore() {
		score = 0;
		level = 1;
		lines = INITIAL_ROWS_PER_LEVEL;
		nextPiece = new Piece(0, 0, clockwise);
	}

	public int lineCleared() {
		lines--;
		if (lines == 0) {
			level++;
			lines = (int) Math.min(INITIAL_ROWS_PER_LEVEL + (level - 1)
					* ROWS_PER_LEVEL_INC, MAX_ROWS_PER_LEVEL);
		}
		return level;
	}

	public void add(int x) {
		score += x;
	}

	public void changeDirection()
	{
		if (clockwise)
			clockwise = false;
		else
			clockwise = true;
		if (nextPiece != null)
			nextPiece.changeDirection();
	}

	public boolean getDirection()
	{
		return clockwise;
	}

	public Piece getNextPiece(int row, int col) {
		Piece temp = nextPiece;
		temp.setTop(row);
		temp.setLeft(col);
		nextPiece = new Piece(0, 0, clockwise);
		return temp;
	}

	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("MonoSpaced", Font.BOLD, 20));
		g.drawString("Score", x, y + 22);
		g.drawString("" + score, x, y + 44);
		g.drawString("Level", x, y + 88);
		g.drawString("" + level, x, y + 110);
		g.drawString("Lines", x, y + 154);
		g.drawString("" + lines, x, y + 176);
		g.drawString("Next", x, y + 220);
		if (nextPiece != null)
			nextPiece.draw(g, x, y + 240);
		g.setColor(Color.white);
		if (level >= Schmetriss.RANDBLOCKLEVEL) {
			if (rbTimeLeft == 0)
				g.setColor(Color.red);
			g.drawString("Danger!", x, y + 340);
			g.drawString(rbTimeLeft + "", x + 30, y + 367);			
			new AddBlock(TetrisBoard.BORDER_COLOR, Color.red, 0, 0).draw(g, x, y + 350);
		}
	}

	public int getRBTimeLeft() {
		return rbTimeLeft;
	}
	public void setRBTimeLeft(int rbtl) {
		rbTimeLeft = rbtl;
	}
}
