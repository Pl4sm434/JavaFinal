//Production Release 
//Date: 5/23/22
import java.awt.Graphics;

public class Shooter extends Platform {
	private int pSpeed, pRate, pDistance, pWidth, pHeight;
	private int projCount = 0;
	private Projectile proj[] = new Projectile[20];
	private boolean up, down, left, right;
	
	//x, y, w, h, speed, rate, distance, proj width, proj height, up, down, right, left
	public Shooter(int sX, int sY, int sW, int sH, int sS, int sR, int sD, int sPw, int sPh, boolean sUp, boolean sDown, boolean sRight, boolean sLeft) {
		super(sX, sY, sW, sH);
		pSpeed = sS;
		pRate = sR;
		pDistance = sD;
		pWidth = sPw;
		pHeight = sPh;
		up = sUp;
		down = sDown;
		right = sRight;
		left = sLeft;
	}
	
	//returns the projectile count
	public int getProjCount() {
		return projCount;
	}
	
	//returns projectiles
	public Projectile[] getProjectiles() {
		return proj;
	}
	
	//updates it
	public void action() {
		if (Game.time % pRate == 0) { //higher the rate, slower it is
			if (up) {
				if (projCount < 19) {
					projCount++;
				} else {
					projCount = 0;
				}
				proj[projCount] = new Projectile((super.getX()*2+super.getWidth())/2-pWidth/2, super.getY(), pWidth, pHeight, pDistance, pSpeed, 0);
			}
			if (down) {
				if (projCount < 19) {
					projCount++;
				} else {
					projCount = 0;
				}
				proj[projCount] = new Projectile((super.getX()*2+super.getWidth())/2-pWidth/2, super.getY()+super.getHeight()-pHeight, pWidth, pHeight, pDistance, pSpeed, 1);
			}
			if (right) {
				if (projCount < 19) {
					projCount++;
				} else {
					projCount = 0;
				}
				proj[projCount] = new Projectile(super.getX()+super.getWidth()-pHeight, (super.getY()*2+super.getHeight())/2-pWidth/2, pHeight, pWidth, pDistance, pSpeed, 2);
			}
			if (left) {
				if (projCount < 19) {
					projCount++;
				} else {
					projCount = 0;
				}
				proj[projCount] = new Projectile(super.getX(), (super.getY()*2+super.getHeight())/2-pWidth/2, pHeight, pWidth, pDistance, pSpeed, 3);
			}
		}
		for (int i = 0; i < proj.length; i++) {
			if (proj[i] != null) {
				proj[i].action(); 
				if (proj[i].getStopped() == true) {
					proj[i] = null;
				}
			}
		}
	}
	
	//draws
	@Override
	public void paint(Graphics g) {
		g.fillRect(super.getX(),super.getY(), super.getWidth(), super.getHeight());
		for (int i = 0; i < proj.length; i++) {
			if (proj[i] != null) {
				proj[i].paint(g);
			}
		}
	}
	
	
}
