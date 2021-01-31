import java.util.Random;
public class ColonyNode extends ColonyNodeView{
	private int numScouts,numSoldiers,numForagers,numBalas, food, pharamone, x,y;
	private Random rand;
	
	public ColonyNode() {
		numScouts=0;
		numSoldiers=0;
		numForagers=0;
		numBalas=0;
		rand = new Random();
	}
	
	/**
	 * getters
	 */
	public int getScouts() {
		return this.numScouts;
	}
	
	public int getSoldiers() {
		return numSoldiers;
	}
	
	public int getForagers() {
		return numForagers;
	}
	
	public int getBalas() {
		return numBalas;
	}
	
	public int getFood() {
		return food;
	}
	
	public int getPharamone() {
		return pharamone;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	/** 
	 * setters
	 */
	
	public void setScouts(int newScouts) {
		numScouts = newScouts;
	}
	
	public void setSoldiers(int newSoldiers) {
		numSoldiers = newSoldiers;
	}
	
	public void setForagers(int newForagers) {
		numForagers=newForagers;
	}
	
	public void setBalas(int newBalas) {
		numBalas = newBalas;
	}
	
	public void setFood(int newFood) {
		food = newFood;
	}
	
	public void setPharamone(int newPharemone) {
		pharamone = newPharemone;
	}
	
	public void setX(int newX) {
		x = newX;
	}
	
	public void setY(int newY) {
		y=newY;
	}
}
