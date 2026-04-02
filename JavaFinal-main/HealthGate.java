//Production Release 
//Date: 5/23/22

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class HealthGate extends Platform {
	
	private double minHealth;
	private boolean minHealthVisible;
	private Rectangle2D stringRect;
	private FontRenderContext frc;
	private Font font;
	
	public HealthGate(int sX, int sY, int sW, int sH, double sMin, boolean sMinVisible) {
		super(sX, sY, sW, sH);
		minHealth = sMin;
		minHealthVisible = sMinVisible;
		
		int fontSize;
		if (super.getHeight() > super.getWidth()) {
			fontSize = (int)(super.getWidth()/2.0); //limit by width if width is smaller
		} else {
			fontSize = (int)(super.getHeight()/2.0); //limit by height if height is smaller
		}
		
		font = new Font("Monospaced", Font.BOLD, fontSize);
		frc = new FontRenderContext(null, true, true);
		
	}

	//returns if the player is able to pass this gate
	public boolean isPassed(double health) {
		return health > minHealth;
	}
	
	//draws a rectangle and the minimum health
	@Override
	public void paint(Graphics g) {
		if (!isPassed(Game.p.getHealth())) {
			g.setColor(new Color(40, 44, 47));
		} else {
			g.setColor(new Color(40, 44, 47, 140));
		}
		super.paint(g);
		
		if (!isPassed(Game.p.getHealth())) {
			g.setColor(new Color(255, 0, 0));
		} else {
			g.setColor(new Color(0, 100, 0, 140));
		}
		
		String healthString = ">"+(int)minHealth;
		stringRect = font.getStringBounds(healthString, frc);
		int a = (super.getWidth() / 2) - ((int)(stringRect.getWidth()+0.5)/2) - (int)(stringRect.getX()+0.5);
		int b = (super.getHeight() / 2) - ((int)(stringRect.getHeight()+0.5)/2) - (int)(stringRect.getY()+0.5);
		
		g.setFont(font);
		if (minHealthVisible) {
			g.drawString(healthString, super.getX() + a, super.getY() + b);
		}
	}
}
