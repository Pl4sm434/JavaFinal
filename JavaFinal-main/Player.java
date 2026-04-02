//Production Release 
//Date: 5/23/22
import java.awt.event.*;

import javax.swing.ImageIcon;

public class Player {
	
	//directions used by the Player
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;
	
	private double x, y; //coordinates
	private double newX, newY; //used for collisions
	private double vx, vy; //velocity x and velocity y
	private int dir; //uses up down left right symbolic constants
	private int dashDir; //uses up down +left right symbolic constants
	private boolean onGround = false; //if the player is on ground
	private boolean invGravity = false; //gravity is in inventory
	private boolean gotGravity = false; //gravity powerup selected
	private boolean invDash = false; //dash is in inventory
	private boolean gotDash = false; //dash powerup selected
	private boolean hasDash = false; //has dash available
	private boolean invWJ = false; //wall jump is in inventory
	private boolean gotWJ = false; //wall jump selected
	private boolean hasWJ = false; //has wall jump available
	private boolean justWJed = false; //just wall jumped
	private boolean hasInstrOpen = false; //has instruction open
	private int WJdir = 0; //walljump direction: left = -1, right = 1;
	private double aR = 0; //air resistance from wall jumps
	private double aR2 = 0; //air resistance from ice (slipperiness)
	public Inventory inv = new Inventory(); //the player's inventory
	private int shift = 5; //shift from wall jumps
	private int lastDir = 1; //uses up down left right symbolic constants
	private int gravityPos = 1; //1 = normal gravity, -1 = anti-gravity
	private int dashCooldown = 0; //cooldown for dash powerup
	private int gravityCooldown = 0; //cooldown for gravity powerup
	private int dash = 4; // variable used for keeping track of dashing
	private int timeInLava = 0; //tracks time in lava
	private final double speed = 5; //movement speed
	private final int w = 20; //player's width
	private double health = 40; //health
	private int invincible = 0; //invincibility frames
	private double weight = 1; //the heaviness of the player
	private int lastCheckPoint = 0; //the last checkpoint that the player got 
	private int deathCount = 0; //amount of deaths
	private int resetCount = 0; //amount of resets to last checkpoint
	
	//keypress booleans
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean spacePressed = false;
	private boolean ePressed = false;
	private boolean pPressed = false;
	private boolean rPressed = false;
	private boolean onePressed = false;
	private boolean twoPressed = false;
	private boolean threePressed = false;
	
	public Player(int startX, int startY) {
		x = startX;
		y = startY;
		onGround = false;
		vx = 4.0;
	}
	
	//Handles rectangle collision with moving things
	public void intersectMovingPlatform(double lx, double rx, double yT, double yB, int type, int direction, int speed, double curX, double curY) {
		if((newX <= lx && lx < newX + w) && (newY < yT && yT < newY + health)) {
			if((type == 0 && curY + health <= yT) || (type == 1 && newY > y)) {
				newY = yT - health;
				vy = 0;
				if(gravityPos == 1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else {
				newX = lx - w;
				onGround = false;
			}
		} else if((newX < rx && rx <= newX + w) && (newY < yT && yT < newY + health)) {
			if((type == 0 && curY + health <= yT) || (type == 1 && newY > y)) {
				newY = yT - health;
				vy = 0;
				if(gravityPos == 1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else {
				newX = rx;
				onGround = false;
			}
		} else if((newX <= lx && lx < newX + w) && (newY < yB && yB < newY + health)) {
			if(curY >= yB) {
				if(vy < 0) {
					vy = 0;
				}
				newY = yB;
				if(gravityPos == -1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else {
				newX = lx - w;
			}
			onGround = false;
		} else if((newX < rx && rx <= newX + w) && (newY < yB && yB < newY + health)) {
			if(curY >= yB) {
				if(vy < 0) {
					vy = 0;
				}
				newY = yB;
				if(gravityPos == -1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else {
				newX = rx;
			}
			onGround = false;
		} else if(newX <= lx && lx < newX + w) {
			if(newY >= y) {
				vy = 0;
				newY = yT - health;
				if(gravityPos == 1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else {
				if(vy < 0) {
					vy = 0;
				}
				newY = yB;
				if(gravityPos == -1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			}
		} else if(newX < rx && rx <= newX + w) {
			if(newY < y) {
				if(vy < 0) {
					vy = 0;
				}
				newY = yB;
				if(gravityPos == -1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else if(newY >= y) {
				vy = 0;
				newY = yT - health;
				if(gravityPos == 1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else {
				newX = rx;
				onGround = false;
			}
		} else if(newY < yT && yT < newY + health) {
			if(newY < y) {
				if(vy < 0) {
					vy = 0;
				}
				newY = yB;
				if(gravityPos == -1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else if(newY >= y) {
				vy = 0;
				newY = yT - health;
				if(gravityPos == 1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else {
				newX = rx;
				onGround = false;
			}
		} else if(newY < yB && yB < newY + health) {
			if(vy < 0) {
				vy = 0;
			}
			if(newY >= y && !(type == 1 && direction == DOWN)) {
				newY = yT - health;
				onGround = true;
				if (gotDash) {
					hasDash = true;
				}
			} else {
				newY = yB;
				onGround = false;
			}
		} else if(lx <= newX && newX + w <= rx && yT < newY && newY + health < yB) {
			if(newY < y) {
				if(vy < 0) {
					vy = 0;
				}
				newY = yB;
				if(gravityPos == -1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else if(newY >= y) {
				vy = 0;
				newY = yT - health;
				if(gravityPos == 1) {
					onGround = true;
					if (gotDash)
						hasDash = true;
				} else {
					onGround = false;
				}
			} else if(newX < x) {
			  newX = lx - w;
			  onGround = false;
			} else if(newX >= x) {
			  newY = rx;
			  onGround = false;
			}
		}
	}
	
	//Checks for collision
	private boolean noCollisions(double newX, double newY) {
		Level level = Game.levels[Game.level];
		Platform[] platforms = level.getPlatforms();
		MovingPlatform[] movingPlatforms = level.getMovingPlatforms();
		HealthGate[] healthGates = level.getHealthGate();
		Shooter[] shooters = level.getShooters();
		IcePlatform[] icePlatforms= level.getIcePlatforms();

		for(int i = 1; i <= level.getPlatCount(); i++) {
			double lx = platforms[i].getX();
			double rx = lx + platforms[i].getWidth();
			double yT = platforms[i].getY();
			double yB = yT + platforms[i].getHeight();
			if(lx - w < newX && newX < rx && yT - health < newY && newY < yB) {
				return false;
			}
		}
		for(int i = 1; i <= level.getHealthGateCount(); i++) {
			double lx = healthGates[i].getX();
			double rx = lx + healthGates[i].getWidth();
			double yT = healthGates[i].getY();
			double yB = yT + healthGates[i].getHeight();
			if(lx - w < newX && newX < rx && yT - health < newY && newY < yB && !healthGates[i].isPassed(health)) {
				return false;
			}
		}
		for(int i = 1; i <= level.getShooterCount(); i++) {
			double lx = shooters[i].getX();
			double rx = lx + shooters[i].getWidth();
			double yT = shooters[i].getY();
			double yB = yT + shooters[i].getHeight();
			if(lx - w < newX && newX < rx && yT - health < newY && newY < yB) {
				return false;
			}
		}
		for(int i = 1; i <= level.getIcePlatCount(); i++) {
			double lx = icePlatforms[i].getX();
			double rx = lx + icePlatforms[i].getWidth();
			double yT = icePlatforms[i].getY();
			double yB = yT + icePlatforms[i].getHeight();
			if(lx - w < newX && newX < rx && yT - health < newY && newY < yB) {
				return false;
			}
		}
		for(int i = 1; i <= level.getMovingPlatCount(); i++) {
			int lx = movingPlatforms[i].getX();
			int rx = lx + movingPlatforms[i].getWidth();
			int yT = movingPlatforms[i].getY();
			int yB = yT + movingPlatforms[i].getHeight();
			int type = movingPlatforms[i].getType();
			int d = movingPlatforms[i].getDir();
			int speed = movingPlatforms[i].getSpeed();
			if(type == 0) {
				if(d == 1) {
					lx -= speed;
					rx -= speed;
				} else {
					lx += speed;
					rx += speed;
				}
			} else {
				if(d == 1) {
					yT -= speed;
					yB -= speed;
				} else {
					yT += speed;
					yB += speed;
				}
			}
			if(lx - w < newX && newX < rx && yT - health < newY && newY < yB) {
				return false;
			}
		}
		return true;
	}
	
	//Collision for landing on platforms
	private void landingOnPlatform() {
		Level level = Game.levels[Game.level];
		Platform[] platforms = level.getPlatforms();
		HealthGate[] healthGates = level.getHealthGate();
		Shooter[] shooters = level.getShooters();
		IcePlatform[] icePlatforms= level.getIcePlatforms();
		for(int i = 1; i <= level.getPlatCount(); i++) {
			int lx = platforms[i].getX();
			int rx = lx + platforms[i].getWidth();
			int yT = platforms[i].getY();
			int yB = yT + platforms[i].getHeight();
			if(lx - w < x && x < rx && yT - health < y && y < yB) {
				if(dir == DOWN) {
					if(gravityPos == 1) {
						y = yT - health;
					} else if(gravityPos == -1) {
						y = yB;
					}
					vy = 0;
					onGround = true;
					if(gotDash)
						hasDash = true;
				} else {
					if(dir == UP) {
						if(gravityPos == 1) {
							y = yB;
						} else if(gravityPos == -1) {
							y = yT - health;
						}
						vy = 0;
					} 
					onGround = false;
				}
			} 
		
		}
		for(int i = 1; i <= level.getHealthGateCount(); i++) {
			int lx = healthGates[i].getX();
			int rx = lx + healthGates[i].getWidth();
			int yT = healthGates[i].getY();
			int yB = yT + healthGates[i].getHeight();
			if(lx - w < x && x < rx && yT - health < y && y < yB && !healthGates[i].isPassed(health)) {
				if(dir == DOWN) {
					if(gravityPos == 1) {
						y = yT - health;
					} else if(gravityPos == -1) {
						y = yB;
					}
					vy = 0;
					onGround = true;
					if(gotDash)
						hasDash = true;
				} else {
					if(dir == UP) {
						if(gravityPos == 1) {
							y = yB;
						} else if(gravityPos == -1) {
							y = yT - health;
						}
						vy = 0;
					}
					onGround = false;
				} 
			} 
		}
		for(int i = 1; i <= level.getShooterCount(); i++) {
			int lx = shooters[i].getX();
			int rx = lx + shooters[i].getWidth();
			int yT = shooters[i].getY();
			int yB = yT + shooters[i].getHeight();
			if(lx - w < x && x < rx && yT - health < y && y < yB) {
				if(dir == DOWN) {
					if(gravityPos == 1) {
						y = yT - health;
					} else if(gravityPos == -1) {
						y = yB;
					}
					vy = 0;
					onGround = true;
					if(gotDash)
						hasDash = true;
				} else {
					if(dir == UP) {
						if(gravityPos == 1) {
							y = yB;
						} else if(gravityPos == -1) {
							y = yT - health;
						}
						vy = 0;
					}
					onGround = false;
				} 
			} 
		}
		for(int i = 1; i <= level.getIcePlatCount(); i++) {
			int lx = icePlatforms[i].getX();
			int rx = lx + icePlatforms[i].getWidth();
			int yT = icePlatforms[i].getY();
			int yB = yT + icePlatforms[i].getHeight();
			if(lx - w < x && x < rx && yT - health < y && y < yB) {
				if(dir == DOWN) {
					if(gravityPos == 1) {
						y = yT - health;
					} else if(gravityPos == -1) {
						y = yB;
					}
					vy = 0;
					onGround = true;
					if(gotDash)
						hasDash = true;
				} else {
					if(dir == UP) {
						if(gravityPos == 1) {
							y = yB;
						} else if(gravityPos == -1) {
							y = yT - health;
						}
						vy = 0;
					}
					onGround = false;
				} 
			} 
		}
	}
	
	//Checks if player exits to another room
	private boolean checkExit() {
		for(int i = 1; i <= Game.levels[Game.level].getExitCount(); i++) {
			int bD = Game.levels[Game.level].getBD();
			int bU = Game.levels[Game.level].getBU();
			int bL = Game.levels[Game.level].getBL();
			int bR = Game.levels[Game.level].getBR();
			int type = Game.levels[Game.level].getExits()[i].getType();
			int low = Game.levels[Game.level].getExits()[i].getLow();
			int high = Game.levels[Game.level].getExits()[i].getHigh();
			int nc = Game.levels[Game.level].getExits()[i].getNewCoord();
			int nextLevel = Game.levels[Game.level].getExits()[i].getLevel();
			if(type == 0) {
				if(y < bD && (low <= x && x <= high)) {
					y = Game.levels[nextLevel].getBU()+1;
					if(nc != -1) {
					  x += nc;
					}
					Game.level = nextLevel;
					return true; 
				}
			} else if(type == 1) {
				if(x + w > bR && (low <= y && y <= high)) {
					x = Game.levels[nextLevel].getBL()+1;
					if(nc != -1) {
					  y += nc;
					}
					Game.level = nextLevel;
					return true;
				}
			} else if(type == 2) {
				if(y > bU && (low <= x && x <= high)) {
					y = Game.levels[nextLevel].getBD()-1;
					if(nc != -1) {
					  x += nc;
					}
					Game.level = nextLevel;
					return true; 
				}
			} else if(type == 3) {
				if(x < bL && (low <= y && y <= high)) {
					x = Game.levels[nextLevel].getBR() - 1 - w;
					if(nc != -1) {
					  y += nc;
					}
					Game.level = nextLevel;
					return true;
				}
			}
		}
		return false;
	}
	
	//Checks if player is touching lava
	private boolean isTouchingLava() {
		Level level = Game.levels[Game.level];
		Platform[] lava = level.getLava();
		for(int i = 1; i <= level.getLavaCount(); i++) {
			int lx = lava[i].getX();
			int rx = lx + lava[i].getWidth();
			int by = lava[i].getY();
			int ty = by + lava[i].getHeight();
			if(lx < x + w && x < rx && by < y + health && y < ty) {
				return true;
			}
		}
		MovingPlatform[] movingLava = level.getMovingLava();
		for(int i = 1; i <= level.getMovingLavaCount(); i++) {
			int lx = movingLava[i].getX();
			int rx = lx + movingLava[i].getWidth();
			int by = movingLava[i].getY();
			int ty = by + movingLava[i].getHeight();
			if(lx < x + w && x < rx && by < y + health && y < ty) {
				return true;
			}
		}
		return false;
	}
	
	//Collect powerup that the player currently touches
	private int collectPowerups() {
		Level level = Game.levels[Game.level];
		PowerUp[] powerups = level.getPowerUps();
		int ret = -1;
		for(int i = 1; i <= level.getPowerCount(); i++) {
			int lx = powerups[i].getX();
			int rx = lx + powerups[i].getWidth();
			int yT = powerups[i].getY();
			int yB = yT + powerups[i].getHeight();
			int prevCur = inv.getCur();
			if(lx - w < x && x < rx && yT - health < y && y < yB) {
				if(powerups[i].getType() == 0 && !invDash) {
					if(inv.addItem("Dash", (new ImageIcon(getClass().getResource("Dash-min.png"))).getImage())) {
						gotDash = true;
						invDash = true;
						ret = 0;
					}
				} else if(powerups[i].getType() == 1 && !invGravity) {
					if(inv.addItem("Gravity", (new ImageIcon(getClass().getResource("Gravity.png"))).getImage())) {
						gravityPos = -1;
						invGravity = true;
						ret = 1;
					}
				} else if(powerups[i].getType() == 2 && !invWJ) {
					if(inv.addItem("Wall Jump", (new ImageIcon(getClass().getResource("WallJump.png"))).getImage())) {
						gotWJ = true;
						invWJ = true;
						ret = 2;
					}
				}
			}
			if(prevCur != 0) {
				inv.switchItem(prevCur);
			}
		}
		return ret;
	}
	
	public boolean isOnIce() {
		Level level = Game.levels[Game.level];
		IcePlatform[] icePlatforms = level.getIcePlatforms();
		for(int i = 1; i <= level.getIcePlatCount(); i++) {
			int lx = icePlatforms[i].getX();
			int rx = lx + icePlatforms[i].getWidth();
			int yT = icePlatforms[i].getY();
			int yB = yT + icePlatforms[i].getHeight();
			if(lx - w < x && x < rx && yT - health < y+1*gravityPos && y+1*gravityPos < yB) {
				return true;
			} 
		}
		return false;
	}
	
	//Collect item that the player currently touches
	public void collectItems() {
		Level level = Game.levels[Game.level];
		PowerUp[] items = level.getItems();
		for(int i = 1; i <= level.getItemCount(); i++) {
			int lx = items[i].getX();
			int rx = lx + items[i].getWidth();
			int yT = items[i].getY();
			int yB = yT + items[i].getHeight();
			if(lx - w < x && x < rx && yT - health < y && y < yB) {
				if(items[i].getType() == 0 && health < 40 && ((Item) items[i]).getCollected() == false) {
					((Item) items[i]).collect();
					double startHealth = health;
					health += 10;
					if (health > 40) health = 40;
					for (int j = 0; j < health - startHealth; j++) {
						if (noCollisions(x,y-1)) {
							if (gravityPos == 1) {
								y--;
							}
						} else {
							break;
						}
					}
				}
			}
		}
	}
	
	//opens or closes instructions
	public void openInstructions() {
		Level level = Game.levels[Game.level];
		Instruction[] instr = level.getInstructions();
		hasInstrOpen = false;
		for(int i = 1; i <= level.getInstructionCount(); i++) {
			int lx = instr[i].getX();
			int rx = lx + instr[i].getWidth();
			int yT = instr[i].getY();
			int yB = yT + instr[i].getHeight();
			if(instr[i].getOpened() == true) {
				hasInstrOpen = true;
			}
			if(lx - w < x && x < rx && yT - health < y && y < yB) {
				instr[i].standOn();
				if(ePressed) {
					instr[i].open();
				} 
			} else {
				instr[i].standOff();
			}
		}
	}
	
	// handles player obtaining checkpoints
	public void getCheckPoints() {
		CheckPoint[] checkpoints = Game.checkpoints;
		for(int i = 1; i <= Game.cpCount; i++) {
			int lx = checkpoints[i].getX();
			int rx = lx + checkpoints[i].getWidth();
			int yT = checkpoints[i].getY();
			int yB = yT + checkpoints[i].getHeight();
			int level = checkpoints[i].getLevel();
			if(lx - w < x && x < rx && yT - health < y && y < yB && Game.level == level) {
				if(lastCheckPoint > 0) {
					checkpoints[lastCheckPoint].unmark();
				}
				lastCheckPoint = i;
				checkpoints[i].get();
			}
		}
	}
	
	public void intersectProjectile() {
		Level level = Game.levels[Game.level];
		Shooter[] shooters = level.getShooters();
		for(int i = 1; i <= level.getShooterCount(); i++) {
			Projectile[] projectiles = shooters[i].getProjectiles();
			for(int j = 0; j < projectiles.length; j++) {
				if (projectiles[j] != null) {
					int lx = projectiles[j].getX();
					int rx = lx + projectiles[j].getWidth();
					int yT = projectiles[j].getY();
					int yB = yT + projectiles[j].getHeight();
					if(lx - w < x && x < rx && yT - health < y && y < yB) {
						health-=10;
						invincible = 30;
						projectiles[j].stop();
						break;
					}
				}
			}
		}
	}
	
	//Updates everything
	public void update() {
		if(vy > 25) {
			vy = 25;
		}
		boolean touchingLava = isTouchingLava();
		boolean onIce = isOnIce();
		Level level = Game.levels[Game.level];
		MovingPlatform[] movingPlatforms = level.getMovingPlatforms();
		
		// Movement - - - - - - - - - - - - 
		weight = health / 20.0 * 0.35 + 0.8;
		
		//detects if player is on the ground
		if ((gravityPos == -1 && noCollisions(x, y-1)) || (gravityPos == 1 && noCollisions(x, y+1))) {
			onGround = false;	
		}
		
		//jumping
		if(upPressed && onGround && !hasInstrOpen) {
			vy = -13.5 * gravityPos;
			onGround = false;
		}
		//wall jumping
		if(upPressed && gotWJ && hasWJ && !hasInstrOpen) {
			justWJed = true;
			vx = shift * WJdir * (2.5-weight)+(2.6-weight)*WJdir;
			aR = 0;
			vy = -13.5;
			hasWJ = false;
			onGround = false;
		}
		//right after wall jumping
		if(justWJed == true) {
			aR += 0.1 * WJdir;
			vx -= aR;
			if((WJdir == 1 && vx <= 0.0) || (WJdir == -1 && vx >= 0.0)) {
			  vx = 0;
			  aR = 0;
			  justWJed = false;
			}
		}
		//lava swimming
		if (upPressed && touchingLava && timeInLava > 1 && !hasInstrOpen) {
			vy = -7.5 * gravityPos;
			onGround = false;
		}
		
		//smaller jump when letting go of up
		if(!upPressed && vy < -6.0) {
			vy = -6.0;
		}
		
		//wall sticking
		if(!noCollisions(x-1, y) && noCollisions(x, y) && leftPressed && !onGround && !upPressed && !justWJed) {
			hasWJ = true;
			WJdir = 1;
			if (gotWJ)
				vy = 0;
		} else if(!noCollisions(x+1, y) && noCollisions(x, y) && rightPressed && !onGround && !upPressed && !justWJed) {
			hasWJ = true;
			WJdir = -1;
			if (gotWJ)
				vy = 0;
		} else {
			hasWJ = false;
		}
		
		//moving right
		if(rightPressed && !(justWJed && WJdir == -1) && !hasInstrOpen) {
			dir = RIGHT;
			for (int i = 0; i <= speed; i++) {
				if(noCollisions(x + (speed-i), y)) {
					if (touchingLava && i < (int)speed-1) {
						i = (int)speed-2;
					}
					vx = (speed-i);
					if (onIce && noCollisions(x+1, y)) aR2 = -5.5;
					break;
				} 
			}
		} 
		//moving left
		else if(leftPressed && !(justWJed && WJdir == 1) && !hasInstrOpen) {
			dir = LEFT;
			for (int i = 0; i <= speed; i++) {
				if(noCollisions(x - (speed-i), y)) {
					if (touchingLava && i < (int)speed-1) {
						i = (int)speed-2;
					}
					vx = -(speed-i);
					if (onIce && noCollisions(x-1, y)) aR2 = 5.5;
					break;
				} 
			}
		} else if(!justWJed) {
			vx = 0;
		}
		
		//ice physics
		if (!onIce && onGround) aR2 = 0;
		
		vx -= aR2;
		
		if(!justWJed && !onGround) {
			if(((vx > 0 || aR2 < 0) && leftPressed) || ((vx < 0 || aR2 > 0) && rightPressed)) {
				vx = 0;
				aR2 = 0;
			}
		}
		
		if (aR2 > 0 && !noCollisions(x-1, y)) {
			aR2 = 0;
		} else if (aR2 < 0 && !noCollisions(x+1, y)) {
			aR2 = 0;
		}
		
		if (!justWJed && aR2 <= 0.1 && aR2 >= -0.1 && aR2 != 0) {
			aR2 = 0;
		} else if (!justWJed && aR2 < 0 && Game.time % 2 == 0 ) {
			aR2 += 0.1;
		} else if (!justWJed && aR2 > 0 && Game.time % 2 == 0 ) {
			aR2 -= 0.1;
		}
		
		//gravity powerup cooldown
		if(gravityCooldown == 0) {
			if(spacePressed && gotGravity) {
				gravityPos *= -1;
				gravityCooldown = 7;
			}
		}
		
		if(gravityCooldown > 0) {
			gravityCooldown--;
		}
		
		//dash powerup cooldown
		if (dashCooldown == 0) {
			if (spacePressed && !rightPressed && !leftPressed && !upPressed && !downPressed && hasDash && gotDash && !hasInstrOpen) {
				dash = 0;
				dashDir = lastDir;
			}
			if (spacePressed && rightPressed && hasDash && gotDash && !hasInstrOpen) {
				dash = 0;
				dashDir = RIGHT;
			}
			if (spacePressed && leftPressed && hasDash && gotDash && !hasInstrOpen) {
				dash = 0;
				dashDir = LEFT;
			}
			//vertical dashes
			if (spacePressed && downPressed && hasDash && gotDash && !hasInstrOpen) {
				dash = 0;
				dashDir = DOWN;
			}
			if (spacePressed && upPressed && hasDash && gotDash && !hasInstrOpen) {
				dash = 0;
				dashDir = UP;
			}
			if (spacePressed && !hasInstrOpen) {
				hasDash = false;
				spacePressed = false;
			}
		}
		
		//Handles lots of physics while not dashing
		if (dash == 4) {
			newX = x;
			int dist = 0;
			while(Math.abs(dist) < Math.abs(vx)) {
				if(vx < 0) dist--;
				else dist++;
				if(noCollisions(x+dist, y)) {
					newX = x+dist;
				}
			}
			if (touchingLava && !upPressed) {
				if (onGround) {
					vy = 0;
				} else {
					vy = 15*gravityPos;
				}
				newY = y + vy * 0.2;
			} else {
				vy += Game.gravity * gravityPos * weight;
				newY = y + vy;
				for(int i = 1; i <= Game.levels[Game.level].getMovingPlatCount(); i++) {
					int lx = movingPlatforms[i].getX();
					int rx = lx + movingPlatforms[i].getWidth();
					int yT = movingPlatforms[i].getY();
					int yB = yT + movingPlatforms[i].getHeight();
					int type = movingPlatforms[i].getType();
					int dir = movingPlatforms[i].getDir();
					int speed = movingPlatforms[i].getSpeed();
					if(lx - w < newX && newX < rx && yT - health < newY && newY < yB) {
						intersectMovingPlatform(lx, rx, yT, yB, type, dir, speed, x, y);
					}
				}
			}
			x = newX;
			y = newY;
		}
		
		if (dashCooldown > 0) {
			dashCooldown--;
		}
		
		//main dash physics
		if (dash < 4 && dashCooldown == 0) {
			dash++;
			if (vy > 0) {
				vy = 0;
			}
			if (dashDir == UP) {
				for (int i = 0; i < 21; i++) {
					if (noCollisions(x, y - 1*gravityPos)) {
						y = y - 1*gravityPos;
					}
					if (isTouchingLava() && i < 17) {
						i = 17;
					}
				}
			}
			//down
			else if (dashDir == DOWN) {
				for (int i = 0; i < 21; i++) {
					if (noCollisions(x, y+1)) {
						y = y + 1*gravityPos;
					}
					if (isTouchingLava() && i < 17) {
						i = 17;
					}
				}	
			}
			//right
			else if (dashDir == RIGHT) {
				for (int i = 0; i < 21; i++) {
					if (noCollisions(x+1, y)) {
						x++;
					}
					if (isTouchingLava() && i < 17) {
						i = 17;
					}
				}	
			}
			//left
			else if (dashDir == LEFT) {
				for (int i = 0; i < 21; i++) {
					if (noCollisions(x-1, y)) {
						x--;
					}
					if (isTouchingLava() && i < 17) {
						i = 17;
					}
				}	
			}
			if (dash == 4) {
				dashCooldown = 7;
			}
		}
		
		if(vy * gravityPos > 0) {
			dir = DOWN;
		} else {
			dir = UP;
		}
		
		// Collisions - - - - - - - - - - - - -
		collectPowerups();
		collectItems();
		openInstructions();
		landingOnPlatform();
		checkExit();
		getCheckPoints();
		
		// Health - - - - - - - - - - - - -
		if (invincible == 0) {
			intersectProjectile();
		} else {
			invincible--;
		}
		
		if(touchingLava && !hasInstrOpen) {
			if (invincible == 0) {
				health -= 1;
				if (gravityPos == 1) {
					y += 1;
				}
			}
			timeInLava++;
		} else {
			timeInLava = 0;
		}
		
		if ((health <= 0 || rPressed) && !hasInstrOpen) { //death
			if (rPressed) {
				resetCount++;
				rPressed = false;
			} else {
				deathCount++;
			}
			Game.reset(lastCheckPoint);
			aR2 = vx = 0;
			health = 10;
			if (gravityPos == -1) {
				gravityPos = 1;
			}
			if(lastCheckPoint == 0) {
				health += 30;
			}
		}
		
		//Inventory - - - - - - - - - - - - - - -
		if(onePressed && !hasInstrOpen) {
			inv.switchItem(1);
		}
		if(twoPressed && !hasInstrOpen) {
			inv.switchItem(2);
		}
		if(threePressed && !hasInstrOpen) {
			inv.switchItem(3);
		}
		if(inv.getItem() != "Gravity") {
			gravityPos = 1;
		}
	}
	
	//returns the x
	public double getX() {
		return x;
	}
	
	//returns the y
	public double getY() {
		return y;
	}
	
	//returns the width
	public int getW() {
		return w;
	}
	
	//returns the height/health
	public double getHealth() {
		return health;
	}
	
	//returns the death count
	public int getDeaths() {
		return deathCount;
	}
	
	//returns the reset count
	public int getResets() {
		return resetCount;
	}
	
	//returns invincible
	public int getInvincible() {
		return invincible;
	}
	
	//sets the x
	public void setX(int newX) {
		x = newX;
	}
	
	//sets the y
	public void setY(int newY) {
		y = newY;
	}
	
	//returns if the player has a dash
	public boolean getHasDash() {
		return hasDash;
	}
	
	//returns if the player has gotten the dash powerup or not
	public boolean getGotDash() {
		return gotDash;
	}
	
	//sets got dash
	public void setGotDash(boolean b) {
		gotDash = b;
	}
	
	//returns if the player has gotten the wj powerup or not
	public boolean getGotWJ() {
		return gotWJ;
	}
	
	public void setGotWJ(boolean b) {
		gotWJ = b;
	}
	
	public boolean getInvWJ() {
		return invWJ;
	}
	
	public boolean getInvDash() {
		return invDash;
	}
	
	public boolean getInvGravity() {
		return invGravity;
	}
	
	//returns the gravity state
	public int getGravityPos() {
		return gravityPos;
	}
	
	public void setGravityPos(int x) {
		gravityPos = x;
	}
	
	public void setGotGravity(boolean b) {
		gotGravity = b;
	}
	
	public boolean getSpacePressed() {
		return spacePressed;
	}
	
	public boolean getEPressed() {
		return ePressed;
	}
	
	public boolean getPPressed() {
		return pPressed;
	}
	
	public boolean getUpPressed() {
		return upPressed;
	}
	
	public boolean getDownPressed() {
		return downPressed;
	}
	
	public boolean getRightPressed() {
		return rightPressed;
	}
	
	public boolean getLeftPressed() {
		return leftPressed;
	}
	
	//Listen for key presses, called by Game
	public void keyPressed(KeyEvent e) {
		if((e.getKeyCode() == KeyEvent.VK_LEFT) || (e.getKeyCode() == KeyEvent.VK_A)) {
			leftPressed = true;
		}
		if((e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_D)) {
			rightPressed = true;
		}
		if((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_W)) {
			upPressed = true;
		}
		if((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_S)) {
			downPressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			spacePressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_E) {
			ePressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_R) {
			rPressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_P) {
			pPressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_1) {
			onePressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_2) {
			twoPressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_3) {
			threePressed = true;
		}
	}
	
	//Listen for key releases, called by Game
	public void keyReleased(KeyEvent e) {
		if((e.getKeyCode() == KeyEvent.VK_LEFT) || (e.getKeyCode() == KeyEvent.VK_A)) {
			leftPressed = false;
			lastDir = LEFT;
		}
		if((e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_D)) {
			rightPressed = false;
			lastDir = RIGHT;
		}
		if((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_W)) {
			upPressed = false;
		}
		if((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_S)) {
			downPressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			spacePressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_E) {
			ePressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_P) {
			pPressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_R) {
			rPressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_1) {
			onePressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_2) {
			twoPressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_3) {
			threePressed = false;
		}
	}
}