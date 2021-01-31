/**
 * Controls actions unique to Bala ants in simulation
 * @author Perry Andrysczyk
 */
import java.util.*;
public class Bala extends Ant{

	private Random rand = new Random();
	
	/**
	 * constructor, the only difference is the spawn location
	 */
	public Bala() {
		super();
		this.setAntSpotX(0);
		this.setAntSpotY(0);
	}
	
	/**
	 * move randomly until an enemy is found
	 * @param grid
	 */
	public void move(ColonyNodeView[][] grid) {		
		
		int x = this.getAntSpotX();
		int y = this.getAntSpotY();
		int lastX = this.getAntSpotX(), lastY=this.getAntSpotY();
		int offset;
		
		try {
			grid[this.getAntSpotX()][this.getAntSpotY()].setBalaCount(grid[x][y].getBalas()-1);
			//hide bala icon of the previous spot before moving the bala
			grid[lastX][lastY].hideBalaIcon();
			
			//generate random number from -1 to 1
			offset = rand.nextInt(3)-1;
			
			//ant's x coordinate will have the offset value added
			this.setAntSpotX(x+offset);
			
			//generate a new random number from -1 to 1
			offset = rand.nextInt(3)-1;
			
			//and's y coordinate will have offset added
			this.setAntSpotY(y+offset);
				
			
			//add 1 bala to the new square's bala count
			grid[this.getAntSpotX()][this.getAntSpotY()].setBalaCount(grid[x][y].getBalas()+1);
			
			//remove bala from the previous spot
			grid[lastX][lastY].setBalaCount(grid[lastX][lastY].getBalas());
			
			//show bala icon in the new square
			grid[this.getAntSpotX()][this.getAntSpotY()].showBalaIcon();	
			
		}catch(ArrayIndexOutOfBoundsException e) {
			//if a number is generated that is not within the grid, no movement takes place.
			this.setAntSpotX(lastX);
			this.setAntSpotY(lastY);
		}		
	}

	/**
	 * generate a number indicating the outcome of the attack
	 * @param current
	 * @return 
	 */
	public int attack(ColonyNodeView current) {
		int chanceOfSuccess = rand.nextInt(100)+1;
		
		/**
		 * the following print statement is for testing purposes, uncomment to see if attack was successful
		 */
				//System.out.println("Bala Attack Value: " + chanceOfSuccess);
		
		
		//50% chance of a failed attack
		if(chanceOfSuccess > 50) {
			return 0;
		}
		else {		
			if(current.getQueen()) {
				return 1;
			}
			else if(current.getSoldiers()>0) {
				return 2;
			}
			else if(current.getScouts()>0) {
				return 3;
			}
			else {
				return 4;
			}
		}
	
	}
	
}
