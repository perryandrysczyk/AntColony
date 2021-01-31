/**
 * Controls movements and actions unique to foragers in simulation
 * @author Perry Andrysczyk
 */
import java.util.*;
public class Forager extends Ant{

	/**
	 * private variables
	 */
	private boolean hasFood;
	private LinkedList<ColonyNodeView> movements = new LinkedList<>();
	private LinkedList<ColonyNodeView> adjacentSquares;
	private Random rand = new Random();
	
	/**
	 * constructor
	 */
	public Forager() {
		super();
		hasFood = false;
		this.setId(AntColonySimulation.ants.size()+1);
	}
	
	/**
	 * move to nodes with most pheromone
	 * @param grid
	 */
	public void move(ColonyNodeView[][] grid) {
		//variables
		int x = this.getAntSpotX();
		int y = this.getAntSpotY();
		int lastX=this.getAntSpotX(), lastY=this.getAntSpotY();
		
		try {
		//add the current spot to movements, this will be traversed when food is found
		movements.add(grid[this.getAntSpotX()][this.getAntSpotY()]);
		
		//hide icon before moving
		grid[this.getAntSpotX()][this.getAntSpotY()].hideForagerIcon();
		
		//find adjacent squares
		adjacentSquares = this.findAdjacentSquares(grid);

		//initialize the next move before finding the best move
		ColonyNodeView nextMove = adjacentSquares.get(0);
		
		//check nextMove against other elements in adjacentSquares to see which square has the most pheromone	
		for(int i=1; i<adjacentSquares.size(); i++) {
			
			//if a higher pheromone count is found, or if the nextMove exists in movment history
			if(nextMove.getPheromone() < adjacentSquares.get(i).getPheromone() || movements.contains(nextMove)) {
				//reassign nextMove
				nextMove = adjacentSquares.get(i);
			}
			
			//if two adjacenSquares have the same amount of pheromone
			else if(nextMove.getPheromone() == adjacentSquares.get(i).getPheromone()) {
				//choose randomly between the two
				boolean next = rand.nextBoolean();
				if(next)
					nextMove = adjacentSquares.get(i);
			}
		}
		
		//change ant x, y coordinates to nextMove x, y coordinates
		this.setAntSpotX(nextMove.getXCoordinate());
		this.setAntSpotY(nextMove.getYCoordinate());
		
		//set forager amounts
		grid[lastX][lastY].setForagerCount(grid[lastX][lastY].getForagers()-1);
		
		//show forager in new square
		grid[this.getAntSpotX()][this.getAntSpotY()].showForagerIcon();
		
		//add forager to new square
		grid[this.getAntSpotX()][this.getAntSpotY()].setForagerCount(grid[this.getAntSpotX()][this.getAntSpotY()].getForagers()+1);
		
		//check new square for food
		if(grid[this.getAntSpotX()][this.getAntSpotY()].getFood() > 0) {
			this.setHasFood(true);
			//take one unit of food
			grid[this.getAntSpotX()][this.getAntSpotY()].setFoodAmount(grid[this.getAntSpotX()][this.getAntSpotY()].getFood()-1);
		}
	}catch(Exception e) {
		//if exception occurs, no movement happens
		this.setAntSpotX(lastX);
		this.setAntSpotY(lastY);
		grid[lastX][lastY].showForagerIcon();
	}
}
			

	/**
	 * find adjacent squares to a current node
	 * @param grid
	 * @return
	 */
	private LinkedList<ColonyNodeView> findAdjacentSquares(ColonyNodeView[][] grid) {
		
		//initialize linkedlist and current node
		LinkedList<ColonyNodeView> adjacentNodes = new LinkedList<>();
		ColonyNodeView currentNode = grid[this.getAntSpotX()][this.getAntSpotY()];
		
		//check current node against all adjacent nodes
		for(int i=-1; i<2;i++) {
			
			for(int j=-1; j<2;j++) {
				
				//check that current node is not the same as antSpotX+i, antSpotY+j.
				//this keeps the current node from being added to the list of valid moves a Forager can make
				if((currentNode.getXCoordinate() == this.getAntSpotX()+i && currentNode.getYCoordinate() == this.getAntSpotY()+j) 
						) {
					continue;
				}
				
				//if an adjacent square is shown, add it to the list of valid moves
				if(grid[this.getAntSpotX()+i][this.getAntSpotY()+j].getShown()) {
					adjacentNodes.add(grid[this.getAntSpotX()+i][this.getAntSpotY()+j]);
				}
			}
		}
		
		//return list of adjacent nodes
		return adjacentNodes;
	}
		
	
	/**
	 * getter
	 * @return
	 */
	public boolean getHasFood() {
		return this.hasFood;
	}
	
	/**
	 * setter
	 * @param food
	 */
	public void setHasFood(boolean food) {
		hasFood = food;
	}

	/**
	 * this will be used instead of the move() method when foragers have food
	 * @param grid
	 */
	public void returnToNest(ColonyNodeView[][] grid) {
		
		//if current node is the queen node
		if(this.getAntSpotX() == 13 && this.getAntSpotY() == 13) {
			
			//deposit food
			grid[13][13].setFoodAmount(grid[13][13].getFood()+1);
			//set food to false and skip the rest of the method
			this.setHasFood(false);
			return;
		}
		
		//initialize x, y
		int x = this.getAntSpotX(), y = this.getAntSpotY();
		
		//hide forager before moving and decrement foragerCount
		grid[x][y].hideForagerIcon();
		grid[x][y].setForagerCount(0);
		
		//reference the most recent item in movements
		ColonyNodeView last = movements.get(movements.size()-1);
		
		//move into the previous spot
		this.setAntSpotX(last.getXCoordinate());
		this.setAntSpotY(last.getYCoordinate());
		
		//show forager and set forager amount
		grid[this.getAntSpotX()][this.getAntSpotY()].showForagerIcon();
		grid[this.getAntSpotX()][this.getAntSpotY()].setForagerCount(grid[this.getAntSpotX()][this.getAntSpotY()].getForagers()+1);
		
		//deposit pheromone in all squares except the queen, this will prevent the queen square from changing colors
		if(last.getXCoordinate() != 13 && last.getYCoordinate() != 13)
			last.setPheromoneLevel(last.getPheromone()+10);
		
		//remove previous item in the movements
		movements.remove(movements.size()-1);
		
	}
}
