/**
 * Ant super class will be extended to all ants, provides basic building blocks for all ants in the simulation
 * @author Perry Andrysczyk
 *
 */
public class Ant {

	 	private int ID, lifeSpan, antSpotX, antSpotY, turnBorn;
	 	private boolean isDead;
	 	
	 	/**
	 	 * Constructor
	 	 */
	 	public Ant() {
	 		isDead = false;
	 		lifeSpan = 3650;//lifespan is one year, 10 turns = 1 day, 365*10=3650 turns
	 		antSpotX = 13;
	 		antSpotY = 13;
	 		turnBorn = AntColonySimulation.getTurn();
	 		
	 		try {
	 			setId(AntColonySimulation.ants.get(AntColonySimulation.ants.size()).getId()+1);
	 			}catch(Exception e) {}
	 	}
	 	 	 	
	 	/**
	 	 * getters
	 	 * @return
	 	 */
	 	public int getId() {
	 		return this.ID;
	 	}
	 	
	 	public int getLifeSpan() {
	 		return this.lifeSpan;
	 	}
	 	
	 	public boolean getIsDead() {
	 		return this.isDead;
	 	}
	 	
	 	public int getAntSpotX() {
	 		return this.antSpotX;
	 	}
	 	
	 	public int getAntSpotY() {
	 		return this.antSpotY;
	 	}
	 	
	 	public int getTurnBorn() {
	 		return this.turnBorn;
	 	}
	 	
	 	public int getAge(){
	 		return getTurnBorn()-AntColonySimulation.getTurn();
	 	}
	 	
	 	/**
	 	 * setters
	 	 * 
	 	 * setters are protected and should only be accessed by child Ant classes
	 	 * 
	 	 * @param xID
	 	 */
	 	protected void setId(int xID) {
	 		this.ID=xID;
	 	}
	 	
	 	protected void setLifeSpan(int xLifeSpan) {
	 		this.lifeSpan = xLifeSpan;
	 	}
	 	
	 	protected void setIsDead(boolean xIsDead) {
	 		this.isDead = xIsDead;
	 	}
	 	
	 	public void setAntSpotX(int x) {
	 		this.antSpotX = x;
	 	}
	 	
	 	public void setAntSpotY(int y) {
	 		this.antSpotY = y;
	 	}
	 			
}
