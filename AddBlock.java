import java.awt.Color;
import java.awt.Graphics;

public class AddBlock extends Block {
	
	Color color2;
	
	AddBlock(Color c, Color c2, int x, int y)
	{
		super(c,x,y);
		color2 = c2;
		setScoreMultiplyer(0);
	}
	
	public void draw(Graphics g, int x, int y)
	{
		int drawX = x+col*SIZE+1;
		int drawY = y+row*SIZE+1;
		int size = SIZE-2;
		g.setColor(color);
		g.fillRect(drawX, drawY, size, size);
		g.setColor(color2);
		g.drawLine(drawX+2, drawY+2, drawX+size-3, drawY+size-3);
		g.drawLine(drawX+size-3,drawY+2,drawX+2,drawY+size-3);
	}
}
