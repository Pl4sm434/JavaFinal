//Production Release 
//Date: 5/23/22
import java.awt.*;
// Creates a platform
public class Platform {
	private int x, y, width, height;
	
	public Platform(int sX, int sY, int sW, int sH) {
		x = sX;
		y = sY;
		width = sW;
		height = sH;
	}
	
	//draws the platform which is just a rectangle
	public void paint(Graphics g) {
		g.fillRect(x,y, width, height);
	}
	
	//returns the x
	public int getX() {
		return x;
	}
	
	//returns the y
	public int getY() {
		return y;
	}
	
	//sets the x
	public void setX(int newX) {
		x = newX;
	}
	
	//sets the y
	public void setY(int newY) {
		y = newY;
	}
	
	//returns the width
	public int getWidth() {
		return width;
	}
	
	//returns the height
	public int getHeight() {
		return height;
	}
	
	
}
