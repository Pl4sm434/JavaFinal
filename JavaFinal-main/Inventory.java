//Production Release 
//Date: 5/23/22

import java.awt.*;

public class Inventory {
	private String[] item = new String[1+3];
	private Image image[] = new Image[1+3];
	private int cur; //currently selected
	
	public Inventory() {
		item[0] = item[1] = item[2] = item[3] = "";
	}
	
	//adds an item to the inventory
	public boolean addItem(String it, Image pic) {
		if(item[1] == "") {
			item[1] = it;
			image[1] = pic;
			cur = 1;
			return true;
		} else if(item[2] == "") {
			item[2] = it;
			image[2] = pic;
			cur = 2;
			return true;
		} else if(item[3] == "") {
			item[3] = it;
			image[3] = pic;
			cur = 3;
			return true;
		}
		return false;
	}
	
	//draws the inventory
	public void paint(Graphics g) {
		for(int i = 1; i <= 3; i++) {
			g.setColor(Color.GRAY);
			if(cur == i) {
				g.setColor(Color.LIGHT_GRAY);
			}
			g.fillRect(36*i-25, 5, 30, 30);
			if(item[i] != "") {
				g.drawImage(image[i],
						36*i-23,
						7,
						36*i+3,
						33,
						0, 0, 600, 600, null);			
			}
		}
	}
	
	public int getCur() {
		return cur;
	}
	
	public String getItem() {
		return item[cur];
	}
	
	public void switchItem(int newCur) {
		if(item[cur].equals("Dash")) {
			Game.p.setGotDash(false);
		} else if(item[cur].equals("Gravity")) {
			Game.p.setGotGravity(false);
		} else if(item[cur].equals("Wall Jump")) {
			Game.p.setGotWJ(false);
		}
		cur = newCur;
		if(item[cur].equals("Dash")) {
			Game.p.setGotDash(true);
		} else if(item[cur].equals("Gravity")) {
			Game.p.setGotGravity(true);
		} else if(item[cur].equals("Wall Jump")) {
			Game.p.setGotWJ(true);
		}
		
	}
}
