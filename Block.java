import java.awt.Color;
import java.awt.Graphics;


public class Block {

	static final int SIZE = 20;
	Color color;
	int row, col;
	float scoreMultiplyer;
	
	Block(Color color, int row, int col)
	{
		this.color = color;
		this.row = row;
		this.col = col;
		setScoreMultiplyer(1);
	}
	
	public void setPos(int row, int col)
	{
		this.row = row;
		this.col = col;
	}
	
	public int getRow()
	{
		return this.row;
	}
	
	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}	
	
	public int getCol()
	{
		return this.col;
	}	
	
	public void draw(Graphics g, int x, int y)
	{
		g.setColor(color);
		g.fillRect(x+col*SIZE+1,y+row*SIZE+1,SIZE-2,SIZE-2);
	}

	public void setColor(Color color)
	{
		this.color = color; 
	}

	/**
	 * @return the scoreMultiplyer
	 */
	public float getScoreMultiplyer() {
		return scoreMultiplyer;
	}

	/**
	 * @param scoreMultiplyer the scoreMultiplyer to set
	 */
	public void setScoreMultiplyer(float scoreMultiplyer) {
		this.scoreMultiplyer = scoreMultiplyer;
	}
}



