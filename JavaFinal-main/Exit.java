//Production Release 
//Date: 5/23/22

public class Exit {
	private int type; //0 = exit up, 1 = exit right, 2 = exit down, 3 = exit left
	private int bL, bH;
	private int levelTo;
	private int newCoord;
	
	public Exit(int t, int boundLow, int boundHigh, int nc, int nextLevel) {
		type = t;
		bL = boundLow;
		bH = boundHigh;
		levelTo = nextLevel;
		newCoord = nc;
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
	
	//returns the level that the exit leads to
	public int getLevel() {
		return levelTo;
	}
	
	//returns the new coordinate in the level that the exit leads to
	public int getNewCoord() {
		return newCoord;
	}
	
}
