import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JApplet;

public class TetrisBoard {

	static final int GRAY = 128;
	static final int ANIMDELAY = 10;
	static final int WIDTH = 12;
	static final int HEIGHT = 21;
	static final Color BORDER_COLOR = new Color(GRAY, GRAY, GRAY);

	public Block[][] boardArray = new Block[TetrisBoard.HEIGHT][TetrisBoard.WIDTH];
	public Block[] border = new Block[TetrisBoard.HEIGHT * 2
			+ TetrisBoard.WIDTH - 2]; // here
	// for
	// border
	// animation?
	int y, x;
	int highLight = 0;
	int animRow;
	JApplet applet;

	TetrisBoard(JApplet a, int x, int y) {
		this.y = y;
		this.x = x;
		int b = 0;
		this.applet = a;

		for (int row = 0; row < TetrisBoard.HEIGHT; row++) {
			boardArray[row][0] = new Block(BORDER_COLOR, row, 0);
			border[b] = boardArray[row][0];
			b++;
			boardArray[row][WIDTH - 1] = new Block(BORDER_COLOR, row, WIDTH - 1);
		}
		for (int col = 1; col < TetrisBoard.WIDTH - 1; col++) {
			boardArray[HEIGHT - 1][col] = new Block(BORDER_COLOR, HEIGHT - 1,
					col);
			border[b] = boardArray[HEIGHT - 1][col];
			b++;
		}
		for (int row = TetrisBoard.HEIGHT - 1; row >= 0; row--) {
			boardArray[row][WIDTH - 1] = new Block(BORDER_COLOR, row, WIDTH - 1);
			border[b] = boardArray[row][WIDTH - 1];
			b++;
		}
	}

	public class MakePermanentAnim implements Runnable {
		Piece p;

		MakePermanentAnim(Piece piece) {
			p = piece;
			new Thread(this).start();
		}

		public void run() {
			Color c = p.getColor();
			for (int rInc = 0; rInc < 4; rInc++)
				for (int cInc = 0; cInc < 4; cInc++)
					if (p.getBlock(rInc, cInc) != null) {
						p.getBlock(rInc, cInc).setColor(Color.white);
						applet.repaint();
						try {
							Thread.sleep(ANIMDELAY * 4);
						} catch (InterruptedException e) {
						}
						p.getBlock(rInc, cInc).setColor(c);
						applet.repaint();
						try {
							Thread.sleep(ANIMDELAY * 4);
						} catch (InterruptedException e) {
						}
					}
		}
	}

	public void makePermanent(Piece p) {
		for (int rInc = 0; rInc < 4; rInc++)
			for (int cInc = 0; cInc < 4; cInc++)
				if (p.getBlock(rInc, cInc) != null)
					boardArray[p.getTop() + rInc][p.getLeft() + cInc] = p
							.getBlock(rInc, cInc);
		new MakePermanentAnim(p);
	}

	public double clearRow() {
		for (int r = TetrisBoard.HEIGHT - 2; r >= 0; r--) {
			int c;
			for (c = 1; c < TetrisBoard.WIDTH - 1; c++)
				if (boardArray[r][c] == null)
					break;
			if (c == TetrisBoard.WIDTH - 1) {
				double mult;
				mult = deleteRow(r);
				return r * mult;
			}
		}
		return -1;
	}

	public boolean randomBlock() {
		int col = (int) (Math.random() * (WIDTH - 2) + 1);
		int row;
		for (int i = 1; i < 20; i++) {
			for (row = 1; row < HEIGHT; row++)
				if (boardArray[row][col] != null)
					break;
			if (boardArray[row - 1][col] == null) {
				int counter = 0;
				for (int c = 1; c < WIDTH - 1; c++)
					if (boardArray[row - 1][c] == null)
						counter++;
				if (counter > 1) {
					boardArray[row - 1][col] = new AddBlock(BORDER_COLOR,
							Color.red, row - 1, col);
					applet.repaint();
					return true;
				}
			}
		}
		return false;
	}

	public void clear() {
		for (int row = 0; row < HEIGHT - 1; row++)
			for (int col = 1; col < WIDTH - 1; col++)
				boardArray[row][col] = null;
	}

	private double deleteRow(int r) {
		new ClearRowAnimation();
		double mult = 1;
		for (int c = 1; c < WIDTH - 1; c++)
			mult *= boardArray[r][c].getScoreMultiplyer();
		for (int rr = r; rr > 0; rr--)
			for (int c = 1; c < WIDTH - 1; c++) {
				boardArray[rr][c] = boardArray[rr - 1][c];
				if (boardArray[rr][c] != null)
					boardArray[rr][c].setRow(boardArray[rr][c].getRow() + 1);
			}
		for (int c = 1; c < WIDTH - 1; c++)
			boardArray[0][c] = null;
		return mult;
	}

	public void draw(Graphics g) {
		for (int row = 0; row < TetrisBoard.HEIGHT; row++)
			for (int col = 0; col < TetrisBoard.WIDTH; col++)
				if (boardArray[row][col] != null)
					boardArray[row][col].draw(g, x, y);
	}

	public void levelUpAnim(int level) {
		new LevelUpAnimation(level);
	}

	/**
	 * @return the boardArray
	 */
	public Block[][] getBoardArray() {
		return boardArray;
	}

	/**
	 * @param boardArray
	 *            the boardArray to set
	 */
	public void setBoardArray(Block[][] boardArray) {
		this.boardArray = boardArray;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	public Block getBlock(int row, int col) {
		return this.boardArray[row][col];
	}

	public class ClearRowAnimation implements Runnable {
		ClearRowAnimation() {
			new Thread(this).start();
		}

		public void run() {
			for (int col = WIDTH / 2; col >= 0; col--) {
				boardArray[HEIGHT - 1][col].setColor(Color.yellow);
				boardArray[HEIGHT - 1][WIDTH - col - 1].setColor(Color.yellow);
				applet.repaint();
				try {
					Thread.sleep(ANIMDELAY);
				} catch (InterruptedException e) {
				}
				boardArray[HEIGHT - 1][col].setColor(BORDER_COLOR);
				boardArray[HEIGHT - 1][WIDTH - col - 1].setColor(BORDER_COLOR);
			}
			for (int row = HEIGHT - 2; row >= 0; row--) {
				boardArray[row][0].setColor(Color.yellow);
				boardArray[row][WIDTH - 1].setColor(Color.yellow);
				boardArray[row + 1][0].setColor(BORDER_COLOR);
				boardArray[row + 1][WIDTH - 1].setColor(BORDER_COLOR);
				applet.repaint();
				try {
					Thread.sleep(ANIMDELAY);
				} catch (InterruptedException e) {
				}
			}
			boardArray[0][0].setColor(BORDER_COLOR);
			boardArray[0][WIDTH - 1].setColor(BORDER_COLOR);
			applet.repaint();
		}
	}

	public class LevelUpAnimation implements Runnable {
		static final int NUM_FLASHES = 15;
		int level;

		LevelUpAnimation(int l) {
			level = l;
			new Thread(this).start();
		}

		public void run() {
			for (int c = 0; c < NUM_FLASHES; c++) {
				Color flashColor = new Color(255 - (255 - GRAY) / NUM_FLASHES
						* c, GRAY / NUM_FLASHES * c, GRAY / NUM_FLASHES * c);
				for (int c2 = 0; c2 < border.length; c2++) {
					border[c2].setColor(flashColor);
				}
				applet.repaint();
				try {
					Thread.sleep(ANIMDELAY * 5);
				} catch (InterruptedException e) {
				}
				for (int c2 = 0; c2 < border.length; c2++) {
					border[c2].setColor(BORDER_COLOR);
				}
				applet.repaint();
				try {
					Thread.sleep(ANIMDELAY * 5);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
