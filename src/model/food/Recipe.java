package model.food;

import java.util.*;

/**
 * Represents a recipe food and holds its properties
 *
 * @author Team A
 */
public class Recipe implements IFood {
	private String name;

	private Map<IFood,Double> ingredients; // Contains the ingredient and its serving size

	public Recipe(String name, Map<IFood, Double> ingredients) {
		this.name = name;
		this.ingredients = ingredients;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public double getCalories() {
		double cals = 0;

		// Iterate through all mappings
		for(Map.Entry<IFood,Double> entry : ingredients.entrySet()) {
			// Calculate calories
			cals += entry.getKey().getCalories() * entry.getValue();
			cals = Math.round(cals * 10.0) / 10.0;
		}

		return cals;
	}

	@Override
	public double getFat() {
		double fat = 0;

		for(Map.Entry<IFood,Double> entry : ingredients.entrySet()) {
			fat += entry.getKey().getFat() * entry.getValue();
			fat = Math.round(fat * 10.0) / 10.0;
		}

		return fat;
	}

	@Override
	public double getCarb() {
		double carbs = 0;

		for(Map.Entry<IFood,Double> entry : ingredients.entrySet()){
			carbs += entry.getKey().getCarb() * entry.getValue();
			carbs = Math.round(carbs * 10.0) / 10.0;
		}

		return carbs;
	}

	@Override
	public double getProtein() {
		double protein = 0 ;

		for (Map.Entry<IFood,Double> entry : ingredients.entrySet()) {
			protein += entry.getKey().getProtein() * entry.getValue();
			protein = Math.round(protein * 10.0) / 10.0;
		}

		return protein;
	}

	/**
	 * Returns the ingredients and serving sizes of this recipe
	 *
	 * @return Map
	 */
	public Map<IFood, Double> getIngredients() {
		return this.ingredients;
	}
}
