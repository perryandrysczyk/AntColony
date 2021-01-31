/**
 * Simulation is controlled from AntCollonySimulation class. All actions will be called from this class
 * @author Perry Andrysczyk
 */

import java.util.*;
public class AntColonySimulation extends AntSimGUI implements SimulationEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3200548802952958791L;
	
	/**
	 * ATTRIBUTES
	 */
	
	ArrayList<Scout> scouts = new ArrayList<>();
	ArrayList<Forager>foragers = new ArrayList<>();
	ArrayList<Soldier> soldiers = new ArrayList<>();
	public static ArrayList<Ant> ants = new ArrayList<>();
	ArrayList<Bala> balas = new ArrayList<>();
	//Create a new ColonyView	
	private static ColonyView colView = new ColonyView(27,27);
	//Create the grid of ColonyNodeView objects
	private static ColonyNodeView[][] grid = new ColonyNodeView[27][27];
	private static int turn = 0;
	private AntSimGUI simGui = new AntSimGUI();
	public static int foodAmount = 1000, foragerCount = 50, scoutCount = 4, soldierCount = 10;
	public Queen queen = new Queen();
	private static int localScoutCount = 0;
	private static int localBalaCount = 0;
	private static int localForagerCount = 0;
	private static int localSoldierCount = 0;
	//random number generation
	public static Random rand = new Random();
	Scout testScout = new Scout();
	
	public static int getTurn() {
		return turn;
	}
	
	public AntColonySimulation() {
		simGui.initGUI(colView);
		simGui.addSimulationEventListener(this);
		initGrid(grid);
		ants.add(queen);
	}
	
	
	private static void initGrid(ColonyNodeView[][] grid) {
		
		//randomized chance of food and amount of food
		int chanceOfFood, amountOfFood;
		//set up the values in each square
		for(int i=0; i<grid.length;i++) {
			for(int j=0;j<grid[i].length; j++) {
				chanceOfFood = rand.nextInt(100);
				if(chanceOfFood<=25) {
					amountOfFood = rand.nextInt(501)+500;
				}else {
					amountOfFood = 0;
				}
				grid[i][j] = new ColonyNodeView();
				grid[i][j].setQueen(false);
				grid[i][j].setXCoordinate(i);
				grid[i][j].setYCoordinate(j);
				grid[i][j].setPheromoneLevel(rand.nextInt(1001));
				grid[i][j].setFoodAmount(amountOfFood);
				grid[i][j].hideBalaIcon();
				grid[i][j].hideForagerIcon();
				grid[i][j].hideScoutIcon();
				grid[i][j].hideSoldierIcon();
				grid[i][j].setScoutCount(localScoutCount);
				grid[i][j].setSoldierCount(localSoldierCount);
				grid[i][j].setBalaCount(localBalaCount);
				grid[i][j].setForagerCount(localForagerCount);
				colView.addColonyNodeView(grid[i][j], i, j);
				if(i >= 12 && i<=14 && j >= 12 && j <= 14)
					grid[i][j].showNode();
			}
		}
		initQueenSquare();
	}
	
	

	/**
	 * initialize the queen square at the begining of the simulation
	 */
	private static void initQueenSquare() {
		ColonyNodeView queenSquare = grid[13][13];
		queenSquare.setQueen(true);
		queenSquare.showQueenIcon();
		queenSquare.showForagerIcon();
		queenSquare.showScoutIcon();
		queenSquare.showSoldierIcon();
		queenSquare.setFoodAmount(foodAmount);
		queenSquare.setForagerCount(foragerCount);
		queenSquare.setSoldierCount(soldierCount);
		queenSquare.setScoutCount(scoutCount);
		queenSquare.showNode();
	}
	
	
	private void queenTurn() {
		//queen will die if food amount is empty or queen's life span is reached
		if((foodAmount <= 0) || turn >= 73000) {
			//stepMode = false;
			queen.die();
		}
		
		//hatch a new ant every day
		ColonyNodeView queenSquare = grid[13][13];
		if(turn%10 == 0 ) {
			Ant newAnt = queen.hatch();
			if(newAnt.getClass().equals(Forager.class)) {
				foragerCount++;
				foragers.add((Forager)newAnt);
				queenSquare.setForagerCount(queenSquare.getForagers()+1);
			}
			else if(newAnt.getClass().equals(Scout.class)) {
				scoutCount++;
				scouts.add((Scout)newAnt);
				queenSquare.setScoutCount(queenSquare.getScouts()+1);
			}
			else if(newAnt.getClass().equals(Soldier.class)) {
				soldierCount++;
				soldiers.add((Soldier)newAnt);
				queenSquare.setSoldierCount(queenSquare.getSoldiers()+1);
			}			
		}
		//consume 1 unit of food
		queen.eat(queenSquare);

	}

	
	/**
	 * each turn, the scouts should each make 1 move
	 */
	private void scoutTurn() {
		//initialize them all to an arraylist at start of simulation
		if(turn == 0) {
			for(int i=0; i<scoutCount; i++) {
				Scout newScout = new Scout();
				scouts.add(newScout);
				ants.add(newScout);
			}
			
		}
		
		//check to verify age, if greater than lifespan, remove that ant from simulaiton
		for(int i=0; i<scouts.size(); i++) {
			if(scouts.get(i).getAge() >= scouts.get(i).getLifeSpan()) {
				grid[scouts.get(i).getAntSpotX()][scouts.get(i).getAntSpotY()].hideScoutIcon();
				grid[scouts.get(i).getAntSpotX()][scouts.get(i).getAntSpotY()].setScoutCount(grid[scouts.get(i).getAntSpotX()][scouts.get(i).getAntSpotY()].getScouts()-1);
				scouts.remove(scouts.get(i));
				
			}
			//if scouts are not dead of old age, move normally
			else {
				scouts.get(i).move(grid);
			}
		}
		
	}
	
	/**
	 * each forager should make 1 move each turn. Generally speaking, they will all move in unison unless two squares have the same amount of pheromone.
	 */
	private void foragerTurn() {
		//initialize to an arraylist
		if(turn == 0) {
			for(int i = 0; i<foragerCount;i++) {
				Forager newForager = new Forager();
				foragers.add(newForager);
				ants.add(newForager);
			}
		}
		
		//remove if dead of old age, otherwise, move normally until food is found
		for(int i=0; i<foragers.size(); i++) {
			if(foragers.get(i).getAge() >= foragers.get(i).getLifeSpan()) {
				grid[foragers.get(i).getAntSpotX()][foragers.get(i).getAntSpotY()].hideForagerIcon();
				grid[foragers.get(i).getAntSpotX()][foragers.get(i).getAntSpotY()].setForagerCount(grid[foragers.get(i).getAntSpotX()][foragers.get(i).getAntSpotY()].getForagers()-1);
				foragers.remove(foragers.get(i));
			}
			else {
				if(foragers.get(i).getHasFood()) {
					foragers.get(i).returnToNest(grid);
				}
				else {
					foragers.get(i).move(grid);
				}
			}
			
		}
		
	}
	
	
	/**
	 * each soldier should make 1 move per turn
	 */
	private void soldierTurn() {
		//initialize to arraylist
		if(turn == 0) {
			for(int i=0; i<soldierCount; i++) {
				Soldier newSoldier = new Soldier();
				soldiers.add(newSoldier);
				ants.add(newSoldier);
			}
		}
		
				
		for(int i=0; i<soldiers.size(); i++) {
			//remove if dead of old age
			if(soldiers.get(i).getAge() >= soldiers.get(i).getLifeSpan()) {
				grid[soldiers.get(i).getAntSpotX()][soldiers.get(i).getAntSpotY()].setSoldierCount(grid[soldiers.get(i).getAntSpotX()][soldiers.get(i).getAntSpotY()].getSoldiers()-1);
				grid[soldiers.get(i).getAntSpotX()][soldiers.get(i).getAntSpotY()].hideSoldierIcon();
				soldiers.remove(soldiers.get(i));
			}
			else {
				//move to adjacent squares with Bala ants and attack
				if(grid[soldiers.get(i).getAntSpotX()][soldiers.get(i).getAntSpotY()].getBalas()>0) {
					if(soldiers.get(i).attack()) {
						grid[soldiers.get(i).getAntSpotX()][soldiers.get(i).getAntSpotY()].setBalaCount(grid[soldiers.get(i).getAntSpotX()][soldiers.get(i).getAntSpotY()].getBalas()-1);
						balas.remove(balas.get(i).getId());
						if(grid[soldiers.get(i).getAntSpotX()][soldiers.get(i).getAntSpotY()].getBalas()==0) {
							grid[soldiers.get(i).getAntSpotX()][soldiers.get(i).getAntSpotY()].hideBalaIcon();
						}
					}
					
				}
				//otherwise, move randomly
				else {
					soldiers.get(i).move(grid);
				}
		  }
		}
	}
	
	/**
	 * balas should appear at ramdom on the top left of the colony. Once instantiated, they must move once per turn
	 */
	private void balaTurn() {
		int killedAttackedAnt = -1;
		//3% chance of a bala being created and added to balas
		int balaChance = rand.nextInt(100)+1;
			if(balaChance >= 1 && balaChance <= 3) {
				Bala newBala = new Bala();
				balas.add(newBala);
			}
			
			//move randomly
			for(int i=0; i<balas.size();i++) {
				balas.get(i).move(grid);
				
				//bala should attack, they will prioritize the queen, then soldiers, then scouts, then foragers 
				if(grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].getScouts()>0 ||
						   grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].getSoldiers() > 0||
						   grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].getForagers()	> 0 ||
						   grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].getQueen()) {
				killedAttackedAnt = balas.get(i).attack(grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()]);
				
				}
				
				//bala attack method returns an integer from 0-4 indicating which outcome should happen. 1-4 will kill an ant 0 will be a missed attack
				switch(killedAttackedAnt) {
				
				case 1: 
					queen.die();
					break;
				case 2:
					grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].setSoldiers(grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].getSoldiers()-1);
					grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].hideSoldierIcon();
					soldiers.remove(i);
					break;
				case 3:
					grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].setScoutCount(grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].getScouts()-1);
					grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].hideScoutIcon();
					scouts.remove(i);
					break;
				case 4:
					grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].setForagerCount(grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].getSoldiers()-1);
					grid[balas.get(i).getAntSpotX()][balas.get(i).getAntSpotY()].hideForagerIcon();
					foragers.remove(i);
					break;
				default:
					break;
				}

			}			
			
	}
	
	
	@Override
	public void simulationEventOccurred(SimulationEvent simEvent) {		
		 if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT) { 
			 initGrid(grid);
			 return;
		 }
		 else if (simEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT) { 
			queenTurn();
		 }
		 else if (simEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT) { 
			 scoutTurn(); 
		 }
		 else if (simEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT) { 
			 foragerTurn();
		 }
		 else if (simEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT) {
			 soldierTurn();
		 } 
		 else if (simEvent.getEventType() == SimulationEvent.RUN_EVENT) {
			while(queen.getIsDead() == false){
				while(true) {
					queenTurn();
					foragerTurn();
					balaTurn();
					scoutTurn();
					soldierTurn();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {break;}
					break;
				}
				
			}
		 }	 
		 else if (simEvent.getEventType() == SimulationEvent.STEP_EVENT) {
			 queenTurn();
			 foragerTurn();
			 scoutTurn();
			 balaTurn();
			 soldierTurn();
		 }
		 else { 
			 // invalid event occurred - probably will never happen 
		 }
		 
		//decrease pheromone by half each day
		if(turn%10 == 0 && turn != 0) {
			for(int i =0; i<grid.length; i++) {
				for(int j=0;j<grid[i].length;j++) {
					if(i == 13 && j == 13)
						continue;
					grid[i][j].setPheromoneLevel(grid[i][j].getPheromone()/2);
				}
			}
		}
		 
		 turn++;//increment turn
		}



	
		
	}

	

