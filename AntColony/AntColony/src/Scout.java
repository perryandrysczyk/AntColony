/**
 * provides methods necessarry to allow the scouts to move randomly
 * @author Perry Andrysczyk
 */
import java.util.*;
public class Scout extends Ant{
	
	private static Random rand;
	
	/**
	 * constructor
	 */
	public Scout() {
		super();
		this.setId(AntColonySimulation.ants.size()+1);
		rand = new Random();
	}
	
	/**
	 * move randomly through the grid, revealing squares as it moves 
	 * @param grid
	 */
	public void move(ColonyNodeView[][] grid) {
		//initialize variables to use
		int x = this.getAntSpotX();
		int y = this.getAntSpotY();
		int lastX = this.getAntSpotX(), lastY=this.getAntSpotY();
		int offset;
		
		try {
		//hide scout icon before moving
		grid[x][y].hideScoutIcon();
		
		//generate random integer from -1 to 1
		offset = rand.nextInt(3)-1;
		
		//add the offset to the x value. The result will be x-1, x+0, x+1
		this.setAntSpotX(this.getAntSpotX()+offset);	
		
		//generate a new offset for y
		offset = rand.nextInt(3)-1;
		
		//add the new offset to y, x and y will each become a random adjacent square
		this.setAntSpotY(this.getAntSpotY()+offset);	
		
		//update scout count in previous square and new square
		grid[lastX][lastY].setScoutCount(0);
		grid[this.getAntSpotX()][this.getAntSpotY()].setScoutCount(grid[x][y].getScouts()+1);
				
		//show scout in new square
		grid[this.getAntSpotX()][this.getAntSpotY()].showScoutIcon();
		
		//show node if not alread shown
		grid[this.getAntSpotX()][this.getAntSpotY()].showNode();
		}catch(ArrayIndexOutOfBoundsException e) {
			//if the try block generates an array position outside of the grid then no movement takes place
			this.setAntSpotX(lastX);
			this.setAntSpotY(lastY);
		}
	}

}
