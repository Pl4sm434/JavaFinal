//Production Release 
//Date: 5/23/22
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.JPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

public class Game extends JPanel implements ActionListener {
	//Statics
	private static final long serialVersionUID = 1L;
	public static int width = 650, height = 480;
	public static double gravity = 1.0;
	public static int level = 1;
	public static int cpCount = 22;
	public static Level levels[] = new Level [50];
	public static CheckPoint checkpoints[] = new CheckPoint [23];
	public static Player p;
	public static int time = 0;
	public static int levelCount = 20;
	//Images
	public final Image DASH = (new ImageIcon(getClass().getResource("Dash-min.png"))).getImage();
	public final Image E = (new ImageIcon(getClass().getResource("E.png"))).getImage();
	public final Image FENCEPOST = (new ImageIcon(getClass().getResource("Fencepost.png"))).getImage();
	public final Image GRAVITYIMAGE = (new ImageIcon(getClass().getResource("Gravity.png"))).getImage();
	public final Image WALLJUMP = (new ImageIcon(getClass().getResource("WallJump.png"))).getImage();
	public final Image HEALTHKIT = (new ImageIcon(getClass().getResource("HealthKit.png"))).getImage();
	public final Image CHECKPOINT = (new ImageIcon(getClass().getResource("Checkpoint.png"))).getImage();
	public final Image CHECKPOINTY = (new ImageIcon(getClass().getResource("CheckpointY.png"))).getImage();
	public final Image[] BACKGROUNDS = {(new ImageIcon(getClass().getResource("bg2.jpg"))).getImage(),
										(new ImageIcon(getClass().getResource("bg1.jpg"))).getImage(),
										(new ImageIcon(getClass().getResource("bg3.png"))).getImage(),
									   };
	public BufferedImage bufferedBackground;

	private boolean titleScreen = true;
	private int currentBackground = 0;
	private Color whiteRect = new Color(255, 255, 255, 180);
	private Font f = new Font("Monospaced", Font.BOLD, 20);
	private double finalTime = -1;


	//updates the game
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (!titleScreen && level != 0) {
			levels[level].action();
			if (level < levelCount && levels[level+1].getShooterCount() > 0) {
				levels[level+1].action();
			}
			p.update();
			time++;
			repaint();
		} else if (level == 0) {
			time++;
			repaint();
		}
	}

	//combines two colors and returns a new color
	private Color mergeColors(Color a, Color b, float bPercent) {
		float aPercent = 1-bPercent;
		return new Color((aPercent * a.getRed()   + bPercent * b.getRed())   / 255,
						 (aPercent * a.getGreen() + bPercent * b.getGreen()) / 255,
						 (aPercent * a.getBlue()  + bPercent * b.getBlue())  / 255);
	}

	//Draws everything
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (currentBackground != BACKGROUNDS.length) {
			g.drawImage(bufferedBackground, 0, 0, this);
		}
		g.setColor(whiteRect);
		g.fillRect(0, 0, width, height);
		if (!titleScreen && level != 0) {
			//Level
			levels[level].paint(g);

			//Checkpoints
			for(int i = 1; i <= cpCount; i++) {
				if(checkpoints[i].getLevel() == level) {
					g.drawImage(checkpoints[i].getPicture(), checkpoints[i].getX(), checkpoints[i].getY(), null);
				}
			}

	 		//Player and health bar
			g.setFont(f);
			double hPercent = p.getHealth()/40.0*100;
			Color c;
			if (hPercent >= 80.0) { //dark green
	 			c = new Color(0, (int)((80-hPercent)*155.0/31)+255, 0);
	 		} else if (hPercent >= 60.0) { //green
	 			c = new Color((int)((100-(hPercent+20))*255.0/20), 255, 0);
	 		} else if (hPercent >= 40.0) { //yellow
	 			c = new Color(255, (int)(2*(hPercent-40)+215), 0);
	 		} else if (hPercent >= 20.0) { //orange
	 			c = new Color(255, (int)((215.0/30)*(hPercent-10)), 0);
	 		} else { //red
	 			c = new Color((int)((116.0/20)*hPercent)+139, 0, 0);
	 		}
			if (!p.getHasDash() && p.getGotDash()) {
				c = mergeColors(Color.WHITE, c, 0.6f);
			}

	 		int invincible = p.getInvincible();

	 		if (invincible != 0) {
	 			g.setColor(Color.GRAY);
	 			g.drawString((int) invincible + "", 410, 25);
	 		} else {
	 			g.setColor(c);
	 			g.drawString((int) p.getHealth() + "", 410, 25);
	 		}

	 		if (invincible % 7 == 0) {
	 			g.setColor(Color.GRAY);
		 		g.fillRect(440, 15, 120, 10);
		 		g.setColor(c);
	 			g.fillRect(440, 15, 3*(int) p.getHealth(), 10);
		 		g.fillRect((int) p.getX(), (int) p.getY(), p.getW(), (int) p.getHealth());
	 		}

	 		//Inventory
	 		p.inv.paint(g);

	 		//Things to draw after player
	 		levels[level].paintAfterPlayer(g);
		} else if (level == 0) { //game beaten!
			if (finalTime < 0) {
				finalTime = time*30.0/1000/60;
				time = 0;
			}
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			g.setFont(f);
			g.setColor(Color.WHITE);
			if (time > 40) {
				g.drawString("The Health Platformer", 100, 100);
			}
			if (time > 60) {
				g.drawString("by Team Noobs", 100, 140);
			}
			if (time > 100) {
				g.drawString("Final time: " + (int)finalTime + " minutes and "
						+ (int)((finalTime - (int)finalTime) * 60) + " seconds.", 100, 220);
			}
			if (time > 125) {
				g.drawString("Total deaths: " + (p.getDeaths() + p.getResets())
						+ " (" + p.getDeaths() + " deaths, " +  p.getResets() + " resets)", 100, 260);
			}
			if (time > 160) {
				g.drawString("Thanks for playing!", 100, 340);
			}
		}
	}

	//resets items and makes player return to the last checkpoint
	public static void reset(int lastCP) {
		for(int i = 1; i <= levelCount; i++) {
			for(int j = 1; j <= levels[i].getItemCount(); j++) {
				//Item item = levels[i].getItems()[j];
				int type = levels[i].getItems()[j].getType();
				if(type == 0) {
					((Item)(levels[i].getItems()[j])).reset();
				}
			}
		}
		if(lastCP == 0) {
			Game.level = 1;
			p.setX(Game.levels[Game.level].getX());
			p.setY(Game.levels[Game.level].getY());
		} else {
			Game.level = checkpoints[lastCP].getLevel();
			p.setX(checkpoints[lastCP].getX());
			p.setY(checkpoints[lastCP].getY());
		}
	}

	private void createLevels() {
		//level 0 is actually the ending screen
		levels[0] = new Level(0, 0, 0, 650, 0, 480);

		levels[1] = new Level(170, 100, 120, 520, 40, 400);
		levels[1].addPlatform(120, 40, 400, 40); //top
		levels[1].addPlatform(120, 40, 40, 360); //left
		levels[1].addPlatform(120, 360, 400, 40); //bottom
		levels[1].addExit(1, 40, 400, -1, 2); //right exit
		String[] s1 = new String[5]; //instructions for level 1
		s1[1] = "Use the left and right arrow\n"
			 + "keys to move, and the up arrow\n"
			 + "key to jump. You can also use\n"
			 + "WASD.\n";
		s1[2] = "The height of your jump will\n"
			 +	"be determined by your weight.\n";
		s1[3] = "Less health means that you\n"
			 + "weigh less.\n";
		s1[4] = "In this game, you are the\n"
			 + "health bar!\n";
		levels[1].addInstruction(185, 310, 50, 50, FENCEPOST, E, s1);

		levels[2] = new Level(170, 100, 120, 520, 40, 400);
		levels[2].addPlatform(120, 40, 150, 40); //top left
		levels[2].addPlatform(370, 40, 150, 40); //top right
		levels[2].addPlatform(120, 360, 400, 40); //bottom
		levels[2].addPlatform(180, 318, 280, 42); //pyramid 1
		levels[2].addPlatform(220, 276, 200, 42); //pyramid 2
		levels[2].addPlatform(260, 234, 120, 42); //pyramid 3
		levels[2].addPlatform(300, 192, 40, 42); //pyramid 4
		levels[2].addExit(3, 40, 400, -1, 1); //left
		levels[2].addExit(1, 40, 400, -1, 3); //right
		levels[2].addExit(0, 270, 370, -110, 4); //up
		String[] s2 = new String[3];
		s2[1] = "Your jump is not high enough!\n"
			  + "Go collect the dash powerup\n"
			  + "from the room to the right.\n";
		s2[2] = "You might need to get lighter\n"
			  + "to make the jump.\n"
			  + "(Try less than 20 health!)\n";
		levels[2].addInstruction(295, 142, 50, 50, FENCEPOST, E, s2);

		levels[3] = new Level(120, 320, 80, 560, 40, 400);
		levels[3].addPlatform(80, 40, 480, 40); //top
		levels[3].addPlatform(220, 320, 40, 40); //lava side left
		levels[3].addPlatform(380, 320, 40, 40); //lava side right
		levels[3].addPlatform(80, 360, 480, 40); //bottom
		levels[3].addPlatform(520, 40, 40, 360); //right
		levels[3].addLava(260, 320, 120, 40);
		levels[3].addPowerUp(310, 200, 30, 30, 0, DASH);
		levels[3].addExit(3, 40, 400, -1, 2);
		//levels[3].addMovingPlatform(300, 280, 50, 20, 100, 5, 0);
		String[] s3 = new String[4];
		s3[1] = "That powerup is the dash\n"
			  + "powerup! Be careful though,\n"
			  + "going in the lava pit will\n"
			  + "decrease your health.\n";
		s3[2] = "The smaller you are, the\n"
			  + "lighter you get, which allows\n"
			  + "you to jump higher!\n";
		s3[3] = "You can press space and a\n"
			  + "direction key to dash after\n"
			  + "getting the powerup.\n"
			  + "Your dash refills when you\n"
			  + "touch the ground.\n";
		levels[3].addInstruction(100, 310, 50, 50, FENCEPOST, E, s3);

		levels[4] = new Level(170, 100, 120, 520, 40, 360);
		levels[4].addPlatform(120, 40, 150, 40); //top left h
		levels[4].addPlatform(370, 40, 150, 40); //top right h
		levels[4].addPlatform(120, 40, 40, 360); //top left v
		levels[4].addPlatform(260, 360, 260, 40); //bottom h
		levels[4].addPlatform(480, 80, 40, 300); //top right v
		levels[4].addPlatform(160, 270, 290, 40); //middle bottom h
		levels[4].addPlatform(190, 180, 290, 40); //middle top h
		levels[4].addLava(420, 330, 30, 30); //bottom
		levels[4].addLava(300, 240, 75, 30); //middle bottom
		levels[4].addItem(310, 110, 30, 30, 0, HEALTHKIT);
		checkpoints[1] = new CheckPoint(310, 310, 50, 50, 4, CHECKPOINT, CHECKPOINTY);
		levels[4].addExit(2, 160, 260, 110, 2);
		levels[4].addExit(0, 270, 370, -110, 5);

		levels[5] = new Level(170, 100, 120, 520, 40, 360);
		levels[5].addPlatform(120, 40, 150, 40); //top left h
		levels[5].addPlatform(370, 40, 150, 40); //top right h
		levels[5].addPlatform(120, 40, 40, 360); //top left v
		levels[5].addPlatform(260, 360, 260, 40); //bottom h
		levels[5].addPlatform(480, 40, 40, 360); //top right v
		levels[5].addPlatform(360, 40, 40, 215); //top right middle v
		levels[5].addPlatform(160, 270, 290, 40); //middle bottom h
		levels[5].addMovingPlatform(240, 150, 50, 20, 180, 340, 2, 0);
		levels[5].addLava(310, 340, 80, 20); //bottom
		levels[5].addLava(310, 340, 80, 20); //bottom
		levels[5].addLava(420, 310, 30, 20); //middle top
		levels[5].addExit(2, 160, 260, 110, 4);
		levels[5].addExit(0, 270, 370, -190, 7);

		//numbering is in order of right to left
		levels[6] = new Level(120, 320, 0, 640, 0, 400);
		String[] s6 = new String[2];
		s6[1] = "You can't get up there yet!\n"
			  + "Come back here when you have\n"
			  + "the appropriate powerup.\n"
			  + "Try heading to the right.\n";
		levels[6].addInstruction(550, 350, 50, 50, FENCEPOST, E, s6, 2);
		levels[6].addPlatform(0, 0, 650, 40); //top
		levels[6].addPlatform(0, 400, 640, 40); //bottom
		levels[6].addPlatform(600, 40, 40, 270); //right
		levels[6].addPlatform(0, 130, 40, 270); //left
		levels[6].addPlatform(500, 90, 40, 310); //wall 1
		levels[6].addLava(420, 90, 80, 30); //lava 1
		levels[6].addPlatform(350, 40, 40, 310); //wall 2
		levels[6].addLava(390, 230, 80, 30); //lava 2
		levels[6].addLava(420, 370, 80, 30); //lava 3
		levels[6].addLava(240, 40, 40, 50); //lava 4
		levels[6].addMovingPlatform(240, 90, 40, 120, 90, 300, 5, 1); //moving wall 1
		levels[6].addPlatform(240, 200, 40, 200); //wall 3
		levels[6].addPlatform(120, 40, 40, 290); //wall 4
		levels[6].addLava(120, 380, 40, 20); //lava 5 bottom
		levels[6].addLava(120, 330, 40, 20); //lava 5 top
		levels[6].addExit(1, 40, 400, -1, 7);
		levels[6].addExit(3, 40, 400, -1, 11);
		checkpoints[4] = new CheckPoint(505, 40, 50, 50, 6, CHECKPOINT, CHECKPOINTY);

		levels[7] = new Level(120, 320, 0, 640, 0, 400);
		levels[7].addPlatform(0, 0, 650, 40); //top
		levels[7].addPlatform(170, 400, 470, 40);//bottom
		levels[7].addPlatform(0, 400, 80, 40);//bottom left
		levels[7].addPlatform(600, 130, 40, 270);//right
		levels[7].addPlatform(0, 40, 40, 270);//left
		levels[7].addPlatform(210, 360, 40, 40); //lava divider
		levels[7].addLava(250, 360, 350, 40);//bottom lava
		levels[7].addMovingLava(210, 140, 40, 40, 40, 360, 5, 1);
		levels[7].addMovingPlatform(330, 180, 40, 20, 100, 320, 2, 1); //left
		levels[7].addMovingLava(400, 60, 20, 40, 40, 360, 5, 1);
		levels[7].addMovingLava(400, 180, 20, 40, 40, 360, 5, 1);
		levels[7].addMovingPlatform(450, 100, 60, 20, 100, 320, 2, 1); //right
		levels[7].addMovingLava(545, 60, 20, 40, 40, 360, 5, 1);
		levels[7].addMovingLava(545, 180, 20, 40, 40, 360, 5, 1);
		levels[7].addExit(2, 0, 370, 190, 5); //down
		levels[7].addExit(1, 0, 400, 270, 8); //right
		levels[7].addExit(3, 0, 400, 0, 6); //left

		levels[8] = new Level(120, 320, 0, 640, 0, 400);
		levels[8].addPlatform(0, 40, 40, 270); //left v
		levels[8].addPlatform(0, 400, 640, 40);//bottom
		levels[8].addPlatform(0, 0, 300, 40); //top h left
		levels[8].addPlatform(380, 0, 270, 40); //top h right
		levels[8].addPlatform(600, 130, 40, 270);//right
		levels[8].addHealthGate(300, 320, 80, 80, 30, true);
		levels[8].addMovingPlatform(320, 100, 40, 20, 100, 300, 3, 1);
		levels[8].addMovingPlatform(560, 100, 40, 20, 100, 300, 3, 1);
		levels[8].addHealthGate(600, 40, 40, 90, 30, true);
		checkpoints[2] = new CheckPoint(110, 350, 50, 50, 8, CHECKPOINT, CHECKPOINTY);
		String[] s4 = new String[4];
		s4[1] = "What you see in front of you\n"
			 + "is called a \"Health Gate\".\n"
			 + "These are special obstacles\n"
			 + "that you will encounter.\n";
		s4[2] = "Currently the gate is solid.\n"
			  +	"You need to have more than\n"
			  + "the amount of health shown on\n"
			  + "the gate to pass through.\n";
		s4[3] = "There is a blocked exit at the\n"
			  + "top right of this room. Find\n"
			  + "a way to increase your health\n"
			  + "and return here.\n"
			  + "\n"
			  + "(Go straight up!)\n";
		levels[8].addInstruction(185, 350, 50, 50, FENCEPOST, E, s4, 1);
		levels[8].addExit(3, 0, 400, -270, 7); //left
		levels[8].addExit(1, 0, 400, -1, 10); //right
		levels[8].addExit(0, 0, 650, 0, 9); //up

		levels[9] = new Level(120, 320, 0, 640, 0, 400);
		levels[9].addPlatform(0, 400, 300, 40); //bottom h left
		levels[9].addPlatform(380, 400, 270, 40); //bottom h right
		levels[9].addPlatform(0, 40, 40, 360); //left
		levels[9].addPlatform(610, 40, 40, 360); //right
		levels[9].addPlatform(380, 40, 40, 280); //middle right
		levels[9].addPlatform(0, 0, 650, 40); //top
		levels[9].addPlatform(260, 280, 200, 40); //middle horizontal
		levels[9].addPlatform(420, 160, 40, 40); //health kit right middle left
		levels[9].addMovingLava(460, 120, 110, 40, 120, 320, 4, 1); //moving lava right
		levels[9].addPlatform(570, 120, 40, 40); //health kit right middle right
		levels[9].addPlatform(570, 240, 40, 40); //health kit right bottom right
		levels[9].addHealthGate(260, 320, 40, 80, 10, true);
		levels[9].addItem(500, 70, 30, 30, 0, HEALTHKIT); //right
		levels[9].addItem(125, 70, 30, 30, 0, HEALTHKIT); //left
		levels[9].addItem(265, 70, 30, 30, 0, HEALTHKIT); //middle
		levels[9].addMovingLava(40, 120, 80, 20, 40, 380, 8, 0); //moving lava left
		levels[9].addMovingLava(40, 260, 80, 20, 40, 260, 4, 0); //moving lava left bottom
		levels[9].addMovingPlatform(100, 180, 80, 20, 61, 359, 3, 0); // moving lava top
		levels[9].addExit(2, 0, 650, 0, 8); //down

		levels[10] = new Level(120, 320, 0, 640, 0, 400);
		levels[10].addPlatform(0, 0, 650, 40); //top h
		levels[10].addPlatform(0, 400, 650, 40); //bottom h
		levels[10].addPlatform(0, 130, 190, 40);//middle left h 1
		levels[10].addPlatform(110, 210, 190, 40);//middle left h 2
		levels[10].addPlatform(0, 290, 190, 40);//middle left h 3
		levels[10].addMovingLava(190, 170, 40, 40, 40, 300, 5, 0);//moving lava 1
		levels[10].addMovingLava(140, 170, 40, 40, 40, 300, 5, 0);//moving lava 2
		levels[10].addMovingLava(190, 250, 40, 40, 110, 300, 10, 0);//moving lava 3
		levels[10].addPlatform(0, 130, 40, 200); //left v
		levels[10].addPlatform(600, 130, 40, 200); //right v
		levels[10].addPlatform(300, 40, 40, 360);//middle v
		levels[10].addHealthGate(0, 40, 40, 90, 30, true);
		checkpoints[3] = new CheckPoint(50, 80, 50, 50, 10, CHECKPOINT, CHECKPOINTY);
		//right side
		levels[10].addMovingPlatform(400, 260, 80, 20, 380, 560, 2, 0); //moving platform 1
		levels[10].addMovingPlatform(500, 160, 80, 20, 380, 560, 2, 0); //moving platform 2
		levels[10].addMovingLava(460, 180, 160, 20, 340, 600, 2, 0); //moving lava right side 1
		levels[10].addMovingLava(360, 280, 160, 20, 340, 600, 2, 0); //moving lava right side 2
		levels[10].addPowerUp(460, 100, 30, 30, 2, WALLJUMP);
		String[] s5 = new String[3];
		s5[1] = "That powerup allows you to\n"
			  + "wall jump! You can switch\n"
			  + "to different powerups using\n"
			  + "the number keys 1, 2, and 3.\n"
			  + "Currently, you only have the\n"
			  + "dash powerup.\n";
		s5[2] = "To use this powerup, hold a\n"
			  + "direction key when sliding\n"
			  + "down a wall to \"stick\". Then,\n"
			  + "you can press the up arrow\n"
			  + "key to jump off walls.\n";
		levels[10].addInstruction(450, 350, 50, 50, FENCEPOST, E, s5);
		levels[10].addExit(3, 0, 200, -1, 8); //left
		levels[10].addExit(1, 0, 200, 270, 8); //right
		levels[10].addExit(3, 200, 400, -1, 10); //left same level
		levels[10].addExit(1, 200, 400, -1, 10); //right same level

		levels[11] = new Level(120, 320, 0, 640, 0, 400);
		levels[11].addPlatform(0, 0, 650, 40); //top
		levels[11].addPlatform(0, 400, 640, 40); //bottom
		levels[11].addPlatform(80, 130, 560, 40); //middle h
		levels[11].addPlatform(600, 130, 40, 180); //right
		levels[11].addPlatform(0, 40, 40, 360); //left
		levels[11].addMovingLava(460, 40, 80, 25, 350, 560, 4, 0); //moving lava top 1
		levels[11].addMovingLava(360, 105, 80, 25, 350, 560, 4, 0); //moving lava top 2
		levels[11].addMovingLava(260, 70, 30, 20, 40, 130, 4, 1); //moving lava top 3 v
		levels[11].addHealthGate(40, 130, 40, 40, 9, true); //health gate top
		levels[11].addItem(260, 70, 30, 30, 0, HEALTHKIT); //top
		levels[11].addPlatform(80, 130, 40, 220); //left wall 1
		levels[11].addLava(60, 200, 20, 40); //left wall lava 1
		levels[11].addLava(40, 310, 20, 40); //left wall lava 2
		levels[11].addHealthGate(80, 350, 40, 50, 9, true); //health gate bottom
		levels[11].addPlatform(200, 220, 40, 180); //left wall 2
		levels[11].addLava(160, 210, 40, 190); //left wall 2 lava
		levels[11].addPlatform(280, 130, 40, 220); //left wall 3
		levels[11].addPlatform(360, 220, 40, 180); //left wall 4
		levels[11].addLava(240, 385, 120, 15); //lava floor
		levels[11].addItem(485, 270, 30, 30, 0, HEALTHKIT); //bottom
		levels[11].addExit(1, 40, 200, -1, 6);
		levels[11].addExit(1, 200, 400, -1, 12);
		checkpoints[5] = new CheckPoint(115, 350, 50, 50, 11, CHECKPOINT, CHECKPOINTY);

		levels[12] = new Level(20, 320, 0, 640, 0, 400);
		levels[12].addPlatform(0, 0, 650, 40); //top
		levels[12].addPlatform(0, 400, 80, 40); //bottom
		levels[12].addPlatform(0, 440, 640, 40); //bottom off-screen
		levels[12].addLava(80, 400, 640, 40); //lava floor
		levels[12].addPlatform(0, 40, 40, 270); //left
		levels[12].addPlatform(150, 200, 40, 40); //platform 1
		//levels[12].addPlatform(300, 40, 20, 200); //wall 1
		levels[12].addMovingPlatform(300, 240, 20, 100, 40, 400, 5, 1); //moving wall
		levels[12].addMovingLava(300, 140, 20, 100, -60, 300, 5, 1); //moving lava
		levels[12].addPlatform(340, 90, 20, 310); //wall 2
		levels[12].addPlatform(410, 40, 20, 20);
		levels[12].addPlatform(400, 200, 40, 40); //platform 2
		levels[12].addMovingLava(400, 200, 40, 40, 40, 360, 4, 1); //moving lava on platform 2
		levels[12].addPlatform(480, 90, 20, 310); //wall 3
		levels[12].addPlatform(520, 40, 20, 200); //wall 4
		levels[12].addPlatform(560, 300, 80, 40); //platform 2
		levels[12].addPlatform(620, 40, 20, 200); //wall 5
		levels[12].addItem(565, 100, 30, 30, 0, HEALTHKIT); //bottom
		levels[12].addExit(3, 200, 400, -1, 11);
		levels[12].addExit(1, 0, 300, -21, 13);
		checkpoints[6] = new CheckPoint(20, 350, 50, 50, 12, CHECKPOINT, CHECKPOINTY);

		levels[13] = new Level(70, 150, 0, 640, 0, 400);
		levels[13].addPlatform(0, 280, 150, 20); //start floor
		levels[13].addPlatform(0, -100, 60, 320); //left
		levels[13].addPlatform(130, 30, 20, 250); //wj wall right 1
		levels[13].addPlatform(220, -100, 20, 455); //divisor wall middle l
		levels[13].addPlatform(90, 355, 150, 20); //middle floor left
		levels[13].addPlatform(0, 300, 20, 160); //left bottom
		levels[13].addPlatform(0, 425, 645, 20); //floor
		levels[13].addPlatform(0, -100, 645, 20); //ceiling
		levels[13].addMovingPlatform(260, 410, 40, 20, 100, 410, 5, 1); //elevator
		levels[13].addPlatform(320, 50, 20, 375); //divisor wall middle 2
		levels[13].addPlatform(320, 50, 250, 20); // 1
		levels[13].addPlatform(395, 145, 250, 20); // 2
		levels[13].addPlatform(320, 230, 250, 20); // 3
		levels[13].addPlatform(395, 335, 250, 20); // 4
		levels[13].addPlatform(625, -100, 20, 506); //right
		levels[13].addItem(100, 190, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(60, 110, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(100, 30, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(190, 190, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(150, 110, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(190, 30, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(90, 325, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(120, 325, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(240, 330, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(290, 250, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(240, 170, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(290, 90, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(425, 115, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(455, 115, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(485, 115, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(450, 200, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(480, 200, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(510, 200, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(455, 305, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(485, 305, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(455, 250, 30, 30, 0, HEALTHKIT);
		levels[13].addItem(485, 250, 30, 30, 0, HEALTHKIT);

		String[] s7 = new String[2];
		s7[1] = "Don't touch the health kits!\n"
			  + "If you need to restart press R.\n";
		levels[13].addInstruction(50, 230, 50, 50, FENCEPOST, E, s7);
		levels[13].addExit(3, 230, 300, 21, 12);
		levels[13].addExit(1, 300, 450, -1, 14);
		checkpoints[7] = new CheckPoint(10, 230, 50, 50, 13, CHECKPOINT, CHECKPOINTY);

		levels[14] = new Level(70, 350, 0, 640, 0, 425);
		levels[14].addPlatform(0, 425, 645, 20); //floor
		levels[14].addPlatform(20, 0, 110, 20); //small ceiling left
		levels[14].addPlatform(200, 0, 420, 20); //ceiling
		levels[14].addPlatform(0, 0, 20, 406); //left
		levels[14].addPlatform(620, 0, 20, 430); //right
		levels[14].addLava(20, 20, 20, 386); //left lava
		levels[14].addLava(600, 20, 20, 405); //right lava
		levels[14].addLava(40, 220, 130, 20); //lava divider left
		levels[14].addLava(470, 220, 130, 20); //lava divider right
		levels[14].addMovingPlatform(240, 220, 150, 20, 170, 470, 2, 0);
		levels[14].addShooter(600, 280, 20, 20, 5, 30, 650, 15, 30, false, false, false, true);
		levels[14].addShooter(0, 200, 20, 20, 5, 30, 650, 15, 30, false, false, true, false);
		levels[14].addShooter(600, 120, 20, 20, 5, 30, 650, 15, 30, false, false, false, true);
		levels[14].addShooter(0, 40, 20, 20, 5, 30, 650, 15, 30, false, false, true, false);
		String[] s8 = new String[4];
		s8[1] = "Congratulations on making it\n"
			  + "this far! Go collect the\n"
			  + "antigravity powerup, your\n"
			  + "final powerup on this journey!\n";
		s8[2] = "To use this powerup, press\n"
			  + "space to toggle the gravity.\n"
			  + "This allows you to walk on\n"
			  + "the ceiling!\n";
		s8[3] = "Watch out for the red bullets.\n"
			  + "They will take off 10 hp when\n"
			  + "you hit them. However, you\n"
			  + "also get some invincibility\n"
			  + "time after, so you don't get\n"
			  + "constantly hit.\n";
		checkpoints[8] = new CheckPoint(250, 375, 50, 50, 14, CHECKPOINT, CHECKPOINTY);
		levels[14].addInstruction(350, 375, 50, 50, FENCEPOST, E, s8);
		levels[14].addExit(3, 300, 450, -1, 13);
		levels[14].addPowerUp(310, 330, 30, 30, 1, GRAVITYIMAGE);
		levels[14].addExit(0, 0, 450, -1, 15);
		
		levels[15] = new Level(70, 350, 0, 640, 0, 425);
		levels[15].addPlatform(0, 425, 130, 20); //small floor left
		levels[15].addPlatform(200, 425, 445, 20); //floor
		levels[15].addLava(240, 405, 350, 20); //floor lava
		levels[15].addPlatform(0, 0, 645, 20); //ceiling
		levels[15].addLava(20, 20, 570, 20); //ceiling lava
		levels[15].addPlatform(0, 0, 20, 430); //left
		//levels[15].addPlatform(620, 0, 20, 430); //right
		levels[15].addLava(240, 320, 40, 105); //lava 1
		levels[15].addLava(240, 20, 40, 205); //lava 1 up
		levels[15].addLava(390, 250, 40, 175); //lava 2
		levels[15].addLava(390, 20, 40, 175); //lava 2 up
		levels[15].addLava(550, 180, 40, 245); //lava 3
		levels[15].addLava(550, 20, 40, 105); //lava 3 up
		levels[15].addShooter(480, 420, 20, 20, 5, 30, 650, 15, 30, true, false, false, false);
		levels[15].addExit(2, 0, 450, -1, 14);
		levels[15].addExit(1, 0, 450, -1, 16);
		
		levels[16] = new Level(20, 350, 0, 640, 0, 425);
		levels[16].addIcePlatform(0, 405, 640, 40); //floor ice
		levels[16].addIcePlatform(0, 0, 600, 40); //ceiling ice
		levels[16].addIcePlatform(600, 0, 40, 345); //right ice
		checkpoints[9] = new CheckPoint(30, 355, 50, 50, 16, CHECKPOINT, CHECKPOINTY);
		String[] s9 = new String[3];
		s9[1] = "Be careful! These are ice\n"
			  + "platforms. Movement on these\n"
			  + "is more slippery than normal\n"
			  + "platforms.\n";
		s9[2] = "Hint: Cancel your ice momentum\n"
			  + "by moving left after changing\n"
			  + "gravity!\n";
		levels[16].addInstruction(90, 355, 50, 50, FENCEPOST, E, s9);
		levels[16].addLava(160, 40, 40, 305); //lava 1
		levels[16].addLava(260, 100, 40, 305); //lava 2
		levels[16].addLava(360, 40, 40, 305); //lava 3
		levels[16].addLava(460, 100, 40, 305); //lava 4
		levels[16].addLava(560, 40, 40, 305); //lava 5
		levels[16].addExit(3, 0, 450, -1, 15);
		levels[16].addExit(1, 0, 450, -1, 17);
		
		levels[17] = new Level(20, 350, 0, 640, 0, 425);
		levels[17].addIcePlatform(0, 405, 640, 40); //floor ice
		levels[17].addIcePlatform(0, 0, 600, 40); //ceiling ice
		levels[17].addIcePlatform(600, 0, 40, 345); //right ice
		levels[17].addIcePlatform(0, 0, 40, 345); //left ice
		levels[17].addIcePlatform(300, 135, 40, 270); //center ice
		levels[17].addIcePlatform(120, 135, 400, 40); //upper floor
		levels[17].addIcePlatform(120, 390, 20, 20); //stopper stub
		levels[17].addHealthGate(40, 135, 80, 40, 10, true); //health gate left
		levels[17].addHealthGate(520, 135, 80, 40, 10, true); //health gate right
		levels[17].addItem(195, 200, 30, 30, 0, HEALTHKIT); //healthkit 1
		levels[17].addShooter(195, 420, 30, 30, 5, 30, 650, 15, 30, true, false, false, false);
		levels[17].addShooter(415, 420, 30, 30, 5, 30, 650, 15, 30, true, false, false, false);
		levels[17].addMovingLava(320, 40, 80, 25, 140, 500, 4, 0); //moving lava top 1
		levels[17].addMovingLava(420, 110, 80, 25, 140, 500, 4, 0); //moving lava top 2
		levels[17].addLava(120, 175, 180, 25); //lava above health kit
		levels[17].addExit(1, 0, 450, -1, 18);
		levels[17].addExit(3, 0, 450, -1, 16);
		checkpoints[10] = new CheckPoint(40, 355, 50, 50, 17, CHECKPOINT, CHECKPOINTY);
		
		levels[18] = new Level(20, 350, 0, 640, 0, 425);
		levels[18].addIcePlatform(0, 405, 640, 40); //floor ice
		levels[18].addIcePlatform(0, 0, 600, 40); //ceiling ice
		levels[18].addLava(40, 40, 560, 20); //ceiling lava
		levels[18].addIcePlatform(600, 0, 40, 345); //right ice
		levels[18].addIcePlatform(0, 0, 40, 345); //left ice
		levels[18].addIcePlatform(100, 363, 440, 42); //ice part of pyramid
		levels[18].addLava(140, 321, 360, 42); //pyramid 2
		levels[18].addLava(180, 279, 280, 42); //pyramid 3
		levels[18].addLava(220, 237, 200, 42); //pyramid 4
		levels[18].addLava(260, 195, 120, 42); //pyramid 5
		levels[18].addLava(300, 153, 40, 42); //pyramid 6
		levels[18].addShooter(0, 280, 20, 20, 5, 30, 650, 12, 24, false, false, true, false);
		levels[18].addShooter(600, 200, 20, 20, 5, 30, 650, 12, 24, false, false, false, true);
		levels[18].addShooter(0, 120, 20, 20, 5, 30, 650, 12, 24, false, false, true, false);
		String[] s10 = new String[2];
		s10[1] = "You are almost at the end of\n"
			   + "your journey! Go to the right\n"
			   + "to face your final challenge...\n"
			   + "Good luck and don't give up!\n";
		levels[18].addInstruction(545, 355, 50, 50, FENCEPOST, E, s10);
		levels[18].addExit(3, 345, 405, -1, 17);
		levels[18].addExit(1, 345, 405, -1, 19);
		checkpoints[11] = new CheckPoint(10, 355, 50, 50, 18, CHECKPOINT, CHECKPOINTY);
		
		levels[19] = new Level(500, 60, 0, 640, 0, 425);
		levels[19].addIcePlatform(0, 405, 640, 40); //floor ice
		levels[19].addIcePlatform(0, 0, 640, 40); //ceiling ice
		levels[19].addIcePlatform(600, 40, 40, 305); //right ice
		levels[19].addIcePlatform(0, 40, 40, 305); //left ice
		levels[19].addIcePlatform(100, 100, 40, 305); //ice wall 1
		levels[19].addIcePlatform(200, 40, 40, 355); //ice wall 2
		levels[19].addIcePlatform(300, 50, 40, 355); //ice wall 3
		levels[19].addItem(240, 305, 30, 30, 0, HEALTHKIT); //health kit 1
		levels[19].addItem(270, 235, 30, 30, 0, HEALTHKIT); //health kit 2
		levels[19].addItem(240, 165, 30, 30, 0, HEALTHKIT); //health kit 3
		levels[19].addItem(270, 95, 30, 30, 0, HEALTHKIT);  //health kit 4
		levels[19].addIcePlatform(400, 40, 40, 355); //ice wall 4
		levels[19].addItem(370, 305, 30, 30, 0, HEALTHKIT); //mirrored health kit 1
		levels[19].addItem(340, 235, 30, 30, 0, HEALTHKIT); //mirrored health kit 2
		levels[19].addItem(370, 165, 30, 30, 0, HEALTHKIT); //mirrored health kit 3
		levels[19].addItem(340, 95, 30, 30, 0, HEALTHKIT);  //mirrored health kit 4
		levels[19].addIcePlatform(500, 100, 40, 305); //ice wall 5
		levels[19].addLava(40, 40, 60, 20); //lava top 1
		levels[19].addShooter(100, 100, 30, 30, 7, 28, 100, 15, 30, false, false, false, true);
		levels[19].addShooter(0, 300, 30, 30, 7, 28, 100, 15, 30, false, false, true, false);
		levels[19].addShooter(500, 100, 30, 30, 7, 28, 100, 15, 30, false, false, true, false);
		levels[19].addShooter(600, 300, 30, 30, 7, 28, 90, 15, 30, false, false, false, true);
		levels[19].addMovingLava(40, 140, 15, 40, 140, 300, 4, 1); //first column up left
		levels[19].addMovingLava(85, 260, 15, 40, 140, 300, 4, 1); //first column up right
		levels[19].addMovingLava(140, 140, 60, 40, 0, 360, 2, 1); //second column top
		levels[19].addMovingLava(140, 340, 60, 40, 100, 460, 2, 1); //second column bottom
		levels[19].addMovingLava(585, 140, 15, 40, 140, 300, 4, 1); //mirrored first column up left
		levels[19].addMovingLava(540, 260, 15, 40, 140, 300, 4, 1); //mirrored first column up right
		levels[19].addMovingLava(440, 320, 60, 40, 0, 360, 2, 1); //mirrored second column top
		levels[19].addMovingLava(440, 320, 60, 40, 100, 460, 2, 1); //mirrored second column bottom
		levels[19].addLava(140, 180, 20, 40); //second column left
		levels[19].addLava(180, 300, 20, 40); //second column right
		levels[19].addLava(480, 180, 20, 40); //mirrored second column left
		levels[19].addLava(440, 300, 20, 40); //mirrored second column right
		levels[19].addExit(3, 345, 405, -1, 18);
		levels[19].addExit(1, 345, 405, -1, 20);
		checkpoints[12] = new CheckPoint(10, 355, 50, 50, 19, CHECKPOINT, CHECKPOINTY);
		
		levels[20] = new Level(20, 350, 0, 640, 0, 425);
		levels[20].addPlatform(0, 405, 640, 40); //floor
		levels[20].addPlatform(0, 0, 640, 40); //ceiling
		levels[20].addPlatform(600, 40, 40, 305); //right
		levels[20].addPlatform(0, 40, 40, 305); //left
		levels[20].addLava(600, 305, 40, 40);
		levels[20].addPowerUp(600, 225, 40, 40, 2, GRAVITYIMAGE);
		levels[20].addPowerUp(600, 145, 40, 40, 1, WALLJUMP);
		levels[20].addPowerUp(600, 65, 40, 40, 0, DASH);
		levels[20].addIcePlatform(600, 0, 40, 40);
		levels[20].addLava(520, 0, 40, 40);
		levels[20].addPowerUp(440, 0, 40, 40, 2, GRAVITYIMAGE);
		levels[20].addPowerUp(360, 0, 40, 40, 1, WALLJUMP);
		levels[20].addPowerUp(280, 0, 40, 40, 0, DASH);
		levels[20].addIcePlatform(200, 0, 40, 40);
		levels[20].addLava(120, 0, 40, 40);
		levels[20].addPowerUp(40, 0, 40, 40, 2, GRAVITYIMAGE);
		levels[20].addPowerUp(0, 65, 40, 40, 1, WALLJUMP);
		levels[20].addPowerUp(0, 145, 40, 40, 0, DASH);
		levels[20].addIcePlatform(0, 225, 40, 40);
		levels[20].addLava(0, 305, 40, 40);
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 4; j++) {
				levels[20].addItem(60 + 80*i, 285 - 60*j, 30, 30, 0, HEALTHKIT);
			}
		}
		for(int j = 0; j < 3; j++) {
			levels[20].addMovingPlatform(80, 260 - 60*j, 40, 20, 80, 560, 5, 0);
		}
		checkpoints[13] = new CheckPoint(50, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		checkpoints[14] = new CheckPoint(100, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		checkpoints[15] = new CheckPoint(150, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		checkpoints[16] = new CheckPoint(200, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		checkpoints[17] = new CheckPoint(250, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		String[] s11 = new String[2];
		s11[1] = "Congratulations! You have\n"
			   + "completed your journey!\n"
			   + "Go to the right to finish\n"
			   + "the game and see your stats!\n";
		levels[20].addInstruction(300, 355, 50, 50, FENCEPOST, E, s11);
		checkpoints[18] = new CheckPoint(350, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		checkpoints[19] = new CheckPoint(400, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		checkpoints[20] = new CheckPoint(450, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		checkpoints[21] = new CheckPoint(500, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		checkpoints[22] = new CheckPoint(550, 355, 50, 50, 20, CHECKPOINT, CHECKPOINTY);
		levels[20].addExit(1, 0, 405, -1, 0);
		levels[20].addExit(3, 345, 405, -1, 19);

		
		p = new Player(levels[level].getX(), levels[level].getY());
		//p.inv.addItem("Dash", DASH);
		//p.inv.addItem("Wall Jump", WALLJUMP);
		//p.inv.addItem("Gravity", GRAVITYIMAGE);
		//p.inv.switchItem(1);
	}
	
	public Game() {
		JFrame w = new JFrame("Game");
		w.setSize(width + 6, height - 1);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createLevels();

		bufferedBackground = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedBackground.getGraphics();
		g.drawImage(BACKGROUNDS[0], 0, 0, width, height, null);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;

		JButton button = new JButton("<html><div style='border-bottom:1px solid black;'>PLAY</div></html>");
		button.setBackground(Color.WHITE);
		button.setPreferredSize(new Dimension(150, 50));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEtchedBorder(0));

		JButton bgChangeBtn = new JButton("<html><div>Change Background</div></html>");
		bgChangeBtn.setBackground(Color.WHITE);
		bgChangeBtn.setPreferredSize(new Dimension(150, 50));
		bgChangeBtn.setFocusPainted(false);
		bgChangeBtn.setBorder(BorderFactory.createEtchedBorder(0));

		JLabel title = new JLabel("<html><h1 style='background-color:#708090;padding:50px;border:2px solid black;'>The Health Platformer</h1></html>");
		
		add(title, gbc);
		add(button, gbc);
		add(bgChangeBtn, gbc);
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button.setVisible(false);
				title.setVisible(false);
				bgChangeBtn.setVisible(false);
				titleScreen = false;
				requestFocus();
			}
		});

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent evt) {
				button.setBackground(new Color(0, 155, 0));
			}
			
			@Override
			public void mouseExited(MouseEvent evt) {
				button.setBackground(Color.WHITE);
			}
		});

		bgChangeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentBackground < BACKGROUNDS.length + 1) {
					currentBackground++;
				} else {
					currentBackground = 0;
				}
				Graphics g = bufferedBackground.getGraphics();
				g.clearRect(0, 0, width, height);
				if (currentBackground < BACKGROUNDS.length) {
					g.drawImage(BACKGROUNDS[currentBackground], 0, 0, width, height, null);
				}
				repaint();
			}
		});

		bgChangeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent evt) {
				bgChangeBtn.setBackground(new Color(0, 155, 0));
			}
			
			@Override
			public void mouseExited(MouseEvent evt) {
				bgChangeBtn.setBackground(Color.WHITE);
			}
		});

		Container c = w.getContentPane();
		c.add(this);
		Timer clock = new Timer(30, this);
		clock.start();
		w.setResizable(false);
		w.setVisible(true);
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				//ignore
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					titleScreen = true;
					button.setVisible(true);
					title.setVisible(true);
					bgChangeBtn.setVisible(true);
					repaint();
				} else {
					p.keyPressed(e);
				}
			}

			public void keyReleased(KeyEvent e) {
				p.keyReleased(e);
			}
		});
	}

	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		Game game = new Game();
	}
}
