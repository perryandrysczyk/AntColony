import java.util.*;

public class Soldier extends Ant{

	Random rand = new Random();
	
	/**
	 * constructor
	 */
	public Soldier() {
		super();
		this.setId(AntColonySimulation.ants.size()+1);
	}
	
	/**
	 * soldiers should move randomly unless an adjacent square contains any bala ants
	 * @param grid
	 */
	public void move(ColonyNodeView[][] grid) {
			//variables			
			int x = this.getAntSpotX();
			int y = this.getAntSpotY();
			int lastX=this.getAntSpotX(), lastY=this.getAntSpotY();
			
			try {
			//hide soldier icon before moving
			grid[this.getAntSpotX()][this.getAntSpotY()].hideSoldierIcon();
			
			
			//adjacent nodes and forbidden nodes
			LinkedList<ColonyNodeView> adjacentSquares = this.findAdjacentSquares(grid);
			
			
			//forbidden nodes will likely only contain the previous node
			LinkedList<ColonyNodeView> forbiddenNodes = new LinkedList<ColonyNodeView>();
			
			//add current node to previous node before moving
			forbiddenNodes.add(grid[this.getAntSpotX()][this.getAntSpotY()]);
			
			
			//next move initialized arbitrarily to final element in adjacentSquars
			ColonyNodeView nextMove = adjacentSquares.get(rand.nextInt(adjacentSquares.size()));
			
			
			//if there are no balas in nextMove, or nextMove is the previous node
			if(nextMove.getBalas() == 0 || forbiddenNodes.contains(nextMove)) {
				//randomly assign another option
				for(int i=0; i<adjacentSquares.size(); i++) {
					nextMove = adjacentSquares.get(rand.nextInt(adjacentSquares.size()));
				}
			}
			
			
			//perform the movement
			this.setAntSpotX(nextMove.getXCoordinate());
			this.setAntSpotY(nextMove.getYCoordinate());
			
			
			//set soldier count in previous node
			grid[lastX][lastY].setSoldierCount(0);
			
			
			//show icon in new node and increment soldier count
			grid[this.getAntSpotX()][this.getAntSpotY()].showSoldierIcon();
			grid[this.getAntSpotX()][this.getAntSpotY()].setSoldierCount(grid[this.getAntSpotX()][this.getAntSpotY()].getSoldiers()+1);
			
		}catch(Exception e) {
			//catch generic exception
		}
	}
		
	
	/**
	 * attack a bala
	 * @return
	 */
	public boolean attack() {
		boolean attacked = rand.nextBoolean();
		
			/**
			 * following print statement is for troubleshooting and testing, uncomment to print weather attack was sucessful
			 */
				//System.out.println("Soldier Attack Success: " + attacked);
		
		return attacked;
	}

	/**
	 * find adjacen nodes
	 * @param grid
	 * @return
	 */
	private LinkedList<ColonyNodeView> findAdjacentSquares(ColonyNodeView[][] grid) {
		//linkedlist to be returned
		LinkedList<ColonyNodeView> adjacentNodes = new LinkedList<>();
		
		//current node
		ColonyNodeView currentNode = grid[this.getAntSpotX()][this.getAntSpotY()];
		
		//take current nodes x, y values and add every number from -1 to 1 and check if those nodes are shown
		for(int i=-1; i<=1;i++) {
			
			for(int j=-1; j<=1;j++) {
				
				//prevent current node from being added as a possible move
				if((currentNode.getXCoordinate() == this.getAntSpotX()+i && currentNode.getYCoordinate() == this.getAntSpotY()+j)) {
					continue;
				}
				
				//add all nearby squares that have been revealed to the list of possible moves
				if(grid[this.getAntSpotX()+i][this.getAntSpotY()+j].getShown() == true) {
					adjacentNodes.add(grid[this.getAntSpotX()+i][this.getAntSpotY()+j]);
				}
			}
		}
		
		return adjacentNodes;
	}

	
}
