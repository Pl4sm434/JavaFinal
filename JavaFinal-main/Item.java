//Production Release 
//Date: 5/23/22
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Item extends PowerUp {
	private boolean collected = false;
	private BufferedImage buffItem; 
	public Item(int sX, int sY, int sW, int sH, int t, Image pic) {
		super(sX,sY, sW, sH, t, pic);
		
		buffItem = new BufferedImage(sW, sH, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffItem.getGraphics();
		g.drawImage(super.getPicture(), 0, 0, sW, sH, null);
	}
	
	//returns if the item was collected or not
	public boolean getCollected() {
		return collected;
	}
	
	//collects the item
	public void collect() {
		collected = true;
	}
	// returns the buffered image
	public Image getBuffImage() {
		return buffItem;
	}
	
	//resets the item
	public void reset() {
		collected = false;
	}
	
}
