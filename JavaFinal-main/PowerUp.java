//Production Release 
//Date: 5/23/22
import java.awt.Image;

public class PowerUp {
	private int x, y, width, height;
	private int type; //0 = dash, 1 = gravity, 2 = wall jump
	private Image picture;
	
	public PowerUp(int sX, int sY, int sW, int sH, int t, Image pic) {
		x = sX;
		y = sY;
		width = sW;
		height = sH;
		type = t;
		picture = pic;
	}
	
	//returns the type
	public int getType() {
		return type;
	}
	
	//returns the picture
	public Image getPicture() {
		return picture;
	}
	
	//returns the x
	public int getX() {
		return x;
	}
	
	//returns the y
	public int getY() {
		return y;
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
