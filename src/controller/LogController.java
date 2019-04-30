package controller;

import model.exercise.Exercise;
import model.exercise.ExerciseCollection;
import model.food.FoodCollection;
import model.log.LogCollection;
import model.food.IFood;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles actions from log related panels
 *
 * @author Team A
 */
public class LogController {
	private LogCollection lCollection;
	private ExerciseController eController;
	private FoodController fController;

	public LogController(FoodCollection f, LogCollection l, ExerciseCollection e) {
		this.lCollection = l; // Get model
		this.eController = new ExerciseController(e); // Get model
		this.fController = new FoodController( f );
	}

	/**
	 * Returns a list of the names of all foods in FoodCollection
	 *
	 * @return List of food names
	 */
	public List<String> getFoodData() {
		List<IFood> foods = fController.getFoods();

		// Create an ArrayList to hold the names of the foods from List foods
		List<String> foodNames = new ArrayList<>();

		for(IFood f : foods) {
			foodNames.add( f.getName() );
		}

		return foodNames;
	}

	/**
	 * Returns a list of the names of all exercises in ExerciseCollection
	 *
	 * @return List of exercise names
	 */
	public List<String> getExerciseData() {
		List<Exercise> exercises = eController.getAllExercises();

		// Create an ArrayList to hold the names of the exercise
		List<String> exerciseNames = new ArrayList<>();

		for(Exercise e : exercises) {
			exerciseNames.add( e.getName() );
		}

		return exerciseNames;
	}

	/**
	 * Returns the weight logged by the day given
	 *
	 * @param date
	 * @return logged weight for the day
	 */
	public Double getWeightByDay(String date){
		return lCollection.getWeight( date );
	}

	/**
	 * Returns the desired calories by the day given
	 *
	 * @param date
	 * @return List of values or null
	 */
	public Double getDesiredCalorieByDay(String date) {
		return lCollection.getDesiredCalories( date );
	}

	/**
	 * Gets all food logs by date
	 *
	 * @param date
	 * @return List
	 */
	public List<String> getFoodData(String date) {
		return lCollection.getFoodLog( date );
	}

	/**
	 * Gets all exercise logs by date
	 *
	 * @param date
	 * @return List
	 */
	public List<String> getExerciseData(String date) {
		return lCollection.getExerciseLog( date );
	}

	/**
	 * Logs a new food to the LogCollection
	 *
	 * @param date
	 * @param name
	 * @param servings
	 * @return Error String; empty if valid execution
	 */
	public String logFood(String date, String name, String servings) {
		try {
			name = name.toLowerCase();
			if (name.equals("")) {
				throw new NullPointerException();
			}
			if (Double.parseDouble(servings) <= 0) {
				throw new NumberFormatException();
			}
		}
		catch(NullPointerException npe) {
			return "Please select a food!";
		}
		catch(NumberFormatException nfe) {
			return "Please enter a number greater than 0 for servings!";
		}

		if(this.fController.getFoodByName(name) == null){
			return "This is not a valid selection, please create this food or recipe before logging it!";
		}

		lCollection.addFoodLog(date, name, servings);
		return "";
	}

	/**
	 * Logs a new exercise to the LogCollection
	 *
	 * @param date
	 * @param name
	 * @param duration
	 * @return String based on the results of this method
	 */
	public String logExercise(String date, String name, String duration) {
		try {
			name = name.toLowerCase();
			if(name.equals("")) {
				throw new NullPointerException();
			}

			if(Double.parseDouble(duration) <= 0) {
				throw new NumberFormatException();
			}
		}
		catch(NullPointerException npe) {
			return "Please select a exercise!";
		}
		catch(NumberFormatException nfe) {
			return "Please enter a number greater than 0 for duration!";
		}

		if(this.eController.getExercise(name) == null){
			return "This is not a valid selection, please create this exercise before logging it!";
		}

		lCollection.addExerciseLog(date, name, duration);
		return "";
	}

	/**
	 * Logs weight for the day
	 *
	 * @param date
	 * @param weight
	 * @return String based on the results of the method
	 */
	public String logWeight(String date, String weight) {
		try {
			if (Double.parseDouble(weight) <= 0) {
				throw new NumberFormatException();
			}
		}
		catch(NumberFormatException nfe) {
			return "Please enter a number greater than 0 for weight";
		}

		this.lCollection.addWeightLog(date, Double.parseDouble( weight ));
		return "";
	}

	/**
	 * Logs calorie limit for the day
	 *
	 * @param date
	 * @param calorie
	 * @return String based on the results of the method
	 */
	public String logCalorieLimit(String date, String calorie) {
		try {
			if (Double.parseDouble(calorie) <= 0) {
				throw new NumberFormatException();
			}
		}
		catch(NumberFormatException nfe) {
			return "Please enter a number greater than 0 for calorie limit";
		}

		this.lCollection.addCalorieLimit(date, Double.parseDouble( calorie ));
		return "";
	}

	/**
	 * Deletes a food log from the daily log
	 *
	 * @param date
	 * @param name
	 * @param servings
	 * @return String based on the results of the method
	 */
	public String removeFoodLog(String date, String name, String servings) {
		try {
			name = name.toLowerCase();
			if (name.equals("")) {
				throw new NullPointerException();
			}
			if (Double.parseDouble(servings) <= 0) {
				throw new NumberFormatException();
			}
		}
		catch(NullPointerException npe) {
			return "Please select a food";
		}
		catch(NumberFormatException nfe) {
			return "Please enter a number greater than 0 for servings";
		}

		// Check the return value of removeFoodLog
		String resString = lCollection.removeFoodLog(date, name, servings);
		if(resString.equals("Null")) {
			return "No such log exists!";
		}
		else if(resString.contains("duplicate")) {
			return resString;
		}

		return "";
	}

	/**
	 * Deletes a exercise log from the daily log
	 *
	 * @param date
	 * @param name
	 * @param duration
	 * @return String based on the results of the method
	 */
	public String removeExerciseLog(String date, String name, String duration) {
		try {
			name = name.toLowerCase();
			if (name.equals("")) {
				throw new NullPointerException();
			}
			if (Double.parseDouble(duration) <= 0) {
				throw new NumberFormatException();
			}
		}
		catch(NullPointerException npe) {
			return "Please select an exercise!";
		}
		catch(NumberFormatException nfe) {
			return "Please enter a number greater than 0 for duration!";
		}

		// Check the return value of removeExerciseLog
		String resString = lCollection.removeExerciseLog(date, name, duration);
		if(resString.equals("Null")) {
			return "No such log exists!";
		}
		else if(resString.contains("duplicate")) {
			return resString;
		}

		return "";
	}
}
