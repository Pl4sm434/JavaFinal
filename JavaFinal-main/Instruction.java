//Production Release 
//Date: 5/23/22

import java.awt.Image;
import java.awt.*;

public class Instruction {
	private int x;
	private int y;
	private int width;
	private int height;
	private int bobDir = 0; //press E bob animation
	private double bobDistance = 0; //bob animation distance
	private Image picture1; //sign
	private Image picture2; //Press E
	private String[] message = new String[2];
	private boolean opened = false;
	private boolean standingOn = false;
	private int curMessage = 1;
	private int charsTyped = 0;
	private int index = 1;
	private int special = 0;
	private Color c1 = new Color(61, 44, 19);
	private Color c2 = new Color(247, 247, 129);
	private Font f1 = new Font("Monospaced", Font.BOLD, 15);
	private Font f2 = new Font("Monospaced", Font.ITALIC, 10);


	
	public Instruction(int xPos, int yPos, int w, int h, Image pic1, Image pic2, String[] s) {
		x = xPos;
		y = yPos;
		width = w;
		height = h;
		picture1 = pic1;
		picture2 = pic2;
		message = s;
	}
	
	public Instruction(int xPos, int yPos, int w, int h, Image pic1, Image pic2, String[] s, int sp) {
		this( xPos, yPos, w, h, pic1, pic2, s);
		special = sp;
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
	
	//returns the bottom image
	public Image getPicture1() {
		return picture1;
	}
	
	//returns the top image
	public Image getPicture2() {
		return picture2;
	}
	
	//returns the message
	public String[] getMessage() {
		return message;
	}
	
	//returns if the instructions are open or not
	public boolean getOpened() {
		return opened;
	}
	
	//returns if the player is standing on the instructions or not
	public boolean getStand() {
		return standingOn;
	}
	
	//opens the instructions
	public void open() {
		opened = true;
	}
	
	//closes the instructions
	public void close() {
		opened = false;
	}
	
	//stands on the instructions
	public void standOn() {
		standingOn = true;
	}
	
	//walks away from the instructions
	public void standOff() {
		standingOn = false;
	}
	
	//draws the instructions
	public void paint(Graphics g) {
		g.drawImage(picture1,
				x,
				y,
				x + width,
				y + height,
				0, 0, 600, 600, null);
		if (bobDir == 0) {
			bobDistance += 0.5;
			if (bobDistance >= 7) {
				bobDistance=7;
				bobDir = 1;
			}
		} else {
			bobDistance -= 0.5;
			if (bobDistance <= 0) {
				bobDistance=0;
				bobDir = 0;
			}
		}
		if(opened == true) {
			g.setColor(c1);
			g.fillRect(180, 100, 280, 100);
		
			g.setColor(c2);
			g.setFont(f1);
			int lb = 0;
			String[] lineBreaks = new String[50];
			
			if ((Game.p.getInvWJ() || Game.p.getGotWJ()) && special == 1) {
				index = 1;
				message[1] = "You can now wall jump!\n"
						   + "Go back into the exit to\n"
						   + "the left and keep going\n"
						   + "left after that!\n";
				special = 0;
			} else if ((Game.p.getInvWJ() || Game.p.getGotWJ()) && special == 2) {
				index = 1;
				message[1] = "Use your new abilities to\n"
						   + "wall jump and wall slide\n"
						   + "your way through these\n"
						   + "obstacles!\n";
				special = 0;
			}
			
			String copy = message[curMessage];
			int last = 0;
			for(int i = 0; i < copy.length(); i++) {
				if(copy.charAt(i) == '\n') {
					String s = copy.substring(last, i);
					lb++;
					lineBreaks[lb] = s;
					last = i+1;
				}
			}
			for(int i = 1; i <= Math.min(lb, index-1); i++) {
				g.drawString(lineBreaks[i], 185, 100 + 15*i);
			}
			if(index <= lb) {
				if (Game.p.getPPressed()) {
					charsTyped++;
				} else if (Game.time % 3 == 1 || Game.time % 3 == 2) {
					charsTyped++;
				}
				
				if(charsTyped <= lineBreaks[index].length()) {
					g.drawString(lineBreaks[index].substring(0, charsTyped), 185, 100 + 15*index);
				} else {
					charsTyped = 0;
					index++;
				}
			} else {
			  g.setFont(f2);
			  g.drawString("(Press P)", 400, 195);
			  if(Game.p.getPPressed() == true) {
				  index = 1;
				  charsTyped = 0;
				  curMessage++;
				  if(curMessage >= message.length) {
					  close();
					  curMessage = 1;
				  }
			  }
			}
		} else if(standingOn == true) {
			g.drawImage(picture2,
					x,
					y - height + (int)bobDistance,
					x + width,
					y + (int)bobDistance,
					0, 0, 600, 600, null);
		}
	}
}
