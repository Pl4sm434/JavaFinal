import java.awt.Color;
import java.awt.Graphics;

//Production Release 
//Date: 5/23/22

public class Projectile extends MovingPlatform {
	private boolean stopped = false;
	private int distance;
	
	public Projectile(int sX, int sY, int sW, int sH, int sD, int sS, int sType) {
		super(sX, sY, sW, sH, 1, 1, sS, sType);
		distance = sD;
		//type: 0 = up, 1 = down, 2 = right, 3 = left
	}
	
	//returns if it has stopped or not
	public boolean getStopped() {
		return stopped;
	}
	
	//returns the travel distance
	public int getDistance() {
		return distance;
	}
	
	//stops the projectile
	public void stop() {
		stopped = true;
	}
	
	//sets the color, then calls the paint method of its superclass
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		super.paint(g);
	}
	
	//updates it
	@Override
	public void action() {
		//System.out.println(super.getStartY() - super.getY());
		//System.out.println(super.getStartX() - super.getX());
		if (super.getType() == 0) {
			if (super.getStartY() - super.getY() >= distance) {
				//stopped = true;
			} else {
				super.setY(super.getY() - super.getSpeed());
			}
		} else if (super.getType() == 1) {
			if (super.getY() - super.getStartY() >= distance) {
				//stopped = true;
			} else {
				super.setY(super.getY() + super.getSpeed());
			}
		} else if (super.getType() == 2) {
			if (super.getX() - super.getStartX() >= distance) {
				//stopped = true;
			} else {
				super.setX(super.getX() + super.getSpeed());
			}
		} else if (super.getType() == 3) {
			if (super.getStartX() - super.getX() >= distance) {
				//stopped = true;
			} else {
				super.setX(super.getX() - super.getSpeed());
			}
		}
	}
	
	
}
