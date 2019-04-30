package model.food;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Stores food objects in a Hash Map depending on the type of food with the Key as the food's name and Value as the object
 *
 * @author Team A
 */
public class FoodCollection extends Observable {
	private Map<String, IFood> foods; // ie.) "Hot Dog" -> HotDogObject

	public FoodCollection() {
		this.foods = new HashMap<>();
	}

	/**
	 * Adds a food to hash map
	 *
	 * @param name name of the food
	 * @param food food object
	 */
	public void addFood(String name, IFood food) {
		foods.put( name, food );

		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}

	/**
	 * Gets a food from hash map based on the name
	 *
	 * @param name name of the food
	 * @return Food object
	 */
	public IFood getFood(String name) {
		return foods.get( name );
	}

	/**
	 * Gets foods hash map
	 *
	 * @return map
	 */
	public Map<String, IFood> getCollection() {
		return foods;
	}
}
