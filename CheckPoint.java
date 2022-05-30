//Production Release 
//Date: 5/23/22

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class CheckPoint {
	private int x;
	private int y;
	private int width;
	private int height;
	private int level;
	private boolean gotten = false;
	private BufferedImage picture;
	private BufferedImage pictureGotten;
	
	public CheckPoint(int sX, int sY, int w, int h, int l, Image pic, Image picGotten) {
		x = sX;
		y = sY;
		width = w;
		height = h;
		
		picture = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = picture.getGraphics();
		g.drawImage(pic, 0, 0, w, h, null);
		
		pictureGotten = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = pictureGotten.getGraphics();
		g2.drawImage(picGotten, 0, 0, w, h, null);
		
		level = l;
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
	
	//returns the image
	public Image getPicture() {
		if (gotten) {
			return pictureGotten;
		} else {
			return picture;
		}
		
	}
	
	//returns the level this checkpoint is on
	public int getLevel() {
		return level;
	}
	
	//obtains the checkpoint
	public void get() {
		gotten = true;
	}
	
	//unmarks the checkpoint
	public void unmark() {
		gotten = false;
	}
}
