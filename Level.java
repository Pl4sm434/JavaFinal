//Production Release 
//Date: 5/23/22
import java.awt.*;

public class Level {
	private int platCount = 0;
	private int movingPlatCount = 0;
	private int icePlatCount = 0;
	private int lavaCount = 0;
	private int movingLavaCount = 0;
	private int powerCount = 0;
	private int itemCount = 0;
	private int exitCount = 0;
	private int healthGateCount = 0;
	private int instrCount = 0;
	private int shooterCount = 0;
	private int lavaGreen = 0;
	private int lavaGreenDirection = 0;
	private int sX, sY; // spawn coordinates
	private Exit exits[] = new Exit[10];
	private int bL, bR, bD, bU; //border or boundary left, right, down, up
	private Platform platforms[] = new Platform[15];
	private Shooter shooters[] = new Shooter[10];
	private MovingPlatform movingPlatforms[] = new MovingPlatform[5];
	private IcePlatform icePlatforms[] = new IcePlatform[15];
	private Platform lava[] = new Lava[10];
	private HealthGate healthGates[] = new HealthGate[5];
	private MovingPlatform movingLava[] = new MovingLava[10];
	private PowerUp powerups[] = new PowerUp[50];
	private PowerUp items[] = new PowerUp[50];
	private Instruction instr[] = new Instruction[10];
	private Color c1 = new Color(207,55+lavaGreen,16); //the amount of green in the RGB color of lava

	public Level(int x, int y, int L, int R, int D, int U) {
		sX = x;
		sY = y;
		bL = L;
		bR = R;
		bD = D;
		bU = U;
	}
	
	//draws everything before player
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		// Shooters
		for (int i = 1; i <= shooterCount; i++) {
			shooters[i].paint(g);
		}
		// Platforms
		g.setColor(Color.BLACK);
		for (int i = 1; i <= platCount; i++) {
			platforms[i].paint(g);
		}
		// MovingPlatforms
		for (int i = 1; i <= movingPlatCount; i++) {
			movingPlatforms[i].paint(g);
		}
		// IcePlatforms
		g.setColor(new Color(173,216,230));
		for (int i =1; i <= icePlatCount; i++) {
			icePlatforms[i].paint(g);
		}
		// Lava		
		if (lavaGreenDirection == 0) {
			if (lavaGreen < 45) {
				lavaGreen+=2;
			} else {
				lavaGreenDirection = 1;
			}
		} else {
			if (lavaGreen > 0) {
				lavaGreen-=2;
			} else {
				lavaGreenDirection = 0;
			}
		}
		g.setColor(c1);
		for (int i = 1; i <= lavaCount; i++) {
			lava[i].paint(g);
		}
		// MovingLava
		for (int i = 1; i <= movingLavaCount; i++) {
			movingLava[i].paint(g);
		}
		
		// PowerUps
		for (int i = 1; i <= powerCount; i++) {
			if (powerups[i].getType() == 0 && !Game.p.getInvDash()) { // dash
				g.drawImage(powerups[i].getPicture(),
							powerups[i].getX(),
							powerups[i].getY(),
							powerups[i].getX() + powerups[i].getWidth(),
							powerups[i].getY() + powerups[i].getHeight(),
							0, 0, 600, 600, null);
			} else if (powerups[i].getType() == 1 && !Game.p.getInvGravity()) {
				g.drawImage(powerups[i].getPicture(),
							powerups[i].getX(),
							powerups[i].getY(),
							powerups[i].getX() + powerups[i].getWidth(),
							powerups[i].getY() + powerups[i].getHeight(),
							0, 0, 600, 600, null);
			} else if (powerups[i].getType() == 2 && !Game.p.getInvWJ()) {
				g.drawImage(powerups[i].getPicture(),
						powerups[i].getX(),
						powerups[i].getY(),
						powerups[i].getX() + powerups[i].getWidth(),
						powerups[i].getY() + powerups[i].getHeight(),
						0, 0, 600, 600, null);
			}
		}
		// Items
		for (int i = 1; i <= itemCount; i++) {
			if (((Item) items[i]).getCollected() == false) {
				g.drawImage(((Item) items[i]).getBuffImage(), items[i].getX(), items[i].getY(), null);
			}
		}
		// Instructions
		for(int i = 1; i <= instrCount; i++) {
			instr[i].paint(g);
		}
	}
	
	//draws things after the player, so that the player appears behind them
	public void paintAfterPlayer(Graphics g) {
		for (int i = 1; i <= healthGateCount; i++) {
			healthGates[i].paint(g);
		}
	}
	
	//Updates moving parts of the level
	public void action() {
		for (int i = 1; i <= getMovingPlatCount(); i++) {
			((MovingPlatform) movingPlatforms[i]).action();
		}
		for (int i = 1; i <= getMovingLavaCount(); i++) {
			((MovingLava) movingLava[i]).action();
		}
		for (int i = 1; i <= getShooterCount(); i++) {
			((Shooter) shooters[i]).action();
		}
	}
	
	//returns platforms
	public Platform[] getPlatforms() {
		return platforms;
	}
	
	//returns moving platforms
	public MovingPlatform[] getMovingPlatforms() {
		return movingPlatforms;
	}
	
	// returns ice platforms
	public IcePlatform[] getIcePlatforms() {
		return icePlatforms;
	}
	
	//returns lava
	public Platform[] getLava() {
		return lava;
	}
	
	//returns shooters
	public Shooter[] getShooters() {
		return shooters;
	}
	
	//returns moving lava
	public MovingPlatform[] getMovingLava() {
		return movingLava;
	}
	
	//returns health gates
	public HealthGate[] getHealthGate() {
		return healthGates;
	}
	
	//returns powerups
	public PowerUp[] getPowerUps() {
		return powerups;
	}
	
	//returns items
	public PowerUp[] getItems() {
		return items;
	}
	
	//returns instructions
	public Instruction[] getInstructions() {
		return instr;
	}
	
	//returns the amount of exits
	public int getExitCount() {
		return exitCount;
	}
	
	//returns exits
	public Exit[] getExits() {
		return exits;
	}
	
	//returns the number of platforms
	public int getPlatCount() {
		return platCount;
	}
	
	//returns the number of shooters
	public int getShooterCount() {
		return shooterCount;
	}
	
	//returns the number of moving platforms
	public int getMovingPlatCount() {
		return movingPlatCount;
	}
	
	// returns the number of ice platforms
	public int getIcePlatCount() {
		return icePlatCount;
	}
	
	//returns the number of lava
	public int getLavaCount() {
		return lavaCount;
	}
	
	//returns the number of moving lava
	public int getMovingLavaCount() {
		return movingLavaCount;
	}
	
	//returns the number of health gates
	public int getHealthGateCount() {
		return healthGateCount;
	}
	
	//returns the number of powerups
	public int getPowerCount() {
		return powerCount;
	}
	
	//returns the number of items
	public int getItemCount() {
		return itemCount;
	}
	
	//returns the number of instructions
	public int getInstructionCount() {
		return instrCount;
	}
	
	//returns x
	public int getX() {
		return sX;
	}
	
	//returns y
	public int getY() {
		return sY;
	}
	
	//returns bottom border
	public int getBD() {
		return bD;
	}
	
	//returns upper border
	public int getBU() {
		return bU;
	}
	
	//returns right border
	public int getBR() {
		return bR;
	}
	
	//returns left border
	public int getBL() {
		return bL;
	}
	
	//adds a platform to the level
	public void addPlatform(int x, int y, int width, int height) {
		platCount++;
		platforms[platCount] = new Platform(x, y, width, height);
	}
	
	//adds a shooter to the level
	public void addShooter(int x, int y, int width, int height, int speed, int rate, int distance, int pWidth, int pHeight, boolean up, boolean down, boolean right, boolean left) {
		shooterCount++;
		shooters[shooterCount] = new Shooter(x, y, width, height, speed, rate, distance, pWidth, pHeight, up, down, right, left);
	}
	
	//adds a moving platform to the level
	public void addMovingPlatform(int x, int y, int width, int height, int boundLow, int boundHigh, int speed, int type) {
		movingPlatCount++;
		movingPlatforms[movingPlatCount] = new MovingPlatform(x, y, width, height, boundLow, boundHigh, speed, type);
	}
	
	//adds a ice platform to the level
	public void addIcePlatform(int x, int y, int width, int height) {
		icePlatCount++;
		icePlatforms[icePlatCount] = new IcePlatform(x, y, width, height);
	}
	
	//adds lava to the level
	public void addLava(int x, int y, int width, int height) {
		lavaCount++;
		lava[lavaCount] = new Lava(x, y, width, height);
	}
	
	//adds moving lava to the level
	public void addMovingLava(int x, int y, int width, int height, int boundLow, int boundHigh, int speed, int type) {
		movingLavaCount++;
		movingLava[movingLavaCount] = new MovingLava(x, y, width, height, boundLow, boundHigh, speed, type);
		System.out.println();
	}
	
	//adds health gate to the level
	public void addHealthGate(int x, int y, int width, int height, double minHealth, boolean minHealthVisible) {
		healthGateCount++;
		healthGates[healthGateCount] = new HealthGate(x, y, width, height, minHealth, minHealthVisible);
	}
	
	//adds a powerup to the level
	public void addPowerUp(int x, int y, int width, int height, int type, Image pic) {
		powerCount++;
		powerups[powerCount] = new PowerUp(x, y, width, height, type, pic);
	}
	//adds an item to the level
	public void addItem(int x, int y, int width, int height, int type, Image pic) {
		itemCount++;
		items[itemCount] = new Item(x, y, width, height, type, pic);
	}
	
	//adds an exit to the level
	public void addExit(int type, int bL, int bH, int nc, int nextLevel) {
		exitCount++;
		exits[exitCount] = new Exit(type, bL, bH, nc, nextLevel);
	}
	
	//adds an instruction to the level
	public void addInstruction(int sX, int sY, int w, int h, Image pic1, Image pic2, String[] message) {
		instrCount++;
		instr[instrCount] = new Instruction(sX, sY, w, h, pic1, pic2, message);
	}
	
	//adds a special instruction to the level
	public void addInstruction(int sX, int sY, int w, int h, Image pic1, Image pic2, String[] message, int sp) {
		instrCount++;
		instr[instrCount] = new Instruction(sX, sY, w, h, pic1, pic2, message, sp);
	}
}
