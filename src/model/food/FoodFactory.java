package model.food;

import java.util.Map;

/**
 * Factory class that handles the creation of food related objects
 *
 * @author Team A
 */
public class FoodFactory {
	/**
	 * Creates a food object depending on the array of values being passed in
	 *
	 * @param values array of values, the first index determines the type of food object being created
	 * @return BasicFood or Recipe
	 */
	public IFood createFood(String[] values, Map<IFood, Double> ingredientsByServing) {
		// BasicFood
		if(values[0].equals("b")) {
			// Get nutritional values in #.# format
			double calories = Math.round(Double.parseDouble( values[2] ) * 10.0) / 10.0;
			double fat = Math.round(Double.parseDouble( values[3] ) * 10.0) / 10.0;
			double carb = Math.round(Double.parseDouble( values[4] ) * 10.0) / 10.0;
			double protein = Math.round(Double.parseDouble( values[5] ) * 10.0) / 10.0;

			return new BasicFood(values[1], calories, fat, carb, protein);
		}
		// Recipe
		else if(values[0].equals("r")) {
			return new Recipe(values[1], ingredientsByServing);
		}

		return null;
	}

	/**
	 * Creates a BasicFood
	 *
	 * @param name
	 * @param c calories
	 * @param f fat
	 * @param cb carb
	 * @param p protein
	 * @return
	 */
	public IFood createBasicFood(String name, String c , String f, String cb, String p) {
		// Get nutritional values in #.# format
		double calories = Math.round(Double.parseDouble( c ) * 10.0) / 10.0;
		double fat = Math.round(Double.parseDouble( f ) * 10.0) / 10.0;
		double carb = Math.round(Double.parseDouble( cb ) * 10.0) / 10.0;
		double protein = Math.round(Double.parseDouble( p ) * 10.0) / 10.0;

		return new BasicFood(name, calories, fat, carb, protein);
	}

	/**
	 * Creates a Recipe
	 *
	 * @param name
	 * @param ingredientsByServings
	 * @return IFood
	 */
	public IFood createRecipe(String name, Map<IFood, Double> ingredientsByServings) {
		return new Recipe( name, ingredientsByServings );
	}
}
