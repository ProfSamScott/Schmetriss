import java.awt.Color;
import java.awt.Graphics;

public class Piece {

	static final Color COLOR1 = new Color(200,100,0);
	static final int[][][] LAYOUT1 = { 
		{ { 0, 0, 0, 0 }, 
		  { 1, 1, 1, 1 },
		  { 0, 0, 0, 0 }, 
		  { 0, 0, 0, 0 }
		},
		{
		  { 0, 1, 0, 0 }, 
		  { 0, 1, 0, 0 },
		  { 0, 1, 0, 0 }, 
		  { 0, 1, 0, 0 } 
		}
};
	static final Color COLOR2 = new Color(200,0,200);	
	static final int[][][] LAYOUT2 = { 
		{ { 0, 0, 0, 0 }, 
		  { 1, 1, 1, 0 },
		  { 0, 0, 1, 0 }, 
		  { 0, 0, 0, 0 } 
		},
		{ { 0, 1, 1, 0 }, 
		  { 0, 1, 0, 0 },
		  { 0, 1, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		},
		{ { 1, 0, 0, 0 }, 
		  { 1, 1, 1, 0 },
		  { 0, 0, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		},	
		{ { 0, 1, 0, 0 }, 
		  { 0, 1, 0, 0 },
		  { 1, 1, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		}			
	};

	static final Color COLOR3 = new Color(200,0,0);	
	static final int[][][] LAYOUT3 = { 
		{ { 1, 1, 0, 0 }, 
		  { 1, 1, 0, 0 },
		  { 0, 0, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		}		
	};
	
	static final Color COLOR4 = new Color(0,0,225);	
	static final int[][][] LAYOUT4 = { 
		{ { 0, 0, 0, 0 }, 
		  { 1, 1, 1, 0 },
		  { 1, 0, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		},
		{ { 0, 1, 0, 0 }, 
		  { 0, 1, 0, 0 },
		  { 0, 1, 1, 0 }, 
		  { 0, 0, 0, 0 } 
		},
		{ { 0, 0, 1, 0 }, 
		  { 1, 1, 1, 0 },
		  { 0, 0, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		},	
		{ { 1, 1, 0, 0 }, 
		  { 0, 1, 0, 0 },
		  { 0, 1, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		}			
	};
	static final Color COLOR5 = new Color (0,176,200);	
	static final int[][][] LAYOUT5 = { 
		{ { 0, 0, 0, 0 }, 
		  { 0, 1, 1, 0 },
		  { 1, 1, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		},
		{ { 0, 1, 0, 0 }, 
		  { 0, 1, 1, 0 },
		  { 0, 0, 1, 0 }, 
		  { 0, 0, 0, 0 } 
		}			
	};
	static final Color COLOR6 = new Color (0,200,0);	
	static final int[][][] LAYOUT6 = { 
		{ { 0, 0, 0, 0 }, 
		  { 1, 1, 0, 0 },
		  { 0, 1, 1, 0 }, 
		  { 0, 0, 0, 0 } 
		},
		{ { 0, 0, 1, 0 }, 
		  { 0, 1, 1, 0 },
		  { 0, 1, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		}			
	};
	static final Color COLOR7 = new Color (200,200,0);	
	static final int[][][] LAYOUT7 = { 
		{ { 0, 0, 0, 0 }, 
		  { 1, 1, 1, 0 },
		  { 0, 1, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		},
		{ { 0, 1, 0, 0 }, 
		  { 0, 1, 1, 0 },
		  { 0, 1, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		},
		{
	      { 0, 1, 0, 0 }, 
		  { 1, 1, 1, 0 },
		  { 0, 0, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		},
		{ { 0, 1, 0, 0 }, 
		  { 1, 1, 0, 0 },
		  { 0, 1, 0, 0 }, 
		  { 0, 0, 0, 0 } 
		}		
	};	
	static final int[][][][] LAYOUTS = {LAYOUT1, LAYOUT2, LAYOUT3, LAYOUT4, LAYOUT5, LAYOUT6, LAYOUT7};
	static final Color[] COLORS = {COLOR1, COLOR2, COLOR3, COLOR4, COLOR5, COLOR6, COLOR7};
	
	int [][][] layout;
	Color color;
	int frame, top, left;
	Block [] blocks = new Block[4];
	Block [][] blockLayout = new Block[4][4];
	boolean clockwise = false;
	
	public Piece(int top, int left, boolean clockwise)
	{
		this.top = top;
		this.left = left;
		this.clockwise = clockwise;
		int i = (int)(Math.random()*LAYOUTS.length);
		this.color = COLORS[i];
		this.layout = LAYOUTS[i];
		this.frame = 0;
		
		int b = 0;
		for (int rInc = 0; rInc < 4; rInc++)
			for (int cInc = 0; cInc < 4; cInc++)
				if (layout[0][rInc][cInc] == 1)
				{
					this.blocks[b] = new Block(this.color,this.top+rInc,this.left+cInc);
					this.blockLayout[rInc][cInc] = this.blocks[b];
					b++;
				}
	}
	
	public boolean rotate(TetrisBoard board)
	{
		if (board == null || !rotateCollision(board))
		{
			if (clockwise)
				this.frame = (frame+1)%layout.length;
			else
			{
				this.frame = frame - 1;
				if (frame == -1)
					frame = layout.length-1;
			}
			int b = 0;
			for (int rInc = 0; rInc < 4; rInc++)
				for (int cInc = 0; cInc < 4; cInc++)
					if (this.layout[this.frame][rInc][cInc] == 1)
					{
						this.blocks[b].setPos(this.top+rInc,this.left+cInc);
						this.blockLayout[rInc][cInc] = this.blocks[b];
						b++;
					}	
					else
						this.blockLayout[rInc][cInc] = null;
			return true;
		}
		return false;
	}
	
	public boolean rotateCollision(TetrisBoard board)
	{
		int newFrame = (frame+1)%layout.length;
		for (int rInc = 0; rInc < 4; rInc++)
			for (int cInc = 0; cInc < 4; cInc++)
				if ((this.layout[newFrame][rInc][cInc] == 1) && (board.getBlock(this.top+rInc,this.left+cInc) != null)) 
					return true;
		return false;
	}
	
	public boolean advance (TetrisBoard board)
	{
		if (!advanceCollision(board))
		{
			top++;
			for (int rInc = 0; rInc < 4; rInc++)
				for (int cInc = 0; cInc < 4; cInc++)
					if (this.blockLayout[rInc][cInc] != null)
						this.blockLayout[rInc][cInc].setPos(this.top+rInc,this.left+cInc);	
			return true;
		}
		return false;
	}

	public boolean moveLeft (TetrisBoard board)
	{
		if (!leftCollision(board))
		{
			left--;
			for (int rInc = 0; rInc < 4; rInc++)
				for (int cInc = 0; cInc < 4; cInc++)
					if (this.blockLayout[rInc][cInc] != null)
						this.blockLayout[rInc][cInc].setPos(this.top+rInc,this.left+cInc);	
			return true;
		}
		return false;
	}

	public boolean moveRight (TetrisBoard board)
	{
		if (!rightCollision(board))
		{
			left++;
			for (int rInc = 0; rInc < 4; rInc++)
				for (int cInc = 0; cInc < 4; cInc++)
					if (this.blockLayout[rInc][cInc] != null)
						this.blockLayout[rInc][cInc].setPos(this.top+rInc,this.left+cInc);	
			return true;
		}
		return false;
	}
	
	public boolean collision (TetrisBoard board)
	{
		for (int b=0; b<4; b++)
			if (board.getBlock(blocks[b].getRow(),blocks[b].getCol()) != null)
				return true;
		return false;
	}	
	
	private boolean advanceCollision (TetrisBoard board)
	{
		for (int b=0; b<4; b++)
			if (board.getBlock(blocks[b].getRow()+1,blocks[b].getCol()) != null)
				return true;
		return false;
	}
	
	private boolean leftCollision (TetrisBoard board)
	{
		for (int b=0; b<4; b++)
			if (board.getBlock(blocks[b].getRow(),blocks[b].getCol()-1) != null)
				return true;
		return false;
	}
	
	private boolean rightCollision (TetrisBoard board)
	{
		for (int b=0; b<4; b++)
			if (board.getBlock(blocks[b].getRow(),blocks[b].getCol()+1) != null)
				return true;
		return false;
	}
	
	public int getTop()
	{
		return top;
	}
	
	public int getLeft()
	{
		return left;
	}
	
	public Block getBlock(int r, int c)
	{
		return blockLayout[r][c];
	}

	
	public void draw(Graphics g, TetrisBoard board)
	{
		for (int b = 0; b<4; b++)
			this.blocks[b].draw(g, board.getX(), board.getY());
	}
	
	public void draw(Graphics g, int x, int y)
	{
		for (int b = 0; b<4; b++)
			this.blocks[b].draw(g, x, y);
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(int top) {
		this.top = top;
		for (int rInc = 0; rInc < 4; rInc++)
			for (int cInc = 0; cInc < 4; cInc++)
				if (this.blockLayout[rInc][cInc] != null)
					this.blockLayout[rInc][cInc].setPos(this.top+rInc,this.left+cInc);			
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(int left) {
		this.left = left;
		for (int rInc = 0; rInc < 4; rInc++)
			for (int cInc = 0; cInc < 4; cInc++)
				if (this.blockLayout[rInc][cInc] != null)
					this.blockLayout[rInc][cInc].setPos(this.top+rInc,this.left+cInc);			
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void changeDirection()
	{
		if (clockwise)
			clockwise = false;
		else
			clockwise = true;
	}
}
