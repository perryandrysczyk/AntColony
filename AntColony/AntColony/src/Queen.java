/**
 * Queen class extends Ant, controls actions unique to the queen in the sumulation
 * 
 * @author Perry Andrysczyk
 */
import java.util.*;
public class Queen extends Ant{
	
	private static Random random = new Random();
	
	/**
	 * constructor
	 */
	public Queen() {
		super();
		super.setId(0);
		super.setLifeSpan(73000);
	}
	
	
	/**
	 * will hatch a random ant when called
	 * @return
	 */
	public Ant hatch() {
		int hatcher = random.nextInt(100);
		
		//return a new Ant of type scout, soldier, or forager
		//based on random hatcher integer
		if(hatcher>=0 && hatcher<=24)
			return new Scout();
		else if(hatcher>=25 && hatcher<=50)
			return new Soldier();
		else
			return new Forager();
	}
	
	/**
	 * consume one unit of food
	 * @param queenSquare
	 */
	public void eat(ColonyNodeView queenSquare) {
		queenSquare.setFoodAmount(queenSquare.getFood()-1);
	}
	
	/**
	 * if the queen dies, the simulation should end
	 */
	public void die() {
		this.setIsDead(true);
		System.exit(0);
	}
	
	
	
	
}
