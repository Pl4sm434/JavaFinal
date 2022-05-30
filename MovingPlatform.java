///Production Release 
//Date: 5/23/22
// Platform that moves
public class MovingPlatform extends Platform {
	private int startX, startY, speed, bH, bL;
	private int type, direction = 1; //type 0: horizontal, 1: vertical
	
	public MovingPlatform(int sX, int sY, int sW, int sH, int boundLow, int boundHigh, int sS, int sType) {
		super(sX, sY, sW, sH);
		startX = sX;
		startY = sY;
		bH = boundHigh;
		bL = boundLow;
		type = sType;
		speed = sS;
		
	}
	
	//returns direction
	public int getDir() {
		return direction;
	}
	
	//returns the type
	public int getType() {
		return type;
	}
	
	//returns the lower boundary
	public int getLow() {
		return bL;
	}
	
	//returns the upper boundary
	public int getHigh() {
		return bH;
	}
	
	//returns the starting y
	public int getStartY() {
		return startY;
	}
	
	//returns the starting x
	public int getStartX() {
		return startX;
	}
	
	//returns the speed
	public int getSpeed() {
		return speed;
	}
	
	//updates it
	public void action() {
		if (type == 0) {
			if (super.getX() > bH - super.getWidth()) {
				direction = -1;
				super.setX(super.getX()-speed);
			} else if (super.getX() < bL) {
				direction = 1;
				super.setX(super.getX()+speed);
			} else {
				super.setX(super.getX()+direction*speed);
			}
		} else if (type == 1) {
			if (super.getY() > bH - super.getHeight()) {
				direction = -1;
				super.setY(super.getY()-speed);
			} else if (super.getY() < bL) {
				direction = 1;
				super.setY(super.getY()+speed);
			} else {
				super.setY(super.getY()+direction*speed);
			}
		}
	}
	
}
