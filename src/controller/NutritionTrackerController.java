package controller;

import model.exercise.Exercise;
import model.exercise.ExerciseCollection;
import model.food.FoodCollection;
import model.food.IFood;
import model.log.LogCollection;

import java.util.*;

/**
 * Handles actions from NutritionTrackerPanel
 *
 * @author Team A
 */
public class NutritionTrackerController {
	private static String DEFAULT_WEIGHT_STRING = "150.0";
	private static String DEFAULT_CALORIES_STRING = "2000.0";

	private FoodController fController;
	private ExerciseController eController;
	private LogController lController;

	public NutritionTrackerController(FoodCollection f, LogCollection l, ExerciseCollection e) {
		this.fController = new FoodController( f );
		this.eController = new ExerciseController( e );
		this.lController = new LogController( f, l, e );
	}

	/**
	 * Gets list of weight and calorie for the day
	 *
	 * @param date
	 * @return List of values or null
	 */
	public String getWeight(String date) {
		try {
			return lController.getWeightByDay(date).toString();
		}
		catch(NullPointerException npe){
			return DEFAULT_WEIGHT_STRING;
		}
	}


	/**
	 * Gets list of weight and calorie for the day
	 *
	 * @param date
	 * @return List of values or null
	 */
	public String getDesiredCalorie(String date) {
		try {
			return lController.getDesiredCalorieByDay(date).toString();
		}
		catch(NullPointerException npe){
			return DEFAULT_CALORIES_STRING;
		}
	}

	/**
	 * Calculates all calories and nutrients for the given date
	 *
	 * @param date the date for which to calculate totals
	 * @return ArrayList of calculated totals in format: [calories, fats, carbs, proteins]
	 */
	public ArrayList<Double> calculateTotalConsumedNutrients( String date ) {
		double calories = 0.0;
		double fats = 0.0;
		double carbs = 0.0;
		double proteins = 0.0;

		// Get list of foods
		List<String> foods = lController.getFoodData( date );

		// Calculate all nutrient values
		if(foods != null) {

			for(int i=0; i<foods.size()-1; i+=2) {
				IFood target = fController.getFoodByName(foods.get(i));

				// Get serving
				double serving =  Double.parseDouble( foods.get(i+1) );

				if(target != null) {
					calories += target.getCalories() * serving;
					fats += target.getFat() * serving;
					carbs += target.getCarb() * serving;
					proteins += target.getProtein() * serving;
				}
			}
		}

		// Setup return array and return
		ArrayList<Double> nutrientValues = new ArrayList<>();
		nutrientValues.add(calories);
		nutrientValues.add(fats);
		nutrientValues.add(carbs);
		nutrientValues.add(proteins);

		return nutrientValues;
	}

	/**
	 * Calculates all calories and nutrients for the given date
	 *
	 * @param date the date for which to calculate totals
	 * @return ArrayList of calculated totals in format: [total consumed calories, total burned calories, net calories]
	 */
	public Double calculateBurnedCalories( String date ) {

		Double totalBurnedCalories = 0.0;
		Double userWeight = 150.0;
		if(this.lController.getWeightByDay(date) != null){
			userWeight = this.lController.getWeightByDay(date);
		}

		// Get list of foods
		List<String> exercises = lController.getExerciseData( date );

		// Calculate all nutrient values
		if(exercises != null) {

			for(int i=0; i < exercises.size()-1; i+=2) {
				// Get exercise object
				Exercise target = eController.getExercise(exercises.get(i));

				// Get duration
				double duration =  Double.parseDouble( exercises.get(i+1) );

				if(target != null) {
					totalBurnedCalories += target.calculateCalories(userWeight, duration);
				}
			}
		}

		return totalBurnedCalories;
	}

	/**
	 * Sets the log for the weight for the day
	 *
	 * @param date
	 * @param weight
	 * @return true/false based on result
	 */
	public boolean setLogWeight(String date, String weight) {
		if(lController.logWeight( date, weight).equals("")) {
			return true;
		}

		return false;
	}

	/**
	 * Sets the log for the calorie limit for the day
	 *
	 * @param date
	 * @param calorie
	 * @return true/false based on result
	 */
	public boolean setLogCalorie(String date, String calorie) {
		if(lController.logCalorieLimit( date, calorie ).equals("")) {
			return true;
		}

		return false;
	}
}
