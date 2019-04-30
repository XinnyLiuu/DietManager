package controller;

import model.food.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles actions from the food related panels
 *
 * @author Team A
 */
public class FoodController {
	private FoodFactory fFactory;
	private FoodCollection fCollection;

	public FoodController(FoodCollection f) {
		this.fCollection = f; // Get model
		this.fFactory = new FoodFactory();
	}

	/**
	 * Gets the names of all foods from FoodCollection
	 *
	 * @return List
	 */
	public List<IFood> getFoods() {
		// Gets the hash map of foods
		Map<String, IFood> foodMap = fCollection.getCollection();

		// Create an ArrayList containing all the IFood objects
		List<IFood> foods = new ArrayList<>( foodMap.values() );

		return foods;
	}

	/**
	 * Gets an IFood object from the collection by name
	 *
	 * @param name
	 * @return IFood
	 */
	public IFood getFoodByName(String name) {
		IFood target = fCollection.getFood(name);

		return target;
	}

	/**
	 * Adds a basic food to the FoodCollection
	 *
	 * @param name
	 * @param calories
	 * @param fat
	 * @param carb
	 * @param protein
	 * @param overwrite
	 * @return failure message; empty if valid execution
	 */
	public String addBasicFood(String name, String calories, String fat, String carb, String protein, boolean overwrite) {
		try {
			// Check that a valid name was entered
			if (name.equals("")) {
				return "Please enter a name for the food";
			}

			// Check that all of the nutrition values entered are valid
			if (Double.parseDouble(calories) < 0) {
				throw new NumberFormatException();
			}

			if (Double.parseDouble(fat) < 0) {
				throw new NumberFormatException();
			}

			if (Double.parseDouble(carb) < 0) {
				throw new NumberFormatException();
			}

			if (Double.parseDouble(protein) < 0) {
				throw new NumberFormatException();
			}

            // Check that this food does not already exist in the FoodCollection before adding and that it is not a recipe
			if(!overwrite) {
				IFood food = fCollection.getFood(name);

				if(food != null) {
					if(food instanceof Recipe) {
						// Food is an recipe
						return name + " is an already created recipe!";
					}
					else {
						// Food exists
						return name + " already exists!";
					}
				}
			}

            // Create basic food
            IFood newFood = fFactory.createBasicFood(name, calories, fat, carb, protein);
            fCollection.addFood(name, newFood);
            return "";
		}
		catch(NumberFormatException nfe){
			return "Please enter a number greater than or equal to 0 for all nutrition values";
		}
	}

    /**
     * Adds a recipe to the FoodCollection
     *
     * @param name
     * @param ingredients
     * @param overwrite
     * @return errorString; empty if valid execution
     */
	public String addRecipe(String name, String ingredients, boolean overwrite) {
        // Check that a name was entered
        if(name.equals("")){
            return "Please enter a name for the recipe.";
        }

		// Check that this food does not already exist in the FoodCollection before adding and that it is not a basicfood
		if(!overwrite) {
			IFood food = fCollection.getFood(name);

			if(food != null) {
				if(food instanceof BasicFood) {
					// Food is an basicfood
					return name + " is an already created food!";
				}
				else {
					// Food exists
					return name + " already exists!";
				}
			}
		}

        // Check that ingredients were entered
        if(ingredients.equals("")){
            return "Please enter ingredients for the recipe.";
        }

		// Parse entries and prepare it for addRecipe function
        String[] ingredientsArr = ingredients.split(",");
        Map<IFood, Double> ingredientsByServing = new HashMap<>();
        for (int i = 0; i < ingredientsArr.length; i += 2) {
            try {
                // Check that each ingredient exists
                if (fCollection.getFood(ingredientsArr[i]) == null) {
                    return ingredientsArr[i] + " doesn't exist. Please create it first.";
                }

                // Add to map
                ingredientsByServing.put(
                		fCollection.getFood(ingredientsArr[i]),
		                Double.parseDouble(ingredientsArr[i + 1])
                );

            }
            catch(ArrayIndexOutOfBoundsException e){
                return "The ingredients were not entered in the correct format. Please enter as \"ingredient,servings,ingredient,servings,...\"";
            }
        }

        // Create recipe
        IFood newFood = this.fFactory.createRecipe( name, ingredientsByServing ) ;
        fCollection.addFood(name, newFood);
        return "" ;
	}
}
